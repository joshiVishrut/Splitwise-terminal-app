package Splitwise.Database;

public class Edge {
    int id1, id2;

    public Edge (int id1, int id2) {
        if (id1 > id2) {
            int t = id2;
            id2 = id1;
            id1 = t;
        }

        this.id1 = id1;
        this.id2 = id2;
    }

    public boolean contains (int id) {
        if (id1 == id || id2 == id) return true;
        return false;
    }

    public int getId1() {
        return id1;
    }

    public int getId2() {
        return id2;
    }
}
