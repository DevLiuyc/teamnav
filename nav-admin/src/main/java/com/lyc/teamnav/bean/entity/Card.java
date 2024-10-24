package com.lyc.teamnav.bean.entity;

import com.lyc.teamnav.bean.dto.CardIconDto;
import com.lyc.teamnav.bean.dto.CardZipDto;
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
@Table(name = "nav_card")
@DynamicInsert
@DynamicUpdate
public class Card implements ISortEntity<Card>, Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", length = 32)
    private String id;

    @Column(name = "type", length = 100)
    private String type;

    @Column(name = "category", length = 32)
    private String category;

    @Column(name = "icon", length = 400)
    private CardIconDto icon;

    @Column(name = "title", length = 200)
    private String title;

    @Column(name = "content", length = 400)
    private String content;

    @Column(name = "url", length = 200)
    private String url;

    @Column(name = "zip", length = 400)
    private CardZipDto zip;

    @Column(name = "sort")
    private Integer sort;
}
