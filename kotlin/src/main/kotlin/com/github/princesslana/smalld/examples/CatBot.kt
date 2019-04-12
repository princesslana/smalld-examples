package com.github.princesslana.smalld.examples;

import java.net.URL
import com.github.kittinunf.fuel.*
import com.github.kittinunf.fuel.gson.*
import com.github.kittinunf.result.*

data class CatResponse(val file: String)

fun getCatUrl(): URL {
  return URL("http://aws.random.cat/meow".httpGet()
    .responseObject<CatResponse>()
    .third
    .getOrElse(CatResponse(""))
    .file)
}

val catBot = bot {
  fun onMessageCreate(msg: Message) {
    when (msg.content) {
      "++cat" -> post("/channels/${msg.channelId}/messages", Attachment("cat.jpg", "image/jpeg",getCatUrl()))
    }
  }

  onGatewayPayload { p ->
    when (p.t) {
      "MESSAGE_CREATE" -> onMessageCreate(gson.fromJson(p.d, Message::class.java))
    }
  }
}

fun main(args: Array<String>) = catBot.run(System.getenv("SMALLD_TOKEN"))
