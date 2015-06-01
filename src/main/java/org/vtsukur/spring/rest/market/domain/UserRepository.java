package org.vtsukur.spring.rest.market.domain;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author volodymyr.tsukur
 */
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
}
