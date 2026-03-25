package org.kiroff.products.controllers;

import org.kiroff.products.arguments.CreateProductArgument;
import org.kiroff.products.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/products")
public class ProductRestController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductRestController.class);
    ProductService productService;

    public ProductRestController(ProductService productService)
    {
        this.productService = productService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Object> get(@PathVariable("productId") String productId) {
        final String product = this.productService.getProduct(productId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(product);
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody CreateProductArgument product)
    {
        final String result;
        try
        {
            result = this.productService.createProduct(product);
        }
        catch (ExecutionException | InterruptedException e)
        {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorMessage(e.getMessage(), LocalDateTime.now(), "/products"));
        }
        LOGGER.info("Created product= {}", result);
        return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Created product: id=%s.", result));
    }
}
