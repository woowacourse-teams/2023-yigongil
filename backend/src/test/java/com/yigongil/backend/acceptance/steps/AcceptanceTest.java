package com.yigongil.backend.acceptance.steps;

import com.yigongil.backend.BackendApplication;
import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = BackendApplication.class)
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @Before
    public void before() {
        RestAssured.port = port;
    }
}
