package ke.co.xently.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ke.co.xently.models.Customer
import ke.co.xently.models.customerStorage

fun Route.customerRouting(): Unit {
    route("/customers") {
        get {
            if (customerStorage.isEmpty()) {
                call.respondText("No customers found", status = HttpStatusCode.OK)
            } else {
                call.respond(customerStorage)
            }
        }
        post {
            val customer = call.receive<Customer>()
            customerStorage.add(customer)
            call.respond(message = customer, status = HttpStatusCode.Created)
        }
        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                text = "Missing ID",
                status = HttpStatusCode.BadRequest,
            )

            val customer = customerStorage.find {
                it.id == id
            } ?: return@get call.respondText("Customer with ID $id not found", status = HttpStatusCode.NotFound)

            call.respond(customer)
        }
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            val customerPosition = customerStorage.indexOfFirst { it.id == id }
            if (customerPosition == -1) {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            } else {
                customerStorage.removeAt(customerPosition)
                call.respondText("Customer removed correctly", status = HttpStatusCode.Accepted)
            }
        }
    }
}