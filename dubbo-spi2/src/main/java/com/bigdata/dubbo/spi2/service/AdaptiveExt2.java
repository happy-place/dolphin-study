package com.bigdata.dubbo.spi2.service;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;

// 需要指定用谁
@SPI("dubbo")
public interface AdaptiveExt2 {
    @Adaptive({"key"})
    String echo(String msg, URL url);
}