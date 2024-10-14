package com.lyc.teamnav.repository;

import com.lyc.teamnav.bean.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
