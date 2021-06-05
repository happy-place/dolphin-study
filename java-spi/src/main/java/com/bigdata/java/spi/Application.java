package com.bigdata.java.spi;

import com.sun.tools.javac.util.ServiceLoader;
import com.bigdata.java.spi.service.UploadCDN;

public class Application {
    public static void main(String[] args) {
        ServiceLoader<UploadCDN> uploadCDN = ServiceLoader.load(UploadCDN.class);
        for (UploadCDN u : uploadCDN) {
            u.upload("filePath");
        }
    }

}
