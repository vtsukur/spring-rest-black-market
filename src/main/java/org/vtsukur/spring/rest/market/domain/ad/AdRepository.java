package org.vtsukur.spring.rest.market.domain.ad;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author volodymyr.tsukur
 */
public interface AdRepository extends PagingAndSortingRepository<Ad, Long> {
}
