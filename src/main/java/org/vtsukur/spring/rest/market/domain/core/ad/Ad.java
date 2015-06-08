package org.vtsukur.spring.rest.market.domain.core.ad;

import lombok.*;
import org.springframework.hateoas.Identifiable;
import org.vtsukur.spring.rest.market.domain.core.user.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * @author volodymyr.tsukur
 */
@Entity
@Data
public class Ad implements Identifiable<Long> {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    public enum Type {

        BUY,

        SELL

    }

    @Column(nullable = false)
    private BigInteger amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    public enum Currency {

        USD,

        EUR

    }

    @Column(nullable = false)
    private BigDecimal rate;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    private Location location;

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Location {

        @Column(nullable = false)
        private String city;

        private String area;

    }

    private String comment;

    @Lob
    private LocalDateTime publishedAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;

    public enum Status {

        NEW,

        PUBLISHED,

        OUTDATED

    }

    public void publish() {
        if (status == Status.NEW) {
            publishedAt = LocalDateTime.now();
            status = Status.PUBLISHED;
        }
        else {
            throw new InvalidAdStateTransitionException("Ad is already published");
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
