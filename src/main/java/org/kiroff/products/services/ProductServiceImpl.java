package org.kiroff.products.services;

import org.kiroff.kafka.events.ProductCreatedEvent;
import org.kiroff.products.arguments.CreateProductArgument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class ProductServiceImpl implements ProductService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
    KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate)
    {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createProduct(CreateProductArgument product) throws ExecutionException, InterruptedException
    {
        final String productId = UUID.randomUUID().toString();

        final ProductCreatedEvent event = new ProductCreatedEvent(productId, product.getTitle(), product.getPrice(), product.getQuantity());
        LOGGER.info("Sending message {}", event);
//        kafkaTemplate.send("product-created-events-topic", productId, event)
//                .whenComplete((result, exception) -> {
//                    if (exception != null) {
//                        LOGGER.info("Unable to send product created events", exception);
//                    } else  {
//                        LOGGER.error("Message sent successfully.\n{}", result.getRecordMetadata());
//                    }
//                });
////                .join();//to block/syncronze the client/request
//        LOGGER.info("Created product with id {}", productId);
        final SendResult<String, ProductCreatedEvent> result = kafkaTemplate.send(
                "product-created-events-topic", productId, event).get();
        LOGGER.info("Sent to topic= {}, partition={}, offset={}", result.getRecordMetadata().topic(), result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
        return productId;
    }

    @Override
    public String getProduct(String productId)
    {
        return "Mock product " + productId;
    }

}
