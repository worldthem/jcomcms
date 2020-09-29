package ecommercejava.cms.icommyjava.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import javax.servlet.ServletContext;


@Configuration
@ComponentScan("ecommercejava.cms.icommyjava")

@EnableWebMvc
@EnableJpaRepositories("ecommercejava.cms.icommyjava.repository")
public class MvcConfig implements WebMvcConfigurer {
    private final ApplicationContext applicationContext;
    @Autowired
    private ServletContext servletContext;

    @Value("${static.path}")
    private String staticPath;

    public MvcConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ViewInterceptor());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        if(staticPath != null) {
            System.out.println("Serving static content from " + staticPath);
            registry.addResourceHandler("/**").addResourceLocations(staticPath);
        }
    }



/*
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**", "/content/images/**", "/content/imgproduct/**", "/content/lang/**", "/content/views/**")
               // .addResourceHandler("/images/**").addResourceLocations("file:images/");
                .addResourceLocations("classpath:/META-INF/resources/", "classpath:/resources/",
                        "classpath:/static/", "classpath:/public/", "classpath:/templates/",
                        "file:content/images/","file:content/imgproduct/", "file:content/lang/", "file:content/views/").setCachePeriod(0);
    }

 */

    @Bean
    public ViewResolver getViewResolver() {

        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setCache(false);
        //  resolver.setPrefix("");
        resolver.setSuffix(".html");
        resolver.setPrefix("");
        resolver.setContentType("text/html;charset=utf-8");
        return resolver;

    }
/*
    @Bean
    public FreeMarkerConfigurer freemarkerConfig() {
        FreeMarkerConfigurer result = new FreeMarkerConfigurer();
        result.setTemplateLoaderPath("file:content/views/");
        return result;
   }

 */


}
