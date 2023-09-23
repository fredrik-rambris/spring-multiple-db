package dev.rambris.springmultipledb;

import dev.rambris.springmultipledb.model.Customer;
import dev.rambris.springmultipledb.model.Order;
import dev.rambris.springmultipledb.model.OrderLine;
import dev.rambris.springmultipledb.model.Product;
import dev.rambris.springmultipledb.repository.CustomerRepository;
import dev.rambris.springmultipledb.repository.OrderRepository;
import dev.rambris.springmultipledb.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.util.List;

@SpringBootApplication
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner runner(
            CustomerRepository customerRepository,
            ProductRepository productRepository,
            OrderRepository orderRepository
    ) {
        return args -> {
            var steveId = customerRepository.insertCustomer(new Customer(
                 null,
                 "Steve McQueen",
                 "steve.mcqueen@example.org"
            ));

            log.info("Inserted customer {}", steveId);

            var steveCustomer = customerRepository.getCustomerById(steveId).orElseThrow();

            log.info("Got customer {}", steveCustomer);

            var productId = productRepository.insertProduct(new Product(
                    null,
                    "1968 Mustang hub cap",
                    "mustang-68-hubcap",
                    10
            ));

            log.info("Inserted product {}", productId);

            var hubCap = productRepository.getProductById(productId).orElseThrow();

            log.info("Got product {}", hubCap);

            var orderId = orderRepository.insertOrder(new Order(
                    null,
                    steveId,
                    Instant.now(),
                    List.of(
                            new OrderLine(
                                    null,
                                    null,
                                    productId,
                                    1
                            )
                    )
            ));

            log.info("Inserted order {}", orderId);

            var order = orderRepository.getOrderById(orderId).orElseThrow();

            log.info("Got order {}", order);
        };
    }

}
