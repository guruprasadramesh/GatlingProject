package com.performance.test

import io.gatling.core.Predef.Simulation
import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._
import scala.language.postfixOps

class FixedDurationLoadSimulation extends Simulation {

  val httpConfiguration: HttpProtocolBuilder = http.proxy(Proxy("localhost", 8866))
    .baseUrl("https://gorest.co.in/")
    .header("Authorization", value = "Bearerf87aa92b8cd050bd52f1f3a978af1a2f36895ab0dfdcf18d1a5738722364001a")

  def getAllUsersRequest(): ChainBuilder= {
    repeat(2){
      exec(http("get all users request")
        .get("public-api/users/5")
        .check(status is 200))
    }
  }
  def getAUserRequest(): ChainBuilder= {
    repeat(2) {
      exec(http("get user1 request")
        .get("api/users/2")
        .check(status is 200))
    }
  }
  def addUser(): ChainBuilder= {
    repeat(2) {
      exec(http("Get comments")
        .get("/public/v1/comments")
        .check(status.in(200 to 201)))
    }
  }
  val testScenario: ScenarioBuilder = scenario("user request scenario")
    .forever() {
      exec(getAllUsersRequest())
        .pause(5)
        .exec(getAUserRequest())
        .pause(5)
        .exec(addUser())
    }
  setUp(
    testScenario.inject(
      nothingFor(5 seconds),
      atOnceUsers(10),
      rampUsers(50) during(30 seconds)
  ).protocols(httpConfiguration)
  ).maxDuration(1 minute)

}
