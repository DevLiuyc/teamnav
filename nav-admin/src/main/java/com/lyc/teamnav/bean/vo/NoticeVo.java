package com.lyc.teamnav.bean.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Accessors(chain = true)
public class NoticeVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    private Boolean status;

    private Integer sort;

    /**
     * getStatus
     *
     * @return Boolean
     */
    public Boolean getStatus() {
        return Objects.nonNull(endTime) && endTime.isAfter(LocalDateTime.now());
    }

}
