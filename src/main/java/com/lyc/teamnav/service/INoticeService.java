package com.lyc.teamnav.service;

import com.lyc.teamnav.bean.dto.NoticeDto;
import com.lyc.teamnav.bean.vo.NoticeVo;

import java.util.List;

public interface INoticeService {

    List<NoticeVo> select(Boolean status);
    void save(String id, NoticeDto dto);

    void changeSort(int before, int after);

    void delete(String id);
}
