## design

---

### ipc 信息

======================================================================
+ my ipc

rtsp://192.168.9.6:554/0

add device
http://192.168.0.1/onvif/device_service


======================================================================
//                                  eye4 摄像头 2018/4/13 13 14:59
192.168.9.100 admin@888888   UID: VSTA-503020-MXWJM 可以通过ONVIF-PTZ

rtsp://admin:888888@192.168.9.100:10554/tcp/av0_0

======================================================================
//                                  海尔摄像头 2018/4/13 13 14:24 pop-fbp YTD05 1590679819 4000172660
不能PTZ

39XXX@qq.com aaa123456

设备id 4650112， hipc@hipc123456
用户名密码

pc 端： admin， 无密码

dlink 路由器 用户名: admin 无登录密码
mercury 路由器 192.168.9.2 无密码， wifi ssid dlink@11111111



---


### IPC 功能：
有一个通用 Settings， 打开 调试选项。


有 Identification 功能
有 Time settings 功能
有 Maintenance 功能
有 Network settings 功能
有 User management 功能
有 Relays 功能
有 Web page 功能
有 Events 功能
有 Live video 功能
有 Video Sreaming 功能
有 PTZ 功能
有 Profiles 功能

用户能够手动添加 ipc



---


2018-11-15 task
点击 setting， 打开debug 选项
点击 discovery， 搜索 ， 如果 debug ， 把192.168.9.6 加入
可以 手动添加设备


### 读取AppSettigns
用户打开 App 时， 读取整个 AppSettings， 并更新view。
用户设置 AppSettings， 保存Appsettings

AppSettings 通过 menu item 跳转到 activity，点击保存或取消后返回主界面。


[Android开发——设置界面的创建](http://www.cnblogs.com/kexing/p/8548648.html)
[Android：详解如何创建Google风格的SettingsActivity](https://www.jianshu.com/p/f5f8834ee9af)

