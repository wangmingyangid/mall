package org.wmy.mall.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wmy.mall.MallApplicationTests;
import org.wmy.mall.enums.ResponseEnum;
import org.wmy.mall.service.ICategoryService;
import org.wmy.mall.vo.CategoryVo;
import org.wmy.mall.vo.ResponseVo;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author wmy
 * @create 2020-11-08 11:50
 */
public class CategoryServiceImplTest extends MallApplicationTests {

    @Autowired
    ICategoryService categoryService;
    
    @Test
    public void selectAll() {
        ResponseVo<List<CategoryVo>> listResponseVo = categoryService.selectAll();
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),listResponseVo.getStatus());
    }
}