package org.vtsukur.spring.rest.market.domain.core.ad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author volodymyr.tsukur
 */
@Service
@Transactional
public class AdService {

    @Autowired
    private AdRepository adRepository;

    public Ad publish(Long id) throws InvalidAdStateTransitionException {
        return findDoAndSave(id, Ad::publish);
    }

    public Ad expire(Long id) throws InvalidAdStateTransitionException {
        return findDoAndSave(id, Ad::expire);
    }

    private Ad findDoAndSave(Long id, Action action) {
        Ad foundAd = adRepository.findOne(id);
        Ad modifiedAd = action.perform(foundAd);
        return adRepository.save(modifiedAd);
    }

    @FunctionalInterface
    private interface Action {

        Ad perform(Ad ad);

    }

}
