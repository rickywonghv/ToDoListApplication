package xyz.damonwong.todolist;

/**
 * Created by Damon on 4/11/2016.
 */

public class Items {
    private int id;
    private String cont;

    public Items(int id, String cont) {
        this.id = id;
        this.cont = cont;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }
}
