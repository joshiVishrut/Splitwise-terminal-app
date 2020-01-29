package Splitwise.Database;

import Splitwise.Model.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Database {
    HashMap<Integer, Person> peopleDB;
    int peopleDBSize;
    HashMap<String, Graph> groupsDB;
    HashMap<Integer, HashMap<Integer, Integer>> hisabDB;
    //int groupDBSize;

    public Database() {
        peopleDB = new HashMap<>();
        peopleDBSize = 0;
        groupsDB = new HashMap<>();
        hisabDB = new HashMap<>();
        //groupDBSize = 0;
    }

    public void addPerson (String name) {
        peopleDBSize++;
        Person person = new Person(peopleDBSize, name);
        peopleDB.put(peopleDBSize, person);
    }

    public boolean containsPerson (int id) {
        return peopleDB.containsKey(id);
    }

    public Person getPerson (int id) {
        if (peopleDB.containsKey(id)) return peopleDB.get(id);
        return new Person();
    }

    public HashMap<Integer, Person> getPeopleDB () {
        return peopleDB;
    }

    public int getPeopleDBSize () {
        return peopleDBSize;
    }

    public boolean containsHisabOf (int id) {
        return hisabDB.containsKey(id);
    }

    public HashMap<Integer, Integer> getHisabOf (int id) {
        if (!hisabDB.containsKey(id)) return new HashMap<>();
        return hisabDB.get(id);
    }

    public void putHisabOfId (int id, HashMap<Integer, Integer> hisabOfId) {
        if (hisabOfId.isEmpty()) return;
        hisabDB.put(id, hisabOfId);
    }

    public void deleteHisabOf (int id) {
        if (hisabDB.containsKey(id)) hisabDB.remove(id);
    }

    public void createGroup (String groupName, ArrayList<Integer> people) {
        Graph g = new Graph();
        g.createGraph(people);
        groupsDB.put(groupName, g);
    }

    public boolean containsGroup (String groupName) {
        return groupsDB.containsKey(groupName);
    }

    public Graph getGroup (String groupName) {
        if (groupsDB.containsKey(groupName)) return groupsDB.get(groupName);

        return new Graph();
    }

    public HashMap<String, Graph> getGroupsDB () {
        return groupsDB;
    }
}
