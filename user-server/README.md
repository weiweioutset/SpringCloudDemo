# 工程简介
用户服务工程，提供用户的注册，账号生成，登录等相关服务
# 延伸阅读
用户id需要调用分布式Id生成服务id-generator，对于特殊号段(如88888888这种账号，需要特殊处理)

工程结构  
|-- common 基础类型包  
|-- config 配置类包  
|-- controller 接口  
|-- enums 枚举类  
|-- form 表单包装类(请求体)  
|-- mapper mapper包  
|-- po 实体类包  
|-- service service包  
    |-- api 接口类   
    |-- impl 接口实现类    
|-- utils 工具包  
|-- vo 包装类包  
|-- UserServiceApplication.java 启动类  