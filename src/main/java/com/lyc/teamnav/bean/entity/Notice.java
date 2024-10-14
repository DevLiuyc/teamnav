package com.lyc.teamnav.bean.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nav_notice")
@DynamicInsert
@DynamicUpdate
public class Notice {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", length = 32)
    private String id;

    @Column(name = "content", length = 400)
    private String content;

    @Column(name = "endTime")
    private LocalDateTime endTime;
}
