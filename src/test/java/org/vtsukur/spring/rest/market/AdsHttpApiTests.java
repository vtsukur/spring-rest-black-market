package org.vtsukur.spring.rest.market;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.vtsukur.spring.rest.market.domain.core.ad.Ad;
import org.vtsukur.spring.rest.market.domain.core.ad.AdRepository;
import org.vtsukur.spring.rest.market.domain.core.user.User;
import org.vtsukur.spring.rest.market.domain.core.user.UserRepository;
import org.vtsukur.spring.rest.market.infrastructure.Admin;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.RestDocumentation.document;
import static org.springframework.restdocs.RestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author volodymyr.tsukur
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Transactional
public class AdsHttpApiTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    private User referenceUser;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration())
                .alwaysDo(document("{method-name}"))
                .build();

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
                .contentType(MediaType.APPLICATION_JSON));

        final Ad createdBooking = findCreatedBooking();
        resultActions.andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost:8080/ads/" + createdBooking.getId()))
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
        ad.setLocation(new Ad.Location("Kharkiv", "Moskovskiy"));
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
