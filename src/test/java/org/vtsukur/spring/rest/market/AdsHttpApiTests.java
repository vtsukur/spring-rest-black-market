package org.vtsukur.spring.rest.market;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.vtsukur.spring.rest.market.domain.core.ad.Ad;
import org.vtsukur.spring.rest.market.domain.core.ad.AdRepository;
import org.vtsukur.spring.rest.market.domain.core.user.User;
import org.vtsukur.spring.rest.market.domain.core.user.UserRepository;
import org.vtsukur.spring.rest.market.infrastructure.Admin;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author volodymyr.tsukur
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AdsHttpApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    private User referenceUser;

    @Before
    public void setup() {
        referenceUser = userRepository.findByPhoneNumber(Admin.HONTAREVA);
    }

    @Test
    public void createAd() throws Exception {
        Ad ad = ad();
        String requestBody = saveRequestJsonString(ad);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/ads")
                .accept(MediaTypes.HAL_JSON)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(userDetailsService.loadUserByUsername(Admin.HONTAREVA))));

        final Ad createdBooking = findCreatedBooking();
        resultActions.andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/ads/" + createdBooking.getId()))
                .andExpect(jsonPath("$.type", is(ad.getType().name())))
                .andExpect(jsonPath("$.amount", is(ad.getAmount().intValue())))
                .andExpect(jsonPath("$.currency", is(ad.getCurrency().name())))
                .andExpect(jsonPath("$.rate", is(ad.getRate().doubleValue())))
                .andExpect(jsonPath("$.location.city", is(ad.getLocation().getCity())))
                .andExpect(jsonPath("$.location.area", is(ad.getLocation().getArea())))
                .andExpect(jsonPath("$.comment", is(ad.getComment())));
    }

    private Ad findCreatedBooking() {
        return adRepository.findAll(new Sort(Sort.Direction.DESC, "id")).iterator().next();
    }

    private Ad ad() {
        Ad ad = new Ad();
        ad.setType(Ad.Type.BUY);
        ad.setAmount(BigInteger.valueOf(9999));
        ad.setCurrency(Ad.Currency.USD);
        ad.setRate(BigDecimal.valueOf(21.5));
        ad.setLocation(new Ad.Location("Kyiv", "Pechersk"));
        ad.setComment("need it now!");
        ad.setUser(referenceUser);
        return ad;
    }

    private static String saveRequestJsonString(Ad ad) {
        return "{\n" +
                "  \"type\": \"" + ad.getType() + "\",\n" +
                "  \"amount\": " + ad.getAmount() + ",\n" +
                "  \"currency\": \"" + ad.getCurrency() + "\",\n" +
                "  \"rate\": " + ad.getRate() + ",\n" +
                "  \"location\": {\n" +
                "    \"city\": \"" + ad.getLocation().getCity() + "\",\n" +
                "    \"area\": \"" + ad.getLocation().getArea() + "\"\n" +
                "  },\n" +
                "  \"user\": \"/users/" + ad.getUser().getId() + "\",\n" +
                "  \"comment\": \"" + ad.getComment() + "\"\n" +
                "}";
    }

}
