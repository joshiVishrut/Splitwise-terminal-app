package Splitwise;

import Splitwise.SplitwiseUI.SplitwiseUI;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SplitwiseUI ui = new SplitwiseUI();
        Scanner input = new Scanner(System.in);

        int op = 0;
        String choice;

        for (; true;) {
            System.out.println();
            System.out.println();
            ui.displayOptions();
            System.out.print("Enter choice: ");
            op = input.nextInt();

            if (0 > op || op > 13) {
                System.out.println("Incorrect Entry");
            }
            else if (op == 1) {
                ui.addPerson();
            }
            else if (op == 2) {
                ui.createGroup();
            }
            else if (op == 3) {
                ui.addAmountInGroupById();
            }
            else if (op == 4) {
                ui.getHisabOfId();
            }
            else if (op == 5) {
                ui.getHisabOfIdInGroup();
            }
            else if (op == 6) {
                ui.getHisabOfId1WithId2();
            }
            else if (op == 7) {
                ui.getHisabOfId1WithId2InGroup();
            }
            else if (op == 8) {
                ui.settleHisabBetweenId1AndId2();
            }
            else if (op == 9) {
                ui.settleHisabBetweenId1AndId2InGroup();
            }
            else if (op == 10) {
                ui.addPersonInGroup();
            }
            else if (op == 11) {
                ui.displayPeopleDB();
            }
            else if (op == 12) {
                ui.displayPeopleInGroup();
            }
            else if (op == 13) {
                ui.displayGroups();
            }

            System.out.print("Do you want to continue? (Y/N): ");
            choice = input.next();
            if (!(choice.equals("Y") || choice.equals("y"))) break;
        }
    }
}
