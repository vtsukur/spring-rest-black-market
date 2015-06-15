package org.vtsukur.spring.rest.market.domain.core.ad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author volodymyr.tsukur
 */
@Service
public class AdService {

    @Autowired
    private AdRepository adRepository;

    public Ad publish(Long id) throws InvalidAdStateTransitionException {
        Ad ad = adRepository.findOne(id);
        ad.publish();
        return adRepository.save(ad);
    }

    public Ad finish(Long id) throws InvalidAdStateTransitionException {
        Ad ad = adRepository.findOne(id);
        ad.finish();
        return adRepository.save(ad);
    }

}
