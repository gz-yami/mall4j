> 有很多人不清楚我们的swagger ui的路径，由于我们使用了更为好用的`swagger-bootstrap-ui`，实际上使 域名+端口 +/doc.html，如：http://localhost:8086/doc.html



在没有Swagger之前，我们需要自己手写文档，手写文档的出现问题：

1. 文档更新时需要要与前端人员进行对接，文档存在更新不及时
2. 接口文档多，没有进行分组管理，增加管理难度
3. 不能直接在线接口调试，通常需要借助工具（如postman）,效率大大降低
4. 接口说明与返回结果不明确

而通过swagger就能轻松解决这些问题，而且`spirngboot`整合swagger也相对简单

[swagger官网](https://swagger.io/)

## 添加依赖

```
 <dependency>
     <groupId>io.springfox</groupId>
     <artifactId>springfox-swagger2</artifactId>
     <version>1.9.3</version>
</dependency>
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>swagger-bootstrap-ui</artifactId>
    <version>2.9.2</version>
</dependency>
```

## 添加配置类并开启

在**yami-shop-api**工程中的**config**文件下，有swagger相应的配置类，只要了解具体能配置哪些东西就好了，毕竟这个东西配置一次之后就不用再动了

```java
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

     @Bean
     public Docket baseRestApi() {
         return new Docket(DocumentationType.SWAGGER_2)
         .apiInfo(apiInfo())
         .groupName("基础版")
         .select()
         .apis(RequestHandlerSelectors.basePackage("com.yami.shop.api"))
         .paths(PathSelectors.any())
         .build();
     }
     @Bean
     public ApiInfo apiInfo() {
         return new ApiInfoBuilder()
         .title("mall4j商城接口文档")
         .description("mall4j商城接口文档Swagger版")
         .termsOfServiceUrl("http://www.gz-yami.com/")
         .contact(new Contact("广州市蓝海创新科技有限公司","https://www.mall4j.com/", ""))
         .version("1.0")
         .build();
     }
}
```

特别要注意的是里面配置了api文件也就是controller包的路径，不然生成的文档扫描不到接口

```java
apis(RequestHandlerSelectors.basePackage("com.yami.shop.api"))
```

用`@Configuration`注解该类，让spring托管这个类，`@Bean`标注方法等价于XML中配置bean

用`@EnableSwagger2`标识要开启`Swagger2`

## 接口使用

在配置好之后，我们就可以对swagger进行使用,比如在`AreaController`类中

```java
@RestController
@RequestMapping("/p/area")
@Api(tags="省市区接口")
public class AreaController {

    @Autowired
    private AreaService areaService;
    
    @GetMapping("/listByPid")
    @ApiOperation(value="获取省市区信息", notes="根据省市区的pid获取地址信息")
    @ApiImplicitParam(name = "pid", value = "省市区的pid(pid为0获取所有省份)", required = true, dataType = "String")
    public ResponseEntity<List<Area>> listByPid(Long pid){
        List<Area> list = areaService.listByPid(pid);
        return ResponseEntity.ok(list);
    }
}
```

`@Api(tags="省市区接口")`定义标签分组接口，在这个类下定义的所有接口将位于这个标签之下

`@ApiOperation()`定义具体的接口标题信息，notes可以为这个标签添加注释

`@ApiImplicitParam()`对应的参数列表信息，用户告诉前端开发人员，这个接口需要传递什么参数及参数的说明

如有多个参数需要说明，可使用`@ApiImplicitParams()`下面可包含多个`@ApiImplicitParam()`

## 实体类

```java
@Data
@TableName("tz_area")
public class Area implements Serializable {
    private static final long serialVersionUID = -6013320537436191451L;
    @TableId
    @ApiModelProperty(value = "地区id",required=true)
    private Long areaId;

    @ApiModelProperty(value = "地区名称",required=true)
    private String areaName;

    @ApiModelProperty(value = "地区上级id",required=true)
    private Long parentId;

    @ApiModelProperty(value = "地区层级",required=true)
    private Integer level;

    @TableField(exist=false)
    private List<Area> areas;
}
```

`@ApiModelProperty()`利用这个注解可以告诉前端开发人员该字段代表的含义

## 常用注解

| 注解               | 作用                                 |
| ------------------ | ------------------------------------ |
| @Api               | 修饰整个类，描述Controller的作用     |
| @ApiOperation      | 描述一个类的一个方法，或者说一个接口 |
| @ApiParam          | 单个参数描述                         |
| @ApiModel          | 用对象来接收参数                     |
| @ApiProperty       | 用对象接收参数时，描述对象的一个字段 |
| @ApiResponse       | HTTP响应其中1个描述                  |
| @ApiResponses      | HTTP响应整体描述                     |
| @ApiIgnore         | 使用该注解忽略这个API                |
| @ApiError          | 发生错误返回的信息                   |
| @ApiImplicitParam  | 一个请求参数                         |
| @ApiImplicitParams | 多个请求参数                         |



## 分页参数的文档以及关于swagger文档的骚操作



我们仔细留意swagger文档，可以发现 swagger文档返回接口数据的url为：`/v2/api-docs` 。这个url被 `springfox.documentation.swagger2.web.Swagger2Controlle#getDocumentation()` 关联。通过`jsonSerializer.toJson(swagger)` 生成特定的json文档。



当我们使用`PageParam<T>` 这个分页参数生成文档的时候，总是会返回泛型里面的对象信息，我们根据无论使用`@ApiParam(hidden = true)` 又或者是 `@JsonIgnore` 都无效，所以我们可以修改自己的`jsonSerializer`生成的响应的json



自定义Swagger 的序列化，去除分页参数中的records值

```java
public class SpringfoxJsonSerializer extends JsonSerializer {

    public SpringfoxJsonSerializer(List<JacksonModuleRegistrar> modules) {
        super(modules);
    }

    @Override
    public Json toJson(Object toSerialize) {
        if (!(toSerialize instanceof Swagger)) {
            return super.toJson(toSerialize);
        }
        Swagger swagger = (Swagger)toSerialize;

        swagger.getPaths().forEach((key, path) ->{
            Operation get = path.getGet();
            if (get != null) {

                List<Parameter> parameters = get.getParameters();
                if (parameters != null) {
                    parameters.removeIf(parameter -> parameter.getName().startsWith("records[0]."));
                }
            }
        });

        return super.toJson(swagger);
    }
}
```



新序列化的bean

```java
@Configuration
public class SpringFoxJsonSerializerConfig {

    @Bean
    @Primary
    public JsonSerializer yamiSpringfoxJsonSerializer(List<JacksonModuleRegistrar> moduleRegistrars) {
        return new SpringfoxJsonSerializer(moduleRegistrars);
    }
}
```

