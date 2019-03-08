package com.github.prkaspars.selling.repository;

import com.github.prkaspars.selling.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer> {
}
