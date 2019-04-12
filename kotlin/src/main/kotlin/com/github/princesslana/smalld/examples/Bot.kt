package com.github.princesslana.smalld.examples

import com.github.princesslana.smalld.SmallD
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName


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


typealias Bot = (SmallDGson) -> Unit
fun bot(bot: SmallDGson.() -> Unit): Bot = bot

fun Bot.run(token: String) {
  SmallD.run(token) { s -> this(SmallDGson(s)) }
}

class SmallDGson(val smalld: SmallD) {
  val gson = Gson()

  fun onGatewayPayload(f: (GatewayPayload) -> Unit) {
    smalld.onGatewayPayload { s ->
      f(gson.fromJson(s, GatewayPayload::class.java))
    }
  }

  fun post(path: String, payload: Any) {
    smalld.post(path, gson.toJson(payload))
  }
}

