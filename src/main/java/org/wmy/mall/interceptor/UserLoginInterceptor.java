package org.wmy.mall.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.wmy.mall.constant.MallConstant;
import org.wmy.mall.enums.ResponseEnum;
import org.wmy.mall.exception.UserLoginException;
import org.wmy.mall.pojo.User;
import org.wmy.mall.vo.ResponseVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wmy
 * @create 2020-11-07 20:40
 */
@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {

    /**
     * 返回true代表流程继续，false表示中断流程
     * 被拦截的路径都会先进行该方法的执行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle.....");
        User user = (User) request.getSession().getAttribute(MallConstant.CURRENT_USER);
        if(user == null) {
            log.info("user == null");
            //为了给前端返回的结果有内容，可以采用抛异常，然后由统一异常处理
            throw new UserLoginException();

            //return false;
        }
        return true;
    }
}
