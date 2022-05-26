package com.performance.fcpq

import io.gatling.core.Predef.{scenario, _}
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._

class FCPQAPIs extends Simulation{

//  val BASE_URL = "https://integration.sifuturecpq.de"
  val BASE_URL = "https://alb.sifuturecpq.de"
//  val JSON_Data: String = "{"Input": {"OpportunityId":"0063V000002NFymQA"}}/"
//  val jsonString = "{""""Input"""":{""""OpportunityId"""": """"0063V000002NFymQA""""}}"

//  println(""""Guru"""")

  val httpConfiguration: HttpProtocolBuilder = http.baseUrl(BASE_URL)

  val getOppID: ScenarioBuilder = scenario("GET Opportunity ID")
    .exec(http("Get Opportunity ID")
      .get("/api/v1/fcpq/siesales/getOpportunityId?customer=&countryOfInstallation=&opportunityName=&salesCountry=pak&endCustomer=&opportunityId="))

  val OBMDataPost: ScenarioBuilder = scenario("POST OBM Data")
    .exec(http("OBM Data")
      .post("/api/v1/fcpq/siesales/obmdata")
      .body(RawFileBody("./src/test/resources/bodies/OBMdataJson.json"))).pause(1.seconds)

  val PostEvent: ScenarioBuilder = scenario("Post Event")
    .exec(http("Solution Event Controller")
    .post("/api/v1/solution-event-service/event")
    .body(RawFileBody("./src/test/resources/bodies/EventController.xml")))

//  setUp(getOppID.inject(atOnceUsers(1))).protocols(httpConfiguration)
//  setUp(OBMDataPost.inject(atOnceUsers(1))).protocols(httpConfiguration)
//  setUp(PostEvent.inject(atOnceUsers(1))).protocols(httpConfiguration)

  setUp(getOppID.inject(constantConcurrentUsers(5).during(60.seconds)),
    OBMDataPost.inject(constantConcurrentUsers(5).during(60.seconds)),
    PostEvent.inject(constantConcurrentUsers(5).during(60.seconds))).protocols(httpConfiguration)
}
