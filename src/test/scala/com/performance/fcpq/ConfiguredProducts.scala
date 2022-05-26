package com.performance.fcpq

import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._
import scala.math.BigDecimal

class ConfiguredProducts extends Simulation {

  var testBody = "---------------------This is the body---------------------"
  var responseBody = ""
  val MAX_RESPONSE_TIME = 90000
  val offset = 10
  val limit = 100
  val csvFeeder = csv("./src/test/resources/data/ConfiguredProducts_IDs.csv").circular


  //  For the test environment
  val httpTestProtocol:HttpProtocolBuilder = http//.proxy(Proxy("localhost", 8888))
    .baseUrl("https://siemens-sifuturecpq.test.app.tacton.com")
    .basicAuth("APIUser", "SiemensFcpq123$")
    .header("accept", "application/json, text/javascript, */*;")
    .header("content-type", "application/json")
    .header("accept-encoding", "gzip, deflate, br")

  val httpAdminProtocol:HttpProtocolBuilder = http//.proxy(Proxy("localhost", 8888))
    .baseUrl("https://siemens-sifuturecpq.admin.app.tacton.com")
    .basicAuth("APIUser", "SiemensFcpq123$")
    .header("accept", "application/json, text/javascript, */*;")
    .header("content-type", "application/json")
    .header("accept-encoding", "gzip, deflate, br")

  def listOfConfiguredProducts(): ChainBuilder ={
    repeat(1) {
      exec(http("Calling the Configured products API")
//        .get("/api/configuredproduct/list?limit=100")
        .get("/api/configuredproduct/list?1&isInDialog=false&order=asc&limit="+limit+"&offset="+offset+"&f1=&f2=&f3=&f4=&f5=&f6=&f7=&f8=&f9=")
        .check(status.is(200), bodyLength.saveAs("responseLength"),
          responseTimeInMillis.lte(MAX_RESPONSE_TIME),
          bodyString.saveAs("configuredProductsList"))).pause(0.seconds)

        .exec {
          session =>
            responseBody = session("configuredProductsList").as[String]
            println(responseBody)
            println("-----------------------------Response length is here-----------------------------")
            val responseLen:Double = session("responseLength").as[Int]
            println("Response size: "+BigDecimal(responseLen/(1024*1024)).setScale(2, BigDecimal.RoundingMode.HALF_UP) +" MB")
            session
        }
    }
  }

  def callMihaelaAdminConfiguredLine(): ChainBuilder= {
    repeat(400) {
      exec(http("Call Configured Product")
//        .get("/!tickets~T-00002438/api/configuredproduct/89c2409ece6c415d9998b699343a7b07")
        .get("/!tickets~T-00002582/api/configuredproduct/e4f027b2f3604b23a5c64921baebe3f7")
        .check(status.is(200), bodyString.saveAs("configuredResponseData"),
          responseTimeInMillis.lte(MAX_RESPONSE_TIME), bodyLength.saveAs("responseLength"))
      ).pause(1.minute)
       .exec {
         session =>
           responseBody = session("configuredResponseData").as[String]
           println(responseBody)
           println("-----------------------------Response length is here-----------------------------")
           val responseLen:Double = session("responseLength").as[Int]
           println("Response size: "+BigDecimal(responseLen/(1024*1024)).setScale(2, BigDecimal.RoundingMode.HALF_UP) +" MB")
           session
       }
    }
  }

  def callMihaelaAdminConfiguredLineViaCSVFeeder(): ChainBuilder= {
    repeat(40) {
      feed(csvFeeder)
      .exec(http("Call Configured Product from CSV list")
        //        .get("/!tickets~T-00002438/api/configuredproduct/89c2409ece6c415d9998b699343a7b07")
        .get("/!tickets~T-00002582/api/configuredproduct/${ConfiguredProductID}")
        .check(status.is(200), bodyString.saveAs("configuredResponseData"),
          responseTimeInMillis.lte(MAX_RESPONSE_TIME), bodyLength.saveAs("responseLength"))
      )//.pause(30.seconds)
        .exec {
          session =>
//            responseBody = session("configuredResponseData").as[String]
//            println(responseBody)
            println("-----------------------------Response length is here-----------------------------")
            val responseLen:Double = session("responseLength").as[Int]
            println("Response size: "+BigDecimal(responseLen/(1024*1024)).setScale(2, BigDecimal.RoundingMode.HALF_UP) +" MB")
            session
        }
    }
  }

//  setUp(launchExternalAppReadWrite.inject(atOnceUsers(1))).protocols(httpProtocol)

//  val iteratingScenario: ScenarioBuilder = scenario("Iterate multiple times").exec(listOfConfiguredProducts())
//  val iteratingScenario: ScenarioBuilder = scenario("Iterate multiple times").exec(callMihaelaAdminConfiguredLine())
  val iteratingScenario: ScenarioBuilder = scenario("Iterate multiple times").exec(callMihaelaAdminConfiguredLineViaCSVFeeder())

//  setUp(iteratingScenario.inject(atOnceUsers(10))).protocols(httpAdminProtocol)
//  setUp(iteratingScenario.inject(atOnceUsers(1))).protocols(httpTestProtocol)
  setUp(iteratingScenario.inject(atOnceUsers(10), nothingFor(1.minute))).protocols(httpAdminProtocol)

}
