package com.example.demo.winning.testing.strategy.domain

class OrderNotFoundException(message: String) : RuntimeException(message)

interface OrderService {
  fun trySetOrderToReady(orderId: OrderId): Order
}

interface OrderRepository {
  fun getOrder(orderId: OrderId): Order?

  fun updateOrder(order: Order)
}
