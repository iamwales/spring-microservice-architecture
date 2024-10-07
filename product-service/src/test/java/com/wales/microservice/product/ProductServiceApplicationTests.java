package com.wales.microservice.product;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

	@ServiceConnection
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5");

	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	static {
		mongoDBContainer.start();
	}

	@Test
	void shouldCreateProduct() {
		String requestBody = """
				{
				    "name": "iPhone 16",
				    "description": "latest iPhone from apple",
				    "price": 1200
				}
				""";

		RestAssured.given().contentType("application/json")
				.body(requestBody).when().post("/api/v1/products")
				.then().statusCode(201).body("id", Matchers.notNullValue())
				.body("name", Matchers.equalTo("iPhone 16"))
				.body("description", Matchers.equalTo("latest iPhone from apple"))
				.body("price", Matchers.equalTo(1200));
	}

}
