package com.github.prkaspars.selling.service;

import com.github.prkaspars.selling.model.Listing;
import com.github.prkaspars.selling.model.Offer;
import com.github.prkaspars.selling.repository.ListingRepository;
import com.github.prkaspars.selling.repository.OfferRepository;
import com.github.prkaspars.selling.request.OfferPayload;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class TradeServiceTest {
  private ListingRepository listingRepository;
  private OfferRepository offerRepository;

  @Before
  public void setUp() {
    listingRepository = mock(ListingRepository.class);
    offerRepository = mock(OfferRepository.class);
  }

  @Test
  public void listOffer() {
    ArgumentCaptor<Listing> listingArgumentCaptor = ArgumentCaptor.forClass(Listing.class);
    ArgumentCaptor<Offer> offerArgumentCaptor = ArgumentCaptor.forClass(Offer.class);

    when(offerRepository.save(any(Offer.class)))
      .thenAnswer(a -> {
        Offer in = a.getArgument(0);
        Offer out = new Offer();
        out.setId(123);
        out.setName(in.getName());
        out.setDescription(in.getDescription());
        out.setCurrency(in.getCurrency());
        out.setPrice(in.getPrice());
        return out;
      });

    OfferPayload payload = new OfferPayload();
    payload.setName("Foo");
    payload.setDescription("Bar");
    payload.setCurrency("GBP");
    payload.setPrice(89.99);
    payload.setDuration(17);

    TradeService service = new TradeService(listingRepository, offerRepository);
    service.list(payload);

    verify(listingRepository, times(1)).save(listingArgumentCaptor.capture());
    verify(offerRepository, times(1)).save(offerArgumentCaptor.capture());

    Offer offer = offerArgumentCaptor.getValue();
    assertNull(offer.getId());
    assertEquals("Foo", offer.getName());
    assertEquals("Bar", offer.getDescription());
    assertEquals(Currency.getInstance("GBP"), offer.getCurrency());
    assertEquals(89.99, offer.getPrice(), 0);

    Listing listing = listingArgumentCaptor.getValue();
    assertNull(listing.getId());
    assertEquals(Integer.valueOf(123), listing.getOffer().getId());
    assertEquals(LocalDate.now().plusDays(17), listing.getExpires());
    assertEquals(Listing.State.ACTIVE, listing.getState());
  }

  @Test
  public void readShouldReturnEmptyWhenRepoReturnsEmpty() {
    when(listingRepository.findById(123)).thenReturn(Optional.empty());
    TradeService service = new TradeService(listingRepository, offerRepository);
    Optional<Listing> result = service.read(123);
    assertEquals(Optional.empty(), result);
  }

  @Test
  public void readShouldNotReturnCancelledListing() {
    Listing listing = new Listing();
    listing.setState(Listing.State.CANCELLED);
    listing.setExpires(LocalDate.now().plusDays(2));
    when(listingRepository.findById(123)).thenReturn(Optional.of(listing));
    TradeService service = new TradeService(listingRepository, offerRepository);
    Optional<Listing> result = service.read(123);
    assertEquals(Optional.empty(), result);
  }

  @Test
  public void readShouldNotReturnExpiredListing() {
    Listing listing = new Listing();
    listing.setState(Listing.State.ACTIVE);
    listing.setExpires(LocalDate.now().minusDays(2));
    when(listingRepository.findById(123)).thenReturn(Optional.of(listing));
    TradeService service = new TradeService(listingRepository, offerRepository);
    Optional<Listing> result = service.read(123);
    assertEquals(Optional.empty(), result);
  }

  @Test
  public void readShouldReturnListing() {
    Listing listing = new Listing();
    listing.setState(Listing.State.ACTIVE);
    listing.setExpires(LocalDate.now().plusDays(2));
    when(listingRepository.findById(123)).thenReturn(Optional.of(listing));
    TradeService service = new TradeService(listingRepository, offerRepository);
    Optional<Listing> result = service.read(123);
    assertEquals(listing, result.get());
  }
}
