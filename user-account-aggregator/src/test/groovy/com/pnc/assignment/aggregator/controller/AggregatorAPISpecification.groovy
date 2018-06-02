package com.pnc.assignment.aggregator.controller

import com.pnc.assignment.aggregator.model.user.Address
import com.pnc.assignment.aggregator.model.user.User
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import io.restassured.http.ContentType
import io.restassured.response.Response
import spock.lang.Unroll

import static io.restassured.RestAssured.given
import static org.hamcrest.CoreMatchers.containsString
import static org.hamcrest.CoreMatchers.equalTo

class AggregatorAPISpecification extends BaseSpecification {

    def "GET | +ve | get all users should return 200 with status as 'SUCCESS'"() {

        when:
        Response response = given().get("/users")

        then:
        response.statusCode == 200
        def responseMap = new JsonSlurper().parseText(response.then().body().contentType(ContentType.JSON).extract().response().asString())
        println responseMap
        responseMap.status == "SUCCESS"
        responseMap.data.size >= 0
    }

    @Unroll
    def "GET | +ve | userId = #userId is valid user"() {

        when:
        Response response = given().get("/users/${userId}")

        then:
        response.statusCode == 200
        def responseMap = new JsonSlurper().parseText(response.then().body().contentType(ContentType.JSON).extract().response().asString())
        responseMap.status == "SUCCESS"
        responseMap.data.id == userId

        where:
        userId  | _
        1       | _
        3       | _
    }

    @Unroll
    def "GET | -ve | userId = #userId doesn't exist"() {

        when:
        Response response = given().get("/users/${userId}")

        then:
        response.statusCode == 404
        def responseMap = new JsonSlurper().parseText(response.then().body().contentType(ContentType.JSON).extract().response().asString())
        responseMap.status == "FAILURE"
        responseMap.data.errorCode == 404
        responseMap.data.errorData != null
        responseMap.data.errorData.errorType == "UserNotFoundException"

                where:
        userId  | _
        new Random().nextLong() | _
        -4 | _
    }

    @Unroll
    def "GET | -ve | userId = #userId exist but without account"() {

        when:
        Response response = given().get("/users/${userId}")

        then:
        response.statusCode == 404
        def responseMap = new JsonSlurper().parseText(response.then().body().contentType(ContentType.JSON).extract().response().asString())
        responseMap.status == "FAILURE"
        responseMap.data.errorCode == 404
        responseMap.data.errorData != null
        responseMap.data.errorData.errorType == "AccountNotFoundException"

        where:
        userId  | _
        10 | _
    }

    @Unroll
    def "GET | -ve | userId = #userId out of long range"() {

        when:
        Response response = given().get("/users/${userId}")

        then:
        response.statusCode == 400
        def responseMap = new JsonSlurper().parseText(response.then().body().contentType(ContentType.JSON).extract().response().asString())
        responseMap.status == "FAILURE"

        where:
        userId  | _
        4655698546546546546546541265 | _
    }

    @Unroll
    def "GET | +ve | #userLastName is a valid user"() {

        when:
        Response response = given().get("/users/lastname/${userLastName}")

        then:
        response.statusCode == 200
        def responseMap = new JsonSlurper().parseText(response.then().body().contentType(ContentType.JSON).extract().response().asString())
        responseMap.status == "SUCCESS"
        responseMap.data[0].lastName == userLastName

        where:
        userLastName  | _
        "Haque" | _
        "Bond" | _
    }

    @Unroll
    def "GET | -ve | user with last name as #userLastName doesn't exist"() {

        when:
        Response response = given().get("/users/lastname/${userLastName}")

        then:
        response.statusCode == 200
        def responseMap = new JsonSlurper().parseText(response.then().body().contentType(ContentType.JSON).extract().response().asString())
        responseMap.status == "SUCCESS"
        responseMap.data.size == 0

        where:
        userLastName  | _
        "unknownLN" | _
    }

    @Unroll
    //@IgnoreRest
    def "PUT | +ve | updating user for user id = #userId"() {

        when:
        Response response = given().body(user).when().contentType(ContentType.JSON).put("/users/${userId}");

        then:
        response.statusCode == 200
        def responseMap = new JsonSlurper().parseText(response.then().body().contentType(ContentType.JSON).extract().response().asString())
        responseMap.status == "SUCCESS"
        responseMap.data == "User with id [1] updated successfully."

        when:
        response = given().get("/users/${userId}")

        then:
        response.statusCode == 200
        def responseMap1 = new JsonSlurper().parseText(response.then().body().contentType(ContentType.JSON).extract().response().asString())
        responseMap1.data.id == userId
        responseMap1.data.firstName == user.firstName

        where:
        userId  | user
        1       | new User('M Zeyaul', 'HAQUE', new Address(addressOne: 'Invalid Address One', addressTwo: 'Apt 000', city: 'Pittsburgh', state: 'PA', zip: '15106', user: null))
    }

