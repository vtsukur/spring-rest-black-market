package org.vtsukur.spring.rest.market.domain.core.ad;

/**
 * @author volodymyr.tsukur
 */
public class InvalidAdStateTransitionException extends RuntimeException {

    public InvalidAdStateTransitionException(String message) {
        super(message);
    }

}
