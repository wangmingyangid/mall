package org.wmy.mall.service;

import org.wmy.mall.pojo.User;
import org.wmy.mall.vo.ResponseVo;

/**
 * @author wmy
 * @create 2020-11-07 11:32
 */
public interface IUserService {
    /**
     * 注册
     */

     ResponseVo register(User user);
    /**
     *
     * 登录
     */
    ResponseVo<User> login(String username,String password);
}
