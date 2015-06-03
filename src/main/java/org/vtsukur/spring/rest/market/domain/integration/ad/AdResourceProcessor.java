package org.vtsukur.spring.rest.market.domain.integration.ad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;
import org.vtsukur.spring.rest.market.domain.core.ad.Ad;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * @author volodymyr.tsukur
 */
@Component
public class AdResourceProcessor implements ResourceProcessor<Resource<Ad>> {

    @Autowired
    private EntityLinks entityLinks;

    @Override
    public Resource<Ad> process(Resource<Ad> resource) {
        Ad ad = resource.getContent();
        if (ad.getStatus() == Ad.Status.NEW) {
            resource.add(entityLinks.linkToSingleResource(ad).withRel("update"));
            resource.add(entityLinks.linkToSingleResource(ad).withRel("delete"));
            resource.add(linkTo(methodOn(AdResourceController.class).publish(ad.getId(), null)).withRel("publish"));
        }
        return resource;
    }

}
