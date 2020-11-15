package org.wmy.mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.wmy.mall.constant.MallConstant;
import org.wmy.mall.form.CartAddForm;
import org.wmy.mall.form.CartUpdateForm;
import org.wmy.mall.pojo.User;
import org.wmy.mall.service.ICartService;
import org.wmy.mall.vo.CartVo;
import org.wmy.mall.vo.ResponseVo;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author wmy
 * @create 2020-11-10 21:05
 */

@RestController
public class CartController {

    @Autowired
    private ICartService cartService;

    @GetMapping("/carts")
    public ResponseVo<CartVo> list(HttpSession httpSession){
        User user = (User)httpSession.getAttribute(MallConstant.CURRENT_USER);
        return cartService.list(user.getId());
    }

    @PostMapping("/carts")
    public ResponseVo<CartVo> add(@Valid @RequestBody CartAddForm cartAddForm,
                                  HttpSession httpSession){

        User user = (User)httpSession.getAttribute(MallConstant.CURRENT_USER);
        return cartService.add(user.getId(),cartAddForm);
    }

    @PutMapping("/carts/{productId}")
    public ResponseVo<CartVo> update(@PathVariable Integer productId,
                                     @Valid @RequestBody CartUpdateForm form,
                                     HttpSession httpSession){

        User user = (User)httpSession.getAttribute(MallConstant.CURRENT_USER);
        return cartService.update(user.getId(),productId,form);
    }
    @DeleteMapping("/carts/{productId}")
    public ResponseVo<CartVo> delete(@PathVariable Integer productId,
                                     HttpSession httpSession){

        User user = (User)httpSession.getAttribute(MallConstant.CURRENT_USER);
        return cartService.delete(user.getId(),productId);
    }
    @PutMapping("/carts/selectAll")
    public ResponseVo<CartVo> selectAll(HttpSession httpSession){

        User user = (User)httpSession.getAttribute(MallConstant.CURRENT_USER);
        return cartService.selectAll(user.getId());
    }

    @PutMapping("/carts/unSelectAll")
    public ResponseVo<CartVo> unSelectAll(HttpSession httpSession){

        User user = (User)httpSession.getAttribute(MallConstant.CURRENT_USER);
        return cartService.unSelectAll(user.getId());
    }
    @GetMapping("/carts/products/sum")
    public ResponseVo<Integer> sum(HttpSession httpSession){

        User user = (User)httpSession.getAttribute(MallConstant.CURRENT_USER);
        return cartService.sum(user.getId());
    }

}
