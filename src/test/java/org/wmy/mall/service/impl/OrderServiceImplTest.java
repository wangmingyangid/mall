package org.wmy.mall.service.impl;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.wmy.mall.MallApplicationTests;
import org.wmy.mall.enums.ResponseEnum;
import org.wmy.mall.form.CartAddForm;
import org.wmy.mall.service.ICartService;
import org.wmy.mall.service.IOrderService;
import org.wmy.mall.vo.CartVo;
import org.wmy.mall.vo.OrderVo;
import org.wmy.mall.vo.ResponseVo;

import static org.junit.Assert.*;

/**
 * @author wmy
 * @create 2020-11-14 21:54
 */

@Slf4j
@Transactional
public class OrderServiceImplTest extends MallApplicationTests {

    @Autowired
    private IOrderService orderService;
    @Autowired
    private ICartService cartService;

    private Integer uid = 1;

    private Integer shippingId = 11;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Before
    public void before(){
        CartAddForm cartAddForm = new CartAddForm(26, true);
        ResponseVo<CartVo> list = cartService.add(uid, cartAddForm);
        log.info("list{}",gson.toJson(list));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),list.getStatus());
    }

    @Test
    public void createTest(){
        ResponseVo<OrderVo> responseVo = create();
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }
    
    private ResponseVo<OrderVo> create() {
        ResponseVo<OrderVo> responseVo = orderService.create(uid, shippingId);
        log.info("result = {}",gson.toJson(responseVo));
        return responseVo;

    }
    @Test
    public void list(){
        ResponseVo<PageInfo> responseVo = orderService.list(uid, 1, 2);
        log.info("result = {}",gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }
    @Test
    public void detail(){
        ResponseVo<OrderVo> vo = create();
        ResponseVo<OrderVo> responseVo = orderService.detail(uid,vo.getData().getOrderNo());
        log.info("result = {}",gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }

    @Test
    public void cancel(){
        ResponseVo<OrderVo> vo = create();
        ResponseVo responseVo = orderService.cancel(uid, vo.getData().getOrderNo());
        log.info("result = {}",gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }
}