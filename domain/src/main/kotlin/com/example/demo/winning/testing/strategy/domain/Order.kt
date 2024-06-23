package com.example.demo.winning.testing.strategy.domain

enum class Product {
  APPLE
}

enum class ProductSupplier {
  GROCERY
}

class OrderId(private val id: Long)

class Order(private val orderId: OrderId, private val product: Product) {
  fun getProductSupplier(): ProductSupplier =
      when (product) {
        Product.APPLE -> ProductSupplier.GROCERY
      }
}
