package ke.co.xently.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import ke.co.xently.routes.*

fun Application.configureRouting(apiConfig: Application.() -> Unit) {
    routing {
        route("api") {
            apiConfig(this@configureRouting)
            customerRouting()
            orderListRoute()
            orderRetrieveRoute()
            orderTotalRoute()
        }
        articleRoute()
        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }

        get("/") {
            call.respondRedirect("articles")
        }
    }
}
