package com.pnc.assignment.aggregator.controller

import io.restassured.RestAssured
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

/*@SpringBootTest(
	classes = AggregatorApplication.class,
	webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)*/
//@ActiveProfiles(value = "test")
//@PropertySource("classpath:application-test.yml")
@ContextConfiguration
class BaseSpecification extends Specification {

    def setup() {
        RestAssured.useRelaxedHTTPSValidation()
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = 9090
        RestAssured.basePath = "/aggregator"
    }
}
