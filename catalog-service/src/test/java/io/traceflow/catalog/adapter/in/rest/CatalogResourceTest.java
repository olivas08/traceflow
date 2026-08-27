package io.traceflow.catalog.adapter.in.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

@QuarkusTest
class CatalogResourceTest {
    @Test
    void registersPlantAndComponentAndReadsTrace() {
        given().contentType(ContentType.JSON)
                .body(
                        """
                        {"code":"PT01","name":"Porto Plant","country":"PT"}
                        """)
                .when()
                .post("/api/v1/plants")
                .then()
                .statusCode(201)
                .body("code", equalTo("PT01"));

        given().contentType(ContentType.JSON)
                .header("X-Correlation-Id", "it-1")
                .body(
                        """
                        {"plantCode":"PT01","serialNumber":"SN-IT-1","partNumber":"BRAKE-DISC"}
                        """)
                .when()
                .post("/api/v1/components")
                .then()
                .statusCode(201)
                .body("status", equalTo("REGISTERED"));

        given().when()
                .get("/api/v1/components/SN-IT-1/trace")
                .then()
                .statusCode(200)
                .body("component.serialNumber", equalTo("SN-IT-1"))
                .body("timeline", hasSize(1));
    }
}
