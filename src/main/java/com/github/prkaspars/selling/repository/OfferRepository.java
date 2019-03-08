package com.github.prkaspars.selling.repository;

import com.github.prkaspars.selling.model.Offer;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface OfferRepository extends CrudRepository<Offer, Integer> {

  List<Offer> findByStateIsAndExpiresGreaterThanEqual(Offer.State state, LocalDate date);
}
