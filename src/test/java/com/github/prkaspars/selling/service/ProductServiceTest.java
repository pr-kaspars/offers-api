package com.github.prkaspars.selling.service;

import com.github.prkaspars.selling.model.Offer;
import com.github.prkaspars.selling.model.Product;
import com.github.prkaspars.selling.repository.OfferRepository;
import com.github.prkaspars.selling.repository.ProductRepository;
import com.github.prkaspars.selling.request.CreateOfferPayload;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {
  private OfferRepository offerRepository;
  private ProductRepository productRepository;

  @Before
  public void setUp() {
    offerRepository = mock(OfferRepository.class);
    productRepository = mock(ProductRepository.class);
  }

  @Test
  public void listOffer() {
    ArgumentCaptor<Offer> offerArgumentCaptor = ArgumentCaptor.forClass(Offer.class);
    ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);

    when(productRepository.save(any(Product.class)))
      .thenAnswer(a -> {
        Product in = a.getArgument(0);
        Product out = new Product();
        out.setId(123);
        out.setName(in.getName());
        out.setDescription(in.getDescription());
        out.setCurrency(in.getCurrency());
        out.setPrice(in.getPrice());
        return out;
      });

    CreateOfferPayload payload = new CreateOfferPayload();
    payload.setName("Foo");
    payload.setDescription("Bar");
    payload.setCurrency("GBP");
    payload.setPrice(89.99);
    payload.setDuration(17);

    OfferService service = new OfferService(offerRepository, productRepository);
    service.list(payload);

    verify(offerRepository, times(1)).save(offerArgumentCaptor.capture());
    verify(productRepository, times(1)).save(productArgumentCaptor.capture());

    Product product = productArgumentCaptor.getValue();
    assertNull(product.getId());
    assertEquals("Foo", product.getName());
    assertEquals("Bar", product.getDescription());
    assertEquals(Currency.getInstance("GBP"), product.getCurrency());
    assertEquals(89.99, product.getPrice(), 0);

    Offer offer = offerArgumentCaptor.getValue();
    assertNull(offer.getId());
    assertEquals(Integer.valueOf(123), offer.getProduct().getId());
    assertEquals(LocalDate.now().plusDays(17), offer.getExpires());
    assertEquals(Offer.State.ACTIVE, offer.getState());
  }

  @Test
  public void readShouldReturnEmptyWhenRepoReturnsEmpty() {
    when(offerRepository.findById(123)).thenReturn(Optional.empty());
    OfferService service = new OfferService(offerRepository, productRepository);
    Optional<Offer> result = service.read(123);
    assertEquals(Optional.empty(), result);
  }

  @Test
  public void readShouldNotReturnCancelledListing() {
    Offer offer = new Offer();
    offer.setState(Offer.State.CANCELLED);
    offer.setExpires(LocalDate.now().plusDays(2));
    when(offerRepository.findById(123)).thenReturn(Optional.of(offer));
    OfferService service = new OfferService(offerRepository, productRepository);
    Optional<Offer> result = service.read(123);
    assertEquals(Optional.empty(), result);
  }

  @Test
  public void readShouldNotReturnExpiredListing() {
    Offer offer = new Offer();
    offer.setState(Offer.State.ACTIVE);
    offer.setExpires(LocalDate.now().minusDays(2));
    when(offerRepository.findById(123)).thenReturn(Optional.of(offer));
    OfferService service = new OfferService(offerRepository, productRepository);
    Optional<Offer> result = service.read(123);
    assertEquals(Optional.empty(), result);
  }

  @Test
  public void readShouldReturnListing() {
    Offer offer = new Offer();
    offer.setState(Offer.State.ACTIVE);
    offer.setExpires(LocalDate.now().plusDays(2));
    when(offerRepository.findById(123)).thenReturn(Optional.of(offer));
    OfferService service = new OfferService(offerRepository, productRepository);
    Optional<Offer> result = service.read(123);
    assertEquals(offer, result.get());
  }

  @Test
  public void cancelShouldReturnFalseWhenListingDoesNotExist() {
    when(offerRepository.findById(123)).thenReturn(Optional.empty());
    OfferService service = new OfferService(offerRepository, productRepository);
    assertFalse(service.cancel(123));
    verify(offerRepository, times(0)).save(any());
  }

  @Test
  public void cancelShouldPersistCancelledListing() {
    ArgumentCaptor<Offer> offerArgumentCaptor = ArgumentCaptor.forClass(Offer.class);
    Offer offer = new Offer();
    offer.setId(123);
    when(offerRepository.findById(123)).thenReturn(Optional.of(offer));
    OfferService service = new OfferService(offerRepository, productRepository);
    assertTrue(service.cancel(123));
    verify(offerRepository, times(1)).save(offerArgumentCaptor.capture());
    Offer offerSave = offerArgumentCaptor.getValue();
    assertEquals(Integer.valueOf(123), offerSave.getId());
    assertEquals(Offer.State.CANCELLED, offerSave.getState());
  }

  @Test
  public void activeShouldQueryActiveAndUseCurrentDate() {
    when(offerRepository.findByStateIsAndExpiresGreaterThanEqual(Offer.State.ACTIVE, LocalDate.now()))
      .thenReturn(Collections.emptyList());
    OfferService service = new OfferService(offerRepository, productRepository);
    List<Offer> offers = service.active();
    assertEquals(Collections.emptyList(), offers);
  }
}
