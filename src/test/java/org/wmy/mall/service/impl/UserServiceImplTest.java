package org.wmy.mall.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.wmy.mall.MallApplicationTests;
import org.wmy.mall.enums.ResponseEnum;
import org.wmy.mall.enums.RoleEnum;
import org.wmy.mall.pojo.User;
import org.wmy.mall.service.IUserService;
import org.wmy.mall.vo.ResponseVo;

import static org.junit.Assert.*;

/**
 * @author wmy
 * @create 2020-11-07 11:59
 *
 * @Transactional 注解用在单侧上时，会回滚；保证运行结果不会被写入到数据库
 *
 * 单元测试只需要对service层进行测试就好了
 */

@Transactional
public class UserServiceImplTest extends MallApplicationTests {

    private static final String  USERNAME = "wmy1";
    private static final String  PASSWORD = "123456";

    @Autowired
    private IUserService userService;

    @Before
    public void register() {
        User user = new User(USERNAME,PASSWORD,"11@qq.com",RoleEnum.ADMIN.getCode());
        userService.register(user);
    }

    @Test
    public void login(){
        ResponseVo<User> responseVo = userService.login(USERNAME, PASSWORD);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }
}