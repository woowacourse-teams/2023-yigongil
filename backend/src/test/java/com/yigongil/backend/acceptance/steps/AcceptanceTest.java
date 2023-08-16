package com.yigongil.backend.acceptance.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigongil.backend.BackendApplication;
import com.yigongil.backend.config.auth.TokenTheftDetector;
import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = BackendApplication.class)
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ApplicationContext applicationContext;

    @Before
    public void before() {
        RestAssured.port = port;
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
                new ObjectMapperConfig().jackson2ObjectMapperFactory(
                        (cls, charset) -> objectMapper
                ));
        TokenTheftDetector tokenTheftDetector = applicationContext.getBean(TokenTheftDetector.class);
        tokenTheftDetector.getStoredTokens().clear();

        databaseCleaner.clean();
    }
}
