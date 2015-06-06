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

    @RestResource(exported = false)
    @Override
    <S extends User> S save(S entity);

    @RestResource(exported = false)
    @Override
    <S extends User> Iterable<S> save(Iterable<S> entities);

    @RestResource(exported = false)
    @Override
    User findOne(Long aLong);

    @RestResource(exported = false)
    @Override
    boolean exists(Long aLong);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    Iterable<User> findAll();

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    Iterable<User> findAll(Iterable<Long> longs);

    @RestResource(exported = false)
    @Override
    long count();

    @RestResource(exported = false)
    @Override
    void delete(Long aLong);

    @RestResource(exported = false)
    @Override
    void delete(User entity);

    @RestResource(exported = false)
    @Override
    void delete(Iterable<? extends User> entities);

    @RestResource(exported = false)
    @Override
    void deleteAll();

}
