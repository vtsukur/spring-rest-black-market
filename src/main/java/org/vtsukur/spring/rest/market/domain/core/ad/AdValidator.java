package org.vtsukur.spring.rest.market.domain.core.ad;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author volodymyr.tsukur
 */
public class AdValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Ad.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Ad ad = (Ad) target;
        if (ad.getAmount().intValue() <= 0) {
            errors.rejectValue("amount", "Ad.amount.not.positive", "Amount should be positive");
        }
    }

}
