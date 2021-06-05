package com.bigdata.dubbo.spi1.service.impl;

import com.bigdata.dubbo.spi1.service.UploadCDN;

public class QiyiCDN implements UploadCDN {
    @Override
    public void upload(String url) {
        System.out.println("upload to qiyi cdn");
    }
}
