package com.lyc.teamnav.repository;

import com.lyc.teamnav.bean.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, String> {
}
