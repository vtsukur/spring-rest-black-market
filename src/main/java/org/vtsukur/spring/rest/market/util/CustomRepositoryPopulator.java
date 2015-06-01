package org.vtsukur.spring.rest.market.util;

import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.AbstractRepositoryPopulatorFactoryBean;
import org.springframework.data.repository.init.Jackson2ResourceReader;
import org.springframework.data.repository.init.ResourceReader;
import org.vtsukur.spring.rest.market.domain.ad.Ad;
import org.vtsukur.spring.rest.market.domain.user.User;

import java.util.ArrayList;
import java.util.Collection;

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
            Jackson2ResourceReader jsonReader = new Jackson2ResourceReader();
            Collection collection = (Collection) jsonReader.readFrom(resource, classLoader);

            Collection<Object> results = new ArrayList<>();
            Collection<User> users = new ArrayList<>();
            Collection<Ad> ads = new ArrayList<>();

            collection.forEach(o -> {
                User user = (User) o;
                users.add(user);

                ads.addAll(user.getAds());
            });

            results.addAll(ads);
            results.addAll(users);

            return results;
        }

    }

}
