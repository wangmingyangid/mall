package org.wmy.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author wmy
 * @create 2020-11-07 17:30
 */

@Data
public class UserLoginForm {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
