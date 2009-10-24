package org.epseelon.todolist

class TodoList {
    String name
    List todos

    static hasMany = [
        todos:Todo
    ]

    static constraints = {
    }
}
