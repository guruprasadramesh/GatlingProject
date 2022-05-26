package com.performance.fcpq

import io.gatling.http.Predef._
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.protocol.HttpProtocolBuilder

class SCDIntegration extends Simulation{

  val httpConfig: HttpProtocolBuilder = http.baseUrl("http://futurecpq.siemens.com")//.proxy(Proxy("localhost", 8888))
    .basicAuth("awsuser", "PJ6bKbCHRqdsnR1KYVDG")

  val interfaceTest: ScenarioBuilder = scenario("Test the SCD Interface")
    .exec(http("Email API")
      .get("/api/v1/scd-service/searchbyemail?email=manoj.mahabaleshwar@siemens.com")
      .check(status.is(200)))
    .exec(session => {
      println(session)
      session
    })

    .exec(http("GID API")
      .get("/api/v1/scd-service/searchbygid?gid=z003hn2f")
      .check(status.is(200)))
    .exec(session => {
      println(session)
      session
    })

  setUp(interfaceTest.inject(atOnceUsers(1))).protocols(httpConfig)

}
