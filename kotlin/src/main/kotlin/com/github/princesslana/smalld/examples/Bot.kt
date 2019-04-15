package com.github.princesslana.smalld.examples

import java.net.URL
import com.github.princesslana.smalld.Attachment as JAttachment
import com.github.princesslana.smalld.SmallD
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import okhttp3.MediaType


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

data class Attachment(val filename: String, val contentType: String, val url: URL) {
  fun java() = JAttachment(filename, MediaType.parse(contentType), url)
}


typealias Bot = (SmallDGson) -> Unit
fun bot(bot: SmallDGson.() -> Unit): Bot = bot

fun Bot.run(token: String) {
  SmallD.run(token) { this(SmallDGson(it)) }
}

class SmallDGson(val smalld: SmallD) {
  val gson = Gson()

  fun include(vararg bs: Bot) {
    bs.forEach { it(this) }
  }

  fun onGatewayPayload(f: (GatewayPayload) -> Unit) {
    smalld.onGatewayPayload {
      f(gson.fromJson(it, GatewayPayload::class.java))
    }
  }

  fun post(path: String, payload: Any) {
    smalld.post(path, gson.toJson(payload))
  }

  fun post(path: String, attachment: Attachment) {
    smalld.post(path, "abc", attachment.java())
  }
}

