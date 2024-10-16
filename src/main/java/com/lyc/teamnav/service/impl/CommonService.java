package com.lyc.teamnav.service.impl;

import com.lyc.teamnav.bean.dto.CardIconDto;
import com.lyc.teamnav.bean.dto.LoginDto;
import com.lyc.teamnav.bean.entity.Card;
import com.lyc.teamnav.bean.entity.ISortEntity;
import com.lyc.teamnav.common.constants.Constants;
import com.lyc.teamnav.common.exception.ResourceReadException;
import com.lyc.teamnav.common.exception.ResourceWriteException;
import com.lyc.teamnav.common.utils.*;
import com.lyc.teamnav.repository.CardRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Attribute;
import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;



@Service
@Slf4j
public class CommonService implements ApplicationRunner {

    private static final String ICON_SVG_PATH = "static/assets/lib/iview/fonts/ionicons.svg";

    private static final List<String> CATEGORY_ICONS = new ArrayList<>();

    private static final List<String> CARD_ICONS = new ArrayList<>();

    private static final String CARD_ICON_PATH = "/ext-resources/images/default/";

    @Resource
    private CardRepository cardRepository;

    @Resource
    private AuthenticationManager authenticationManager;

    /**
     * 初始化
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        loadCategoryIcons();
        loadCardIcons();
    }

    /**
     * 解析出iview的所有字体图标名称，用于前端选择图标的控件
     */
    private void loadCategoryIcons() {
        org.dom4j.Document document;
        try (InputStream inputStream = new ClassPathResource(ICON_SVG_PATH).getInputStream()) {
            document = SAXReader.createDefault().read(inputStream);
        } catch (Exception ex) {
            throw new ResourceReadException("icon-xml读取失败", ex);
        }
        CATEGORY_ICONS.addAll(document.getRootElement().element("defs").element("font").elements("glyph")
                .stream()
                .map(element -> element.attribute("glyph-name"))
                .filter(Objects::nonNull)
                .map(org.dom4j.Attribute::getValue)
                .collect(Collectors.toList()));
    }

    /**
     * 加载放置在/ext-resources/images/default路径下的图片图标用于卡片图标选择
     */
    public void loadCardIcons() {
        CARD_ICONS.clear();
        File root = new File(Constants.ROOT_DIR + CARD_ICON_PATH);
        if (!root.exists()) {
            return;
        }
        File[] files = root.listFiles();
        if (files == null || files.length <= 0) {
            return;
        }
        for (File file : files) {
            CARD_ICONS.add(file.getName());
        }
    }

    /**
     * 文件上传，包括原型和图标的
     *
     * @param file 文件
     * @param type 文件类型 images 或者 modules
     * @return 保存路径
     */
    public String upload(MultipartFile file, String type) {
        String savePath = formatSavePath(type, file);
        File saveFile = new File(Constants.ROOT_DIR + savePath);
        try {
            org.apache.commons.io.FileUtils.forceMkdirParent(saveFile);
        } catch (IOException ex) {
            throw new ResourceWriteException("父目录生成失败", ex);
        }
        try (InputStream in = file.getInputStream();
             OutputStream out = new FileOutputStream(saveFile)) {
            org.apache.commons.io.IOUtils.copy(in, out);
        } catch (Exception ex) {
            throw new ResourceWriteException("文件写入失败", ex);
        }
        if ("default".equals(type)) {
            CARD_ICONS.add(file.getOriginalFilename());
        }
        return savePath;
    }

