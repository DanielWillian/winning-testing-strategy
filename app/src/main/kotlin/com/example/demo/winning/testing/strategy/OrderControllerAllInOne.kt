package com.example.demo.winning.testing.strategy

import com.example.demo.winning.testing.strategy.domain.Order
import com.example.demo.winning.testing.strategy.domain.OrderId
import com.example.demo.winning.testing.strategy.domain.OrderStatus
import com.example.demo.winning.testing.strategy.domain.Product
import io.github.rybalkinsd.kohttp.ext.httpGet
import java.sql.ResultSet
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementSetter
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ORDERS_MAPPING)
class OrderControllerAllInOne(
    private val jdbcTemplate: JdbcTemplate,
    @Value("\${api.grocery.url}") private val groceryUrl: String,
    @Value("\${api.supermarket.url}") private val supermarketUrl: String
) {
  @PostMapping("/{id}/all-in-one-set-ready")
  fun setOrderReady(@PathVariable id: Long): ResponseEntity<Order> {
    val order =
        jdbcTemplate.query(
            "SELECT order_id, status, product FROM orders WHERE order_id = ?",
            PreparedStatementSetter { it.setLong(1, id) },
            ResultSetExtractor { toOrder(it) })
    if (order == null) return ResponseEntity.notFound().build()

    val response =
        when (order.product) {
          Product.APPLE -> "$groceryUrl/apples/$id".httpGet()
          Product.BANANA -> "$supermarketUrl/bananas/$id".httpGet()
        }
    if (response.code != 200) return ResponseEntity.ok(order)

    val updatedOrder = order.copy(status = OrderStatus.READY)

    jdbcTemplate.update("UPDATE orders SET status = ? WHERE order_id = ?") {
      it.setString(1, updatedOrder.status.toString())
      it.setLong(2, updatedOrder.orderId.id)
    }

    return ResponseEntity.ok(updatedOrder)
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
}
