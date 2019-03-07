package com.github.prkaspars.selling.repository;

import com.github.prkaspars.selling.model.Listing;
import org.springframework.data.repository.CrudRepository;

public interface ListingRepository extends CrudRepository<Listing, Integer> {
}
