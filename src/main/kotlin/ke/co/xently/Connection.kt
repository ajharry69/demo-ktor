package ke.co.xently

import io.ktor.server.websocket.*
import java.util.concurrent.atomic.*

class Connection(val session: DefaultWebSocketServerSession) {
    companion object {
        private val id = AtomicInteger(0)
    }

    val name = "user${id.getAndIncrement()}"
}