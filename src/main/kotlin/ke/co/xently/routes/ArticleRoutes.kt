package ke.co.xently.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import ke.co.xently.models.Article
import ke.co.xently.models.articles

fun Route.articleRoute() {
    route("/articles") {
        get {
            call.respond(FreeMarkerContent("articles/index.ftl", mapOf("articles" to articles)))
        }
        get("new") {
            call.respond(FreeMarkerContent("articles/new.ftl", model = null))
        }
        post {
            val article = call.receiveParameters().run {
                Article.newEntry(
                    title = getOrFail("title"),
                    body = getOrFail("body"),
                )
            }
            articles.add(article)
            call.respondRedirect("/articles/${article.id}")
        }
        get("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val article = articles.find { it.id == id } ?: return@get call.respondText(
                text = "An article with the ID #$id was not found",
                status = HttpStatusCode.NotFound,
            )
            call.respond(FreeMarkerContent("articles/retrieve.ftl", model = mapOf("article" to article)))
        }
        get("{id}/edit") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val article = articles.find { it.id == id } ?: return@get call.respondText(
                text = "An article with the ID #$id was not found",
                status = HttpStatusCode.NotFound,
            )
            call.respond(FreeMarkerContent("articles/edit.ftl", model = mapOf("article" to article)))
        }
        post("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val articlePosition = articles.indexOfFirst { it.id == id }

            if (articlePosition == -1) {
                return@post call.respondText(
                    text = "An article with the ID #$id was not found",
                    status = HttpStatusCode.NotFound,
                )
            }

            val formParams = call.receiveParameters()
            when (formParams.getOrFail("_action")) {
                "update" -> {
                    articles[articlePosition] = articles[articlePosition].apply {
                        title = formParams.getOrFail("title")
                        body = formParams.getOrFail("body")
                    }
                    call.respondRedirect("/articles/${id}")
                }

                "delete" -> {
                    articles.removeAt(articlePosition)
                    call.respondRedirect("/articles")
                }

                else -> {
                    call.respondText("Unknown action", status = HttpStatusCode.BadRequest)
                }
            }
        }
    }
}