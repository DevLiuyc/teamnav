package com.lyc.teamnav.bean.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tn_setting")
public class Setting implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String id;
}
