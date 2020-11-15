package org.wmy.mall.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.wmy.mall.constant.MallConstant;
import org.wmy.mall.enums.ResponseEnum;
import org.wmy.mall.form.UserLoginForm;
import org.wmy.mall.form.UserRegisterForm;
import org.wmy.mall.pojo.User;
import org.wmy.mall.service.IUserService;
import org.wmy.mall.vo.ResponseVo;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static org.wmy.mall.enums.ResponseEnum.PARAM_ERROR;

/**
 * @author wmy
 * @create 2020-11-07 14:25
 */

@RestController
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 使用@RequestBody用于接受前端发过来的json数据
     *  @Valid 用于表单校验；在form下的类字段上有方法
     */

    @PostMapping("/user/register")
    @ResponseBody
    public ResponseVo register(@Valid @RequestBody UserRegisterForm userForm){

        User user = new User();
        //该类时spring框架提供的工具类，用于对象之间属性的复制
        BeanUtils.copyProperties(userForm,user);

        return userService.register(user);
    }

    @PostMapping("/user/login")
    public ResponseVo<User> login(@Valid @RequestBody UserLoginForm userLoginForm,
                                  HttpSession httpSession){

        ResponseVo<User> responseVo = userService.login(userLoginForm.getUsername(), userLoginForm.getPassword());

        //设置session
        httpSession.setAttribute(MallConstant.CURRENT_USER,responseVo.getData());

        log.info("/user/login sessionId:"+httpSession.getId());
        return responseVo;
    }

    //session 是保存在内存中的，所以服务重启后就消失了；可以采用token+redis（token指代cookie）
    //服务器把sessionId发送给浏览器，浏览器会将它作为cookie(JSESSIONID:********)
    @GetMapping("/user")
    public ResponseVo<User> userInfo(HttpSession session){
        log.info("/user sessionId:"+session.getId());
        User user = (User) session.getAttribute(MallConstant.CURRENT_USER);
        return ResponseVo.success(user);
    }

    //可以使用拦截器，避免代码重复
    @PostMapping("/user/logout")
    public ResponseVo logout(HttpSession session){
        log.info("/user/logout sessionId:"+session.getId());

        session.removeAttribute(MallConstant.CURRENT_USER);
        return ResponseVo.success();
    }

}
