package scripts

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import com.github.jeanadrien.gatling.mqtt.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {


  val headers_2 = Map(
    "authorization" -> "Basic YWRtaW46TWluQDE3ODkg",
    "Content-Type" -> "application/json","Accept"->"application/json")

  //Server  path
  val httpConf = http
    .baseURL("http://ims.sfqa.myntra.com")
      .headers(headers_2)

  val httpConfNoWarmUp = httpConf.disableWarmUp


  //Sample silk route create order which is parameterized

 // val json_feeder = csv("silkroutejson.csv").random

 /* val scn = scenario("silkroute").feed(json_feeder)
      .exec(http("createOrder")
      .post("/myntra-silkroute-service/jabong/v1/createOrder")
      .headers(headers_2)
      .body(StringBody("""{"totalAmount": 1800.0,
                         |    "shippingPincode": "${pincode}",
                         |    "processingStartDate": "2018-03-18",
                         |    "processingEndDate": "2018-03-29",
                         |    "storeId" : "5",
                         |    "type": "normal",
                         |    "paymentMethod": "cod",
                         |    "items": [{
                         |        "itemId": "${itemid}",
                         |        "skuId": "${skuid}",
                         |        "quantity": ${quantity},
                         |        "supplyType": "ON_HAND",
                         |        "warehouseId": "20",
                         |        "itemMrp": 1000.0,
                         |        "itemAmountBeforeTax": 1000.0,
                         |        "taxRecovered":5.0,
                         |        "sellerId": 21
                         |    }]
                         |}""")))

  setUp(scn.inject(atOnceUsers(5)).protocols(httpConfNoWarmUp))*/



//script 1

  //******* Enable below line  to have parameterized execution & Also add the test data in  folder data/Get_store_seller_wh_level_whInventory.csv *********
  //To enable parameterization check silkroutecreatorder for reference.

 // val Get_store_seller_wh_level_whInventory_json_feeder = csv("Get_store_seller_wh_level_whInventory.csv").random
   val Get_store_seller_wh_level_whInventory = scenario("Get_store_seller_wh_level_whInventory")//.feed(Get_store_seller_wh_level_whInventory_json_feeder)
     .exec(http("Get_store_seller_wh_level_whInventory")
     .get("/myntra-ims-service/imsV2/inventory/v2/storeSeller/warehouse/")
    .headers(headers_2))



  //script 2


  val Block_WH_Inventory_json_feeder = csv("Block_WH_Inventory.csv").random
  val Block_WH_Inventory = scenario("Block_WH_Inventory").feed(Block_WH_Inventory_json_feeder)
      .exec(http("Block_WH_Inventory")
      .put("/myntra-ims-service/imsV2/inventory/v2/block")
      .headers(headers_2)
      .body(StringBody("""{"storePartnerId":"23","data":[{"warehouseId":"123","sellerPartnerId":"30","skuId":"${skuId}","supplyType":"ON_HAND","quantity":"1"},{"warehouseId":"123","sellerPartnerId":"30","skuId":"${skuId}","supplyType":"ON_HAND","quantity":"2"}]}""")))


// script 3

val Unblock_Inventory_json_feeder = csv("Unblock_Inventory.csv").random
  val Unblock_Inventory = scenario("Unblock_Inventory").feed(Unblock_Inventory_json_feeder)
    .exec(http("Unblock_Inventory")
      .put("/myntra-ims-service/imsV2/inventory/v2/unblock")
      .headers(headers_2)
      .body(StringBody("""{"storePartnerId":"2297","data":[{"warehouseId":"36","sellerPartnerId":"4024","skuId":"${skuId}","supplyType":"ON_HAND","quantity":"1"},{"warehouseId":"36","sellerPartnerId":"4024","skuId":"${skuId}","supplyType":"ON_HAND","quantity":"2"}]}""")))



  // runner script runs all script in parallel which is comma seperated
  setUp(Unblock_Inventory.inject(heavisideUsers(1) over (15 seconds) ).protocols(httpConfNoWarmUp),
        Block_WH_Inventory.inject(heavisideUsers(1) over (15 seconds) ).protocols(httpConfNoWarmUp),
        Get_store_seller_wh_level_whInventory.inject(constantUsersPerSec(1) during (15 seconds) )
      .protocols(httpConfNoWarmUp))



  //For rabbitmq execution script

 /* val rabbitmqscn = scenario("MQTT Test")
    .exec(connect)
    .exec(subscribe("myTopic"))
    .during(20 minutes) {
      pace(1 second).exec(publish("myTopic", "myPayload"))
    }
  setUp(rabbitmqscn.inject(rampUsers(5000) over (10 minutes))).protocols(mqttConf)*/

  /*setUp(
    scn.inject(
      nothingFor(4 seconds), // 1
      atOnceUsers(10), // 2
      rampUsers(10) over (5 seconds), // 3
      constantUsersPerSec(20) during (15 seconds), // 4
      constantUsersPerSec(20) during (15 seconds) randomized, // 5
      rampUsersPerSec(10) to 20 during (10 minutes), // 6
      rampUsersPerSec(10) to 20 during (10 minutes) randomized, // 7
      splitUsers(1000) into (rampUsers(10) over (10 seconds)) separatedBy (10 seconds), // 8
      splitUsers(1000) into (rampUsers(10) over (10 seconds)) separatedBy atOnceUsers(30), // 9
      heavisideUsers(1000) over (20 seconds) // 10
    ).protocols(httpConfNoWarmUp)
  )*/
}
