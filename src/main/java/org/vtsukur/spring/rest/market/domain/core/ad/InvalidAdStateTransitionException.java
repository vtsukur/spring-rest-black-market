package org.vtsukur.spring.rest.market.domain.core.ad;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author volodymyr.tsukur
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidAdStateTransitionException extends RuntimeException {

    public InvalidAdStateTransitionException(String message) {
        super(message);
    }

}
