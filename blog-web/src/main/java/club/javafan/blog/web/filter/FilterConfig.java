package club.javafan.blog.web.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FilterConfig implements WebMvcConfigurer {
    @Value("${file.file-path}")
    private String FILE_PATH;
    @Value("${linux.deploy-path}")
    private String DEPLOY_PATH;
    @Autowired
    private AdminLoginInterceptor adminLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加一个拦截器，拦截以/admin为前缀的url路径
        registry.addInterceptor(adminLoginInterceptor).addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login").excludePathPatterns("/admin/dist/**")
                .excludePathPatterns("/admin/plugins/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 路径映射
        String systemPath = System.getProperty("user.dir").replaceAll("\\\\", "/");
        //修复linux自启动jar无法获取项目路径，默认为/根目录的bug
        if (systemPath.equals("/")) {
            System.setProperty("user.dir", DEPLOY_PATH);
        }
        String path = systemPath.equals("/")?DEPLOY_PATH:systemPath + FILE_PATH;
        System.out.println("文件上传路径映射: " + path);
        registry.addResourceHandler( "/upload/img/**") //捕捉UploadController返回的uri映射到下面的Locations
                .addResourceLocations("file:" + path); //图片的虚拟路径： 当前系统目录 + file_path
    }
    /**
     1. 假设固定上传地址为： FILE_PATH = /upload

     2. Win本地 部署启动jar路径 = 项目目录
        //win上传到当前目录的
        //path = 当前路径 + FILE_PATH = E:/develop/HTKY/blog + /upload
        String path = System.getProperty("user.dir").replaceAll("\\\\", "/") + FILE_PATH;

     3. Linux 部署启动jar路径 = /work/deploy
         //path = 当前路径 + FILE_PATH = /work/deploy + /upload
         String path = System.getProperty("user.dir").replaceAll("\\\\", "/") + FILE_PATH;

     注意，设置linux开机以root自启动jar会访问不到上传图片，因为
     报错日志：
     文件上传路径映射: //upload

     2020-08-14 21:50:41.292 ERROR 1142 --- [-nio-443-exec-4] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception
     java.net.UnknownHostException: upload

     */

}