package org.wmy.mall.form;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author wmy
 * @create 2020-11-10 21:03
 */

@Data
@AllArgsConstructor
public class CartAddForm {
    @NotNull
    private Integer productId;

    private Boolean selected;
}
