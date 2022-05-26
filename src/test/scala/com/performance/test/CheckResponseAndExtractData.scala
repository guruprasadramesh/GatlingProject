package com.performance.test

import io.gatling.core.Predef.Simulation
import io.gatling.http.Predef._
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.protocol.HttpProtocolBuilder

class CheckResponseAndExtractData extends Simulation {

  val httpConfiguration: HttpProtocolBuilder = http.baseUrl("https://gorest.co.in/")
    .header("Authorization", value = "Bearerf87aa92b8cd050bd52f1f3a978af1a2f36895ab0dfdcf18d1a5738722364001a")

  val testScenario: ScenarioBuilder = scenario("Check correlation and extract data")
    .exec(http("Get all users")
      .get("public-api/users")
      .check(jsonPath("$.data[1].id").saveAs("userID")))

  .pause(2)

    .exec(http("Get specific user")
      .get("public-api/users/${userID}")
      .check(jsonPath("$.data.id").is("13"))
      .check(jsonPath("$.data.name").is("Chandraayan Agarwal"))
      .check(jsonPath("$.data.gender").is("male")))

  setUp(testScenario.inject(atOnceUsers(10))).protocols(httpConfiguration)
}
