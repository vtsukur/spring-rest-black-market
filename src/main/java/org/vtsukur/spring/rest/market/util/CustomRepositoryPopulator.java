package org.vtsukur.spring.rest.market.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.AbstractRepositoryPopulatorFactoryBean;
import org.springframework.data.repository.init.Jackson2ResourceReader;
import org.springframework.data.repository.init.ResourceReader;
import org.vtsukur.spring.rest.market.domain.core.ad.Ad;
import org.vtsukur.spring.rest.market.domain.core.user.User;

import java.util.ArrayList;
import java.util.Collection;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * @author volodymyr.tsukur
 */
public class CustomRepositoryPopulator extends AbstractRepositoryPopulatorFactoryBean {

    @Override
    protected ResourceReader getResourceReader() {
        return new DataReader();
    }

    private static class DataReader implements ResourceReader {

        @Override
        public Object readFrom(Resource resource, ClassLoader classLoader) throws Exception {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JSR310Module());
            objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
            Jackson2ResourceReader jsonReader = new Jackson2ResourceReader(objectMapper);

            Collection collection = (Collection) jsonReader.readFrom(resource, classLoader);

            Collection<Object> results = new ArrayList<>();
            Collection<User> users = new ArrayList<>();
            Collection<Ad> ads = new ArrayList<>();

            collection.forEach(o -> {
                Ad ad = (Ad) o;
                ads.add(ad);
                users.add(ad.getUser());
            });

            results.addAll(users);
            results.addAll(ads);

            return results;
        }

    }

}
