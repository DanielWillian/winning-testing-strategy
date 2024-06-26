package com.example.demo.winning.testing.strategy.application

import com.example.demo.winning.testing.strategy.domain.Order
import com.example.demo.winning.testing.strategy.domain.OrderId
import com.example.demo.winning.testing.strategy.domain.OrderNotFoundException
import com.example.demo.winning.testing.strategy.domain.OrderRepository
import com.example.demo.winning.testing.strategy.domain.OrderService
import com.example.demo.winning.testing.strategy.domain.OrderStatus
import com.example.demo.winning.testing.strategy.domain.Product
import com.example.demo.winning.testing.strategy.domain.ProductSupplier
import com.example.demo.winning.testing.strategy.domain.SupplierResponse
import com.example.demo.winning.testing.strategy.domain.SupplierService
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.junit.jupiter.api.assertThrows

class OrderServiceImplTest :
    FunSpec({
      val orderId = OrderId(1)
      lateinit var repository: OrderRepository
      lateinit var supplierService: SupplierService
      lateinit var orderService: OrderService

      beforeEach {
        repository = mockk<OrderRepository>()
        supplierService = mockk<SupplierService>()
        orderService = OrderServiceImpl(repository, supplierService)
      }

      test("Try to set to ready a non existing order") {
        every { repository.getOrder(orderId) } returns null
        assertThrows<OrderNotFoundException> { orderService.trySetOrderToReady(orderId) }
      }

      test("Try to set an order ready with non existing response") {
        every { repository.getOrder(orderId) } returns
            Order(orderId, Product.BANANA, OrderStatus.CREATED)
        every { supplierService.getProductStatus(orderId, ProductSupplier.SUPERMARKET) } returns
            null
        val order = orderService.trySetOrderToReady(orderId)
        order.status shouldBe OrderStatus.CREATED
      }

      test("Try to set an order ready with negative response") {
        every { repository.getOrder(orderId) } returns
            Order(orderId, Product.BANANA, OrderStatus.CREATED)
        every { supplierService.getProductStatus(orderId, ProductSupplier.SUPERMARKET) } returns
            SupplierResponse(1)
        val order = orderService.trySetOrderToReady(orderId)
        order.status shouldBe OrderStatus.CREATED
      }

      listOf(
              Pair(Product.BANANA, ProductSupplier.SUPERMARKET),
              Pair(Product.APPLE, ProductSupplier.GROCERY))
          .forEach {
            test("Try to set an order of ${it.first} ready with positive response") {
              every { repository.getOrder(orderId) } returns
                  Order(orderId, it.first, OrderStatus.CREATED)
              every { supplierService.getProductStatus(orderId, it.second) } returns
                  SupplierResponse(200)
              justRun { repository.updateOrder(Order(orderId, it.first, OrderStatus.READY)) }
              val order = orderService.trySetOrderToReady(orderId)
              order.status shouldBe OrderStatus.READY
            }
          }
    })
