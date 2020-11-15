package org.wmy.mall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.wmy.mall.dao.UserMapper;
import org.wmy.mall.enums.ResponseEnum;
import org.wmy.mall.enums.RoleEnum;
import org.wmy.mall.pojo.User;
import org.wmy.mall.service.IUserService;
import org.wmy.mall.vo.ResponseVo;

import java.nio.charset.StandardCharsets;

import static org.wmy.mall.enums.ResponseEnum.EMAIL_EXIST;
import static org.wmy.mall.enums.ResponseEnum.ERROR;
import static org.wmy.mall.enums.ResponseEnum.USERNAME_EXIST;

/**
 * @author wmy
 * @create 2020-11-07 11:35
 */

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseVo register(User user) {


        //1.校验用户名
        int countByUsername = userMapper.countByUsername(user.getUsername());
        if(countByUsername > 0) {
            return ResponseVo.error(USERNAME_EXIST);
        }
        //2.校验邮箱
        int countByEmail = userMapper.countByEmail(user.getEmail());
        if(countByEmail > 0) {
           return ResponseVo.error(EMAIL_EXIST);
        }
        //3.MD5摘要算法
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));

        //默认为普通用户；该字段在数据库设计时被要求非空
        user.setRole(RoleEnum.CUSTOMER.getCode());

        //4.注册入库
        int i = userMapper.insertSelective(user);
        if (i == 0) {
            return ResponseVo.error(ERROR);
        }

        return ResponseVo.success();
    }

    @Override
    public ResponseVo<User> login(String username, String password) {

        User user = userMapper.selectByUsername(username);
        if(user == null) {
            //用户名不存在(返回用户名或密码不存在)
            return ResponseVo.error(ResponseEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        if(!user.getPassword().equalsIgnoreCase(DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)))){
            //密码错误(返回用户名或密码不存在)
            return ResponseVo.error(ResponseEnum.USERNAME_OR_PASSWORD_ERROR);
        }

        //返给前端的密码要处理，为了安全
        user.setPassword("");
        return ResponseVo.success(user);
    }
}
