package org.wmy.mall.service;

import org.wmy.mall.form.CartAddForm;
import org.wmy.mall.form.CartUpdateForm;
import org.wmy.mall.pojo.CartItem;
import org.wmy.mall.vo.CartVo;
import org.wmy.mall.vo.ResponseVo;

import java.util.List;

/**
 * @author wmy
 * @create 2020-11-13 19:12
 */
public interface ICartService {
    ResponseVo<CartVo> add(Integer uid,CartAddForm cartAddForm);

    ResponseVo<CartVo> list(Integer uid);

    ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm form);

    ResponseVo<CartVo> delete(Integer uid, Integer productId);

    ResponseVo<CartVo> selectAll(Integer uid);

    ResponseVo<CartVo> unSelectAll(Integer uid);

    ResponseVo<Integer> sum(Integer uid);

    List<CartItem> listForCartItem(Integer uid);

}
