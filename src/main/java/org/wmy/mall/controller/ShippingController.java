package org.wmy.mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.wmy.mall.constant.MallConstant;
import org.wmy.mall.form.ShippingForm;
import org.wmy.mall.pojo.User;
import org.wmy.mall.service.IShippingService;
import org.wmy.mall.vo.ResponseVo;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author wmy
 * @create 2020-11-14 16:52
 */

@RestController
public class ShippingController {
    @Autowired
    private IShippingService shippingService;

    @PostMapping("/shoppings")
    public ResponseVo add(@Valid @RequestBody ShippingForm form,
                          HttpSession session){
        User user = (User) session.getAttribute(MallConstant.CURRENT_USER);

        return shippingService.add(user.getId(),form);
    }

    @DeleteMapping("/shoppings/{shippingId}")
    public ResponseVo delete(@PathVariable Integer shippingId,
                          HttpSession session){
        User user = (User) session.getAttribute(MallConstant.CURRENT_USER);

        return shippingService.delete(user.getId(),shippingId);
    }

    @PutMapping("/shoppings/{shippingId}")
    public ResponseVo update(@PathVariable Integer shippingId,
                             @Valid @RequestBody ShippingForm form,
                             HttpSession session){
        User user = (User) session.getAttribute(MallConstant.CURRENT_USER);

        return shippingService.update(user.getId(),shippingId,form);
    }

    @GetMapping("/shoppings")
    public ResponseVo update(@RequestParam(required = false,defaultValue = "1") Integer pageNum,
                             @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                             HttpSession session){
        User user = (User) session.getAttribute(MallConstant.CURRENT_USER);

        return shippingService.list(user.getId(),pageNum,pageSize);
    }

}
