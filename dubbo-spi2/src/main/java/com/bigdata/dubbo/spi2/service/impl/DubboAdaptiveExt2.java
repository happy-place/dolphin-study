package com.bigdata.dubbo.spi2.service.impl;

import com.bigdata.dubbo.spi2.service.AdaptiveExt2;
import org.apache.dubbo.common.URL;

public class DubboAdaptiveExt2 implements AdaptiveExt2 {
    @Override
    public String echo(String msg, URL url) {
        return "dubbo";
    }
}