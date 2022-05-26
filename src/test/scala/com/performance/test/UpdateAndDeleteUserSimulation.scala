package com.performance.test

import io.gatling.core.Predef.Simulation
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class UpdateAndDeleteUserSimulation extends Simulation {

  val httpConfiguration: HttpProtocolBuilder = http.baseUrl("https://reqres.in")
    .header("Accept", value = "application/json")
    .header("content-type", value = "application/json")

  val testScenario: ScenarioBuilder = scenario("Update user scenario")
    .exec(http("Update specific user")
      .put("/api/users/2")
      .body(RawFileBody("./src/test/resources/bodies/UpdateUser.json")).asJson
        .check(status.in(200 to 210)))

    .pause(4)

    .exec(http("Delete user")
      .delete("/api/users/2")
        .check(status.in(200 to 204)))

  setUp(testScenario.inject(atOnceUsers(10))).protocols(httpConfiguration)

}
