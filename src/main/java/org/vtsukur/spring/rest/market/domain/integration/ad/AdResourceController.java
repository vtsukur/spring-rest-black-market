package org.vtsukur.spring.rest.market.domain.integration.ad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.vtsukur.spring.rest.market.domain.core.ad.Ad;
import org.vtsukur.spring.rest.market.domain.core.ad.AdRepository;

import java.time.LocalDateTime;

/**
 * @author volodymyr.tsukur
 */
@RepositoryRestController
public class AdResourceController {

    @Autowired
    private AdRepository adRepository;

    @ResponseBody
    @RequestMapping(value = "/ads/{id}/publish", method = RequestMethod.POST, produces = "application/hal+json")
    public PersistentEntityResource publish(@PathVariable("id") Long id, PersistentEntityResourceAssembler assembler) {
        Ad ad = adRepository.findOne(id);
        ad.setStatus(Ad.Status.PUBLISHED);
        ad.setPublishedAt(LocalDateTime.now());
        adRepository.save(ad);
        return assembler.toFullResource(ad);
    }

}
