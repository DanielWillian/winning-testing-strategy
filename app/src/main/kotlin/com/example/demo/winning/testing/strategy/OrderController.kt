package com.example.demo.winning.testing.strategy

import com.example.demo.winning.testing.strategy.domain.Order
import com.example.demo.winning.testing.strategy.domain.OrderId
import com.example.demo.winning.testing.strategy.domain.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

const val ORDERS_MAPPING = "/orders"

@RestController
@RequestMapping(ORDERS_MAPPING)
class OrderController(val orderService: OrderService) {
  @PostMapping("/{id}/set-ready")
  fun setOrderReady(@PathVariable id: Long): ResponseEntity<Order> {
    val order = orderService.trySetOrderToReady(OrderId(id))
    return ResponseEntity.ok(order)
  }
}
