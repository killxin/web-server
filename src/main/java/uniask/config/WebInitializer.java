package uniask.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration.Dynamic;

public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    public static final Logger logger = LoggerFactory.getLogger(WebInitializer.class);

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    // @Override
    // protected void customizeRegistration(Dynamic registration) {
    //     String location = "/tmp";
    //     logger.info("MultipartConfigElement location: " + location);
    //     registration.setAsyncSupported(true);
    //     registration.setLoadOnStartup(1);
    //     registration.setMultipartConfig(new MultipartConfigElement
    //         (location, 2097152, 4194304, 0));
    // }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter utf8Filter = new CharacterEncodingFilter();
        utf8Filter.setEncoding("UTF-8");
        utf8Filter.setForceEncoding(true);
        return new Filter[]{utf8Filter};
    }
}