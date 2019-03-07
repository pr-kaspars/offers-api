package com.github.prkaspars.selling.repository;

import com.github.prkaspars.selling.model.Listing;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface ListingRepository extends CrudRepository<Listing, Integer> {

  List<Listing> findByStateIsAndExpiresGreaterThanEqual(Listing.State state, LocalDate date);
}
