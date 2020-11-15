package org.wmy.mall.service.impl;

import com.github.pagehelper.PageInfo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wmy.mall.MallApplicationTests;
import org.wmy.mall.enums.ResponseEnum;
import org.wmy.mall.service.IProductService;
import org.wmy.mall.vo.ProductDetailVo;
import org.wmy.mall.vo.ProductVo;
import org.wmy.mall.vo.ResponseVo;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author wmy
 * @create 2020-11-09 20:31
 */
public class ProductServiceImplTest extends MallApplicationTests {

    @Autowired
    IProductService productService;

    @Test
    public void list() {
        ResponseVo<PageInfo> responseVo = productService.list(null, 1, 1);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }

    @Test
    public void detail(){
        ResponseVo<ProductDetailVo> responseVo = productService.detail(26);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }
}