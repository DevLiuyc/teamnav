package com.lyc.teamnav.service.schedule;

import com.lyc.teamnav.bean.dto.CardIconDto;
import com.lyc.teamnav.bean.dto.CardZipDto;
import com.lyc.teamnav.bean.entity.Card;
import com.lyc.teamnav.common.constants.Constants;
import com.lyc.teamnav.repository.CardRepository;
import com.lyc.teamnav.service.ISettingService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@EnableScheduling
@ConditionalOnProperty(value = "clear-useless-files.enable", havingValue = "true")
public class ClearUselessFilesService {

    @Resource
    private CardRepository cardRepository;

    @Resource
    private ISettingService settingService;

    @Scheduled(cron = "${clear-useless-files.cron}")
    private void clear() {
        Set<String> existFileIds = getExistFileIds();
        deleteUselessFiles("/ext-resources/images", existFileIds);
        deleteUselessFiles("/ext-resources/modules", existFileIds);
    }

    private void deleteUselessFiles(String path, Set<String> existFileIds) {
        File root = new File(Constants.ROOT_DIR + path);
        if (!root.exists()) {
            return;
        }
        File[] files = root.listFiles();
        if (files == null || files.length <= 0) {
            return;
        }
        for (File file : files) {
            // 只删除文件名是日期类型的 images下的default和modules下的已解压的原型文件都忽略
            if (file.isDirectory() && file.getName().length() == 8) {
                deleteUselessFiles(file, existFileIds);
            }
        }
    }

    private void deleteUselessFiles(File parentFile, Set<String> existFileIds) {
        File[] files = parentFile.listFiles();
        if (files == null || files.length <= 0) {
            forceDeleteFile(parentFile);
            return;
        }
        for (File file : files) {
            if (!existFileIds.contains(FilenameUtils.getBaseName(file.getName()))) {
                forceDeleteFile(file);
            }
        }
        // 重新listFiles一次，避免前面删除的过程中有新的文件进入添加进parentFile，虽然可能性很小
        File[] newFiles = parentFile.listFiles();
        if (newFiles == null || newFiles.length <= 0) {
            forceDeleteFile(parentFile);
        }
    }

    private void forceDeleteFile(File file) {
        try {
            FileUtils.forceDelete(file);
        } catch (IOException ex) {
            log.error("文件删除失败", ex);
        }
    }

    /**
     * 获取正在使用的文件ID，每个上传的文件（不论是图标还是原型压缩包）都是new的新的uuid
     * 根据uuid的文件名即可唯一确定一个文件，不需要完整路径
     *
     * @return Set
     */
    private Set<String> getExistFileIds() {
        Set<String> fileIds = new HashSet<>();
        List<Card> cardList = cardRepository.findAll();
        for (Card card : cardList) {
            CardIconDto icon = card.getIcon();
            if (icon != null && StringUtils.isNotBlank(icon.getSrc())) {
                fileIds.add(FilenameUtils.getBaseName(icon.getSrc()));
            }
            CardZipDto zip = card.getZip();
            if (zip != null && StringUtils.isNotBlank(zip.getPath())) {
                fileIds.add(FilenameUtils.getBaseName(zip.getPath()));
            }
        }
        fileIds.add(FilenameUtils.getBaseName(settingService.getSettingCache().getLogoPath()));
        return fileIds;
    }

}
