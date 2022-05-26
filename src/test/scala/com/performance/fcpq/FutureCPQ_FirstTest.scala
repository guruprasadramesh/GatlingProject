package com.performance.fcpq

import io.gatling.core.Predef.{Simulation, _}
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class FutureCPQ_FirstTest extends Simulation {

  val httpConfiguration: HttpProtocolBuilder = http.proxy(Proxy("localhost", 8866))
    .baseUrl("https://siemens-sifuturecpq.test.app.tacton.com/")
    .header("accept", "application/json, text/javascript, */*; q=0.01")
    .header("accept-encoding", "gzip, deflate, br")
    .header("content-type", "application/json")
    .header("cookie","logincsrf=3271ee4b44284bbe9076f09bc1730d1fc5235a319426a7b504aa83fd72f32608; CPQ=150DBC831127497388DA7EE767400AFC.17DBE26CA47")

  def getAccounts: ChainBuilder = {
    repeat(1) {
      exec(http("Call Accounts")
      .get("json/account/search/Account-list?1&isInDialog=false&isInDialog=false&order=asc&limit=10&offset=0&f0=&f1=&f2=&f3=&f4=&f5=&queryId=1639572766080")
      .check(status is 200))
    }
  }

  def getOpportunities: ChainBuilder = {
    repeat(1) {
      exec(http("Call Opportunities")
        .get("json/opportunity/search/Opportunity-list?1&isInDialog=false&isInDialog=false&order=asc&limit=10&offset=0&f0=&f1=&f2=&f3=&f4=&f5=&f6=&queryId=1639574648266")
        .check(status is 200))
    }
  }

  def getSolutions: ChainBuilder = {
    repeat(1) {
      exec(http("Call Solutions")
        .get("/json/solution/search/Solution-list?1&isInDialog=false&isInDialog=false&order=asc&limit=10&offset=0&f0=&f1=&f2=&f3=&f4=&f5=&f6=&queryId=1639574805874")
        .check(status is 200))
    }
  }

  def getConfiguredProducts: ChainBuilder = {
    repeat(1) {
      exec(http("Call Configured Products")
        .get("json/configuredproduct/search/ConfiguredProduct-list?1&isInDialog=false&isInDialog=false&order=asc&limit=10&offset=0&f0=&f1=&f2=&f3=&f4=&f6=&f7=&f8=&f10=&f11=&queryId=1639574924530")
        .check(status is 200))
    }
  }

  def getBusinessApprovals: ChainBuilder = {
    repeat(1) {
      exec(http("Call Business Approvals")
        .get("json/businessapproval/search/BusinessApproval-list?1&isInDialog=false&isInDialog=false&order=asc&limit=10&offset=0&f0=&f1=&f2=&f3=&queryId=1639575056758")
        .check(status is 200))
    }
  }

  def getRFQQueries: ChainBuilder = {
    repeat(1) {
      exec(http("Call RFQ Queries")
        .get("json/rfqconfigurationquery/search/RFQConfigurationQuery-list?1&isInDialog=false&isInDialog=false&order=asc&limit=10&offset=0&f0=&f1=&f2=&f3=&f4=&f5=&f6=&queryId=1639575163627")
        .check(status is 200))
    }
  }

  val testScenario: ScenarioBuilder = scenario("Get Accounts, Opportunities, Solutions, Business Approvals & RFQ Queries")
    .exec(getAccounts)
    .exec(getOpportunities)
    .exec(getSolutions)
    .exec(getConfiguredProducts)
    .exec(getBusinessApprovals)
    .exec(getRFQQueries)

  setUp(testScenario.inject(atOnceUsers(1))).protocols(httpConfiguration)

}