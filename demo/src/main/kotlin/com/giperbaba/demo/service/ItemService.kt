package com.giperbaba.demo.service

import com.giperbaba.demo.dto.TaskDto
import com.giperbaba.demo.dto.toDto
import com.giperbaba.demo.dto.toEntity
import com.giperbaba.demo.repository.ItemRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.giperbaba.demo.entity.Task
import jakarta.transaction.Transactional

//реализация бизнес-логики

@Service
class ItemService(private val repository: ItemRepository) {

    fun save(taskDto: TaskDto): Long? {
        val entityTask = taskDto.toEntity()
        repository.save(entityTask)
        return entityTask.id;
    }

    fun deleteTask(id: Long) {
        repository.deleteById(id)
    }

    fun updateTaskDescription(id: Long, newDescription: String) {
        val taskOption = repository.findById(id)
        if (taskOption.isPresent) {
            val task = taskOption.get()
            task.description = newDescription
            repository.save(task)
        } else {
            throw Exception("task with this id $id is not found")
        }
    }

    fun updateTaskIsDone(id: Long, isDone: Boolean) {
        val taskOption = repository.findById(id)
        if (taskOption.isPresent) {
            val task = taskOption.get()
            task.isDone = isDone
            repository.save(task)
        } else {
            throw Exception("task with this id $id is not found")
        }
    }

    fun getTasks(): List<Task> {
        return repository.findAll()
    }

    @Transactional
    fun uploadArray(tasks: List<TaskDto>) {
        if (tasks.isEmpty()) {
            throw Exception("array is empty")
        }
        repository.deleteAll()
        for (task in tasks) {
            save(task)
        }
    }

    fun uploadJson(file: MultipartFile) {
        if (file.isEmpty) {
            throw Exception("file is empty")
        }
        val objectMapper = jacksonObjectMapper()
        val jsonContent = String(file.bytes)
        val tasks: List<TaskDto> = objectMapper.readValue(jsonContent)
        uploadArray(tasks)
    }
}