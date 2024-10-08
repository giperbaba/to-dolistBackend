package com.giperbaba.demo.controller

import com.giperbaba.demo.service.ItemService
import com.giperbaba.demo.dto.TaskDto
import com.giperbaba.demo.dto.UpdateTaskDescriptionDto
import com.giperbaba.demo.dto.UpdateTaskIsDoneDto
import com.giperbaba.demo.entity.Task
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File

//обработка HTTP-запросов

@RestController
@RequestMapping("api/todo_list")
@CrossOrigin(origins = ["*"])
class UserController(private val service: ItemService) {

    @PostMapping("create")
    fun createTask(@RequestBody taskDto: TaskDto): ResponseEntity<Map<String, Long?>> {
        val idSavedTask = service.save(taskDto)
        println("Сохранённый ID задачи: $idSavedTask")
        return ResponseEntity.ok(mapOf("id" to idSavedTask))
    }

    @DeleteMapping("delete/{id}")
    fun deleteTask(@PathVariable id: Long): ResponseEntity<String> {
        service.deleteTask(id)
        return ResponseEntity.ok().build()
    }

    @PutMapping("update/desc/{id}")
    fun updateTaskDescription(@PathVariable id: Long, @RequestBody updateTaskDto: UpdateTaskDescriptionDto):
            ResponseEntity<String> {
        try {
            service.updateTaskDescription(id, updateTaskDto.newDescription)
            return ResponseEntity.ok().build()
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(e.message)
        }
    }

    @PutMapping("update/is_done/{id}")
    fun updateTaskIsDone(@PathVariable id: Long, @RequestBody updateTaskDto: UpdateTaskIsDoneDto):
            ResponseEntity<String> {
        try {
            service.updateTaskIsDone(id, updateTaskDto.isDone)
            return ResponseEntity.ok().build()
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(e.message)
        }
    }

    @GetMapping
    fun getTasks(): ResponseEntity<List<Task>> {
        val tasks = service.getTasks()
        return ResponseEntity.ok(tasks)
    }

    @PostMapping("upload/array")
    fun uploadArray(@RequestBody tasks: List<TaskDto>): ResponseEntity<String> {
        try {
            service.uploadArray(tasks)
            return ResponseEntity.ok().build()
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(e.message)
        }
    }

    @PostMapping("upload/json")
    fun uploadJson(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        try {
            service.uploadJson(file)
            return ResponseEntity.ok().build()
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(e.message)
        }

    }
}