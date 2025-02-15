package by.funduk.routes

import by.funduk.model.Submission
import by.funduk.model.Task
import by.funduk.services.SubmitService
import by.funduk.services.TaskService
import by.funduk.utils.extractUserId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.taskRoutes() {
    route("/task") {
        get {
            call.respond(TaskService.allTasks())
        }
        get("/views") {
            val count = call.request.queryParameters["count"]?.toInt() ?: 10
            val offset = call.request.queryParameters["offset"]?.toInt() ?: 0
            val userId = call.request.queryParameters["userId"]?.toInt()
            val views = TaskService.getViews(count, offset, userId)
            call.respond(views)
        }
        get("/{id}") {
            val id = call.parameters["id"]!!.toInt()
            val task = TaskService.get(id)
            if (task == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, task)
            }
        }
        post {
            val task: Task = call.receive()
            val taskId = TaskService.add(task)
            call.respond(HttpStatusCode.Created, taskId)
        }
        delete("/{id}") {
            val id = call.parameters["id"]!!.toInt()
            val deleted = TaskService.delete(id)
            call.respond(
                if (deleted) {
                    HttpStatusCode.OK
                } else {
                    HttpStatusCode.NotFound
                }
            )
        }
    }

}