    @Unroll
    def "DELETE | +ve | Delete user for userId = #userId"() {

        when:
        Response response = given().delete("/users/${userId}")

        then:
        response.statusCode == 200
        def responseMap = new JsonSlurper().parseText(response.then().contentType(ContentType.JSON).extract().response().asString())
        responseMap.status == "SUCCESS"

        when:
        response = given().get("/users/${userId}")

        then:
        response.statusCode == 404

        where:
        userId  |   _
        7       | _
    }

    @Unroll
    def "DELETE | -ve | Delete user for userId = #userId not exist"() {

        when:
        Response response = given().delete("/users/${userId}")

        then:
        response.statusCode == 404
        def responseMap = new JsonSlurper().parseText(response.then().contentType(ContentType.JSON).extract().response().asString())
        responseMap.status == "FAILURE"
        responseMap.data.errorCode == 404
        responseMap.data.errorData != null
        responseMap.data.errorData.errorType == "UserNotFoundException"

        where:
        userId  |   _
        -7       | _
    }

    @Unroll
    def "DELETE | +ve | close user account for userId = #userId and accountId = #accountId"() {

        when:
        Response response = given().delete("/users/${userId}/accounts/${accountId}")

        then:
        response.statusCode == 200
        def responseMap = new JsonSlurper().parseText(response.then().contentType(ContentType.JSON).extract().response().asString())
        responseMap.status == "SUCCESS"

        when:
        response = given().get("/accounts/${accountId}")

        then:
        def responseMap1 = new JsonSlurper().parseText(response.then().contentType(ContentType.JSON).extract().response().asString())
        responseMap1.data.status.toUpperCase() == "CLOSED".toUpperCase()

        where:
        userId  |   accountId
        6       |   11
    }

    @Unroll
    def "GET | +ve | get balance for userId = #userId and accountId = #accountId"() {

        when:
        Response response = given().get("/users/${userId}/accounts/${accountId}/balance")

        then:
        response.statusCode == 200
        def responseMap = new JsonSlurper().parseText(response.then().contentType(ContentType.JSON).extract().response().asString())
        responseMap.status == "SUCCESS"
        responseMap.data >= 0

        where:
        userId  |   accountId
        5       |   7
    }

    @Unroll
    def "PUT | +ve | withdraw #amount dollar from accountId = #accountId"() {

        when:

        //def jsonBody = JsonOutput.toJson([withdrawAmount: amount])
        def jsonBuilder = new JsonBuilder()
        def jsonBody = jsonBuilder withdrawAmount: amount

        //Response response = given().contentType(ContentType.JSON).body("{\"withdrawAmount\":\"$amount\"}").put("/users/${userId}/accounts/${accountId}/withdraw")
        Response response = given().contentType(ContentType.JSON).body(jsonBody).put("/users/${userId}/accounts/${accountId}/withdraw")

        then:
        response.statusCode == 200
        def responseMap = new JsonSlurper().parseText(response.then().contentType(ContentType.JSON).extract().response().asString())
        responseMap.status == "SUCCESS"
        Double.valueOf(responseMap.data[0][1]) >= 0

        where:
        amount  |   userId  |   accountId
        10      |   5       |   7
    }

    @Unroll
    def "PUT | -ve | withdrawing Invalid amount (#amount) should throw exception"() {

        when:

        //def jsonBody = JsonOutput.toJson([withdrawAmount: amount])
        def jsonBuilder = new JsonBuilder()
        def jsonBody = jsonBuilder withdrawAmount: amount

        //Response response = given().contentType(ContentType.JSON).body("{\"withdrawAmount\":\"$amount\"}").put("/users/${userId}/accounts/${accountId}/withdraw")
        Response response = given().contentType(ContentType.JSON).body(jsonBody).put("/users/${userId}/accounts/${accountId}/withdraw")

        then:
        response.statusCode == 400
        def responseMap = new JsonSlurper().parseText(response.then().contentType(ContentType.JSON).extract().response().asString())
        responseMap.status == "FAILURE"
        responseMap.data.errorCode == 400
        responseMap.data.errorData != null
        responseMap.data.errorData.errorType == "InvalidAmountWithdrawException"
        response.then().body("data.errorData.message", containsString(expectedErrorMessage) , "data.errorData.error", equalTo(expectedError))

        where:
        amount   |   userId  |   accountId  | expectedError     | expectedErrorMessage
        -5       |   5       |   7          | "Bad Request"     | "Withdraw amount can't be negative"
        99999999 |   5       |   7          |"Bad Request"      | "Insufficient Balance"
    }


}
