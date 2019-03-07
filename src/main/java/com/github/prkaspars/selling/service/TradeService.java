package com.github.prkaspars.selling.service;

import com.github.prkaspars.selling.model.Listing;
import com.github.prkaspars.selling.model.Offer;
import com.github.prkaspars.selling.repository.ListingRepository;
import com.github.prkaspars.selling.repository.OfferRepository;
import com.github.prkaspars.selling.request.OfferPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Currency;

@Service
public class TradeService {
  private ListingRepository listingRepository;
  private OfferRepository offerRepository;

  @Autowired
  public TradeService(ListingRepository listingRepository, OfferRepository offerRepository) {
    this.listingRepository = listingRepository;
    this.offerRepository = offerRepository;
  }

  public Listing list(OfferPayload payload) {
    Offer offer = new Offer();
    offer.setName(payload.getName());
    offer.setDescription(payload.getDescription());
    offer.setCurrency(Currency.getInstance(payload.getCurrency()));
    offer.setPrice(payload.getPrice());
    offer = offerRepository.save(offer);

    Listing listing = new Listing();
    listing.setOffer(offer);
    listing.setExpires(LocalDate.now().plusDays(payload.getDuration()));
    listing.setState(Listing.State.ACTIVE);
    return listingRepository.save(listing);
  }
}
