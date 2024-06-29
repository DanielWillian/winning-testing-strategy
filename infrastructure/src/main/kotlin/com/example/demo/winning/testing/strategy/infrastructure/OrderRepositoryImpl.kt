package com.example.demo.winning.testing.strategy.infrastructure

import com.example.demo.winning.testing.strategy.domain.Order
import com.example.demo.winning.testing.strategy.domain.OrderId
import com.example.demo.winning.testing.strategy.domain.OrderRepository
import com.example.demo.winning.testing.strategy.domain.OrderStatus
import com.example.demo.winning.testing.strategy.domain.Product
import java.sql.ResultSet
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementSetter
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryImpl(private val jdbcTemplate: JdbcTemplate) : OrderRepository {
  override fun getOrder(orderId: OrderId): Order? {
    return jdbcTemplate.query(
        "SELECT order_id, status, product FROM orders WHERE order_id = ?",
        PreparedStatementSetter { it.setLong(1, orderId.id) },
        ResultSetExtractor { toOrder(it) })
  }

  private fun toOrder(resultSet: ResultSet): Order? {
    resultSet.use {
      if (!resultSet.next()) return null

      val orderId = resultSet.getLong("order_id")
      val status = resultSet.getString("status")
      val product = resultSet.getString("product")
      return Order(OrderId(orderId), Product.valueOf(product), OrderStatus.valueOf(status))
    }
  }

  override fun updateOrder(order: Order) {
    jdbcTemplate.update("UPDATE orders SET status = ? WHERE order_id = ?") {
      it.setString(1, order.status.toString())
      it.setLong(2, order.orderId.id)
    }
  }
}
