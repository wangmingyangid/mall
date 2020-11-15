package org.wmy.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wmy.mall.dao.ShippingMapper;
import org.wmy.mall.enums.ResponseEnum;
import org.wmy.mall.form.ShippingForm;
import org.wmy.mall.pojo.Shipping;
import org.wmy.mall.service.IShippingService;
import org.wmy.mall.vo.ResponseVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wmy
 * @create 2020-11-14 15:23
 */

@Service
public class ShippingServiceImpl implements IShippingService {
    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ResponseVo<Map<String,Integer>> add(Integer uid, ShippingForm form) {
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(form,shipping);
        shipping.setUserId(uid);
        int row = shippingMapper.insertSelective(shipping);
        if(row ==0 ){
            return ResponseVo.error(ResponseEnum.ERROR);
        }
        HashMap<String, Integer> map = new HashMap<>();
        map.put("shippingId",shipping.getId());

        return ResponseVo.success(map);
    }

    @Override
    public ResponseVo delete(Integer uid, Integer shippingId) {
        int row = shippingMapper.deleteByIdAndUid(uid, shippingId);
        if(row ==0 ){
            return ResponseVo.error(ResponseEnum.DELETE_SHIPPING_FAIL);
        }
        return ResponseVo.success();
    }

    @Override
    public ResponseVo update(Integer uid, Integer shippingId, ShippingForm form) {
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(form,shipping);
        shipping.setUserId(uid);
        shipping.setId(shippingId);
        int row = shippingMapper.updateByPrimaryKeySelective(shipping);
        if(row == 0 ){
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        return ResponseVo.success();
    }

    @Override
    public ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUid(uid);
        PageInfo<Shipping> pageInfo = new PageInfo<>(shippingList);

        return ResponseVo.success(pageInfo);
    }
}
