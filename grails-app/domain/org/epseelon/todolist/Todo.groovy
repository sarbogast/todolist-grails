package org.epseelon.todolist

class Todo {
    String title
    String description
    Priority priority
    
    static belongsTo = [parentList:TodoList]

    static constraints = {
    }
}
