package com.example.demo.winning.testing.strategy.domain

enum class Product {
  APPLE,
  BANANA
}

enum class ProductSupplier {
  GROCERY,
  SUPERMARKET
}

class OrderId(private val id: Long)

class Order(private val orderId: OrderId, private val product: Product) {
  fun getProductSupplier(): ProductSupplier =
      when (product) {
        Product.APPLE -> ProductSupplier.GROCERY
        Product.BANANA -> ProductSupplier.SUPERMARKET
      }
}
