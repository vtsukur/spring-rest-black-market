package org.vtsukur.spring.rest.market;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vtsukur.spring.rest.market.util.RandomDataLoader;

/**
 * @author volodymyr.tsukur
 */
@Configuration
public class ApplicationConfiguration {

    @Bean
    CommandLineRunner commandLineRunner(RandomDataLoader dataLoader) {
        return (o) -> dataLoader.minimalSet(false).load();
    }

    @Bean
    public Module newJSR310Module() {
        return new JSR310Module();
    }

}
