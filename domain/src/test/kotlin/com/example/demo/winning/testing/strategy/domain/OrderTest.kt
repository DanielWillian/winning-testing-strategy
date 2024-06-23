package com.example.demo.winning.testing.strategy.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class OrderTest :
    StringSpec({
      "Order for Apples has grocery supplier" {
        val order = Order(OrderId(1), Product.APPLE, OrderStatus.CREATED)
        val supplier = order.getProductSupplier()
        supplier shouldBe ProductSupplier.GROCERY
      }

      "Order for Bananas has supermarket supplier" {
        val order = Order(OrderId(1), Product.BANANA, OrderStatus.CREATED)
        val supplier = order.getProductSupplier()
        supplier shouldBe ProductSupplier.SUPERMARKET
      }

      "Order remains created after a non 200 response from supplier" {
        val order = Order(OrderId(1), Product.BANANA, OrderStatus.CREATED)
        val updatedOrder = order.trySetOrderToReady(SupplierResponse(404))
        updatedOrder.status shouldBe OrderStatus.CREATED
      }

      "Order ready after a 200 response from supplier" {
        val order = Order(OrderId(1), Product.BANANA, OrderStatus.CREATED)
        val updatedOrder = order.trySetOrderToReady(SupplierResponse(200))
        updatedOrder.status shouldBe OrderStatus.READY
      }
    })
