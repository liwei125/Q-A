package club.javafan.blog.configs.upload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;
import java.io.File;

/**
 * @author
 * @date 2020/3/26
 * 文件上传呢配置类
 */
@Configuration
public class MultiPartConfig {

    @Value("${file.file-path}")
    private String FILE_PATH;
    @Value("${linux.deploy-path}")
    private String DEPLOY_PATH;

    @Bean
    MultipartConfigElement multipartConfigElement() throws Exception {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String systemPath = System.getProperty("user.dir").replaceAll("\\\\", "/");
        if (systemPath.equals("/")) {
            System.setProperty("user.dir", DEPLOY_PATH);
            systemPath = DEPLOY_PATH;
        }
        String location = systemPath + FILE_PATH;
        File file = new File(location);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new Exception("创建文件夹：" + location + "失败！");
            }
        }
        factory.setLocation(location);
        return factory.createMultipartConfig();
    }
}
