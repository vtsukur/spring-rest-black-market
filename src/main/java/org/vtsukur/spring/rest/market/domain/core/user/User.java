package org.vtsukur.spring.rest.market.domain.core.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.vtsukur.spring.rest.market.domain.core.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author volodymyr.tsukur
 */
@Entity
@Getter
@Setter
@ToString(callSuper = true)
public class User extends BaseEntity {

    @Column(nullable = false)
    private String phoneNumber;

}
