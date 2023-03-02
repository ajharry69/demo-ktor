package ke.co.xently.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import ke.co.xently.Connection
import java.time.Duration
import java.util.*
import kotlin.collections.LinkedHashSet

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing {
        val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
        webSocket("/chats") {
            this@configureSockets.log.info("Adding a new user...")
            val thisConnection = Connection(this)
            connections += thisConnection

            try {
                send("You are connected! There are ${connections.count()} users here.")

                for (frame in incoming) {
                    when (frame) {
                        is Frame.Binary -> {
                            this@configureSockets.log.info("Received: $frame")
                        }
                        is Frame.Close -> {
                            this@configureSockets.log.info("Received: $frame")
                        }
                        is Frame.Ping -> {
                            this@configureSockets.log.info("Received: $frame")
                        }
                        is Frame.Pong -> {
                            this@configureSockets.log.info("Received: $frame")
                        }
                        is Frame.Text -> {
                            val receivedText = frame.readText()
                            val textWithUsername = "[${thisConnection.name}]: $receivedText"
                            connections.forEach {
                                it.session.send(textWithUsername)
                            }
                        }
                    }
                }
            } catch (ex: Exception) {
                this@configureSockets.log.error("An unexpected error was encountered!", ex)
            } finally {
                this@configureSockets.log.info("""Removing user: "${thisConnection.name}!"""")
                connections -= thisConnection
            }
        }
    }
}