package org.wmy.mall.exception;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wmy.mall.enums.ResponseEnum;
import org.wmy.mall.vo.ResponseVo;

import java.util.Objects;

import static org.wmy.mall.enums.ResponseEnum.ERROR;

/**
 * @author wmy
 * @create 2020-11-07 16:39
 */

@ControllerAdvice
public class RuntimeExceptionHandler {
    /**
     *
     *   @ExceptionHandler(RuntimeException.class) //指定拦截异常的类型
     *   @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //自定义返回给浏览器状态码
     *
     *   该类的功能是：全局出现RuntimeException时，被handle方法处理，可以统一返回给前端的信息
     */

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseVo handle(RuntimeException e){
        return ResponseVo.error(ERROR,e.getMessage());
    }

    @ExceptionHandler(UserLoginException.class)
    @ResponseBody
    public ResponseVo userLoginHandle(){
        return ResponseVo.error(ResponseEnum.NEED_LOGIN);
    }

    /**
     *  @Valid字段会验证表单的正确性，如果不满足条件会报MethodArgumentNotValidException
     *
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseVo notValidExceptionHandle(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        Objects.requireNonNull(bindingResult.getFieldError());
        return ResponseVo.error(ResponseEnum.PARAM_ERROR,bindingResult);
    }

}
