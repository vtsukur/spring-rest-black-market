package org.vtsukur.spring.rest.market.domain.integration.ad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.vtsukur.spring.rest.market.domain.core.ad.AdService;
import org.vtsukur.spring.rest.market.domain.core.ad.InvalidAdStateTransitionException;

/**
 * @author volodymyr.tsukur
 */
@RepositoryRestController
public class AdResourceController {

    @Autowired
    private AdService adService;

    @ResponseBody
    @RequestMapping(value = "/ads/{id}/publish", method = RequestMethod.POST, produces = "application/hal+json")
    public PersistentEntityResource publish(@PathVariable("id") Long id, PersistentEntityResourceAssembler assembler)
            throws InvalidAdStateTransitionException {
        return assembler.toFullResource(adService.publish(id));
    }

    @ResponseBody
    @RequestMapping(value = "/ads/{id}/expire", method = RequestMethod.POST, produces = "application/hal+json")
    public PersistentEntityResource expire(@PathVariable("id") Long id, PersistentEntityResourceAssembler assembler)
            throws InvalidAdStateTransitionException {
        return assembler.toFullResource(adService.expire(id));
    }

}
