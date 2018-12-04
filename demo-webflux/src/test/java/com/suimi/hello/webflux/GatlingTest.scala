package com.suimi.hello.webflux

import java.util.concurrent.TimeUnit

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.Duration

/*
 * Copyright (c) 2013-2017, suimi
 */
class GatlingTest extends Simulation {
  // 从系统变量读取 baseUrl、path和模拟的用户数
//  val testPath = System.getProperty("test.path")
//  val sim_users = System.getProperty("sim.users").toInt

  val httpConf = http.baseUrl("http://localhost:8080/")
  val testPath = "list";

  // 定义模拟的请求，重复30次
  val userRequest = repeat(30) {
    // 自定义测试名称
    exec(http("list-user")
      // 执行get请求
      .get("user/list"))
      // 模拟用户思考时间，随机1~2秒钟
      .pause(Duration.create(1,"sec"), 2)
  }

  // 定义模拟的请求，重复30次
  val dateRequest = repeat(30) {
    // 自定义测试名称
    exec(http("date")
      // 执行get请求
      .get("date"))
      // 模拟用户思考时间，随机1~2秒钟
      .pause(Duration.create(1,"sec"), 2)
  }

  // 定义模拟的请求，重复30次
  val timeRequest = repeat(30) {
    // 自定义测试名称
    exec(http("time")
      // 执行get请求
      .get("time"))
      // 模拟用户思考时间，随机1~2秒钟
      .pause(Duration.create(1,"sec"), 2)
  }

  // 定义模拟的场景
  val scn = scenario("user")
    // 该场景执行上边定义的请求
//    .exec(userRequest)
    .exec(dateRequest)
    .exec(timeRequest)

  // 配置并发用户的数量在30秒内均匀提高至sim_users指定的数量
  setUp(scn.inject(rampUsers(10000).during(Duration.apply(30, TimeUnit.SECONDS))).protocols(httpConf))
}
