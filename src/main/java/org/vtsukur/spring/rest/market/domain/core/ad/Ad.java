package org.vtsukur.spring.rest.market.domain.core.ad;

import lombok.Getter;
import lombok.Setter;
import org.vtsukur.spring.rest.market.domain.core.BaseEntity;

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
public class Ad extends BaseEntity {

    @Lob
    private LocalDateTime published;

    private Type type;

    private BigInteger amount;

    private Currency currency;

    private BigDecimal rate;

    private Location location;

    private String comment;

    private Status status = Status.NEW;

    public enum Type {

        BUY,

        SELL

    }

    public enum Currency {

        USD,

        EUR,

        UAH

    }

    public enum Status {

        NEW,

        PUBLISHED,

        OUTDATED

    }

}
