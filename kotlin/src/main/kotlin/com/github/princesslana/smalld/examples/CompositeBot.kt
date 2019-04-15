package com.github.princesslana.smalld.examples

val compositeBot = bot {
  include(pingBot, catBot)
}

fun main(args: Array<String>) = compositeBot.run(System.getenv("SMALLD_TOKEN"))
