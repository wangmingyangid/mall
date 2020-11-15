package org.wmy.mall.service;

import com.github.pagehelper.PageInfo;
import org.wmy.mall.form.ShippingForm;
import org.wmy.mall.vo.ResponseVo;

import java.util.Map;

/**
 * @author wmy
 * @create 2020-11-14 15:19
 */
public interface IShippingService {
    ResponseVo<Map<String,Integer>> add(Integer uid, ShippingForm form);

    ResponseVo delete(Integer uid, Integer shippingId);

    ResponseVo update(Integer uid, Integer shippingId,ShippingForm form);

    ResponseVo<PageInfo> list(Integer uid, Integer pageNum,Integer pageSize);
}
