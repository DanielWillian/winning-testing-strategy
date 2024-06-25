package com.example.demo.winning.testing.strategy.domain

enum class ProductSupplier {
  GROCERY,
  SUPERMARKET
}

class SupplierResponse(val code: Int)

interface SupplierService {
  fun getProductStatus(orderId: OrderId, supplier: ProductSupplier): SupplierResponse?
}
