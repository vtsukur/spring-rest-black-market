package org.vtsukur.spring.rest.market.domain.core.ad;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author volodymyr.tsukur
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String area;

}
