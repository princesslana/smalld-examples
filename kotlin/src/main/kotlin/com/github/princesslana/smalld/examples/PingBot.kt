package com.github.princesslana.smalld.examples

val pingBot = bot {
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

fun main(args: Array<String>) = pingBot.run(System.getenv("SMALLD_TOKEN"))

