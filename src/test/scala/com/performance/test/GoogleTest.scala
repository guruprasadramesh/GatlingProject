package com.performance.test

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import scala.concurrent.duration._

class GoogleTest extends Simulation{
  val httpConfiguration: HttpProtocolBuilder = http.baseUrl("https://www.google.com")

  val testScenario: ScenarioBuilder = scenario("Home page, Doodles and about Doodles")
    .exec(http("Access the Google home page")
      .get("/"))
    .exec(http("Doodles")
      .get("/doodles/"))
    .exec(http("About Doodles")
      .get("/doodles/about"))

//  setUp(testScenario.inject(atOnceUsers(1),rampUsers(4).during(4.seconds)))
//    .maxDuration(1.minutes).protocols(httpConfiguration)

  setUp(testScenario.inject(constantConcurrentUsers(5).during(60.seconds)))
    .protocols(httpConfiguration)
}