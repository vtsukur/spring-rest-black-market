package org.vtsukur.spring.rest.market.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);
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
