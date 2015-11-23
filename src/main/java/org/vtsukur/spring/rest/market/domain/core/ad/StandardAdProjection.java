package org.vtsukur.spring.rest.market.domain.core.ad;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * @author volodymyr.tsukur
 */
@Projection(name = "standard", types = Ad.class)
public interface StandardAdProjection {

    @JsonIgnore
    Long getId();

    Ad.Type getType();

    BigInteger getAmount();

    Ad.Currency getCurrency();

    BigDecimal getRate();

    @Value("#{target.user.phoneNumber}")
    String getPhoneNumber();

    Ad.Location getLocation();

    String getComment();

    LocalDateTime getPublishedAt();

    @JsonIgnore
    Ad.Status getStatus();

}
