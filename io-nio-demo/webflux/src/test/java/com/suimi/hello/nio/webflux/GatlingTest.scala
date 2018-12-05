package com.suimi.hello.nio.webflux

import java.util.concurrent.TimeUnit

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._

import scala.concurrent.duration.Duration

/*
 * Copyright (c) 2013-2017, suimi
 */
class GatlingTest extends Simulation {

  val httpConf = http.baseUrl("http://10.200.172.225:9000/")

  val dateRequest = repeat(30) {
    exec(http("date")
      .get("date"))
      .pause(Duration.create(1,"sec"), 2)
  }

  val timeRequest = repeat(30) {
    exec(http("time")
      .get("time"))
      .pause(Duration.create(1,"sec"), 2)
  }

  val scn = scenario("user")
    .exec(dateRequest)
    .exec(timeRequest)

  setUp(scn.inject(rampUsers(5000).during(Duration.apply(30, TimeUnit.SECONDS))).protocols(httpConf))
}
