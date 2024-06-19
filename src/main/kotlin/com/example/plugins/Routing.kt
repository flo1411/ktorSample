package com.example.plugins

import com.example.model.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        staticResources("static", "static")

        get("/tasks") {
            val tasks = TaskRepository.allTasks()
            call.respond(HttpStatusCode.OK, tasks)
        }

        get("/byName/{taskName}") {
            val name = call.parameters["taskName"]
            if (name == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val task = TaskRepository.taskByName(name)
            if (task == null) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }
            call.respond(task)
        }

        get("/byPriority/{priority}") {
            val priorityAsText = call.parameters["priority"]
            if (priorityAsText == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            try {
                val priority = Priority.valueOf(priorityAsText)
                val tasks = TaskRepository.tasksByPriority(priority)

                if (tasks.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }
                call.respond(tasks)
            } catch (ex: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        get("/powerMove") {
            val response = TaskRepository.getStuffFromOtherServer()
            if (response.status == HttpStatusCode.OK) {
                call.respond(HttpStatusCode.OK, response.status.toString())
                return@get
            }
        }

        get("/pokemon") {
            val response = PokemonRepository.getPokemonOtherServer()
            if (response.status == HttpStatusCode.OK) {
                val test: PokemonResponse = response.body()
                call.respond(HttpStatusCode.OK, test)
                return@get
            }
        }
    }
}
