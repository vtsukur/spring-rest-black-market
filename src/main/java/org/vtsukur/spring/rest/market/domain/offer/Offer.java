package org.vtsukur.spring.rest.market.domain.offer;

import lombok.Getter;
import lombok.Setter;
import org.vtsukur.spring.rest.market.domain.BaseEntity;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author volodymyr.tsukur
 */
@Entity
@Getter
@Setter
public class Offer extends BaseEntity {

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
