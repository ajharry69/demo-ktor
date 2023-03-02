package ke.co.xently.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ke.co.xently.models.orderStorage

fun Route.orderListRoute() {
    get("/orders") {
        if (orderStorage.isNotEmpty()) {
            call.respond(orderStorage)
        }
    }
}

fun Route.orderRetrieveRoute() {
    get("/orders/{number?}") {
        val number = call.parameters["number"] ?: return@get call.respondText(
            text = "Bad request",
            status = HttpStatusCode.BadRequest,
        )

        val order = orderStorage.find { it.number == number } ?: return@get call.respondText(
            text = "Order with number #${number} not found",
            status = HttpStatusCode.NotFound,
        )

        call.respond(order)
    }
}

fun Route.orderTotalRoute() {
    get("/orders/{number?}/total") {
        val number = call.parameters["number"] ?: return@get call.respondText(
            text = "Bad Request",
            status = HttpStatusCode.BadRequest,
        )
        val order = orderStorage.find { it.number == number } ?: return@get call.respondText(
            text = "Order with number #${number} not found",
            status = HttpStatusCode.NotFound,
        )
        val total = order.contents.sumOf { it.price * it.amount }
        call.respond(total)
    }
}