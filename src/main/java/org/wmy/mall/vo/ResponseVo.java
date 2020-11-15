package org.wmy.mall.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.validation.BindingResult;
import org.wmy.mall.enums.ResponseEnum;

/**
 * @author wmy
 * @create 2020-11-07 14:35
 *
 * @JsonInclude(value = JsonInclude.Include.NON_NULL)
 * 该注解表示返回的对象中如果有的属性为null值，则不会返回该属性
 */

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ResponseVo<T> {
    private Integer status;
    private String msg;
    private T data;

    private ResponseVo(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }
    private ResponseVo(Integer status, T data) {
        this.status = status;
        this.data = data;
    }
    public static <T> ResponseVo<T> successByData(String msg){
        return new ResponseVo<>(ResponseEnum.SUCCESS.getCode(),msg);
    }
    
    public static <T> ResponseVo<T> success(T data){
        return new ResponseVo<>(ResponseEnum.SUCCESS.getCode(),data);
    }
    public static <T> ResponseVo<T> success(){
        return new ResponseVo<>(ResponseEnum.SUCCESS.getCode(),ResponseEnum.SUCCESS.getDesc());
    }

    public static <T> ResponseVo<T> error(ResponseEnum responseEnum){
        return new ResponseVo<>(responseEnum.getCode(),responseEnum.getDesc());
    }
    public static <T> ResponseVo<T> error(ResponseEnum responseEnum,String msg){
        return new ResponseVo<>(responseEnum.getCode(),msg);
    }

    public static <T> ResponseVo<T> error(ResponseEnum responseEnum, BindingResult bindingResult){
        return new ResponseVo<>(responseEnum.getCode(),bindingResult.getFieldError().getField()+""
                +bindingResult.getFieldError().getDefaultMessage());
    }
}
