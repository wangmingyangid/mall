package org.wmy.mall.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wmy.mall.MallApplicationTests;
import org.wmy.mall.enums.ResponseEnum;
import org.wmy.mall.form.CartAddForm;
import org.wmy.mall.form.CartUpdateForm;
import org.wmy.mall.service.ICartService;
import org.wmy.mall.vo.CartVo;
import org.wmy.mall.vo.ResponseVo;

import static org.junit.Assert.*;

/**
 * @author wmy
 * @create 2020-11-13 19:44
 */

@Slf4j
public class CartServiceImplTest extends MallApplicationTests {
    @Autowired
    private ICartService cartService;

    //使得打印的json数据格式化输出
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private Integer uid = 1;
    private Integer productId = 26;

    @Before
    public void add() {
        log.info("新增购物车....");
        CartAddForm cartAddForm = new CartAddForm(productId, true);
        ResponseVo<CartVo> list = cartService.add(uid, cartAddForm);
        log.info("list{}",gson.toJson(list));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),list.getStatus());
    }
    @Test
    public void list(){
        ResponseVo<CartVo> list = cartService.list(uid);
        log.info("list{}",gson.toJson(list));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),list.getStatus());
    }
    @Test
    public void update(){
        CartUpdateForm cartUpdateForm = new CartUpdateForm();
        cartUpdateForm.setQuantity(10);
        cartUpdateForm.setSelected(false);
        ResponseVo<CartVo> result = cartService.update(uid,productId,cartUpdateForm);
        log.info("list{}",gson.toJson(result));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),result.getStatus());
    }

    @After
    public void delete(){
        log.info("删除购物车....");
        ResponseVo<CartVo> result = cartService.delete(uid, productId);
        log.info("list{}",gson.toJson(result));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),result.getStatus());
    }

    @Test
    public void selectAll(){
        ResponseVo<CartVo> result = cartService.selectAll(uid);
        log.info("list{}",gson.toJson(result));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),result.getStatus());
    }

    @Test
    public void unSelectAll(){
        ResponseVo<CartVo> result = cartService.unSelectAll(uid);
        log.info("list{}",gson.toJson(result));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),result.getStatus());
    }

    @Test
    public void sum(){
        ResponseVo<Integer> result = cartService.sum(uid);
        log.info("list{}",gson.toJson(result));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),result.getStatus());
    }

}