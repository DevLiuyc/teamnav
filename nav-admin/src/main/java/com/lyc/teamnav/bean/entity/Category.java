package com.lyc.teamnav.bean.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Accessors(chain = true)
@Table(name = "nav_category")
@DynamicInsert
@DynamicUpdate
public class Category implements ISortEntity<Category>, Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", length = 32)
    private String id;

    @Column(name = "icon", length = 400)
    private String icon;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "privateCard")
    private Boolean privateCard;

    @Column(name = "valid")
    private Boolean valid;

    @Column(name = "sort")
    private Integer sort;
}
