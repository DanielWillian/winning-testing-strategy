package com.example.demo.winning.testing.strategy.application

import com.example.demo.winning.testing.strategy.domain.Order
import com.example.demo.winning.testing.strategy.domain.OrderId
import com.example.demo.winning.testing.strategy.domain.OrderNotFoundException
import com.example.demo.winning.testing.strategy.domain.OrderRepository
import com.example.demo.winning.testing.strategy.domain.OrderService
import com.example.demo.winning.testing.strategy.domain.SupplierService

class OrderServiceImpl(
    private val repository: OrderRepository,
    private val supplierService: SupplierService
) : OrderService {
  override fun trySetOrderToReady(orderId: OrderId): Order {
    val order =
        repository.getOrder(orderId)
            ?: throw OrderNotFoundException("Could find order with id: ${orderId.id}")
    val productSupplier = order.getProductSupplier()
    val response = supplierService.getProductStatus(orderId, productSupplier) ?: return order
    val updatedOrder = order.trySetOrderToReady(response)
    if (order == updatedOrder) return order
    repository.updateOrder(updatedOrder)
    return updatedOrder
  }
}
