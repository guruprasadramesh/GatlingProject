package com.performance.fcpq

import io.gatling.http.Predef._
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.protocol.HttpProtocolBuilder

class LoginTokenGeneration extends Simulation {

  val MAX_RESPONSE_TIME = 20000

  val httpProtocol:HttpProtocolBuilder = http//.proxy(Proxy("localhost", 8888))
    .baseUrl("https://siemens-sifuturecpq.admin.app.tacton.com/")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .upgradeInsecureRequestsHeader("1")
    .basicAuth("APIUserGP","SiemensFcpq123$")

  val selectTicket: ScenarioBuilder = scenario("Select the Ticket for launching configuration")
    .exec(http("Go To Solution")
      .get("/!tickets%7ET-00002582/api/solution/b5f076473094479db2ab767add990640")
        .check(status.is(200), responseTimeInMillis.lte(MAX_RESPONSE_TIME), bodyString.saveAs("SelectSolutionResponse")))
    .exitHereIfFailed
    .exec {
      session =>
        print(session("SelectSolutionResponse").as[String])
        session
    }
   .doIfOrElse(session=>session("SelectSolutionResponse").as[String]
     .contains("b5f076473094479db2ab767add990640")
        ||
      session("SelectSolutionResponse").as[String]
      .contains("m3d8f3756c994ab687726dfc042ad56c")) //Owner name
    {
      exec(http("Select Solution")
      .get("/async/!tickets%7ET-00002582/api/configuredproduct/create?solution=b5f076473094479db2ab767add990640&_=1642074372522")
      .check(status.is(200)))
    }
    {
      exec{session=>
        print("Executing Else block statements for not meeting the If condition..!!")
        session
      }
    }

  val editPricing: ScenarioBuilder = scenario("Open the Edit pricing")
    .exec(http("Edit Pricing from First Solution")
    .post("async/!tickets~T-00002582/api/configuredproduct/89fadfd51dc04871b5802a9cf1b29fa6/startpricing")
      .body(StringBody("_csrf=ba3a73ac1da14563beddcc59b9361958eb8866f3ec63b3f28ce11e1a2b7a28d5"))
    .check(status.is(200)))

  setUp(editPricing.inject(atOnceUsers(1))).protocols(httpProtocol)

}
