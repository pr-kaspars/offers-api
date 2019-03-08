package com.github.prkaspars.selling.service;

import com.github.prkaspars.selling.model.Offer;
import com.github.prkaspars.selling.model.Product;
import com.github.prkaspars.selling.repository.OfferRepository;
import com.github.prkaspars.selling.repository.ProductRepository;
import com.github.prkaspars.selling.request.CreateOfferPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDate.now;

@Service
public class OfferService {
  private OfferRepository offerRepository;
  private ProductRepository productRepository;

  @Autowired
  public OfferService(OfferRepository offerRepository, ProductRepository productRepository) {
    this.offerRepository = offerRepository;
    this.productRepository = productRepository;
  }

  public Offer list(CreateOfferPayload payload) {
    Product product = new Product();
    product.setName(payload.getName());
    product.setDescription(payload.getDescription());
    product.setCurrency(Currency.getInstance(payload.getCurrency()));
    product.setPrice(payload.getPrice());
    product = productRepository.save(product);

    Offer offer = new Offer();
    offer.setProduct(product);
    offer.setExpires(now().plusDays(payload.getDuration()));
    offer.setState(Offer.State.ACTIVE);
    return offerRepository.save(offer);
  }

  public Optional<Offer> read(Integer id) {
    return offerRepository.findById(id)
      .filter(l -> l.getState() == Offer.State.ACTIVE && l.getExpires().isAfter(now()));
  }

  public Boolean cancel(Integer id) {
    Optional<Offer> optionalOffer = offerRepository.findById(id);
    optionalOffer.ifPresent(o -> {
      o.setState(Offer.State.CANCELLED);
      offerRepository.save(o);
    });
    return optionalOffer.isPresent();
  }

  public List<Offer> active() {
    return offerRepository.findByStateIsAndExpiresGreaterThanEqual(Offer.State.ACTIVE, now());
  }
}
