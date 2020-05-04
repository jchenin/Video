package jin.chen;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@Configuration
@EnableSwagger2
public class Swagger2 {

    //swagger2的一些基本配置文件，例如一些扫描的包等等
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("jin.chen.controller"))
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                    //标题
                    .title("使用swagger2构建短视频后端api接口文档")
                    //联系人
                   .contact(new Contact("chenjin", "www.wyh21.cn", "1743215401@qq.com"))
                    //描述
                   .description("欢迎访问短视频接口文档，这里是描述信息")
                    //版本号
                   .version("1.0").build();
    }
}
