package com.performance.test

import io.gatling.core.Predef.Simulation
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._
import scala.language.postfixOps

class RampupUsersViaFiddler extends Simulation {

  val httpConfiguration: HttpProtocolBuilder = http.proxy(Proxy("localhost",8866))
    .baseUrl("https://gorest.co.in/")
    .header("Authorization", value = "Bearerf87aa92b8cd050bd52f1f3a978af1a2f36895ab0dfdcf18d1a5738722364001a")

  val csvFeeder = csv("./src/test/resources/data/getUsers.csv").circular

  def getAUser() = {
    repeat(1) {
      feed(csvFeeder)
        .exec(http("Get single user request")
          .get("public-api/users/${userid}")
          .check(jsonPath("$.data.name").is("${name}"))
          .check(status.in(200,304)))
    }
  }

  val testScenario: ScenarioBuilder = scenario("Ramp Users  Load Simulation").exec(getAUser())

  setUp(
    testScenario.inject(
    nothingFor(5),
    constantUsersPerSec(10) during(10 seconds),
    rampUsersPerSec(1) to (5) during (20 seconds)).protocols(httpConfiguration)
  )
}
