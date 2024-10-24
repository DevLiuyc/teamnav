package com.lyc.teamnav.controller;

import com.lyc.teamnav.bean.dto.ChangePasswordDto;
import com.lyc.teamnav.bean.dto.SettingDto;
import com.lyc.teamnav.bean.entity.Setting;
import com.lyc.teamnav.service.impl.SettingService;
import com.lyc.teamnav.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SettingController {

    private final SettingService settingService;

    private final UserService userService;

    /**
     * 获取配置
     *
     * @return Setting
     */
    @GetMapping("/setting")
    public ResponseEntity<Setting> get() {
        return ResponseEntity.ok(settingService.get());
    }

    /**
     * 保存配置
     *
     * @param settingDto settingDto
     * @return Void
     */
    @PatchMapping("/setting")
    public ResponseEntity<Void> saveSetting(@RequestBody SettingDto settingDto) {
        settingService.saveSetting(settingDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * changePassword
     *
     * @param changePassword changePassword
     * @return Void
     */
    @PatchMapping("/user/password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordDto changePassword) {
        userService.changePassword(changePassword);
        return ResponseEntity.noContent().build();
    }
}
