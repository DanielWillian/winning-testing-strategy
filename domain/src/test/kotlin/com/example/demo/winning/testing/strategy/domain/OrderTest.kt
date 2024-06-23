package com.example.demo.winning.testing.strategy.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class OrderTest :
    StringSpec({
      "Order for Apples has grocery supplier" {
        val order = Order(OrderId(1), Product.APPLE)
        val supplier = order.getProductSupplier()
        supplier shouldBe ProductSupplier.GROCERY
      }
    })
