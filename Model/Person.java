package Splitwise.Model;

public class Person {
    int id;
    String name;

    public Person () {
        id = -1;
        name = "";
    }

    public Person (int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName () {
        return name;
    }

    public boolean isPerson () {
        if (this.id == -1) return false;

        return true;
    }
}
