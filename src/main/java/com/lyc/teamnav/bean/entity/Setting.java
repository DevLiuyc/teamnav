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
@Table(name = "nav_setting")
@DynamicInsert
@DynamicUpdate
public class Setting implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "C_ID", length = 32)
    private String id;

    @Column(name = "C_NGINX_OPEN")
    private Boolean nginxOpen;

    @Column(name = "C_NGINX_URL", length = 200)
    private String nginxUrl;

    @Column(name = "C_NAV_NAME", length = 200)
    private String navName;

    @Column(name = "N_CUTOVER_SPEED")
    private Integer cutOverSpeed;

    @Column(name = "C_LOGO_PATH", length = 400)
    private String logoPath;

    @Column(name = "C_LOGO_TO_FAVICON")
    private Boolean logoToFavicon;

    @Column(name = "C_LAYOUTSIZE", length = 50)
    private String layoutSize;
}
