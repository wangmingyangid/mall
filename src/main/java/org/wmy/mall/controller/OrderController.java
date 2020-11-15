package org.wmy.mall.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.wmy.mall.constant.MallConstant;
import org.wmy.mall.form.OrderCreateForm;
import org.wmy.mall.pojo.User;
import org.wmy.mall.service.IOrderService;
import org.wmy.mall.vo.OrderVo;
import org.wmy.mall.vo.ResponseVo;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author wmy
 * @create 2020-11-15 20:21
 */

@RestController
public class OrderController {
    @Autowired
    private IOrderService orderService;

    @PostMapping("/orders")
    public ResponseVo<OrderVo> create(@Valid @RequestBody OrderCreateForm form,
                                      HttpSession session){
        User user = (User) session.getAttribute(MallConstant.CURRENT_USER);
        return orderService.create(user.getId(), form.getShippingId());
    }

    @GetMapping("/orders")
    public ResponseVo<PageInfo> list(@RequestParam("pageNum") Integer pageNum,
                                     @RequestParam("pageSize") Integer pageSize,
                                     HttpSession session){
        User user = (User) session.getAttribute(MallConstant.CURRENT_USER);
        return orderService.list(user.getId(), pageNum,pageSize);
    }
    @GetMapping("/orders/{orderNo}")
    public ResponseVo<OrderVo> detail(@PathVariable("orderNo") Long orderNo,
                                     HttpSession session){
        User user = (User) session.getAttribute(MallConstant.CURRENT_USER);
        return orderService.detail(user.getId(), orderNo);
    }
    @PutMapping("/orders/{orderNo}")
    public ResponseVo cancel(@PathVariable("orderNo") Long orderNo,
                                      HttpSession session){
        User user = (User) session.getAttribute(MallConstant.CURRENT_USER);
        return orderService.cancel(user.getId(), orderNo);
    }
}
