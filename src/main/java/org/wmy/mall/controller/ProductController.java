package org.wmy.mall.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wmy.mall.service.IProductService;
import org.wmy.mall.vo.ProductDetailVo;
import org.wmy.mall.vo.ResponseVo;

/**
 * @author wmy
 * @create 2020-11-09 21:29
 */

@RestController
public class ProductController {
    @Autowired
    private IProductService productService;

    @GetMapping("/products")
    public ResponseVo<PageInfo> list(@RequestParam(required = false) Integer categoryId,
                                     @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                                     @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        return productService.list(categoryId, pageNum, pageSize);
    }

    @GetMapping("/products/{productId}")
    public ResponseVo<ProductDetailVo> detail(@PathVariable("productId") Integer productId){
        return productService.detail(productId);
    }
}
