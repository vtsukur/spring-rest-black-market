package org.vtsukur.spring.rest.market.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.vtsukur.spring.rest.market.domain.core.user.User;
import org.vtsukur.spring.rest.market.domain.core.user.UserRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * @author volodymyr.tsukur
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new CustomUserDetails(userRepository.findByPhoneNumber(username));
    }

    public static class CustomUserDetails implements UserDetails {

        private final SimpleGrantedAuthority USER_ROLE = new SimpleGrantedAuthority("ROLE_USER");

        private final SimpleGrantedAuthority USER_ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");

        private final Collection<? extends GrantedAuthority> ROLES_USER =
                Collections.singletonList(USER_ROLE);

        private final Collection<? extends GrantedAuthority> ROLES_USER_AND_ADMIN =
                Arrays.asList(USER_ROLE, USER_ADMIN);

        private final Long id;

        private final String username;

        private final Collection<? extends GrantedAuthority> roles;

        public CustomUserDetails(final User user) {
            this.id = user.getId();
            this.username = user.getPhoneNumber();
            roles = Admin.HONTAREVA.equals(username) ? ROLES_USER_AND_ADMIN : ROLES_USER;
        }

        public Long getId() {
            return id;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return roles;
        }

        @Override
        public String getPassword() {
            return "123";
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

    }

}
