package com.mine.pub.handlers.websocket;


import com.jfinal.handler.Handler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebSocketHandler extends Handler
{
  public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled)
  {
    int index = target.indexOf("/mywebsocket");
    if (index == -1)
      this.next.handle(target, request, response, isHandled);
  }
}
