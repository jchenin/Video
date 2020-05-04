package jin.chen;

import jin.chen.interceptor.ControllerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//作为配置文件
@Configuration
public class StatisResourcesConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //资源映射，/**代表访问所有资源
        registry.addResourceHandler("/**")
                      //资源所在目录
                     .addResourceLocations("file:C:/videos-dev/")
                     //swagger是以静态资源所发布的，这里也需要映射下它所在的地址
                     .addResourceLocations("classpath:/META-INF/resources/");
    }

    //拦截器作为一个bean注入配置当中
    @Bean
    public ControllerInterceptor controllerInterceptor(){
        return new ControllerInterceptor();
    }

    //zookeeper监听
    @Bean(initMethod = "init")
    public ZKCuratorClient zKCuratorClient(){
        return new ZKCuratorClient();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(controllerInterceptor())
                .addPathPatterns("/user/uploadFace", "/user/queryUserInfo")
                .addPathPatterns("/bgm/**")
                .addPathPatterns("/video/uploadVideo", "/video/addVideoLikeCount", "/video/reduceVideoLikeCount", "/video/report", "/video/saveComment");
    }
}
