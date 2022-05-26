package com.performance.fcpq

import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import scala.concurrent.duration._

import scala.math.BigDecimal

class ExternalAppAPIs extends Simulation {

  var testBody = "---------------------This is the body---------------------"
  var responseBody = ""
  val MAX_RESPONSE_TIME = 30000
  val SessionID = "b31dbfe5a257349c0b4369ac8b704528920d547eacef12f1m3d8f3756c994ab687726dfc042ad56c044b704d914d4fa7a4203acdce101e360000017e9a82cb4901f00bdc46ea18548b09cab33d9e922d"

  val httpProtocol:HttpProtocolBuilder = http//.proxy(Proxy("localhost", 8888))
    .baseUrl("https://integration.sifuturecpq.de/transformer")

  def testExternalAppWithReadAndWriteAPIs(): ChainBuilder ={
    repeat(1) {
      exec(http("Calling the read API")
        .get("/tacton/!tickets~T-00002481/api/external-step-renderer/read/"+SessionID)
        .check(status.is(200), bodyLength.saveAs("responseLength"),
          responseTimeInMillis.lte(MAX_RESPONSE_TIME), bodyString.saveAs("CTVTResponse"),
          jsonPath("$.parameters..committed").find(10).saveAs("committedValue"))).pause(1.seconds)

        .exec {
          session =>
            responseBody = session("CTVTResponse").as[String]
            //        println(session("CTVTResponse").as[String])
            println("-------------------------Committed Value session is below-------------------------")
            try {
            println(session("committedValue").as[String])
            } catch {
              case e: NoSuchElementException => println("============Committed value not available as the response was not right============")
            }
            println("-----------------------------Response length is here-----------------------------")
            val responseLen:Double = session("responseLength").as[Int]
            println("Response size: "+BigDecimal(responseLen/(1024*1024)).setScale(2, BigDecimal.RoundingMode.HALF_UP) +" MB")
            session
        }
    }
  }


  val launchExternalAppReadWrite: ScenarioBuilder = scenario("Call External App Read & Write APIs")
    .exec(http("Calling the read API")
      .get("/tacton/!tickets~T-00002481/api/external-step-renderer/read/"+SessionID)
      .check(status.is(200), bodyLength.saveAs("responseLength"),
        responseTimeInMillis.lte(MAX_RESPONSE_TIME), bodyString.saveAs("CTVTResponse"),
        jsonPath("$.parameters..committed").find(10).saveAs("committedValue")))


   .exec {
      session =>
//        session.set("Read API", title)
          responseBody = session("CTVTResponse").as[String]
//        println(session("CTVTResponse").as[String])
        println("-----------------------------Response length is here-----------------------------")
        println(session("committedValue").as[String])
        println("-------------------------Committed Value session is above-------------------------")
        val responseLen:Double = session("responseLength").as[Int]
        println("Response size: "+BigDecimal(responseLen/(1024*1024)).setScale(2, BigDecimal.RoundingMode.HALF_UP) +" MB")
        session
    }.exitHereIfFailed

    .exec(http("Calling the Write API")
    .post("/tacton/!tickets~T-00002481/api/external-step-renderer/write/"+SessionID)
    .body(RawFileBody("./src/test/resources/bodies/ExternalAppCTVT.json")).asJson
//      .body(StringBody(s"$testBody"))
//      .body(StringBody("---------------------This is the body---------------------"))//.asJson
//      .check(bodyString.saveAs("writeResponse"))

    /*.exec(session=> {
      println("---------------------Write Response---------------------")
      println(session("writeResponse").as[String])
      session
    }*/

  )
//  setUp(launchExternalAppReadWrite.inject(atOnceUsers(1))).protocols(httpProtocol)

  val iteratingScenario: ScenarioBuilder = scenario("Iterate multiple times").exec(testExternalAppWithReadAndWriteAPIs())

  setUp(iteratingScenario.inject(atOnceUsers(1))).protocols(httpProtocol)

}
