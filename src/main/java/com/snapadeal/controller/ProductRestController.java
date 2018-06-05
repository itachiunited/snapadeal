package com.snapadeal.controller;

import com.snapadeal.entity.Product;
import com.snapadeal.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/product/{id}")
public class ProductRestController {

    private final ProductRepository productRepository;

    @Autowired
    ProductRestController(ProductRepository productRepository)
    {
        this.productRepository = productRepository;
    }

    @GetMapping
    Product getProduct(@PathVariable String id)
    {
        return this.productRepository.findById(id).get();
    }

}
