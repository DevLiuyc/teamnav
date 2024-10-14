package com.lyc.teamnav.bean.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tn_category")
public class Category {
    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    private String icon;

    private String name;

    private Boolean privateCard;

    private Boolean valid;

    private Integer sort;
}
