package com.bigdata.dubbo.spi2;

import com.bigdata.dubbo.spi2.service.AdaptiveExt2;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.junit.Test;

public class ApplicationTest {

    /**
     * @SPI("dubbo") // 接口声明使用dubbo 作为默认实现类
     * public interface AdaptiveExt2 {
     *     @Adaptive
     *     String echo(String msg, URL url);
     * }
     */
    @Test
    public void test1(){
        // @SPI("dubbo") 需要再接口注解上指定 用谁
        ExtensionLoader<AdaptiveExt2> extensionLoader = ExtensionLoader.getExtensionLoader(AdaptiveExt2.class);
        AdaptiveExt2 adaptiveExtension = extensionLoader.getAdaptiveExtension();
        URL url = URL.valueOf("test://localhost/test");
        String msg = adaptiveExtension.echo("test", url);
        System.out.println(msg); // dubbo
    }

    /**
     * @SPI("dubbo")
     *  public interface AdaptiveExt2 {
     *      @Adaptive
     *      String echo(String msg, URL url);
     *  }
     *
     *  url（adaptive.ext2=cloud）上声明不使用默认的dubbo，而使用cloud，adaptive.ext2 为接口名称转换过来的
     *
     */
    @Test
    public void test2(){
        ExtensionLoader<AdaptiveExt2> extensionLoader = ExtensionLoader.getExtensionLoader(AdaptiveExt2.class);
        AdaptiveExt2 adaptiveExtension = extensionLoader.getAdaptiveExtension();
        // 注解声明默认暴露的是 @SPI("dubbo")，此处改用 adaptive.ext2=cloud
        URL url = URL.valueOf("test://localhost/test?adaptive.ext2=cloud"); // 类名修改
        String msg = adaptiveExtension.echo("test", url);
        System.out.println(msg); // spring cloud
    }

    /**
     * @SPI("dubbo")
     * public interface AdaptiveExt2 {
     *     @Adaptive
     *     String echo(String msg, URL url);
     * }
     *
     * @Adaptive
     * public class ThriftAdaptiveExt2 implements AdaptiveExt2 {
     *     @Override
     *     public String echo(String msg, URL url) {
     *         return "thrift";
     *     }
     * }
     *
     * 接口实现类上使用通过 @Adaptive 声明默认实现
     *
     */
    @Test
    public void test3(){
        ExtensionLoader<AdaptiveExt2> extensionLoader = ExtensionLoader.getExtensionLoader(AdaptiveExt2.class);
        AdaptiveExt2 adaptiveExtension = extensionLoader.getAdaptiveExtension();
        URL url = URL.valueOf("test://localhost/test?adaptive.ext2=cloud"); // 类名修改
        String msg = adaptiveExtension.echo("test", url);
        System.out.println(msg); // thrift
    }

    /**
     * @SPI("dubbo")
     * public interface AdaptiveExt2 {
     *     @Adaptive({"key"})
     *     String echo(String msg, URL url);
     * }
     *
     * url key 指定谁就是谁
     */
    @Test
    public void test4(){
        ExtensionLoader<AdaptiveExt2> extensionLoader = ExtensionLoader.getExtensionLoader(AdaptiveExt2.class);
        AdaptiveExt2 adaptiveExtension = extensionLoader.getAdaptiveExtension();
        // 注解声明默认暴露的是 @SPI("dubbo")，此处改用 adaptive.ext2=cloud
        URL url = URL.valueOf("test://localhost/test?key=cloud"); // 类名修改
        String msg = adaptiveExtension.echo("test", url);
        System.out.println(msg); // spring cloud
    }

    /**
     * spi 优先级
     * 接口实现类 @Adaptive 默认实现 > url (自定义Key)key=cloud 或 adaptive.ext2=cloud > 接口 @SPI("dubbo") 声明
     *
     */


}
