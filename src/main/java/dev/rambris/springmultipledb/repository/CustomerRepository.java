package dev.rambris.springmultipledb.repository;

import dev.rambris.springmultipledb.model.Customer;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CustomerRepository {

    private final NamedParameterJdbcTemplate customerTemplate;
    private final JdbcClient customerClient;

    public CustomerRepository(
            NamedParameterJdbcTemplate customerTemplate,
            JdbcClient customerClient) {
        this.customerTemplate = customerTemplate;
        this.customerClient = customerClient;

        customerTemplate.update("drop table if exists customer", EmptySqlParameterSource.INSTANCE);
        customerTemplate.update("""
                create table customer (
                    customer_id serial not null primary key,
                    name text,
                    email text
                )
                """, EmptySqlParameterSource.INSTANCE);
    }

    public Optional<Customer> getCustomerById(int id) {
        return customerClient
                .sql("select * from customer where customer_id = :customerId")
                .param("customerId", id)
                .query(Customer.class)
                .optional();
    }

    public int insertCustomer(Customer customer) {
        var kh = new GeneratedKeyHolder();

        customerClient
                .sql("""
                        insert into customer (
                            name,
                            email                        
                        ) values (
                            :name,
                            :email
                        )
                        """)
                .paramSource(new BeanPropertySqlParameterSource(customer))
                .update(kh);

        return (int)kh.getKeys().get("customer_id");
    }
}
