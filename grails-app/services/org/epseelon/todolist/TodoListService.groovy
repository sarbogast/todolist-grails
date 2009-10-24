package org.epseelon.todolist

import org.epseelon.todolist.dto.TodoListItem
import org.epseelon.todolist.dto.TodoListDetail
import org.epseelon.todolist.dto.TodoDetail
import org.epseelon.todolist.exceptions.TodoListNotFoundException
import org.epseelon.todolist.exceptions.TodoNotFoundException
import org.springframework.flex.remoting.RemotingDestination

@RemotingDestination
class TodoListService {

    boolean transactional = true

    public List<TodoListItem> getAllLists() {
        return TodoList.findAll().collect {TodoList list ->
            new TodoListItem(
                    id: list.id,
                    name: list.name
            )
        }
    }

    public TodoListDetail getList(long id) throws TodoListNotFoundException {
        TodoList list = TodoList.findById(id)
        if(list == null) throw new TodoListNotFoundException(todoListId: id)
        return new TodoListDetail(
                id: list.id,
                name: list.name,
                todos: list.todos.collect {Todo todo ->
                    new TodoDetail(
                            id: todo.id,
                            title: todo.title,
                            priority: todo.priority,
                            description: todo.description
                    )
                }
        )
    }

    public void createList(TodoListDetail todoList) {
        TodoList list = new TodoList(
            name: todoList.name,
            todos: todoList.todos.collect {TodoDetail todo ->
                new Todo(
                        title: todo.title,
                        priority: todo.priority,
                        description: todo.description
                )
            }
        );
        list.save()
    }

    public void updateList(TodoListDetail todoList) throws TodoListNotFoundException, TodoNotFoundException {
        TodoList list = TodoList.findById(todoList.id);
        if(list == null) throw new TodoListNotFoundException(todoListId: todoList.id)
        list.setName(todoList.name);
        todoList.todos.each {TodoDetail todoDetail ->
            if(todoDetail.id < 0){
                Todo todo = new Todo(
                        title: todoDetail.title,
                        description: todoDetail.description,
                        priority: todoDetail.priority
                )
                todo.save()
                list.addToTodos todo
            } else {
                Todo todo = Todo.findById(todoDetail.id)
                if(todo == null) throw new TodoNotFoundException(todoId: todoDetail.id)
                todo.title = todoDetail.title
                todo.description = todoDetail.description
                todo.priority = todoDetail.priority
                todo.save()
            }
        }

        List<Todo> toDelete = new ArrayList<Todo>()
        list.todos.each {Todo todo ->
            if(!todoList.todos.find {TodoDetail todoDetail -> todoDetail.id == todo.id}){
                toDelete.add(todo)
            }
        }
        toDelete.each {
            list.removeFromTodos(it);
            it.delete()
        }
    }

    public void deleteList(long id) {
        TodoList.findById(id)?.delete();
    }
}
