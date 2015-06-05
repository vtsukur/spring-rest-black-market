package org.vtsukur.spring.rest.market.domain.core.ad;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
@ToString(callSuper = true)
public class Ad extends BaseEntity {

    @Column(nullable = false)
    private Type type;

    public enum Type {

        BUY,

        SELL

    }

    @Column(nullable = false)
    private BigInteger amount;

    @Column(nullable = false)
    private Currency currency;

    public enum Currency {

        USD {

            public BigDecimal avgStatsRate(Type type) {
                return BigDecimal.valueOf(type == Type.BUY ? 21.81 : 22);
            }

        },

        EUR {

            public BigDecimal avgStatsRate(Type type) {
                return BigDecimal.valueOf(type == Type.BUY ? 24.24 : 24.44);
            }

        };

        public abstract BigDecimal avgStatsRate(Type type);

    }

    @Column(nullable = false)
    private BigDecimal rate;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    private Location location;

    private String comment;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime publishedAt;

    @Column(nullable = false)
    private Status status = Status.NEW;

    public enum Status {

        NEW,

        PUBLISHED,

        OUTDATED

    }

    public void publish() {
        if (status == Status.NEW) {
            status = Status.PUBLISHED;
            publishedAt = LocalDateTime.now();
        }
        else {
            throw new InvalidAdStateTransitionException(
                    "Ad is already published");
        }
    }

    public void finish() {
        if (status == Status.PUBLISHED) {
            status = Status.OUTDATED;
        }
        else {
            throw new InvalidAdStateTransitionException(
                    "Ad can be finished only when it is in the " + Status.PUBLISHED + " state");
        }
    }

}
