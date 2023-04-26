package org.ncpclous.sample.cloudsearch;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ApplicationTests {

    @Test
    public void testGetDomains() {
        Response response = RestAssured.get("http://localhost:8081/api/domain");
        assertEquals(200, response.getStatusCode());
    }

	@Test
	public void contextLoads() {
	}

}
