package com.example.demo.winning.testing.strategy.application

import com.example.demo.winning.testing.strategy.domain.Order
import com.example.demo.winning.testing.strategy.domain.OrderId
import com.example.demo.winning.testing.strategy.domain.OrderNotFoundException
import com.example.demo.winning.testing.strategy.domain.OrderRepository
import com.example.demo.winning.testing.strategy.domain.OrderService

class OrderServiceImpl(private val repository: OrderRepository) : OrderService {
  override fun trySetOrderToReady(orderId: OrderId): Order {
    val order =
        repository.getOrder(orderId)
            ?: throw OrderNotFoundException("Could find order with id: ${orderId.id}")
    return order
  }
}
