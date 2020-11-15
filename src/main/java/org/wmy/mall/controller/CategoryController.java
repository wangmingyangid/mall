package org.wmy.mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wmy.mall.service.impl.CategoryServiceImpl;
import org.wmy.mall.vo.CategoryVo;
import org.wmy.mall.vo.ResponseVo;

import java.util.List;

/**
 * @author wmy
 * @create 2020-11-08 9:53
 */

@RestController
public class CategoryController {

    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("/categories")
    public ResponseVo<List<CategoryVo>> selectAll() {
       return categoryService.selectAll();
    }
}
