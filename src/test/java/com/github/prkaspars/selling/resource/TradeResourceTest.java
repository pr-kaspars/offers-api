package com.github.prkaspars.selling.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.prkaspars.selling.model.Listing;
import com.github.prkaspars.selling.model.Offer;
import com.github.prkaspars.selling.request.OfferPayload;
import com.github.prkaspars.selling.service.TradeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Currency;

import static java.time.LocalDate.now;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(TradeResource.class)
public class TradeResourceTest {
  private ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TradeService tradeService;

  @Test
  public void createShouldCreateListingAndOffer() throws Exception {
    when(tradeService.list(any(OfferPayload.class)))
      .thenAnswer(a -> {
        OfferPayload in = a.getArgument(0);
        Offer o = new Offer();
        o.setId(99);
        o.setName(in.getName());
        o.setDescription(in.getDescription());
        o.setCurrency(Currency.getInstance(in.getCurrency()));
        o.setPrice(in.getPrice());
        Listing out = new Listing();
        out.setId(123);
        out.setState(Listing.State.ACTIVE);
        out.setExpires(now().plusDays(in.getDuration()));
        out.setOffer(o);
        return out;
      });

    OfferPayload payload = new OfferPayload();
    payload.setName("Foo");
    payload.setDescription("Lorem ipsum");
    payload.setCurrency("GBP");
    payload.setPrice(89.99);
    payload.setDuration(17);

    mockMvc
      .perform(
        post("/listings")
          .content(objectMapper.writeValueAsBytes(payload))
          .contentType(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/listings/123"))
      .andExpect(jsonPath("$.id").value(123))
      .andExpect(jsonPath("$.name").value("Foo"))
      .andExpect(jsonPath("$.description").value("Lorem ipsum"))
      .andExpect(jsonPath("$.currency").value("GBP"))
      .andExpect(jsonPath("$.price").value(89.99))
      .andExpect(jsonPath("$.expires").value(now().plusDays(17).format(ISO_LOCAL_DATE)));
  }

  @Test
  public void createShouldRespondWithFailures() throws Exception {
    OfferPayload payload = new OfferPayload();
    payload.setName("");
    payload.setCurrency("XYZ");
    payload.setPrice(-1.0);
    payload.setDuration(0);

    mockMvc
      .perform(
        post("/listings")
          .content(objectMapper.writeValueAsBytes(payload))
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(header().doesNotExist(HttpHeaders.LOCATION))
      .andExpect(jsonPath("$.failures.length()").value(5));

    verify(tradeService, times(0)).list(any());
  }
}
