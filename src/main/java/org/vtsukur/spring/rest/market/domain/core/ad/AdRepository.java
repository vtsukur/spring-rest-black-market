package org.vtsukur.spring.rest.market.domain.core.ad;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.vtsukur.spring.rest.market.domain.core.user.User;

/**
 * @author volodymyr.tsukur
 */
@RepositoryRestResource(excerptProjection = AdExcerpt.class)
public interface AdRepository extends PagingAndSortingRepository<Ad, Long> {

    @RestResource(rel = "by", path = "by")
    Page<Ad> findByTypeAndCurrency(@Param("type") Ad.Type type, @Param("currency") Ad.Currency currency, Pageable pageable);

    @RestResource(rel = "by-user", path = "by-user")
    Page<Ad> findByUser(@Param("value") User user, Pageable pageable);

    @RestResource(rel = "by-user-phonenumber", path = "by-user-phonenumber")
    Page<Ad> findByUser_PhoneNumber(@Param("value") String phoneNumber, Pageable pageable);

}
