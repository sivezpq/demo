责任链模式是一种行为设计模式， 允许你将请求沿着处理者链进行发送。收到请求后， 每个处理者均可对请求进行处理， 或将其传递给链上的下个处理者。

![](D:\intellij\customize\demo\designpattern\src\main\java\com\designpattern\responsibilitychain\doc\640.png)

责任链的使用场景还是比较多的：

- 多条件流程判断：权限控制
- ERP 系统流程审批：总经理、人事经理、项目经理
- Java 过滤器的底层实现 Filter

先来简单介绍一下责任链设计模式的基本组成：

- **抽象处理者（Handler）角色：**定义一个处理请求的接口，包含抽象处理方法和一个后继连接。
- **具体处理者（Concrete Handler）角色：**实现抽象处理者的处理方法，判断能否处理本次请求，如果可以处理请求则处理，否则将该请求转给它的后继者。
- **客户类（Client）角色：**创建处理链，并向链头的具体处理者对象提交请求，它不关心处理细节和请求的传递过程。

![](D:\intellij\customize\demo\designpattern\src\main\java\com\designpattern\responsibilitychain\doc\641.png)