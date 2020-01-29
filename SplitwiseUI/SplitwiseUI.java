package Splitwise.SplitwiseUI;

import Splitwise.Database.Graph;
import Splitwise.Model.Person;
import Splitwise.Service.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class SplitwiseUI {
    Service service;
    Scanner input;

    public SplitwiseUI () {
        service = new Service();
        input = new Scanner(System.in);
    }

    public void displayOptions () {
        System.out.println("App Options:");
        System.out.println("1. Add Person");
        System.out.println("2. Create Group");
        System.out.println("3. Add amount in a group");
        System.out.println("4. Display Hisab of a person");
        System.out.println("5. Display Hisab of a person in a group");
        System.out.println("6. Display Hisab between two people");
        System.out.println("7. Display Hisab between two people in a group");
        System.out.println("8. Settle Hisab between two people");
        System.out.println("9. Settle Hisab between two people in a group");
        System.out.println("10. Add person in a group");
        System.out.println("11. Display all people");
        System.out.println("12. Display people in a group");
        System.out.println("13. Display groups");
    }

    public void displayPeopleDB () {
        HashMap<Integer, Person> peopleDB = service.getPeopleDB();

        for (Integer id : peopleDB.keySet()) {
            System.out.println(id + ": " + peopleDB.get(id).getName());
        }
    }

    public void displayPeopleInGroup (String groupName) {
        HashSet<Integer> peopleInGroup = service.getPeopleInGroup(groupName);

        for (Integer id : peopleInGroup) {
            System.out.println(id + ": " + service.getPerson(id).getName());
        }
    }

    public void displayPeopleInGroup () {
        System.out.println("Choose Group:");
        displayGroups();
        System.out.print("Enter Group Name: ");
        String groupName = input.next();

        if (!service.checkGroupByName(groupName)) {
            System.out.println("Incorrect group name entered");
            return;
        }

        displayPeopleInGroup(groupName);
    }

    public void displayGroups () {
        HashMap<String, Graph> groups = service.getGroupsDB();

        for (String groupName : groups.keySet()) {
            System.out.println("Group Name: " + groupName);
        }
    }

    public void addPerson () {
        System.out.print("Enter Person Name: ");
        String name = input.next();
        service.addPerson(name);
    }

    public void addPersonInGroup () {
        System.out.println("Choose Person:");
        displayPeopleDB();
        System.out.print("Enter ID: ");
        int id = input.nextInt();

        if (!service.checkPersonByID(id)) {
            System.out.println("Incorrect ID entered");
            return;
        }

        System.out.println("Choose Group:");
        displayGroups();
        System.out.print("Enter Group Name: ");
        String groupName = input.next();

        if (!service.checkGroupByName(groupName)) {
            System.out.println("Incorrect group name entered");
            return;
        }

        service.addPersonInGroup(groupName, id);
    }

    public void createGroup () {
        System.out.print("Enter Group Name: ");
        String groupName = input.next();

        System.out.print("Enter size of Group: ");
        int n = input.nextInt();

        if (n > service.getPeopleDBSize()) {
            System.out.println("Insufficient people in DB for size " + n);
            return;
        }

        System.out.println("Enter people to be added in group: " + groupName + " from options below:");
        displayPeopleDB();
        System.out.print("Enter " + n + " IDs: ");

        ArrayList<Integer> people = new ArrayList<>();
        boolean checkIDFlag = false;
        for (int i = 0; i < n; i++) {
            int id = input.nextInt();
            people.add(id);
            if (!service.checkPersonByID(id)) checkIDFlag = true;
        }

        if (checkIDFlag) {
            System.out.println("Incorrect ID entered");
            return;
        }

        service.createGroup(groupName, people);
    }

    public void addAmountInGroupById () {
        System.out.println("Choose a Group:");
        displayGroups();
        System.out.print("Enter Group Name: ");
        String groupName = input.next();

        if (!service.checkGroupByName(groupName)) {
            System.out.println("Incorrect group name entered");
            return;
        }

        displayPeopleInGroup(groupName);
        System.out.print("Enter Person's ID who added the amount: ");
        int id = input.nextInt();

        System.out.print("Enter amount: ");
        int amount = input.nextInt();

        System.out.print("Divide Equally? (Y/N): ");
        String choice = input.next();

        ArrayList<Integer> people = new ArrayList<>();

        if (choice.equals("Y") || choice.equals("y")) {
            HashSet<Integer> tempPeople = service.getPeopleInGroup(groupName);
            for (Integer i : tempPeople) {
                people.add(i);
            }
        }
        else {
            System.out.print("Enter number of people among whom division is made: ");
            int n = input.nextInt();
            System.out.print("Enter " + n + " IDs: ");
            boolean checkIDFlag = false;
            for (int i = 0; i < n; i++) {
                int tempId = input.nextInt();
                people.add(tempId);
                if (!service.checkPersonByID(tempId)) checkIDFlag = true;
            }

            if (checkIDFlag) {
                System.out.println("Incorrect ID entered");
                return;
            }
        }

        service.addAmountInGroupById(groupName, id, people, amount);
    }

    public void getHisabOfId () {
        System.out.println("Get Hisab Of:");
        displayPeopleDB();
        System.out.print("Enter ID: ");
        int id = input.nextInt();

        if (!service.checkPersonByID(id)) {
            System.out.println("Incorrect ID entered");
            return;
        }

        HashMap<Integer, Integer> hisabOfId = service.getHisabOfId(id);

        for (Integer tempID : hisabOfId.keySet()) {
            int hisab = hisabOfId.get(tempID);
            if (hisab == 0) {
                System.out.println("All settled between " +
                        service.getPerson(id).getName() + " and " +
                        service.getPerson(tempID).getName());
            }
            else if (hisab > 0) {
                System.out.println(service.getPerson(tempID).getName() + " owes " +
                        service.getPerson(id).getName() + " " + hisab);
            }
            else {
                hisab = 0 - hisab;
                System.out.println(service.getPerson(id).getName() + " owes " +
                        service.getPerson(tempID).getName() + " " + hisab);
            }
        }
    }

    public void getHisabOfIdInGroup () {
        System.out.println("Choose Group:");
        displayGroups();
        System.out.print("Enter Group Name: ");
        String groupName = input.next();

        if (!service.checkGroupByName(groupName)) {
            System.out.println("Incorrect group name entered");
            return;
        }

        System.out.println("Get Hisab Of:");
        displayPeopleInGroup(groupName);
        System.out.print("Enter ID: ");
        int id = input.nextInt();

        if (!service.checkPersonByIDInGroup(groupName, id)) {
            System.out.println("Incorrect ID entered");
            return;
        }

        HashMap<Integer, Integer> hisabOfId = service.getHisabOfIdInGroup(groupName, id);

        for (Integer tempID : hisabOfId.keySet()) {
            int hisab = hisabOfId.get(tempID);
            if (hisab == 0) {
                System.out.println("All settled between " +
                        service.getPerson(id).getName() + " and " +
                        service.getPerson(tempID).getName());
            }
            else if (hisab > 0) {
                System.out.println(service.getPerson(tempID).getName() + " owes " +
                        service.getPerson(id).getName() + " " + hisab);
            }
            else {
                hisab = 0 - hisab;
                System.out.println(service.getPerson(id).getName() + " owes " +
                        service.getPerson(tempID).getName() + " " + hisab);
            }
        }
    }

    public void getHisabOfId1WithId2 () {
        System.out.println("Get Hisab Between:");
        displayPeopleDB();
        System.out.print("Enter two IDs: ");
        int id1 = input.nextInt();
        int id2 = input.nextInt();

        if (!service.checkPersonByID(id1) || !service.checkPersonByID(id2)) {
            System.out.println("Incorrect ID entered");
            return;
        }

        int hisab = service.getHisabOfId1WithId2(id1, id2);

        if (hisab == 0) {
            System.out.println("All settled between " +
                    service.getPerson(id1).getName() + " and " +
                    service.getPerson(id2).getName());
        }
        else if (hisab > 0) {
            System.out.println(service.getPerson(id2).getName() + " owes " +
                    service.getPerson(id1).getName() + " " + hisab);
        }
        else {
            hisab = 0 - hisab;
            System.out.println(service.getPerson(id1).getName() + " owes " +
                    service.getPerson(id2).getName() + " " + hisab);
        }
    }

    public void getHisabOfId1WithId2InGroup () {
        System.out.println("Choose Group:");
        displayGroups();
        System.out.print("Enter Group Name: ");
        String groupName = input.next();

        if (!service.checkGroupByName(groupName)) {
            System.out.println("Incorrect group name entered");
            return;
        }

        System.out.println("Get Hisab Between:");
        displayPeopleInGroup(groupName);
        System.out.print("Enter two IDs: ");
        int id1 = input.nextInt();
        int id2 = input.nextInt();

        if (!service.checkPersonByIDInGroup(groupName, id1) || !service.checkPersonByIDInGroup(groupName, id2)) {
            System.out.println("Incorrect ID entered");
            return;
        }

        int hisab = service.getHisabOfId1WithId2InGroup(groupName, id1, id2);

        if (hisab == 0) {
            System.out.println("All settled between " +
                    service.getPerson(id1).getName() + " and " +
                    service.getPerson(id2).getName());
        }
        else if (hisab > 0) {
            System.out.println(service.getPerson(id2).getName() + " owes " +
                    service.getPerson(id1).getName() + " " + hisab);
        }
        else {
            hisab = 0 - hisab;
            System.out.println(service.getPerson(id1).getName() + " owes " +
                    service.getPerson(id2).getName() + " " + hisab);
        }
    }

    public void settleHisabBetweenId1AndId2 () {
        System.out.println("Settle Hisab Between:");
        displayPeopleDB();
        System.out.print("Enter two IDs: ");
        int id1 = input.nextInt();
        int id2 = input.nextInt();

        if (!service.checkPersonByID(id1) || !service.checkPersonByID(id2)) {
            System.out.println("Incorrect ID entered");
            return;
        }

        service.settleHisabBetweenId1AndId2(id1, id2);

        System.out.println("All settled between " +
                service.getPerson(id1).getName() + " and " +
                service.getPerson(id2).getName());
    }

    public void settleHisabBetweenId1AndId2InGroup () {
        System.out.println("Choose Group:");
        displayGroups();
        System.out.print("Enter Group Name: ");
        String groupName = input.next();

        if (!service.checkGroupByName(groupName)) {
            System.out.println("Incorrect group name entered");
            return;
        }

        System.out.println("Settle Hisab Between:");
        displayPeopleInGroup(groupName);
        System.out.print("Enter two IDs: ");
        int id1 = input.nextInt();
        int id2 = input.nextInt();

        if (!service.checkPersonByIDInGroup(groupName, id1) || !service.checkPersonByIDInGroup(groupName, id2)) {
            System.out.println("Incorrect ID entered");
            return;
        }

        service.settleHisabBetweenId1AndId2InGroup(groupName, id1, id2);

        System.out.println("All settled between " +
                service.getPerson(id1).getName() + " and " +
                service.getPerson(id2).getName() + " in group: " + groupName);
    }
}
