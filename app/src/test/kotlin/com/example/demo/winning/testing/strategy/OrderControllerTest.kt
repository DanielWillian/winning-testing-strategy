package com.example.demo.winning.testing.strategy

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import java.net.URI
import java.net.http.HttpClient
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.core.env.Environment
import org.springframework.test.context.jdbc.Sql
import org.testcontainers.containers.MockServerContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["classpath:schema-test.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class OrderControllerTest : FunSpec() {
  @Autowired lateinit var environment: Environment
  @Autowired lateinit var objectMapper: ObjectMapper
  lateinit var mockServerClient: MockServerClient

  override fun extensions() = listOf(SpringExtension)

  init {
    mockServerClient = MockServerClient(mockServer.host, mockServer.serverPort)

    val httpClient = HttpClient.newHttpClient()

    context("Update apple order to ready") {
      mockServerClient
          .`when`(HttpRequest.request().withPath("/apples/1"))
          .respond(HttpResponse.response().withStatusCode(200))

      val port = environment.getProperty("local.server.port")
      val baseUrl = "http://localhost:$port"
      val httpRequest =
          java.net.http.HttpRequest.newBuilder()
              .uri(URI.create("$baseUrl/orders/1/set-ready"))
              .POST(java.net.http.HttpRequest.BodyPublishers.noBody())
              .build()
      val response =
          httpClient.send(httpRequest, java.net.http.HttpResponse.BodyHandlers.ofString())

      test("Response is 200") { response.statusCode() shouldBe 200 }
      val json = objectMapper.readTree(response.body())
      test("Order is ready") { json.get("status").textValue() shouldBe "READY" }
    }

    context("Update banana order to ready") {
      mockServerClient
          .`when`(HttpRequest.request().withPath("/bananas/2"))
          .respond(HttpResponse.response().withStatusCode(200))

      val port = environment.getProperty("local.server.port")
      val baseUrl = "http://localhost:$port"
      val httpRequest =
          java.net.http.HttpRequest.newBuilder()
              .uri(URI.create("$baseUrl/orders/2/set-ready"))
              .POST(java.net.http.HttpRequest.BodyPublishers.noBody())
              .build()
      val response =
          httpClient.send(httpRequest, java.net.http.HttpResponse.BodyHandlers.ofString())

      test("Response is 200") { response.statusCode() shouldBe 200 }
      val json = objectMapper.readTree(response.body())
      test("Order is ready") { json.get("status").textValue() shouldBe "READY" }
    }

    context("Update apple order to ready all in one") {
      mockServerClient
          .`when`(HttpRequest.request().withPath("/apples/1"))
          .respond(HttpResponse.response().withStatusCode(200))

      val port = environment.getProperty("local.server.port")
      val baseUrl = "http://localhost:$port"
      val httpRequest =
          java.net.http.HttpRequest.newBuilder()
              .uri(URI.create("$baseUrl/orders/1/all-in-one-set-ready"))
              .POST(java.net.http.HttpRequest.BodyPublishers.noBody())
              .build()
      val response =
          httpClient.send(httpRequest, java.net.http.HttpResponse.BodyHandlers.ofString())

      test("Response is 200") { response.statusCode() shouldBe 200 }
      val json = objectMapper.readTree(response.body())
      test("Order is ready") { json.get("status").textValue() shouldBe "READY" }
    }
  }

  companion object {
    @Container
    @ServiceConnection
    val postgres = PostgreSQLContainer(DockerImageName.parse("postgres:latest"))

    @Container
    val mockServer =
        MockServerContainer(DockerImageName.parse("mockserver/mockserver:mockserver-5.15.0"))

    init {
      postgres.start()
      mockServer.start()
      System.setProperty("api.grocery.url", "http://localhost:${mockServer.serverPort}")
      System.setProperty("api.supermarket.url", "http://localhost:${mockServer.serverPort}")
    }
  }
}
