package org.wmy.mall.enums;

import lombok.Getter;

/**
 * @author wmy
 * @create 2020-11-07 12:03
 *
 * 角色0-管理员,1-普通用户
 */

@Getter
public enum RoleEnum {
    ADMIN(0),
    CUSTOMER(1),
    ;
    Integer code;

    RoleEnum (Integer code){
        this.code=code;
    }
}
