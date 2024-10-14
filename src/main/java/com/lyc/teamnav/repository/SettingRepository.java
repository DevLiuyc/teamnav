package com.lyc.teamnav.repository;

import com.lyc.teamnav.bean.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingRepository extends JpaRepository<Setting, String> {
}
