package org.kiroff.products.services;

import org.kiroff.products.arguments.CreateProductArgument;

import java.util.concurrent.ExecutionException;

public interface ProductService
{
    String createProduct(CreateProductArgument product) throws ExecutionException, InterruptedException;

    String getProduct(String productId);
}
