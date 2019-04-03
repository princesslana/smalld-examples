plugins {
  id("org.jetbrains.kotlin.jvm").version("1.3.21")
  application
}

repositories {
  mavenCentral()
  maven { url = uri("https://jitpack.io") }
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("com.github.princesslana:smalld:0.1.0")
  implementation("com.github.princesslana:smalld-json:master-SNAPSHOT")
  implementation("com.google.code.gson:gson:2.8.5")
  implementation("org.slf4j:slf4j-simple:1.7.25")
}

application {
  mainClassName = System.getProperty("mainClass")
  applicationDefaultJvmArgs = listOf("-Dorg.slf4j.simpleLogger.defaultLogLevel=debug")
}
