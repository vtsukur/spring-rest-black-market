package org.vtsukur.spring.rest.market.domain.core.ad;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author volodymyr.tsukur
 */
public interface AdRepository extends PagingAndSortingRepository<Ad, Long> {

    @RestResource(rel = "by", path = "by")
    Page<Ad> findByTypeAndCurrency(@Param("type") Ad.Type type, @Param("currency") Ad.Currency currency, Pageable pageable);

    @RestResource(rel = "my", path = "my")
    @Query("select ad from Ad ad where ad.user.phoneNumber = ?#{ principal?.username }")
    Page<Ad> findMyAds(Pageable pageable);

}
