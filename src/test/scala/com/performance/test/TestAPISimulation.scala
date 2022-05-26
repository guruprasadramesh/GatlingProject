package com.performance.test

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class TestAPISimulation extends Simulation {

  //http configuration
  val httpConfiguration: HttpProtocolBuilder = http.baseUrl("https://reqres.in")
    .header("Accept", value = "application/json")
    .header("content-type", value="application/json")

  //scenario
  val testScenario: ScenarioBuilder = scenario("get user")
    .exec(http("get user request")
    .get("/api/users/2")
    .check(status is 200))

  //setup
  setUp(testScenario.inject(atOnceUsers(789))).protocols(httpConfiguration)
}