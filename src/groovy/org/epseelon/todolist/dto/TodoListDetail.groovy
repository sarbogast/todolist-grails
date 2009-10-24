package org.epseelon.todolist.dto

/**
 * @author sarbogast
 * @version 24 oct. 2009, 14:07:49
 */
class TodoListDetail {
    long id
    String name;
    Collection<TodoDetail> todos = new ArrayList<TodoDetail>();
}
