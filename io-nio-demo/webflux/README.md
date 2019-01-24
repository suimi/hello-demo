Gatling 压测场景
1. nothingFor(4 seconds)\
   在指定的时间段(4 seconds)内什么都不干
2. atOnceUsers(10)\
   一次模拟的用户数量(10)。
3. rampUsers(10) over(5 seconds)\
   在指定的时间段(5 seconds)内逐渐增加用户数到指定的数量(10)。
4. constantUsersPerSec(10) during(20 seconds)\
   以固定的速度模拟用户，指定每秒模拟的用户数(10)，指定模拟测试时间长度(20 seconds)。
5. constantUsersPerSec(10) during(20 seconds) randomized\
   以固定的速度模拟用户，指定每秒模拟的用户数(10)，指定模拟时间段(20 seconds)。用户数将在随机被随机模拟（毫秒级别）。
6. rampUsersPerSec(10) to (20) during(20 seconds)\
   在指定的时间(20 seconds)内，使每秒模拟的用户从数量1(10)逐渐增加到数量2(20)，速度匀速。
7. rampUsersPerSec(10) to (20) during(20 seconds) randomized\
   在指定的时间(20 seconds)内，使每秒模拟的用户从数量1(10)增加到数量2(20)，速度随机。
8. splitUsers(10) into(rampUsers(10) over(10 seconds)) separatedBy(10 seconds) \
   反复执行所定义的模拟步骤(rampUsers(100) over(10 seconds))，每次暂停指定的时间(10 seconds)，直到总数达到指定的数量(10)
9. splitUsers(100) into(rampUsers(10) over(10 seconds)) separatedBy(atOnceUsers(30))\
   反复依次执行所定义的模拟步骤1(rampUsers(10) over(10 seconds))和模拟步骤2(atOnceUsers(30))，直到总数达到指定的数量(100)左右
10. heavisideUsers(100) over(10 seconds)\
    在指定的时间(10 seconds)内使用类似单位阶跃函数的方法逐渐增加模拟并发的用户，直到总数达到指定的数量(100).简单说就是每秒并发用户数递增。