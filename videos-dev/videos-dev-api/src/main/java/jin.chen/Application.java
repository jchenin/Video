package jin.chen;

import  org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "jin.chen.mapper")
@ComponentScan(basePackages = "jin.chen")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
