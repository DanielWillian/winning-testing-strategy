package com.example.demo.winning.testing.strategy.application

import com.example.demo.winning.testing.strategy.domain.Order
import com.example.demo.winning.testing.strategy.domain.OrderId
import com.example.demo.winning.testing.strategy.domain.OrderNotFoundException
import com.example.demo.winning.testing.strategy.domain.OrderRepository
import com.example.demo.winning.testing.strategy.domain.OrderStatus
import com.example.demo.winning.testing.strategy.domain.Product
import com.example.demo.winning.testing.strategy.domain.ProductSupplier
import com.example.demo.winning.testing.strategy.domain.SupplierResponse
import com.example.demo.winning.testing.strategy.domain.SupplierService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.assertThrows

class OrderServiceImplTest :
    StringSpec({
      "Try to set to ready a non existing order" {
        val repository = mockk<OrderRepository>()
        val supplierService = mockk<SupplierService>()
        val orderService = OrderServiceImpl(repository, supplierService)
        val orderId = OrderId(1)
        every { repository.getOrder(orderId) } returns null
        assertThrows<OrderNotFoundException> { orderService.trySetOrderToReady(orderId) }
      }

      "Try to set an order ready with non existing response" {
        val repository = mockk<OrderRepository>()
        val supplierService = mockk<SupplierService>()
        val orderService = OrderServiceImpl(repository, supplierService)
        val orderId = OrderId(1)
        every { repository.getOrder(orderId) } returns
            Order(orderId, Product.BANANA, OrderStatus.CREATED)
        every { supplierService.getProductStatus(orderId, ProductSupplier.SUPERMARKET) } returns
            null
        val order = orderService.trySetOrderToReady(orderId)
        order.status shouldBe OrderStatus.CREATED
      }

      "Try to set an order ready with negative response" {
        val repository = mockk<OrderRepository>()
        val supplierService = mockk<SupplierService>()
        val orderService = OrderServiceImpl(repository, supplierService)
        val orderId = OrderId(1)
        every { repository.getOrder(orderId) } returns
            Order(orderId, Product.BANANA, OrderStatus.CREATED)
        every { supplierService.getProductStatus(orderId, ProductSupplier.SUPERMARKET) } returns
            SupplierResponse(1)
        val order = orderService.trySetOrderToReady(orderId)
        order.status shouldBe OrderStatus.CREATED
      }

      "Try to set an order ready with positive response" {
        val repository = mockk<OrderRepository>()
        val supplierService = mockk<SupplierService>()
        val orderService = OrderServiceImpl(repository, supplierService)
        val orderId = OrderId(1)
        every { repository.getOrder(orderId) } returns
            Order(orderId, Product.BANANA, OrderStatus.CREATED)
        every { supplierService.getProductStatus(orderId, ProductSupplier.SUPERMARKET) } returns
            SupplierResponse(200)
        val order = orderService.trySetOrderToReady(orderId)
        order.status shouldBe OrderStatus.READY
      }
    })
