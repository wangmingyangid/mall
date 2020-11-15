package org.wmy.mall.enums;

import lombok.Getter;

/**
 * @author wmy
 * @create 2020-11-14 21:03
 */

@Getter
public enum PaymentTypeEnum {
    PAY_ONLINE(1),
    ;
    Integer code;

    PaymentTypeEnum(Integer code) {
        this.code = code;
    }
}
