package org.vtsukur.spring.rest.market.domain.offer;

import lombok.Getter;
import lombok.Setter;
import org.vtsukur.spring.rest.market.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Lob;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * @author volodymyr.tsukur
 */
@Entity
@Getter
@Setter
public class Offer extends BaseEntity {

    @Lob
    private LocalDateTime time;

    private Type type;

    private Currency currency;

    private BigDecimal rate;

    private BigInteger amount;

    public enum Type {

        BUY,

        SELL

    }

    public enum Currency {

        USD,

        EUR,

        UAH

    }

}
