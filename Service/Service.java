package Splitwise.Service;

import Splitwise.Database.*;
import Splitwise.Model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public class Service {
    Database db;

    public Service () {
        db = new Database();
    }

    public void addPerson (String name) {
        db.addPerson(name);
    }

    public void addPersonInGroup (String groupName, int id) {
        db.getGroup(groupName).addPerson(id);
    }

    public Person getPerson (int id) {
        return db.getPerson(id);
    }

    public HashSet<Integer> getPeopleInGroup (String groupName) {
        return db.getGroup(groupName).getPeopleInGroup();
    }

    public HashMap<Integer, Person> getPeopleDB () {
        return db.getPeopleDB();
    }

    public int getPeopleDBSize () {
        return db.getPeopleDBSize();
    }

    public boolean checkPersonByID (int id) {
        return db.getPeopleDB().containsKey(id);
    }

    public boolean checkPersonByIDInGroup (String grooupName, int id) {
        return db.getGroupsDB().get(grooupName).containsPerson(id);
    }

    public HashMap<String, Graph> getGroupsDB () {
        return db.getGroupsDB();
    }

    public void createGroup (String name, ArrayList<Integer> people) {
        db.createGroup(name, people);
    }

    public Graph getGroup (String groupName) {
        return db.getGroup(groupName);
    }

    public boolean checkGroupByName (String groupName) {
        return db.getGroupsDB().containsKey(groupName);
    }

    public void addAmountInGroupById(String groupName, int id, ArrayList<Integer> people, int val) {
        Graph group = getGroup(groupName);

        if (group.isEmpty()) return;

        int n = people.size();
        int amount = val / n;

        for (int i = 0; i < n; i++) {
            if (id == people.get(i)) continue;

            Edge edge = new Edge(id, people.get(i));

            if (id < people.get(i)) addAmountInGroupById(group, edge, amount);
            else addAmountInGroupById(group, edge, 0 - amount);
        }

        computeAdvancedHisabOfGroup(group);
    }

    private void addAmountInGroupById (Graph group, Edge edge, int amount) {
        group.addAmount(edge, amount);

        group.addHisabBetweenId1AndId2(edge.getId1(), edge.getId2(), amount);
        group.addHisabBetweenId1AndId2(edge.getId2(), edge.getId1(), 0 - amount);

        HashMap<Integer, Integer> hisabOfId1 = db.getHisabOf(edge.getId1());
        int tempAmount = amount;
        if (hisabOfId1.containsKey(edge.getId2())) {
            tempAmount += hisabOfId1.get(edge.getId2());
        }
        hisabOfId1.put(edge.getId2(), tempAmount);
        db.putHisabOfId(edge.getId1(), hisabOfId1);


        HashMap<Integer, Integer> hisabOfId2 = db.getHisabOf(edge.getId2());
        tempAmount = 0 - amount;
        if (hisabOfId2.containsKey(edge.getId1())) {
            tempAmount += hisabOfId2.get(edge.getId1());
        }
        hisabOfId2.put(edge.getId1(), tempAmount);
        db.putHisabOfId(edge.getId2(), hisabOfId2);
    }

    private void computeAdvancedHisabOfGroup (Graph group) {
        HashSet<Integer> people = group.getPeopleInGroup();

        for (Integer id : people) {
            Stack<Integer> stackOfPeopleIdOwe = new Stack<>();
            HashSet<Integer> hashSetOfPeopleIdOwe = new HashSet<>();

            HashMap<Integer, Integer> hisabOfId = group.getHisabOfId(id);

            for (Integer tempId : hisabOfId.keySet()) {
                if (hisabOfId.get(tempId) < 0) {
                    stackOfPeopleIdOwe.push(tempId);
                    hashSetOfPeopleIdOwe.add(tempId);
                }
            }

            for (; !stackOfPeopleIdOwe.isEmpty();) {
                int tempId = stackOfPeopleIdOwe.peek();
                stackOfPeopleIdOwe.pop();
                hashSetOfPeopleIdOwe.remove(tempId);

                int amount = 0 - hisabOfId.get(tempId);

                HashMap<Integer, Integer> hisabOfTempId = group.getHisabOfId(tempId);

                for (Integer i : hisabOfTempId.keySet()) {
                    int val = hisabOfTempId.get(i);
                    if (val < 0) {
                        if (amount > 0 - val) {
                            if (id > i) addAmountInGroupById(group, new Edge(i, id), 0 - val);
                            else addAmountInGroupById(group, new Edge(i, id), hisabOfTempId.get(i));

                            settleHisabBetweenId1AndId2InGroup(group, i, tempId);

                            if (id > tempId) addAmountInGroupById(group, new Edge(tempId, id), val);
                            else addAmountInGroupById(group, new Edge(tempId, id), 0 - val);
                        }
                        else {
                            if (id > i) addAmountInGroupById(group, new Edge(i, id), amount);
                            else addAmountInGroupById(group, new Edge(i, id), 0 - amount);

                            if (tempId < i) addAmountInGroupById(group, new Edge(i, tempId), amount);
                            else addAmountInGroupById(group, new Edge(i, tempId), 0 - amount);

                            if (id > tempId) addAmountInGroupById(group, new Edge(tempId, id), 0 -amount);
                            else addAmountInGroupById(group, new Edge(tempId, id), amount);
                        }

                        if (!hashSetOfPeopleIdOwe.contains(i)) {
                            stackOfPeopleIdOwe.push(i);
                            hashSetOfPeopleIdOwe.add(i);
                        }

                        amount = amount + val;
                    }
                    if (amount <= 0) break;
                }
            }
        }
    }

    public HashMap<Integer, Integer> getHisabOfId(int id) {
        return db.getHisabOf(id);
    }

    public HashMap<Integer, Integer> getHisabOfIdInGroup(String groupName, int id) {
        Graph group = getGroup(groupName);

        if (group.isEmpty()) return new HashMap<>();

        return group.getHisabOfId(id);
    }

    public int getHisabOfId1WithId2(int id1, int id2) {
        HashMap<String, Graph> groupDB = getGroupsDB();

        if (groupDB.isEmpty()) return 0;
        if (!db.containsPerson(id1) || !db.containsPerson(id2)) return 0;

        int amount = 0;
        for (String groupName : groupDB.keySet()) {
            amount += getHisabOfId1WithId2InGroup(groupName, id1, id2);
        }

        return amount;
    }

    public int getHisabOfId1WithId2InGroup(String groupName, int id1, int id2) {
        if (!db.containsGroup(groupName)) return 0;
        return getHisabOfId1WithId2InGroup(db.getGroup(groupName), id1, id2);
    }

    public int getHisabOfId1WithId2InGroup(Graph group, int id1, int id2) {
        return group.getHisabOfId1WithId2(id1, id2);
    }

    public void settleHisabBetweenId1AndId2(int id1, int id2) {
        HashMap<String, Graph> groupDB = getGroupsDB();

        if (groupDB.isEmpty()) return;
        if (!db.containsPerson(id1) || !db.containsPerson(id2)) return;
        if (!db.containsHisabOf(id1) || !db.containsHisabOf(id2)) return;
        if (!db.getHisabOf(id1).containsKey(id2) || db.getHisabOf(id2).containsKey(id1)) return;

        for (String groupName : groupDB.keySet()) {
            settleHisabBetweenId1AndId2InGroup(groupName, id1, id2);
        }

        db.getHisabOf(id1).remove(id2);
        if (db.getHisabOf(id1).isEmpty()) db.deleteHisabOf(id1);

        db.getHisabOf(id2).remove(id1);
        if (db.getHisabOf(id2).isEmpty()) db.deleteHisabOf(id2);
    }

    public void settleHisabBetweenId1AndId2InGroup(String groupName, int id1, int id2) {
        Graph group = getGroup(groupName);
        settleHisabBetweenId1AndId2InGroup(group, id1, id2);
    }

    private void settleHisabBetweenId1AndId2InGroup (Graph group, int id1, int id2) {
        if (group.isEmpty()) return;
        if (!db.containsHisabOf(id1) || !db.containsHisabOf(id2)) return;

        HashMap<Integer, Integer> hisabOfId1 = db.getHisabOf(id1);
        if (hisabOfId1.containsKey(id2)) {
            hisabOfId1.put(id2, hisabOfId1.get(id2) - group.getHisabOfId1WithId2(id1, id2));
            group.putHisabOfId(id1, hisabOfId1);
        }

        HashMap<Integer, Integer> hisabOfId2 = db.getHisabOf(id2);
        if (hisabOfId2.containsKey(id1)) {
            hisabOfId2.put(id1, hisabOfId2.get(id1) - group.getHisabOfId1WithId2(id2, id1));
            group.putHisabOfId(id2, hisabOfId2);
        }

        Edge edge = new Edge(id1, id2);

        if (!group.containsEdge(edge)) return;

        group.removeEdge(edge);
        group.settleHisabBetweenId1AndId2(id1, id2);
    }

}
