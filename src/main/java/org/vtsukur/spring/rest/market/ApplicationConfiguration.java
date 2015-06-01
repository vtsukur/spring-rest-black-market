package org.vtsukur.spring.rest.market;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.vtsukur.spring.rest.market.util.CustomRepositoryPopulator;

/**
 * @author volodymyr.tsukur
 */
@Configuration
public class ApplicationConfiguration {

    @Bean
    public CustomRepositoryPopulator repositoryPopulator() {
        CustomRepositoryPopulator factory = new CustomRepositoryPopulator();
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
//        mapper.registerModule(newJSR310Module());
//        factory.setMapper(mapper);
        factory.setResources(new Resource[]{
                new ClassPathResource("data.json")
        });
        return factory;
    }

    @Bean
    public Module newJSR310Module() {
        return new JSR310Module();
    }

}
