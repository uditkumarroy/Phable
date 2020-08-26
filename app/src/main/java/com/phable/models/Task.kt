package com.phable.models

data class Task (
    val id:Int,
    val name:String,
    val email:String
){
    override fun toString(): String {
        return "Task(id=$id, name='$name', email='$email')"
    }
}