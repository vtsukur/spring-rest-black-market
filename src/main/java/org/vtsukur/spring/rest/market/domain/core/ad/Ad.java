package org.vtsukur.spring.rest.market.domain.core.ad;

import lombok.Getter;
import lombok.Setter;
import org.vtsukur.spring.rest.market.domain.core.BaseEntity;
import org.vtsukur.spring.rest.market.domain.core.user.User;
import org.vtsukur.spring.rest.market.infrastructure.LocalDateTimeConverter;

import javax.persistence.*;
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

    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    private BigInteger amount;

    @Column(nullable = false)
    private Currency currency;

    @Column(nullable = false)
    private BigDecimal rate;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    private Location location;

    private String comment;

    @Column(nullable = false)
    private Status status = Status.NEW;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime publishedAt;

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

    public void publish() {
        if (status == Ad.Status.NEW) {
            status = Ad.Status.PUBLISHED;
            publishedAt = LocalDateTime.now();
        }
        else {
            throw new InvalidAdStateTransitionException("Ad is published only when it is new");
        }
    }

}
