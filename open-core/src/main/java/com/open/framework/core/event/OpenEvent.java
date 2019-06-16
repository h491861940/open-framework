package com.open.framework.core.event;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 事件监听基类
 */
@Data
@AllArgsConstructor
public class OpenEvent {
  private String msg;
}