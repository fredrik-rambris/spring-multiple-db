package dev.rambris.springmultipledb.repository;

import dev.rambris.springmultipledb.model.Order;
import dev.rambris.springmultipledb.model.OrderLine;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepository {
    private final NamedParameterJdbcTemplate orderTemplate;
    private final JdbcClient orderClient;

    public OrderRepository(NamedParameterJdbcTemplate orderTemplate, JdbcClient orderClient) {
        this.orderTemplate = orderTemplate;
        this.orderClient = orderClient;
        orderTemplate.update("drop table if exists order_line", EmptySqlParameterSource.INSTANCE);
        orderTemplate.update("drop table if exists \"order\"", EmptySqlParameterSource.INSTANCE);
        orderTemplate.update("""
                create table order_line (
                    order_line_id serial not null primary key,
                    order_id int not null,
                    product_id int not null,
                    quantity int not null
                )
                """, EmptySqlParameterSource.INSTANCE);
        orderTemplate.update("""
                create table "order" (
                    order_id serial not null primary key,
                    customer_id int not null,
                    purchased_at timestamptz not null default current_timestamp
                )
                """, EmptySqlParameterSource.INSTANCE);
    }

    public Optional<Order> getOrderById(int id) {
        var orderLines = orderClient
                .sql("select * from order_line where order_id = :orderId")
                .param("orderId", id)
                .query(OrderLine.class)
                .list();

        return orderClient
                .sql("select * from \"order\" where order_id = :orderId")
                .param("orderId", id)
                .query((rs, i) -> new Order(
                        rs.getInt("order_id"),
                        rs.getInt("customer_id"),
                        Optional.ofNullable(rs.getTimestamp("purchased_at")).map(Timestamp::toInstant).orElse(null),
                        null
                ))
                .optional()
                .map(order -> order.withOrderLines(orderLines));
    }

    public int insertOrder(Order order) {
        var kh = new GeneratedKeyHolder();

        orderClient
                .sql("""
                        insert into "order" (
                            customer_id
                        ) values (
                            :customerId
                        )
                        """)
                .paramSource(new BeanPropertySqlParameterSource(order))
                .update(kh);

        var orderId = (int) kh.getKeys().get("order_id");

        order.orderLines().forEach(orderLine ->
                orderClient
                        .sql("""
                                insert into order_line (
                                    order_id,
                                    product_id,
                                    quantity
                                ) values (
                                    :orderId,
                                    :productId,
                                    :quantity
                                )
                                """)
                        .param("orderId", orderId)
                        .param("productId", orderLine.productId())
                        .param("quantity", orderLine.quantity())
                        .update()
        );

        return orderId;
    }
}
