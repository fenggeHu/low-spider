# low-spider
简单、低级的spider，自己定制业务实现提取信息

# handler
所有的过程都是一连串的处理程序。
并发？大部分情况不需要！个人轻量级使用的情况下，很多时候并发大量请求会导致账号或ip被封或服务器限流

# spring
可以定制多个spider实例，注册成spring bean

# mvn build
mvn clean deploy --settings ~/.m2/settings-ossrh.xml
