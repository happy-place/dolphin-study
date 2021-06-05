# JDK-SPI

## SPI
```aidl
SPI全称Service Provider Interface，是Java提供的一套用来被第三方实现或者扩展的接口，它可以用来启用框架扩展和替换组件。 
SPI的作用就是为这些被扩展的API寻找服务实现。
```

## 作用机理
```aidl
1.一个抽象接口，对应多个具体实现，具体谁起作用，取决于 META-INF/services/接口全类名 文件中声明的是谁？
2.通过 com.sun.tools.javac.util.ServiceLoader#load 状态接口实现类

获取类加载器 -> 创建LazyIterator迭代器，扫描全部jar，读取 META-INF/services/ 下所有文件 -> 按指示实例化接口拓展类对象，并加载到JVM中
```

## 存在的问题
```aidl
1.配置文件只包含接口实现类的全类名，并没有缩写命名，导致程序中很难精准引用
2.扩展如果依赖其他拓展(比如其他spring bean)无法自动注入和装配，因此难以与其他框架集成
3.接口(拓展点)，如果有多个实现(拓展)，并且都在 META-INF/services/接口全类名 配置文件中注释，需要实例化全部接口实现，才能引用其中指定实例，
不能有针对性实例化，然后引用。
```