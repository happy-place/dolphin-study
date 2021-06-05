package com.bigdata.java.spi.service.impl;

import com.bigdata.java.spi.service.UploadCDN;

public class QiyiCDN implements UploadCDN {
    @Override
    public void upload(String url) {
        System.out.println("upload to qiyi cdn");
    }
}
