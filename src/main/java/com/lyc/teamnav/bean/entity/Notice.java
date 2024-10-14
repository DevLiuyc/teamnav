package com.lyc.teamnav.bean.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tn_notice")
public class Notice {
    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    private String content;

    private LocalDateTime endTime;
}
