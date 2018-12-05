package com.suimi.hello

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RecordedSimulation extends Simulation {

	val httpProtocol = http
		.baseUrl("http://localhost:9000")
		.inferHtmlResources()
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7,und;q=0.6")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36")

	val headers_0 = Map("Proxy-Connection" -> "keep-alive")



	val scn = scenario("RecordedSimulation")
		.exec(http("request_0")
			.get("/date")
			.headers(headers_0))
		.pause(3)
		.exec(http("request_1")
			.get("/time")
			.headers(headers_0))

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}