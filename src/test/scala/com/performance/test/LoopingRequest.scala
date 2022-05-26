package com.performance.test

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class LoopingRequest extends Simulation {

  val httpConfiguration: HttpProtocolBuilder = http.baseUrl("https://gorest.co.in/")
    .header("Authorization", value = "Bearerf87aa92b8cd050bd52f1f3a978af1a2f36895ab0dfdcf18d1a5738722364001a")

  def getAllUsersRequest(): ChainBuilder= {
    repeat(2){
      exec(http("get all users request")
        .get("api/users?page=2")
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
      exec(http("add a user request")
        .post("api/users")
        .body(RawFileBody("./src/test/resources/bodies/AddUser.json")).asJson
        .check(status.in(200 to 201)))
    }
  }
    val testScenario: ScenarioBuilder = scenario("user request scenario")
      .exec(getAllUsersRequest())
      .pause(2)
      .exec(getAUserRequest())
      .pause(3)
      .exec(addUser())

    setUp(testScenario.inject(atOnceUsers(1))).protocols(httpConfiguration)

}
