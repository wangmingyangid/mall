package org.wmy.mall.listener;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wmy.mall.pojo.PayInfo;
import org.wmy.mall.service.IOrderService;

/**
 * @author wmy
 * @create 2020-11-15 20:45
 *
 * PayInfo 的正确使用姿势：pay项目提供jar包，mall项目引入该jar包，就可以使用了
 */

@Component
@RabbitListener(queues = "payNotify")
@Slf4j
public class PayMsgListener {

    @Autowired
    private IOrderService orderService;

    @RabbitHandler
    public void process(String msg){
        log.info("接受到的消息{}",msg);
        PayInfo payInfo = new Gson().fromJson(msg, PayInfo.class);
        //由于这里没有引入pay项目的jar包，所以硬编码了
        if(payInfo.getPlatformStatus().equals("SUCCESS")){
            //修改订单状态
            orderService.paid(payInfo.getOrderNo());
        }
    }
}
