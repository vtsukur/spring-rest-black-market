package org.vtsukur.spring.rest.market.domain.core.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * @author volodymyr.tsukur
 */
public interface UserRepository extends CrudRepository<User, Long> {

    @RestResource(exported = false)
    User findByPhoneNumber(String phoneNumber);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    <S extends User> S save(S entity);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    <S extends User> Iterable<S> save(Iterable<S> entities);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    User findOne(Long aLong);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    boolean exists(Long aLong);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    Iterable<User> findAll();

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    Iterable<User> findAll(Iterable<Long> longs);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    long count();

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    void delete(Long aLong);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    void delete(User entity);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    void delete(Iterable<? extends User> entities);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    void deleteAll();

}
