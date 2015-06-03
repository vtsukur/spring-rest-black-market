package org.vtsukur.spring.rest.market.domain.core.ad;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.vtsukur.spring.rest.market.domain.core.user.User;

/**
 * @author volodymyr.tsukur
 */
public interface AdRepository extends PagingAndSortingRepository<Ad, Long> {

    @RestResource(rel = "by-user", path = "byUser")
    Page<Ad> findByUser(@Param("user") User user, Pageable pageable);

}
