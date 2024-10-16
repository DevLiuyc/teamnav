package com.lyc.teamnav.controller;

import com.lyc.teamnav.bean.dto.NoticeDto;
import com.lyc.teamnav.bean.vo.NoticeVo;
import com.lyc.teamnav.service.INoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final INoticeService noticeService;

    @GetMapping
    public ResponseEntity<List<NoticeVo>> select(Boolean status) {
        return ResponseEntity.ok(noticeService.select(status));
    }

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody NoticeDto noticeDto) {
        noticeService.save(null, noticeDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * update
     *
     * @param id id
     * @param noticeDto noticeDto
     * @return Void
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") String id,
                                       @RequestBody NoticeDto noticeDto) {
        noticeService.save(id, noticeDto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/actions/sort")
    public ResponseEntity<Void> changeSort(@RequestParam Integer before, @RequestParam Integer after) {
        noticeService.changeSort(before, after);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        noticeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
