package io.traceflow.inspection.adapter.in.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

@QuarkusTest
class InspectionResourceTest {
    @Test
    void recordsInspectionOnceForSameIdempotencyKey() {
        String body =
                """
                {"serialNumber":"SN-IT-1","result":"FAIL","inspector":"ada","notes":"crack"}
                """;

        String id =
                given().contentType(ContentType.JSON)
                        .header("Idempotency-Key", "it-key-1")
                        .body(body)
                        .when()
                        .post("/api/v1/inspections")
                        .then()
                        .statusCode(201)
                        .body("result", equalTo("FAIL"))
                        .extract()
                        .path("id");

        given().contentType(ContentType.JSON)
                .header("Idempotency-Key", "it-key-1")
                .body(body)
                .when()
                .post("/api/v1/inspections")
                .then()
                .statusCode(201)
                .body("id", equalTo(id));
    }
}
