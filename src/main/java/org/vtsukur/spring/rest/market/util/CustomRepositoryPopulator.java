package org.vtsukur.spring.rest.market.util;

import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.AbstractRepositoryPopulatorFactoryBean;
import org.springframework.data.repository.init.ResourceReader;
import org.vtsukur.spring.rest.market.domain.offer.Offer;
import org.vtsukur.spring.rest.market.domain.user.User;

import java.math.BigDecimal;
import java.math.BigInteger;
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
            Collection<Object> results = new ArrayList<>();
            Collection<User> users = new ArrayList<>();
            Collection<Offer> offers = new ArrayList<>();

            User user = new User();
            user.setPhoneNumber("+380681854104");
            users.add(user);

            Offer offer = new Offer();
            offer.setType(Offer.Type.BUY);
            offer.setAmount(BigInteger.valueOf(3000));
            offer.setCurrency(Offer.Currency.USD);
            offer.setRate(BigDecimal.valueOf(21.5));
            offer.setStatus(Offer.Status.NEW);
            offers.add(offer);

            user.getOffers().add(offer);

            results.addAll(offers);
            results.addAll(users);

            return results;
        }

    }

}
