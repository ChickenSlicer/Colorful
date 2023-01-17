package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 在需要为这个项目打包的时候，首先需要将本类中注释的部分解除，然后是将config包下的
 * WebSocketConfig以及ChatRoomService类中注释的代码解除，并且ChatRoomService中
 * 还需要将@Service注解给注释掉，最后需要在pom.xml文件中将注释的部分解除
 */
@EnableScheduling
@SpringBootApplication
public class Application extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }
}
