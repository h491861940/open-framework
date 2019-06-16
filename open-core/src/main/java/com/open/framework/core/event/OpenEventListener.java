package com.open.framework.core.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 事件监听处理
 */
@Component
public class OpenEventListener {

  /**
   * 异步监听,不加async是同步
   * @param event
   */
  @EventListener
  @Async
  public void handleOrderEvent(OpenEvent event) {
    try{
      Thread.sleep(10000);
    }catch(Exception e){
      e.printStackTrace();
    }
    System.out.println("我监听到了OpenEvent发布的message为:" + event.getMsg());
  }
}