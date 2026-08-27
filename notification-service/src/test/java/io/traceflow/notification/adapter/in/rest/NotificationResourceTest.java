package io.traceflow.notification.adapter.in.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class NotificationResourceTest {
    @Test
    void listsEmptyInbox() {
        given().when()
                .get("/api/v1/notifications")
                .then()
                .statusCode(200)
                .body("total", equalTo(0));
    }
}
