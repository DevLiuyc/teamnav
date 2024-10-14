package com.lyc.teamnav.repository;

import com.lyc.teamnav.bean.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
