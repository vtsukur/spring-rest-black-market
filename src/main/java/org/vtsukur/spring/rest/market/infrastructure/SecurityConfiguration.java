package org.vtsukur.spring.rest.market.infrastructure;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author volodymyr.tsukur
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("ipavlenko").password("secret").roles("USER")
                .and()
                .withUser("vtsukur").password("secret").roles("USER")
                .and()
                .withUser("hontareva").password("moi-baksi").roles("USER", "ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/ads/**").hasRole("USER")
                .antMatchers(HttpMethod.PUT, "/api/ads/**").hasRole("USER")
                .antMatchers(HttpMethod.PATCH, "/api/ads/**").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/api/ads/**").hasRole("USER")
                .and()
                .csrf().disable();
    }

}
