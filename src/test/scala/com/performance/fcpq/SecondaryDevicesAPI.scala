package com.performance.fcpq

import io.gatling.http.Predef._
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._

class SecondaryDevicesAPI extends Simulation {
  val httpConfig: HttpProtocolBuilder = http.baseUrl("https://integration.sifuturecpq.de").proxy(Proxy("localhost", 8888))
    .header("accept", "application/sparql-results+json")
    .header("content-type", "application/x-www-form-urlencoded")

  val directSearchAPI: ScenarioBuilder = scenario("Calling APIs")
    .exec(http("Direct Search API")
      .post("/data-transformation-service/getdevices")
//      .body(RawFileBody("./src/test/resources/data/SecondaryDevicesAPIJsonData.json")).asJson
//      .body(StringBody("""{"orderNumber": "PHO:2868635"}""")).asJson
      .body(StringBody(
        """
          |{
          |  "longText": "circuit breaker",
          |  "shortText": "mcb"
          |}
          |""".stripMargin)).asJson
      .check(status.is(200), jsonPath("$.response_data.(0).deviceType") is "MCB")
//      .check(regex([^\*\.\[\]\(\)=!<>\s]))
    )

  setUp(directSearchAPI.inject(atOnceUsers(1))).protocols(httpConfig)
//  setUp((directSearchAPI.inject(constantConcurrentUsers(1).during(60.seconds))))

}
/*
----------------Request body--------------
{
  "longText": "circuit breaker",
  "shortText": "mcb"
}

{
    "orderNumber" :"PHO:2868635"
}

 */


