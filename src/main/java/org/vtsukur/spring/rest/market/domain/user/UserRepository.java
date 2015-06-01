package org.vtsukur.spring.rest.market.domain.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author volodymyr.tsukur
 */
@RestResource(exported = false)
public interface UserRepository extends CrudRepository<User, Long> {
}
