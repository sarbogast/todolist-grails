package org.epseelon.todolist

enum Priority {
    HIGH(3), MEDIUM(2), LOW(1);

    private int level;

    private Priority(int level){
        this.level = level;
    }

    public int getLevel(){
        return level;
    }
}