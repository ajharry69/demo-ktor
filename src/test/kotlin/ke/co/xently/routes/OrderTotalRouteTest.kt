package ke.co.xently.routes;

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class OrderTotalRouteTest {

    @Test
    fun testGetOrdersNumberTotal() = testApplication {
        client.get("/orders/2020-04-03-01/total").apply {
            assertEquals(status, HttpStatusCode.OK)
            assertEquals(bodyAsText(), "17.37")
        }
    }
}