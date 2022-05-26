package com.performance.test

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class DataFeederFromCSV extends Simulation{
  val httpConfiguration: HttpProtocolBuilder = http.baseUrl("https://gorest.co.in/")
    .header("Authorization", value = "Bearerf87aa92b8cd050bd52f1f3a978af1a2f36895ab0dfdcf18d1a5738722364001a")

  val csvFeeder = csv("./src/test/resources/data/getUsers.csv").circular

  val feederScenario = scenario("Example from Automation step by step")
    .feed(csvFeeder)
    .exec {
      session =>
        println("User ID: " +session("userid").as[String])
        println("Name: " +session("name").as[String])
//        .get("/#{urlEndPoint}") ==> If there is a column named as urlEndPoint in the csv, the values are replaced dynamically during the runtime
        session
    }

  def getAUser() = {
    repeat(7) {
      feed(csvFeeder)
        .exec(http("Get single user request")
        .get("public-api/users/${userid}")
          .check(jsonPath("$.data.name").is("${name}"), status.in(200,304)))
        .pause(1)
    }
  }

  def getAnotherUser() = {
    feed(csvFeeder)
      .exec(http("Get another user request")
      .get("public-api/users/${userid}")
      .check(jsonPath("$.data.name").is("${name}"))
      .check(status.in(200,304)))
  }

  val testScenario: ScenarioBuilder = scenario("CSV Feeder test").exec(getAUser().exec(getAnotherUser()))

  setUp(testScenario.inject(atOnceUsers(1))).protocols(httpConfiguration)
}
