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
@Table(name = "tn_card")
public class Card {
    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    private String type;

    private String category;

    private String title;

    private String content;

    private String url;

}
