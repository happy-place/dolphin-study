package com.bigdata.java.spi.service.impl;

import com.bigdata.java.spi.service.UploadCDN;

public class ChinaNetCDN implements UploadCDN {
    @Override
    public void upload(String url) {
        System.out.println("upload to chinaNet cdn");
    }
}
