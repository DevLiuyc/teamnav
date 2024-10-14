package com.lyc.teamnav.controller;

import com.lyc.teamnav.service.ICardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/card")
@RequiredArgsConstructor
public class CardController {
    private final ICardService cardService;
}
