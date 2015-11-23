package org.vtsukur.spring.rest.market.domain.core.ad;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author volodymyr.tsukur
 */
@RepositoryRestResource(excerptProjection = StandardAdProjection.class)
public interface AdRepository extends PagingAndSortingRepository<Ad, Long> {

    @RestResource(rel = "published", path = "published")
    @Query("select ad from Ad ad where ad.type = :#{#type} and ad.currency = :#{#currency} and ad.status = 'PUBLISHED'")
    Page<Ad> findPublishedByTypeAndCurrency(@Param("type") Ad.Type type, @Param("currency") Ad.Currency currency, Pageable pageable);

    @RestResource(rel = "my", path = "my")
    @Query("select ad from Ad ad where ad.user.phoneNumber = ?#{ principal?.username }")
    Page<Ad> findMyAds(Pageable pageable);

}