    private String formatSavePath(String type, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if ("default".equals(type)) {
            String path = CARD_ICON_PATH + fileName;
            Assert.isTrue(!new File(Constants.ROOT_DIR + path).exists(), "文件名已经存在");
            return path;
        }
        return StringExtUtils.format("/ext-resources/{}/{}/{}.{}",
                type,
                DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.now()),
                StringExtUtils.getUuid(),
                FilenameUtils.getExtension(fileName));
    }

    /**
     * 修改icon文件名
     *
     * @param fileName fileName
     * @param newName newName
     */
    public void updateIconName(String fileName, String newName) {
        String root = Constants.ROOT_DIR + CARD_ICON_PATH;
        File oldFile = new File(root + fileName);
        String newFileName = newName + "." + FilenameUtils.getExtension(fileName);
        File newFile = new File(root + newFileName);
        Assert.isTrue(oldFile.exists(), "原图标已不存在");
        Assert.isTrue(!newFile.exists(), "无法修改为图标名【" + newName + "】，该图标名已存在");
        Assert.isTrue(oldFile.renameTo(newFile), "文件名修改失败");
        CARD_ICONS.set(CARD_ICONS.indexOf(fileName), newFileName);
        resetRefIcon(fileName, newFileName);
    }

    private void resetRefIcon(String fileName, String newFileName) {
        List<Card> updateList = cardRepository.findAll().stream()
                .filter(item -> Objects.nonNull(item.getIcon())
                        && org.apache.commons.lang3.StringUtils.isNotBlank(item.getIcon().getSrc())
                        && org.apache.commons.lang3.StringUtils.contains(item.getIcon().getSrc(), "default")
                        && org.apache.commons.lang3.StringUtils.endsWith(item.getIcon().getSrc(), fileName)
                ).map(card -> {
                    CardIconDto icon = card.getIcon();
                    icon.setSrc(icon.getSrc().replace(fileName, newFileName));
                    return card;
                }).collect(Collectors.toList());
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(updateList)) {
            cardRepository.saveAll(updateList);
        }
    }

    /**
     * 图标删除
     *
     * @param fileName fileName
     * @throws IOException IOException
     */
    public void deleteDefaultIcon(String fileName) throws IOException {
        File file = new File(Constants.ROOT_DIR + CARD_ICON_PATH + fileName);
        if (file.exists()) {
            checkIconRef(fileName);
            FileUtils.forceDelete(file);
            CARD_ICONS.removeIf(name -> org.apache.commons.lang3.StringUtils.equals(fileName, name));
        }
    }

    private void checkIconRef(String fileName) {
        List<String> list = cardRepository.findAll().stream()
                .filter(item -> Objects.nonNull(item.getIcon())
                        && org.apache.commons.lang3.StringUtils.isNotBlank(item.getIcon().getSrc())
                        && org.apache.commons.lang3.StringUtils.contains(item.getIcon().getSrc(), "default")
                        && org.apache.commons.lang3.StringUtils.endsWith(item.getIcon().getSrc(), fileName)
                ).map(Card::getTitle).collect(Collectors.toList());
        Assert.isTrue(org.apache.commons.collections4.CollectionUtils.isEmpty(list), "图标已被卡片【"
                + org.apache.commons.lang3.StringUtils.join(list, ",") + "】使用，不能删除");
    }

    /**
     * 获取分类的图标
     *
     * @return List
     */
    public List<String> categoryIcons() {
        return CATEGORY_ICONS;
    }

    /**
     * 获取卡片的图标
     *
     * @return List
     */
    public List<String> cardIcons() {
        return CARD_ICONS;
    }

    /**
     * 获取卡片对应链接的 favicon.ico 用于icon
     *
     * @param url url
     * @return List
     */
    public List<String> cardIcons(String url) {
        String domainUrl = getDomainUrl(url);
        String docUrl = getFromDocument(domainUrl);
        // 有时候从dom树的link icon中获取的和favicon图标样式并不一样，都返回给用户去选择
        // 但很多时候两个又是一样的，懒得处理了，所以一个地址获取到两个一样的图标的时候是正常的
        List<String> result = new ArrayList<>();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(docUrl)) {
            result.add(docUrl);
        }
        String favUrl = requestFavicon(domainUrl + "/favicon.ico");
        if (org.apache.commons.lang3.StringUtils.isNotBlank(favUrl)) {
            result.add(favUrl);
        }
        return result;
    }

    /**
     * 获取url的根路径，如http://www.test.com/aa/xx.html -> http://www.test.com
     *
     * @param orgUrl 原始url
     * @return url
     */
    private String getDomainUrl(String orgUrl) {
        try {
            URL url = new URL(orgUrl);
            StringBuilder sb = new StringBuilder(url.getProtocol())
                    .append("://").append(url.getHost());
            if (url.getPort() != -1) {
                sb.append(":").append(url.getPort());
            }
            return sb.toString();
        } catch (Exception ex) {
            throw new IllegalArgumentException("url解析错误", ex);
        }
    }

    private String getFromDocument(String domainUrl) {
        try {
            Document doc = Jsoup.connect(domainUrl)
                    .timeout(Constants.FAVICON_TIMEOUT).get();
            Elements links = doc.head().children().select("link[rel~=icon]");
            if (links.isEmpty()) {
                return "";
            }
            String href = links.get(0).attr("href");
            return requestFavicon(formatLinkIcon(domainUrl, href));
        } catch (Exception ex) {
            // 拿不到就算了，不写日志
        }
        return "";
    }

    /**
     * 从link拿到的格式很多种，这里统一格式化一下
     *
     * @param domainUrl domainUrl
     * @param href href
     * @return String
     */
    private String formatLinkIcon(String domainUrl, String href) {
        if (org.apache.commons.lang3.StringUtils.startsWith(href, "http")) {
            return href;
        }
        if (org.apache.commons.lang3.StringUtils.startsWith(href, "//")) {
            return org.apache.commons.lang3.StringUtils.substringBefore(domainUrl, "//") + href;
        }
        if (StringUtils.startsWith(href, "/")) {
            return domainUrl + href;
        }
        return domainUrl + "/" + href;
    }

    private String requestFavicon(String url) {
        URLConnection conn = null;
        try {
            conn = new URL(url).openConnection();
            conn.setConnectTimeout(Constants.FAVICON_TIMEOUT);
            conn.setReadTimeout(Constants.FAVICON_TIMEOUT);
            byte[] body = org.apache.commons.io.IOUtils.toByteArray(conn);
            // 要能实际获取到favicon的数据，如果返回是一个html文件，往往是鉴权导致重定向了
            if (body != null && !FileExtUtils.isHtml(body)) {
                return url;
            }
        } catch (Exception ex) {
            // 拿不到就算了，不写日志
        } finally {
            if (conn != null) {
                IOUtils.close(conn);
            }
        }
        return "";
    }

    /**
     * generateQrCode
     *
     * @param url url
     */
    public void generateQrCode(String url) {
        HttpServletResponse response = RequestUtils.getResponse();
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        try (OutputStream outputStream = response.getOutputStream()) {
            BufferedImage image = QrCodeUtils.generate(url, 200, 200);
            ImageIO.write(image, "png", outputStream);
        } catch (Exception ex) {
            throw new ResourceWriteException("二维码写入失败");
        }
    }

    /**
     * changeSort
     *
     * @param supplier supplier
     * @param before before
     * @param after after
     * @param <T> T
     * @return List
     */
    public <T extends ISortEntity<T>> List<T> changeSort(Supplier<List<T>> supplier, int before, int after) {
        if (before == after) {
            return Collections.emptyList();
        }
        LinkedList<T> list = new LinkedList<>(supplier.get());
        if (CollectionUtils.isEmpty(list) || list.size() == 1) {
            return Collections.emptyList();
        }
        list.add(after, list.remove(before));
        List<T> updateList = new ArrayList<>();
        int index = 0;
        for (T item : list) {
            if (!Objects.equals(item.getSort(), index)) {
                updateList.add(item.setSort(index));
            }
            index++;
        }
        return updateList;
    }

    /**
     * quickLogin
     *
     * @param loginDto loginDto
     * @return boolean
     */
    public boolean quickLogin(LoginDto loginDto) {
        if (SecurityUtils.getUserInfo() != null) {
            return false;
        }
        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        Authentication authenticate = authenticationManager.authenticate(token);
        Assert.notNull(authenticate, "登录失败");
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return SecurityUtils.getUserInfo() != null;
    }

}
