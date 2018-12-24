package org.ncpclous.sample.cloudsearch;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
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

