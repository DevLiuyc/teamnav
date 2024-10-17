package com.lyc.teamnav.service.cardtype;

import com.lyc.teamnav.bean.dto.CardIconDto;
import com.lyc.teamnav.bean.dto.CardZipDto;
import com.lyc.teamnav.bean.entity.Card;
import com.lyc.teamnav.bean.entity.Setting;
import com.lyc.teamnav.bean.vo.CardTreeChildVo;
import com.lyc.teamnav.common.annotation.CardType;
import com.lyc.teamnav.common.enums.CardTypeEnum;
import com.lyc.teamnav.common.utils.FileExtUtils;
import com.lyc.teamnav.common.utils.StringExtUtils;
import com.lyc.teamnav.common.utils.ZipUtils;
import com.lyc.teamnav.repository.CardRepository;
import com.lyc.teamnav.service.ISettingService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@CardType(CardTypeEnum.ZIP)
public class CardTypeZipServiceImpl implements ICardTypeService {

    @Resource
    private ISettingService settingService;

    @Resource
    private CardRepository cardRepository;

    @Override
    public void formatCardVo(CardTreeChildVo cardVo) {
        Setting settingCache = settingService.getSettingCache();
        if (StringUtils.isNotBlank(settingCache.getNginxUrl())) {
            cardVo.setUrl(settingCache.getNginxUrl() + cardVo.getUrl());
        }
    }

    @Override
    public void supplySave(String id, Card card) {
        if (StringUtils.isNotBlank(id)) {
            String existPath = cardRepository.findById(id).map(Card::getZip)
                    .map(CardZipDto::getPath)
                    .orElse(StringUtils.EMPTY);
            String curPath = Optional.of(card).map(Card::getZip)
                    .map(CardZipDto::getPath)
                    .orElse(StringUtils.EMPTY);
            if (Objects.equals(existPath, curPath)) {
                // 没有上传新的压缩包就不重新解压了
                return;
            }
        }
        FileExtUtils.deleteFiles(true, "/ext-resources/modules/" + card.getId());
        ZipUtils.unzip(card.getId(), card.getZip().getPath());
        card.setUrl(StringExtUtils.format("/ext-resources/modules/{}/index.html", card.getId()));
    }

    @Override
    public void supplyDelete(Card card) {
        CardIconDto cardIconDto = card.getIcon();
        List<String> deletePaths = new ArrayList<>();
        if (StringUtils.isNotBlank(cardIconDto.getSrc())
                && !StringUtils.contains(cardIconDto.getSrc(), "default")) {
            deletePaths.add(cardIconDto.getSrc());
        }
        deletePaths.add(card.getZip().getPath());
        deletePaths.add("/ext-resources/modules/" + card.getId());
        FileExtUtils.deleteFiles(false, deletePaths.toArray(new String[0]));
    }

}
