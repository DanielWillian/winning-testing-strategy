package com.example.demo.winning.testing.strategy.infrastructure

import com.example.demo.winning.testing.strategy.domain.OrderId
import com.example.demo.winning.testing.strategy.domain.ProductSupplier
import com.example.demo.winning.testing.strategy.domain.SupplierResponse
import com.example.demo.winning.testing.strategy.domain.SupplierService
import io.github.rybalkinsd.kohttp.ext.httpGet
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class SupplierServiceImpl(@Value("\${api.grocery.url}") private val groceryUrl: String) :
    SupplierService {
  private val log = LoggerFactory.getLogger(javaClass)

  override fun getProductStatus(orderId: OrderId, supplier: ProductSupplier): SupplierResponse? {
    return when (supplier) {
      ProductSupplier.GROCERY -> callApi("$groceryUrl/apples/${orderId.id}")
      ProductSupplier.SUPERMARKET -> throw NotImplementedError()
    }
  }

  private fun callApi(url: String): SupplierResponse? {
    try {
      val response = url.httpGet()
      return SupplierResponse(response.code)
    } catch (e: Exception) {
      log.error("Could not send request to $url", e)
    }
    return null
  }
}
