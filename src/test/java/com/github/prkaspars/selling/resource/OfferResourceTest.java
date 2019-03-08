package com.github.prkaspars.selling.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.prkaspars.selling.model.Offer;
import com.github.prkaspars.selling.model.Product;
import com.github.prkaspars.selling.request.PatchStatePayload;
import com.github.prkaspars.selling.request.CreateOfferPayload;
import com.github.prkaspars.selling.service.OfferService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.*;

import static java.time.LocalDate.now;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(OfferResource.class)
public class OfferResourceTest {
  private ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private OfferService service;

  @Test
  public void createShouldCreateListingAndOffer() throws Exception {
    when(service.list(any(CreateOfferPayload.class)))
      .thenAnswer(a -> {
        CreateOfferPayload in = a.getArgument(0);
        Product p = new Product();
        p.setId(99);
        p.setName(in.getName());
        p.setDescription(in.getDescription());
        p.setCurrency(Currency.getInstance(in.getCurrency()));
        p.setPrice(in.getPrice());
        Offer out = new Offer();
        out.setId(123);
        out.setState(Offer.State.ACTIVE);
        out.setExpires(now().plusDays(in.getDuration()));
        out.setProduct(p);
        return out;
      });

    CreateOfferPayload payload = new CreateOfferPayload();
    payload.setName("Foo");
    payload.setDescription("Lorem ipsum");
    payload.setCurrency("GBP");
    payload.setPrice(89.99);
    payload.setDuration(17);

    mockMvc
      .perform(
        post("/offers")
          .content(objectMapper.writeValueAsBytes(payload))
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/offers/123"))
      .andExpect(jsonPath("$.id").value(123))
      .andExpect(jsonPath("$.name").value("Foo"))
      .andExpect(jsonPath("$.description").value("Lorem ipsum"))
      .andExpect(jsonPath("$.currency").value("GBP"))
      .andExpect(jsonPath("$.price").value(89.99))
      .andExpect(jsonPath("$.expires").value(now().plusDays(17).format(ISO_LOCAL_DATE)));
  }

  @Test
  public void createShouldRespondWithFailures() throws Exception {
    CreateOfferPayload payload = new CreateOfferPayload();
    payload.setName("");
    payload.setCurrency("XYZ");
    payload.setPrice(-1.0);
    payload.setDuration(0);

    mockMvc
      .perform(
        post("/offers")
          .content(objectMapper.writeValueAsBytes(payload))
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(header().doesNotExist(HttpHeaders.LOCATION))
      .andExpect(jsonPath("$.failures.length()").value(5));

    verify(service, times(0)).list(any());
  }

  @Test
  public void readShouldRespondWithListing() throws Exception {
    Product product = new Product();
    product.setName("Foo");
    product.setDescription("Lorem ipsum");
    product.setCurrency(Currency.getInstance("GBP"));
    product.setPrice(89.99);

    Offer offer = new Offer();
    offer.setId(123);
    offer.setProduct(product);
    offer.setState(Offer.State.ACTIVE);
    offer.setExpires(LocalDate.now().plusDays(12));

    when(service.read(123))
      .thenReturn(Optional.of(offer));

    mockMvc
      .perform(get("/offers/123"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(123))
      .andExpect(jsonPath("$.name").value("Foo"))
      .andExpect(jsonPath("$.description").value("Lorem ipsum"))
      .andExpect(jsonPath("$.currency").value("GBP"))
      .andExpect(jsonPath("$.price").value(89.99))
      .andExpect(jsonPath("$.expires").value(now().plusDays(12).format(ISO_LOCAL_DATE)));
  }

  @Test
  public void readShouldRespondWithNotFound() throws Exception {
    when(service.read(123))
      .thenReturn(Optional.empty());

    mockMvc
      .perform(get("/offers/123"))
      .andExpect(status().isNotFound());
  }

  @Test
  public void cancelShouldAcceptRequest() throws Exception {
    when(service.cancel(123))
      .thenReturn(true);

    PatchStatePayload payload = new PatchStatePayload();
    payload.setState(Offer.State.CANCELLED);

    mockMvc
      .perform(
        patch("/offers/123")
          .content(objectMapper.writeValueAsBytes(payload))
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());
  }

  @Test
  public void cancelShouldRespondWithNotFoundWhenResourceDoesNotExist() throws Exception {
    when(service.cancel(123))
      .thenReturn(false);

    PatchStatePayload payload = new PatchStatePayload();
    payload.setState(Offer.State.CANCELLED);

    mockMvc
      .perform(
        patch("/offers/123")
          .content(objectMapper.writeValueAsBytes(payload))
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNotFound());
  }

  @Test
  public void cancelShouldRespondWithBadRequestWhenStatusIsNotCancelled() throws Exception {
    PatchStatePayload payload = new PatchStatePayload();
    payload.setState(Offer.State.ACTIVE);

    mockMvc
      .perform(
        patch("/offers/123")
          .content(objectMapper.writeValueAsBytes(payload))
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());

    verify(service, times(0)).cancel(any());
  }

  @Test
  public void activeShouldReturnEmptyList() throws Exception {
    when(service.active())
      .thenReturn(Collections.emptyList());

    mockMvc
      .perform(get("/offers"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()").value(0));
  }

  @Test
  public void activeShouldReturnList() throws Exception {
    List<Offer> offers = new LinkedList<>();
    Offer offer = new Offer();
    offer.setProduct(new Product());
    offers.add(offer);
    when(service.active())
      .thenReturn(offers);

    mockMvc
      .perform(get("/offers"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()").value(1));
  }
}
