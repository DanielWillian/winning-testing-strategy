package com.example.demo.winning.testing.strategy.domain

enum class Product {
  APPLE,
  BANANA
}

data class OrderId(val id: Long)

enum class OrderStatus {
  CREATED,
  READY
}

data class Order(val orderId: OrderId, val product: Product, val status: OrderStatus) {
  fun getProductSupplier(): ProductSupplier =
      when (product) {
        Product.APPLE -> ProductSupplier.GROCERY
        Product.BANANA -> ProductSupplier.SUPERMARKET
      }

  fun trySetOrderToReady(response: SupplierResponse): Order =
      if (response.code == 200) copy(status = OrderStatus.READY) else this
}
