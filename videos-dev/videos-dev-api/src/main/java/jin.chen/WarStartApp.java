package jin.chen;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 相当于使用web.xml的形式去启动部署
 */
public class WarStartApp extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        //使用web.xml运行程序，指向application 启动springboot
        return builder.sources(Application.class);
    }
}
