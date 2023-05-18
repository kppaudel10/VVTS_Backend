package global;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * @auther kul.paudel
 * @created at 2023-05-18
 */
@Configuration
public class MultipartConfig {

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        // Set any desired configuration options for the resolver
        resolver.setMaxUploadSize(10 * 1024 * 1024); // Set maximum file size to 10MB
        resolver.setMaxInMemorySize(1024 * 1024); // Set maximum in-memory size to 1MB
        return resolver;
    }


}
