package com.bigdata.dubbo.spi1.service;

import org.apache.dubbo.common.extension.SPI;

@SPI
public interface UploadCDN {
    void upload(String url);
}
