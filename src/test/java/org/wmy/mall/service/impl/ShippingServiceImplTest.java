package org.wmy.mall.service.impl;

import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wmy.mall.MallApplicationTests;
import org.wmy.mall.enums.ResponseEnum;
import org.wmy.mall.form.ShippingForm;
import org.wmy.mall.vo.ResponseVo;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author wmy
 * @create 2020-11-14 15:45
 */

@Slf4j
public class ShippingServiceImplTest extends MallApplicationTests {

    @Autowired
    private ShippingServiceImpl shippingService;

    private Integer uid = 1;

    private Integer shippingId = 6;

    private ShippingForm form;

    @Before
    public void before(){
        ShippingForm form = new ShippingForm();
        form.setReceiverName("王明夫");
        form.setReceiverPhone("15803810000");
        form.setReceiverProvince("河南");
        form.setReceiverCity("郑州");
        form.setReceiverDistrict("中原区");
        form.setReceiverZip("462699");
        form.setReceiverAddress("河南省郑州市中原区锦衣大厦11楼1102");
        this.form=form;

        add();
    }

    //@Test
    public void add() {
        ResponseVo<Map<String, Integer>> responseVo = shippingService.add(uid, form);
        log.info("response{}",responseVo);
        this.shippingId = responseVo.getData().get("shippingId");
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());

    }

    @After
    public void delete() {
        ResponseVo responseVo = shippingService.delete(uid, shippingId);
        log.info("responseVo{}",responseVo);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }

    @Test
    public void update() {
        form.setReceiverCity("大连");
        ResponseVo responseVo = shippingService.update(uid, shippingId,form);
        log.info("responseVo{}",responseVo);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }

    @Test
    public void list() {
        ResponseVo<PageInfo> responseVo = shippingService.list(uid, 1, 10);
        log.info("responseVo{}",responseVo);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }
}