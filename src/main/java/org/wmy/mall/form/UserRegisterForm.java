package org.wmy.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author wmy
 * @create 2020-11-07 15:02
 */

@Data
public class UserRegisterForm {

    /**
     *  @NotBlank 用于字符串；会判断空格（空格也会报错）
     *  @NotNull
     *  @NotEmpty 用于集合
     *
     *  @NotBlank(message = "password 不能为空")，也可以这样写，
     *  bindingResult.getFieldError().getDefaultMessage()会得到该信息
     */
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String email;
}
