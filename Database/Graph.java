package Splitwise.Database;

import java.util.*;

public class Graph {
    HashSet<Integer> people;
    HashMap<Edge, Integer> edges;
    HashMap<Integer, HashMap<Integer, Integer>> hisab;

    public Graph () {
        people = new HashSet<>();
        edges = new HashMap<>();
        hisab = new HashMap<>();
    }

    public boolean isEmpty () {
        return people.isEmpty();
    }

    public boolean containsPerson (int id) {
        return people.contains(id);
    }

    public void addPerson (int id) {
        people.add(id);
    }

    public HashSet<Integer> getPeopleInGroup () {
        return people;
    }

    public void createGraph (ArrayList<Integer> people) {
        Iterator<Integer> itr = people.listIterator();

        for (; itr.hasNext();) {
            this.people.add(itr.next());
        }
    }

    public HashMap<Edge, Integer> getEdges () {
        return edges;
    }

    public boolean containsEdge (Edge edge) {
        return edges.containsKey(edge);
    }

    public void removeEdge (Edge edge) {
        if (!containsEdge(edge)) return;
        edges.remove(edge);
    }

    public void addAmount (Edge edge, int amount) {
        if (edges.containsKey(edge)) {
            int val = edges.get(edge) + amount;

            if (val == 0) edges.remove(edge);
            else edges.put(edge, val);

            return;
        }
        edges.put(edge, amount);
    }

    public void addHisabBetweenId1AndId2(int id1, int id2, int amount) {
        if (amount == 0) return;

        HashMap<Integer, Integer> tempHisabOfId1 = new HashMap<>();

        if (hisab.containsKey(id1)) tempHisabOfId1 = hisab.get(id1);

        if (tempHisabOfId1.containsKey(id2)) amount += getHisabOfId1WithId2(id1, id2);

        if (amount == 0) tempHisabOfId1.remove(id2);
        else tempHisabOfId1.put(id2, amount);

        hisab.put(id1, tempHisabOfId1);
    }

    public HashMap<Integer, Integer> getHisabOfId(int id) {
        if (!hisab.containsKey(id)) return new HashMap<>();

        return hisab.get(id);
    }

    public int getHisabOfId1WithId2(int id1, int id2) {
        if (hisab.containsKey(id1)) {
            if (hisab.get(id1).containsKey(id2)) return hisab.get(id1).get(id2);
        }

        return 0;
    }

    public void removeHisabOfId (int id) {
        if (!hisab.containsKey(id)) return;
        hisab.remove(id);
    }

    public void settleHisabBetweenId1AndId2(int id1, int id2) {
        if (!hisab.containsKey(id1) || !hisab.containsKey(id2)) return;

        if (hisab.get(id1).containsKey(id2)) hisab.get(id1).remove(id2);
        if (hisab.get(id1).isEmpty()) removeHisabOfId(id1);

        if (hisab.get(id2).containsKey(id1)) hisab.get(id2).remove(id1);
        if (hisab.get(id2).isEmpty()) removeHisabOfId(id2);
    }

    public void putHisabOfId(int id, HashMap<Integer, Integer> hisabOfId) {
        if (!hisab.containsKey(id)) return;
        hisab.put(id, hisabOfId);
    }
}