package org.wmy.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author wmy
 * @create 2020-11-15 20:22
 */

@Data
public class OrderCreateForm {
    @NotNull
    private Integer shippingId;
}
