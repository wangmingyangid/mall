package org.wmy.mall.service;

import com.github.pagehelper.PageInfo;
import org.wmy.mall.vo.OrderVo;
import org.wmy.mall.vo.ResponseVo;

/**
 * @author wmy
 * @create 2020-11-14 17:58
 */

public interface IOrderService {
    ResponseVo<OrderVo> create(Integer uid,Integer shippingId);

    ResponseVo<PageInfo> list(Integer uid,Integer pageNum,Integer pageSize);

    ResponseVo<OrderVo> detail(Integer uid,Long orderNo);

    ResponseVo cancel(Integer uid,Long orderNo);

    void paid(Long orderNo);
}
