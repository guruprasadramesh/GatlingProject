package com.performance.test

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class AddUserSimulation extends Simulation {

  val httpConfiguration: HttpProtocolBuilder = http.baseUrl("https://reqres.in")
    .header("Accept", value = "application/json")
    .header("content-type", value = "application/json")
    .check(header("date").saveAs("serverDate"))

  val testScenario2: ScenarioBuilder = scenario("Add user scenario: " +"${serverDate}")
    .exec(http("add user request")
      .post("/api/users/")
      .body(RawFileBody("./src/test/resources/bodies/AddUser.json")).asJson
      .header("content-type", value = "application/json")
      .check(status is 201))

  val testScenario: ScenarioBuilder = scenario("Add user scenario "+"${serverDate}")
    .exec(http("add user request")
      .post("/api/users")
      .body(RawFileBody("./src/test/resources/bodies/AddUser.json")).asJson
      .header("content-type", value = "application/json")
      .check(status is 201))

    .pause(3)

    .exec(http("get user request")
      .get("api/users/2")
      .check(status is 200))

    .pause(3)

    .exec(http("get all users request")
      .get("/api/users?page=2")
      .check(status is 200))

  setUp(testScenario2.inject(atOnceUsers(1))).protocols(httpConfiguration)
}
