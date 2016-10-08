package org.vtsukur.spring.rest.market;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.halLinks;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
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
@AutoConfigureRestDocs("build/generated-snippets")
public class AdsHttpApiWithDocsTests {

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
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost:8080/ads/" + createdBooking.getId()))
                .andExpect(jsonPath("$.id", is(createdBooking.getId().intValue())))
                .andExpect(jsonPath("$.type", is(createdBooking.getType().name())))
                .andExpect(jsonPath("$.amount", is(createdBooking.getAmount().intValue())))
                .andExpect(jsonPath("$.currency", is(createdBooking.getCurrency().name())))
                .andExpect(jsonPath("$.rate", is(createdBooking.getRate().doubleValue())))
                .andExpect(jsonPath("$.location.city", is(createdBooking.getLocation().getCity())))
                .andExpect(jsonPath("$.location.area", is(createdBooking.getLocation().getArea())))
                .andExpect(jsonPath("$.comment", is(createdBooking.getComment())))
                .andExpect(jsonPath("$.publishedAt", is(nullValue())))
                .andExpect(jsonPath("$.status", is(createdBooking.getStatus().name())));

        resultActions.andDo(document("create-ad",
                links(halLinks(),
                        linkWithRel("curies").description("CUR-ies"),
                        linkWithRel("self").description("This ad"),
                        linkWithRel("black-market:ad").description("This <<ads, ad>>"),
                        linkWithRel("black-market:user").description("Author of this ad"),
                        linkWithRel("black-market:update").description("Updates this ad via PATCH"),
                        linkWithRel("black-market:deletion").description("Deletes this ad via DELETE"),
                        linkWithRel("black-market:publishing").description("Publishes this ad via POST with empty body")
                ),
                responseFields(
                        fieldWithPath("_links").type(JsonFieldType.OBJECT).description("Links"),
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("Unique ad id"),
                        fieldWithPath("type").type(JsonFieldType.STRING).description("Type of the ad, one of: " +
                                Stream.of(Ad.Type.values()).map(Enum::name).collect(Collectors.joining(", "))),
                        fieldWithPath("amount").type(JsonFieldType.NUMBER).description("Amount to buy or sell"),
                        fieldWithPath("currency").type(JsonFieldType.STRING).description("Type of the currency"),
                        fieldWithPath("rate").type(JsonFieldType.NUMBER).description("Suggested exchange rate"),
                        fieldWithPath("location.city").type(JsonFieldType.STRING).description("City"),
                        fieldWithPath("location.area").type(JsonFieldType.STRING).description("Area of the city to meet"),
                        fieldWithPath("comment").type(JsonFieldType.STRING).description("Arbitrary comment"),
                        fieldWithPath("publishedAt").description("Publishing time"),
                        fieldWithPath("status").type(JsonFieldType.STRING).description("Formal ad status, one of " +
                                Stream.of(Ad.Status.values()).map(Enum::name).collect(Collectors.joining(", ")))
                )));
    }

    private Ad findCreatedBooking() {
        return adRepository.findAll(new Sort(Sort.Direction.DESC, "id")).iterator().next();
    }

    @Test
    public void publishAd() throws Exception {
        final Ad ad = adRepository.save(ad());

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/ads/" + ad.getId() + "/publishing")
                .accept(MediaTypes.HAL_JSON)
                .with(user(userDetailsService.loadUserByUsername(Admin.HONTAREVA))));

        final Ad publishedBooking = adRepository.findOne(ad.getId());
        resultActions.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", is(publishedBooking.getId().intValue())))
                .andExpect(jsonPath("$.type", is(publishedBooking.getType().name())))
                .andExpect(jsonPath("$.amount", is(publishedBooking.getAmount().intValue())))
                .andExpect(jsonPath("$.currency", is(publishedBooking.getCurrency().name())))
                .andExpect(jsonPath("$.rate", is(publishedBooking.getRate().doubleValue())))
                .andExpect(jsonPath("$.location.city", is(publishedBooking.getLocation().getCity())))
                .andExpect(jsonPath("$.location.area", is(publishedBooking.getLocation().getArea())))
                .andExpect(jsonPath("$.comment", is(publishedBooking.getComment())))
                .andExpect(jsonPath("$.publishedAt", is(not(nullValue()))))
                .andExpect(jsonPath("$.status", is(publishedBooking.getStatus().name())));

        resultActions.andDo(document("publish-ad",
                links(halLinks(),
                        linkWithRel("curies").description("CUR-ies"),
                        linkWithRel("self").description("This ad"),
                        linkWithRel("black-market:ad").description("This <<ads, ad>>"),
                        linkWithRel("black-market:user").description("Author of this ad"),
                        linkWithRel("black-market:expiration").description("Expires this ad via POST")
                ),
                responseFields(
                        fieldWithPath("_links").type(JsonFieldType.OBJECT).description("Links"),
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("Unique ad id"),
                        fieldWithPath("type").type(JsonFieldType.STRING).description("Type of the ad, one of: " +
                                Stream.of(Ad.Type.values()).map(Enum::name).collect(Collectors.joining(", "))),
                        fieldWithPath("amount").type(JsonFieldType.NUMBER).description("Amount to buy or sell"),
                        fieldWithPath("currency").type(JsonFieldType.STRING).description("Type of the currency"),
                        fieldWithPath("rate").type(JsonFieldType.NUMBER).description("Suggested exchange rate"),
                        fieldWithPath("location.city").type(JsonFieldType.STRING).description("City"),
                        fieldWithPath("location.area").type(JsonFieldType.STRING).description("Area of the city to meet"),
                        fieldWithPath("comment").type(JsonFieldType.STRING).description("Arbitrary comment"),
                        fieldWithPath("publishedAt").description("Publishing time"),
                        fieldWithPath("status").type(JsonFieldType.STRING).description("Formal ad status, one of " +
                                Stream.of(Ad.Status.values()).map(Enum::name).collect(Collectors.joining(", ")))
                )));
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
