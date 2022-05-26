package com.performance.test

import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class APITestExample extends Simulation{

  val username = "awsuser"
  val password = "PJ6bKbCHRqdsnR1KYVDG"

  val httpConfig1: HttpProtocolBuilder = http.baseUrl("https://jsonplaceholder.typicode.com").proxy(Proxy("localhost", 8888))
    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")

  val httpConfig: HttpProtocolBuilder = http.baseUrl("http://futurecpq.siemens.com")//.proxy(Proxy("localhost", 8888))
    .basicAuth(username, password)

  val integrationTest: ScenarioBuilder = scenario("user scenario")
    .exec(http("Email API")
      .get("/api/v1/scd-service/searchbyemail?email=manoj.mahabaleshwar@siemens.com").check(status.is(200)))
    .exec(session => {
      println(session)
      session
    })

  val todoScenario: ScenarioBuilder = scenario("user scenario")
    .exec(http("calling to do")
      .get("/posts").check(status.is(200))
      .check(jsonPath("$..id").findAll.saveAs("varID")))
    .exec(session => {
      println(session)
      session
    })

  setUp(integrationTest.inject(atOnceUsers(1))).protocols(httpConfig)

}
