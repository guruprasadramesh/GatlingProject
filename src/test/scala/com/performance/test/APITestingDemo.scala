package com.performance.test

import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.protocol.HttpProtocolBuilder

class APITestingDemo extends Simulation{

  val httpConfig:HttpProtocolBuilder = http.baseUrl("https://jsonplaceholder.typicode.com")//.proxy(Proxy("localhost", 8888))

  val todoScenario: ScenarioBuilder = scenario("Add to cart")
    .exec(http("calling ToDo").get("/posts").check(status.is(200)).check(jsonPath("$..id").find(5).saveAs("varID")))
    .exec(session => {
      println(session)
      session
    })

  setUp(todoScenario.inject(atOnceUsers(10))).protocols(httpConfig)
}
