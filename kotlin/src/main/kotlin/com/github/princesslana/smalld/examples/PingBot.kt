package com.github.princesslana.smalld.examples

import kotlin.reflect.KClass
import com.github.princesslana.smalld.SmallD
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

val gson = Gson()

data class GatewayPayload (
  val op: Long,
  val d: JsonElement,
  val s: Long?,
  val t: String?
)

data class Message (
  @SerializedName("channel_id") val channelId: String,
  val content: String) {
}

data class CreateMessage(val content: String)

class SmallDGson(val smalld: SmallD) {
  fun onGatewayPayload(f: (GatewayPayload) -> Unit) {
    smalld.onGatewayPayload { s ->
      f(gson.fromJson(s, GatewayPayload::class.java))
    }
  }

  fun post(path: String, payload: Any) {
    smalld.post(path, gson.toJson(payload))
  }
}


fun runBot(token: String, init: SmallDGson.() -> Unit) {
  SmallD.run(token) { s ->
    init(SmallDGson(s))
  }
}

fun main(args: Array<String>) {
  runBot(System.getenv("SMALLD_TOKEN")) {
    fun onMessageCreate(msg: Message) {
      when (msg.content) {
        "++ping" -> post("/channels/${msg.channelId}/messages", CreateMessage("pong"))
      }
    }

    onGatewayPayload { p ->
      when (p.t) {
        "MESSAGE_CREATE" -> onMessageCreate(gson.fromJson(p.d, Message::class.java))
      }
    }
  }
}
