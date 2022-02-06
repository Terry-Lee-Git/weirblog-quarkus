# weirblog-quarkus

#### 介绍
quarkus 写的博客系统。这里着重介绍下quarkus是什么，当然大家可以去百度，我接触也是在2020年也没有一年时间。
[这是官网](https://quarkus.io/) 当然是英文的，我所了解的到目前为止能和spring抗衡的框架如果说有一个那就是quarkus了，这是我的直观感受。
再介绍下我的博客吧，后端是mysql quarkus-hibernate-orm-panache 和qute 模板 管理后台是easyui，这也是用了我用的最熟悉的东西没什么新奇的。
之前博客当然是用的spring + jpa很老的版本了。
另外想说的是graalvm，我想大家或多或少也听说过，我这次用quarkus重构的博客可以运行到基本所有的jvm上面，
当然我没有每一个都测试过，这点自信也是quarkus这个框架给的。
最后我想说的是我只是想给大家展示下quarkus的很小的一部分功能，我能力也有限也在不断研究学习中[这个是B站我分享的YouTube上面的quarkus视频](https://space.bilibili.com/36507008)，这个都是英文的大家凑活着看。目前开源出来的还有很多不完善的地方这个大家不必太在意，另外我也没有想怎么着，
毕竟只是个小小的博客还带有很重个人色彩，本身也就是我自己在用。
大家需要在意的是quarkus能做什么和spring相比优势在哪，大家值不值得学习和应用在生产环境中，大家看不看好这种框架，我想当你有一定了解时候你会做出判断。
还要说一下weirblog-quarkus-reactive这个还没有完成从reactive这个单词可以看出是响应式异步方式，目前遇到的困难时分页，这个如果有人知道尽管提交代码。
#### 软件架构
软件架构说明
mysql
quarkus-hibernate-orm-panache
quarkus-qute

#### 结构介绍

quarkus-demo-parent  微服务demo(www.loveweir.com博客文章有介绍)，网关利用的是spring cloud gateway有点别扭，
注册中心是consule，微服务之间通讯是microservices的rest client
quarkus-eventuate-tram-sagas-demo  saga事务demo(未跑通)
weirblog-quarkus www.loveweir.com 博客前后端源码
weirblog-quarkus-reactive 博客前后端源码-全异步
weirblog-quarkus-k8s 博客前后端源码(k8s部署demo)


#### 使用说明

1.  resources 里面有数据库weirblog.sql
2.  运行命令 compile quarkus:dev
3.  如果使用idea 已经自带运行了
4.  后端访问(http://localhost:10801/admin/index)  前端访问(http://localhost:10801)# weirblog-quarkus
