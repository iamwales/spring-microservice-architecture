package com.wales.microservice.order;

import com.wales.microservice.order.stubs.InventoryClientStub;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
class OrderServiceApplicationTests {

	@ServiceConnection
	static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.3.0");

	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	static {
		mySQLContainer.start();
	}


	@Test
	void shouldSubmitOrder() {
		String submitOrderJson = """
				{
				    "skuCode": "iphone_16",
				    "price": 1200,
				    "quantity": 1
				}
				""";

		// We need to mock the inventory microservice as it's not good practice to call other services for test
		// we can use mockito or wiremock to mock the inventory microservice

		InventoryClientStub.stubInventoryCall("iphone_16", 1);

		RestAssured.given().contentType("application/json")
				.body(submitOrderJson).when().post("/api/v1/orders")
				.then().statusCode(201).body("id", Matchers.notNullValue())
				.body("skuCode", Matchers.equalTo("iphone_16"))
				.body("quantity", Matchers.equalTo(1))
				.body("price", Matchers.equalTo(1200));
	}

}
