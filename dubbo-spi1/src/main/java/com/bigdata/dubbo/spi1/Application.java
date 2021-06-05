package com.bigdata.dubbo.spi1;

import com.bigdata.dubbo.spi1.service.UploadCDN;
import org.apache.dubbo.common.extension.ExtensionLoader;

public class Application {

    public static void main(String[] args) {
        UploadCDN qiyi = ExtensionLoader.getExtensionLoader(UploadCDN.class).getExtension("qiyi");
        qiyi.upload("hello");
    }

}
