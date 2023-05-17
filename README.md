# 家政服务平台用户端

## 项目介绍

毕业设计家政服务平台

* [用户端](https://github.com/mysilicons/HouseKeepDemo)
* [商家端](https://github.com/mysilicons/Merchant)
* [管理员端](https://github.com/mysilicons/RuoYi)
* [后台SpringBoot](https://github.com/mysilicons/LoginServer)

## 本项目环境

* JDK17
* Gradle7.5
* Gradle插件7.4.2
* Android Studio 2023.1.1

## 用户端运行方法

1. 克隆项目
2. 安装JDK17
3. 安装Gradle7.5
4. 安装Android Studio 2023.1.1
5. 打开项目
6. 申请高德地图API Key
7. 在`AndroidManifest.xml`中填入API Key
```xml
        <meta-data
            android:name="com.amap.api.v2.apikey"
            android:value="地图的key" />
        <service android:name="com.amap.api.location.APSService" />
```
8. 申请环信API Key
9. 在`MainActivity.java`中填入API Key
```java
//环信
Context context = getApplicationContext();
EMOptions options = new EMOptions();
options.setAppKey("填入API key");
```
10. 运行项目
