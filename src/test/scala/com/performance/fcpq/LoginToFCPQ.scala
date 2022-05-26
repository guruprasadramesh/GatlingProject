package com.performance.fcpq

import io.gatling.core.Predef.{scenario, _}
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._

class LoginToFCPQ extends Simulation {

  val CPQToken = ""

  val httpConfiguration:HttpProtocolBuilder = http.proxy(Proxy("localhost", 8888))
    .baseUrl("https://siemens-sifuturecpq.admin.app.tacton.com")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .upgradeInsecureRequestsHeader("1")
    .basicAuth("APIUserGP", "SiemensFcpq123$")

  def callFCPQApplication(): ChainBuilder= {
    repeat(1) {
      exec(http("Home page of FCPQ")
        .get("/home")
        .check(headerRegex("set-cookie", """logincsrf=(.*);""")
          .saveAs("csrfToken"))
        .check(status is 200))
    }
//    println("${csrfToken}")
  }

  def loginToApplication(): ChainBuilder= {
    repeat(1) {
      exec(http("Login with user credentials")
      .post("/do-form-login")
        .formParam("path", "/home")
        .formParam("_csrf", "${csrfToken}")
        .formParam("username", "guruprasad.r@siemens.com")
        .formParam("password", "TactonGP81**ad")
        .check(headerRegex("set-cookie", """CPQ=(.*);""").saveAs("token")))
    }
  }

  def callMyConfiguredLine(): ChainBuilder= {
    repeat(1){
      exec(http("Login with my API Credentials")
//      .get("/!tickets~T-00002582/solution/b5f076473094479db2ab767add990640")
      .get("/!tickets~T-00002582/api/solution/b5f076473094479db2ab767add990640"))
//      .header("cookie","CPQ=152A54F5374C4674968F4E62AA8D3CC6.17E674E7E0B; hasUnreadChanges=false"))
  }}

  def callMyConfiguredLineGetResponseBody(): ChainBuilder= {
    repeat(1){
      exec(http("Login with API Credentials")
        .get("/!tickets~T-00002582/solution/b5f076473094479db2ab767add990640")
//        .get("/!tickets~T-00002582/api/configuredproduct/b5f076473094479db2ab767add990640")
        //      .header("cookie","CPQ=152A54F5374C4674968F4E62AA8D3CC6.17E674E7E0B; hasUnreadChanges=false"))


        /*        .check(bodyString.saveAs("BODY"))
        .exec(session => {
            val response = session("BODY").as[String]
            println(s"Response body: \n$response")
        }*/
      )
    }}

  def callMihaelaConfiguredLine(): ChainBuilder= {
    repeat(1) {
    exec(http("Login with my Mihaela Credentials")
      .get("/!tickets~T-00002438/api/configuredproduct/89c2409ece6c415d9998b699343a7b07")
//      .header("cookie","hasUnreadChanges=false; CPQ=1C07AF5355634C49AA1A425C246CC5FB.17E67097E13"))
    )
  }}

  val scenarioLoginPage: ScenarioBuilder = scenario("Login page calling")
    .exec(callFCPQApplication())
    .pause(2.seconds)
    .exec(loginToApplication())
    .pause(2.seconds)

  val configuredLine: ScenarioBuilder = scenario("Configuration given by Mihaela")
//    .exec(callMyConfiguredLineGetResponseBody())
//    .exec(callMihaelaConfiguredLine())
    .exec(callMyConfiguredLine())

//setUp(configuredLine.inject(rampUsers(10).during(2.minutes))).maxDuration(10.minutes)
setUp(configuredLine.inject(atOnceUsers(1))).protocols(httpConfiguration)
//    setUp(scenarioLoginPage.inject(atOnceUsers(1))).protocols(httpConfiguration)
  //  setUp(homePage.inject(atOnceUsers(1))).protocols(httpConfiguration)
  //  setUp(homePage.inject(atOnceUsers(1)), variableTest.inject(atOnceUsers(1)))
  //    .protocols(httpConfiguration)

}
