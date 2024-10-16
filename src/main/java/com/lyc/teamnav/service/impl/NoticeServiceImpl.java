package com.lyc.teamnav.service.impl;

import com.lyc.teamnav.bean.dto.NoticeDto;
import com.lyc.teamnav.bean.vo.NoticeVo;
import com.lyc.teamnav.service.INoticeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeServiceImpl implements INoticeService {
    @Override
    public List<NoticeVo> select(Boolean status) {
        return null;
    }

    @Override
    public void save(String id, NoticeDto dto) {

    }

    @Override
    public void changeSort(int before, int after) {

    }

    @Override
    public void delete(String id) {

    }
}
