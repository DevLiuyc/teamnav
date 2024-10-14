package com.lyc.teamnav.repository;

import com.lyc.teamnav.bean.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, String> {
}
