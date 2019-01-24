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

  //循环测试
  val dateRequest = repeat(30) {
    exec(http("date")
      .get("date"))
      //间隔停留
      .pause(Duration.create(1, "sec"), 2)
  }

  val timeRequest = repeat(30) {
    exec(http("time")
      .get("time"))
      .pause(Duration.create(1, "sec"), 2)
  }

  val scn = scenario("user")
    .exec(dateRequest)
    .exec(timeRequest)

  //压测场景
  //30秒启动5000个用户
  setUp(scn.inject(rampUsers(5000).during(Duration.apply(30, TimeUnit.SECONDS))).protocols(httpConf))
  //30秒内从每秒50个用户增长到每秒1000个用户
  //  setUp(scn.inject(rampUsersPerSec(50).to(1000).during(Duration.apply(30, TimeUnit.SECONDS))).protocols(httpConf))
  //一次并发每秒300用户
  //  setUp(scn.inject(atOnceUsers(300)).protocols(httpConf))
}
