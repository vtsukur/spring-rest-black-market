package org.vtsukur.spring.rest.market;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.vtsukur.spring.rest.market.domain.core.ad.Ad;
import org.vtsukur.spring.rest.market.domain.core.ad.AdRepository;
import org.vtsukur.spring.rest.market.domain.core.user.User;
import org.vtsukur.spring.rest.market.domain.core.user.UserRepository;
import org.vtsukur.spring.rest.market.infrastructure.CustomUserDetailsService;
import org.vtsukur.spring.rest.market.infrastructure.SecurityUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * @author volodymyr.tsukur
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationConfiguration extends WebSecurityConfigurerAdapter {

    private static final Integer[] MOBILE_OPERATOR_CODES = new Integer[] {
            39,
            50,
            63,
            66,
            67,
            68,
            93,
            95,
            96,
            97,
            98,
            99
    };

    private static final String[] KYIV_DISTRICTS = new String[] {
            "Голосеево",
            "Дарница",
            "Деснянский",
            "Днепровский",
            "Оболонь",
            "Печерск",
            "Подол",
            "Святошино",
            "Соломенский",
            "Шевченковский"
    };

    private static final String[] LVIV_DISTRICTS = new String[] {
            "Шевченківський",
            "Личаківський",
            "Сихівський",
            "Франківський",
            "Залізничний",
            "Личаківський"
    };

    private static final String[] COMMENTS = new String[] {
            "",
            "всю суму",
            "ну дуже треба",
            "можна частинами",
            "маленький, можу під'їхати"
    };
    public static final int PUBLISHING_TIME_MAX_DIFF = 4;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private boolean stableUsersOnly;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/ui/admin.html").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/ads/**").hasRole("USER")
                .antMatchers(HttpMethod.PUT, "/ads/**").hasRole("USER")
                .antMatchers(HttpMethod.PATCH, "/ads/**").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/ads/**").hasRole("USER")
                .and()
                .csrf().disable();
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }

    public void load() {
        int amount = 100;
        LocalDateTime now = LocalDateTime.now();
        final LocalDateTime publishedAt = now.minusMinutes(PUBLISHING_TIME_MAX_DIFF * amount);

        SecurityUtils.run("system", "system", new String[]{"ROLE_ADMIN"}, () -> {
            if (!stableUsersOnly) {
                LocalDateTime at = publishedAt;
                for (int i = 0; i < amount; ++i) {
                    User user = nextUser();
                    userRepository.save(user);

                    Ad ad = nextAd(user, at);
                    adRepository.save(ad);

                    at = ad.getPublishedAt();
                }
            }

            setupAdmin(publishedAt.minusMinutes(10));
            setupStableUser(publishedAt);
        });
    }

    private void setupAdmin(LocalDateTime publishedAt) {
        User admin = new User();
        admin.setPhoneNumber("hontareva");
        userRepository.save(admin);

        if (!stableUsersOnly) {
            Ad ad = new Ad();
            ad.setType(Ad.Type.BUY);
            ad.setAmount(BigInteger.valueOf(100000000));
            ad.setCurrency(Ad.Currency.USD);
            ad.setRate(nextRate(ad.getCurrency(), ad.getType()));
            ad.setUser(admin);
            ad.setStatus(Ad.Status.PUBLISHED);
            ad.setPublishedAt(publishedAt);
            ad.setLocation(new Ad.Location("Киев", "Печерск"));
            ad.setComment("играем по крупному");
            adRepository.save(ad);
        }
    }

    private void setupStableUser(LocalDateTime publishedAt) {
        User user = new User();
        user.setPhoneNumber("0681854104");
        userRepository.save(user);

        if (!stableUsersOnly) {
            Ad ad = new Ad();
            ad.setType(Ad.Type.BUY);
            ad.setAmount(BigInteger.valueOf(4000));
            ad.setCurrency(Ad.Currency.USD);
            ad.setRate(nextRate(ad.getCurrency(), ad.getType()));
            ad.setUser(user);
            ad.setStatus(Ad.Status.PUBLISHED);
            ad.setPublishedAt(publishedAt);
            ad.setLocation(new Ad.Location("Киев", "Соломенка"));
            ad.setComment("нужна валюта срочно, зарплата \"горит\", могу подъехать!");
            adRepository.save(ad);
        }
    }

    private static User nextUser() {
        User user = new User();
        user.setPhoneNumber(nextPhoneNumber());
        return user;
    }

    private static Ad nextAd(User user, LocalDateTime publishedAt) {
        Ad ad = new Ad();

        Ad.Type type = nextType();
        ad.setType(type);
        Ad.Currency currency = nextCurrency();
        ad.setCurrency(currency);

        ad.setAmount(nextAmount());
        ad.setRate(nextRate(currency, type));
        ad.setUser(user);

        ad.setLocation(new Ad.Location("Львів", nextDistrict()));
        ad.setComment(nextComments());
        ad.setStatus(Ad.Status.PUBLISHED);
        ad.setPublishedAt(nextPublishingTime(publishedAt));

        return ad;
    }

    private static String nextPhoneNumber() {
        return String.format("0%d%07d", nextMobileOperatorCode(), nextInt(10000000));
    }

    private static int nextMobileOperatorCode() {
        return nextRandomFromArray(MOBILE_OPERATOR_CODES);
    }

    private static Ad.Type nextType() {
        return nextRandomFromArray(Ad.Type.values());
    }

    private static BigInteger nextAmount() {
        return BigInteger.valueOf(nextInt(100) * 100 + 100);
    }

    private static Ad.Currency nextCurrency() {
        return nextRandomFromArray(Ad.Currency.values());
    }

    private static BigDecimal nextRate(Ad.Currency currency, Ad.Type type) {
        return avgRate(currency, type);
    }

    private static BigDecimal avgRate(Ad.Currency currency, Ad.Type type) {
        return (currency == Ad.Currency.USD ?
                BigDecimal.valueOf(type == Ad.Type.BUY ? 21.58 : 22.18) :
                BigDecimal.valueOf(type == Ad.Type.BUY ? 24.2 : 24.67)
        );
    }

    private static String nextDistrict() {
        return nextRandomFromArray(LVIV_DISTRICTS);
    }

    private static String nextComments() {
        return nextRandomFromArray(COMMENTS);
    }

    private static LocalDateTime nextPublishingTime(LocalDateTime previous) {
        return previous.plusMinutes(nextInt(PUBLISHING_TIME_MAX_DIFF - 1) + 1);
    }

    private static <T> T nextRandomFromArray(T[] array) {
        return array[nextInt(array.length)];
    }

    private static int nextInt(int bound) {
        return new Random().nextInt(bound);
    }

    public ApplicationConfiguration minimalSet(boolean stableUsersOnly) {
        this.stableUsersOnly = stableUsersOnly;
        return this;
    }

    @Bean
    CommandLineRunner commandLineRunner(ApplicationConfiguration dataLoader) {
        return (o) -> dataLoader.minimalSet(false).load();
    }

    @Bean
    public Module newJSR310Module() {
        return new JSR310Module();
    }

    @Configuration
    public static class CustomRepositoryRestMvcConfiguration extends RepositoryRestMvcConfiguration {

        @Override
        protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
            config.exposeIdsFor(Ad.class);
        }

    }

}
