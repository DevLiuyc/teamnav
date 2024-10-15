package com.lyc.teamnav.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.lyc.teamnav.bean.dto.SettingDto;
import com.lyc.teamnav.bean.entity.Setting;
import com.lyc.teamnav.common.constants.Constants;
import com.lyc.teamnav.common.utils.BeanExtUtils;
import com.lyc.teamnav.repository.SettingRepository;
import com.lyc.teamnav.service.ISettingService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

@Service
public class SettingServiceImpl implements ApplicationRunner, ISettingService {
    @Resource
    private SettingRepository settingRepository;

    private Setting settingCache;

    @Value("${nav-name}")
    private String navName;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Setting setting = get();
        if (StrUtil.isBlank(setting.getNavName())) {
            // 加载默认配置，兼容老版本
            setting.setNavName(navName);
            settingRepository.save(setting);
        }
        if (StrUtil.isBlank(setting.getLogoPath())) {
            setting.setLogoPath(Constants.DEFAULT_LOGO_PATH);
            settingRepository.save(setting);
        }
        if (StrUtil.isBlank(setting.getLayoutSize())) {
            setting.setLayoutSize("small-layout");
            settingRepository.save(setting);
        }
        this.settingChange(setting);
    }

    public Setting get() {
        return settingRepository.findAll().stream().findFirst().orElse(new Setting().setId("setting-id"));
    }

    /**
     * save
     *
     * @param settingDto nginx
     */
    public void saveSetting(SettingDto settingDto) {
        Setting setting = BeanExtUtils.convert(settingDto, Setting::new).setId("setting-id");
        setting.setNginxUrl(StrUtil.trimEnd("/"));
        if (StrUtil.isBlank(setting.getLogoPath())) {
            setting.setLogoPath(Constants.DEFAULT_LOGO_PATH);
        }
        settingRepository.save(setting);
        this.settingChange(setting);
    }

    /**
     * 获取经过一些处理能直接使用的设置
     *
     * @return Setting
     */
    public Setting getSettingCache() {
        return this.settingCache;
    }

    private void settingChange(Setting setting) {
        this.settingCache = BeanExtUtils.convert(setting, Setting::new);
        this.settingCache.setNginxUrl(BooleanUtil.isTrue(setting.getNginxOpen())
                && StrUtil.isNotBlank(setting.getNginxUrl())
                ? setting.getNginxUrl() : StrUtil.EMPTY);
        settingCache.setNavName(setting.getNavName());
        settingCache.setLogoToFavicon(BooleanUtil.isTrue(setting.getLogoToFavicon()));
        settingCache.setCutOverSpeed((setting.getCutOverSpeed() == null ? 10 : setting.getCutOverSpeed()) * 1000);
        settingCache.setLogoPath(setting.getLogoPath());
    }

}
