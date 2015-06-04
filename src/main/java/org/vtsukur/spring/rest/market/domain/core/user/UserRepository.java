package org.vtsukur.spring.rest.market.domain.core.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * @author volodymyr.tsukur
 */
@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface UserRepository extends CrudRepository<User, Long> {
}
