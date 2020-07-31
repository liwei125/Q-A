package club.javafan.blog.web;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@MapperScan("club.javafan.blog.repository")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan({
        "club.javafan.blog.service"
        , "club.javafan.blog.web.controller"
        , "club.javafan.blog.web.aop"
        , "club.javafan.blog.web.filter"
        , "club.javafan.blog.configs"
        , "club.javafan.blog.common.util"
        , "club.javafan.blog.common.mail.impl"
        , "club.javafan.blog.common.threadpool"
        , "club.javafan.blog.common.qquserinfo"
        , "club.javafan.blog.worker"
})

public class BlogApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext application = SpringApplication.run(BlogApplication.class, args);

        Environment env = application.getEnvironment();
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String port = env.getProperty("http.port");//http.port 80会自动跳转到https的server.port 443
        String path = env.getProperty("server.servlet.context-path") == null ? "" : env.getProperty("server.servlet.context-path");
        System.out.println("\n----------------------------------------------------------\n\t" +
                "Application Blog is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + path + "\n\t" +
                "博客主页: \thttp://" + ip + ":" + port + path + "\n\t" +
                "后台管理系统: \thttp://" + ip + ":" + port + path + "admin\n" +
                "----------------------------------------------------------");
    }

}
