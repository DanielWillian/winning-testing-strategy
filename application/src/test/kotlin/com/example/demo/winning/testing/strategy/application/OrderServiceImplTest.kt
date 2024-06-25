package com.example.demo.winning.testing.strategy.application

import com.example.demo.winning.testing.strategy.domain.OrderId
import com.example.demo.winning.testing.strategy.domain.OrderNotFoundException
import com.example.demo.winning.testing.strategy.domain.OrderRepository
import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.assertThrows

class OrderServiceImplTest :
    StringSpec({
      "Try to set to ready a non existing order" {
        val repository = mockk<OrderRepository>()
        val orderService = OrderServiceImpl(repository)
        val orderId = OrderId(1)
        every { repository.getOrder(orderId) } returns null
        assertThrows<OrderNotFoundException> { orderService.trySetOrderToReady(orderId) }
      }
    })
