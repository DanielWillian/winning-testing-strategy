package com.example.demo.winning.testing.strategy

import org.springframework.boot.fromApplication
import org.springframework.boot.with

fun main(args: Array<String>) {
  fromApplication<WinningTestingStrategyApplication>()
      .with(TestcontainersConfiguration::class)
      .run(*args)
}
