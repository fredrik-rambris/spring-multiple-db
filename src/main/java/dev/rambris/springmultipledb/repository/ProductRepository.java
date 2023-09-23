package dev.rambris.springmultipledb.repository;

import dev.rambris.springmultipledb.model.Product;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProductRepository {
    private final NamedParameterJdbcTemplate productTemplate;
    private final JdbcClient productClient;

    public ProductRepository(NamedParameterJdbcTemplate productTemplate, JdbcClient productClient) {
        this.productTemplate = productTemplate;
        this.productClient = productClient;
        productTemplate.update("drop table if exists product", EmptySqlParameterSource.INSTANCE);
        productTemplate.update("""
                create table product (
                    product_id serial not null primary key,
                    name text,
                    sku text,
                    stock int
                )
                """, EmptySqlParameterSource.INSTANCE);
    }

    public Optional<Product> getProductById(int id) {
        return productClient
                .sql("select * from product where product_id = :productId")
                .param("productId", id)
                .query(Product.class)
                .optional();
    }

    public int insertProduct(Product product) {
        var kh = new GeneratedKeyHolder();

        productClient
                .sql("""
                        insert into product (
                            name,
                            sku,
                            stock
                        ) values (
                            :name,
                            :sku,
                            :stock
                        )
                        """)
                .paramSource(new BeanPropertySqlParameterSource(product))
                .update(kh);

        return (int)kh.getKeys().get("product_id");
    }

}
