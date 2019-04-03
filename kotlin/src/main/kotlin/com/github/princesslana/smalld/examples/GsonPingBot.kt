package com.github.princesslana.smalld.examples

import com.github.princesslana.smalld.json.SmallDGson
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

  fun reply(smalld: SmallDGson, content: String) {
    smalld.post(
      "/channels/${channelId}/messages",
      CreateMessage(content),
      Unit::class.java)
  }
}

data class CreateMessage(val content: String)

class GsonPingBot(val smalld: SmallDGson) {
  val gson = Gson()

  fun onMessageCreate(msg: Message) {
    when (msg.content) {
      "++ping" -> msg.reply(smalld, "pong")
    }
  }

  fun run() {
    smalld.onGatewayPayload(GatewayPayload::class.java) { p ->
      when (p.t) {
        "MESSAGE_CREATE" -> onMessageCreate(gson.fromJson(p.d, Message::class.java))
      }
    }
  }
}

fun main(args: Array<String>) {
  SmallDGson.run(System.getenv("SMALLD_TOKEN")) { s -> GsonPingBot(s).run() }
}
