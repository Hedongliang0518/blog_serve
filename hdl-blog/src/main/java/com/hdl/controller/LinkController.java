package com.hdl.controller;

import com.hdl.domain.ResponseResult;
import com.hdl.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/link")
/**
 * 查询友情链接
 */
public class LinkController {
    @Autowired
    private LinkService linkService;
    @GetMapping("/getAllLink")
    public ResponseResult getAllLink() {
        return linkService.getAllLink();
    };
}
