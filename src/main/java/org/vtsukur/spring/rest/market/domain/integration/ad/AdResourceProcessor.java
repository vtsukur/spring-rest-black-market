package org.vtsukur.spring.rest.market.domain.integration.ad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.vtsukur.spring.rest.market.domain.core.ad.Ad;
import org.vtsukur.spring.rest.market.infrastructure.CustomUserDetailsService;

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
        if (hasAccessToModify(ad)) {
            if (ad.getStatus() == Ad.Status.NEW) {
                resource.add(entityLinks.linkToSingleResource(ad).withRel("update"));
                resource.add(entityLinks.linkToSingleResource(ad).withRel("delete"));
                resource.add(linkTo(methodOn(AdResourceController.class).publish(ad.getId(), null)).withRel("publish"));
            }
            if (ad.getStatus() == Ad.Status.PUBLISHED) {
                resource.add(linkTo(methodOn(AdResourceController.class).finish(ad.getId(), null)).withRel("finish"));
            }
        }
        return resource;
    }

    private static boolean hasAccessToModify(Ad ad) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return false;
        }
        CustomUserDetailsService.CustomUserDetails principal = (CustomUserDetailsService.CustomUserDetails) auth.getPrincipal();
        return principal != null && ad.getUser().getId().equals(principal.getId());
    }

}
