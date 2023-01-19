import java.util.Scanner;
import java.util.*;

public class TODOManager {
    static Scanner input = new Scanner(System.in);
    static ArrayList<Task> taskList = new ArrayList<Task>();
    static String userId;
    static String password;
    static ArrayList<String> categories = new ArrayList<>();

    public static void main(String[] args) {
        intro();
        functions();
    }

    //Interface of the app to manage your TODOs
    public static void intro() {
        System.out.println("\nWelcome to your TODO app! ");
        System.out.print("Are you a (N)ew or (R)eturning user? ");
        String user = input.nextLine();

        if (user.equals("R")) {
            //Checks to see if the user has entered correct login information
            System.out.print("Please type in your userID: ");
            String idCheck = input.nextLine();
            while (userId != idCheck) {
                System.out.print("Please try again: ");
                idCheck = input.nextLine();
            }
            System.out.print("Please type in your password: ");
            String pwCheck = input.nextLine();
            while (pwCheck != password) {
                System.out.print("Please try again: ");
                pwCheck = input.nextLine();
            }
        }
        else if (user.equals("N")) {
            newLogin();
        }
    }

    //Lets the user set a new username and password
    public static void newLogin(){
            System.out.print("Please set a new userID: ");
            userId = input.nextLine();
            System.out.print("Please set a new password: ");
            password = input.nextLine();
    }

    //Functions of the TODO app listed out
    public static void functions(){
        dashedLines();
        System.out.println("\nWhat would you like to do with your planner: ");
        System.out.println("(1) View To-Do List ");
        System.out.println("(2) Set new username and password\n");
        int answer = input.nextInt();

        if (answer == 1) {
            viewList();
        }
        else if (answer == 2) {
            newLogin();
        }
    }

    //Asks you what you want to do with your list of TODOs
    public static void viewList() {
        if (taskList.size() > 0) {
            dashedLines();
            System.out.println("What would you like to do with your list of tasks?");
            System.out.println("(1) View alphabetically");
            System.out.println("(2) View by due date");
            System.out.println("(3) View by category");
            System.out.println("(4) View by status");
            System.out.println("(5) Add a new task");
            System.out.println("(6) View a specific task");
            System.out.println("(7) View by time \n");
            int func = input.nextInt();

            if (func == 1) {
                alphabetically();
            }
            else if (func == 2) {
                sortDate();
            }
            else if (func == 3) {
                sortCategory();
            }
            else if (func == 4) {
                sortStatus();
            }
            else if (func == 5) {
                Task add_task = Task();
                taskList.add(add_task);
                viewTasks("None");
            }
            else if (func == 7) {
                ArrayList<Task> am = new ArrayList<>();
                ArrayList<Task> pm = new ArrayList<>();

                for (int i = 0; i < taskList.size(); i++) {
                    if (taskList.get(i).dueTime.substring(5, 7).equals("am")) {
                        am.add(taskList.get(i));
                    }
                    else {
                        pm.add(taskList.get(i));
                    }
                }
                am = sortTime(am);
                pm = sortTime(pm);
                for (int i = 0; i < pm.size(); i++) {
                    am.add(pm.get(i));
                }
                taskList = am;
                viewTasks("Time");
            }
            else if (func == 6) {
                viewTasks("Specific task");
                System.out.println("\nEnter a number to view a task: ");
                int view = input.nextInt();
                dashedLines();
                view_task(view);
            }
        }
        //If you don't have any tasks, it prompts you to make one
        else {
            System.out.print("\nNo Tasks! Would you like to add one? (Y) or (N) ");
            String add = input.next();

            if (add.equals("Y")) {
                Task newTask = Task();
                taskList.add(newTask);
                viewTasks("None");
            }
            else {
                intro();
            }
        }
    }

    //Functions that work when you view a singular task
    public static void view_task(int task_num) {
        Task currentTask = taskList.get(task_num);
        summary(currentTask);

        System.out.println("Would you like to: ");
        System.out.println("(1) Modify task");
        System.out.println("(2) Delete task");
        System.out.println("(3) Mark task as done\n");

        int changeTask = input.nextInt();

        if (changeTask == 1) {
            dashedLines();
            System.out.println("In particular, would you like to: ");
            System.out.println("\n(1) Change name");
            System.out.println("(2) Change due date");
            System.out.println("(3) Change due time");
            System.out.println("(4) Change status");
            System.out.println("(5) Change category\n");

            int change = input.nextInt();
            taskList.set(task_num, modify(change, currentTask));
            dashedLines();
            viewTasks("None");
        }
        else if (changeTask == 2) {
            dashedLines();
            System.out.println("Which task would you like to delete? ");
            for (int i = 0; i < taskList.size(); i++) {
                System.out.println(i + ". " + taskList.get(i).name);
            }
            int taskNum = input.nextInt();
            taskList.remove(taskNum);
            viewTasks("None");
        }
        else if (changeTask == 3) {
            currentTask.category = "Complete";
            summary(currentTask);
            viewTasks("None");
        }
    }

    //Makes a new task
    public static Task Task() {
        dashedLines();
        Task newTask = new Task();

        System.out.print("Task name: ");
        input.nextLine();
        newTask.name = input.nextLine();

        System.out.print("Task description: ");
        newTask.description = input.nextLine();

        System.out.print("Due date (DD/MM/YY): ");
        newTask.dueDate = input.next();
        if (newTask.dueDate.length() != 8) {
            while(newTask.dueDate.length() != 8) {
                System.out.print("Make sure to enter it in the correct format: ");
                newTask.dueDate = input.next();
            }
        }

        System.out.print("Time due (xx:xxam/pm): ");
        newTask.dueTime = input.next();
        if (newTask.dueTime.length() != 7) {
            while(newTask.dueTime.length() != 7) {
                System.out.print("Make sure to enter it in the correct format: ");
                newTask.dueTime = input.next();
            }
        }

        System.out.print("Category: ");
        newTask.category = input.next();
        if (!categories.contains(newTask.category)) {
            categories.add(newTask.category);
        }

        System.out.print("Status (1) Not started (2) In progress (3) Complete: ");
        int s = input.nextInt();
        if (s == 1) {
            newTask.status = "Not started";
        }
        else if (s == 2) {
            newTask.status = "In progress";
        }
        else if (s == 3) {
            newTask.status = "Complete";
        }

        System.out.println("\nNew Task Created!");
        summary(newTask);

        return newTask;
    }

    //Shows the sorted and filtered tasks
    public static void viewTasks(String func) {
        if (func.equals("None")) {
            System.out.println("\nHere is a list of your tasks: \n");
            for (int i = 0; i < taskList.size(); i++) {
                System.out.println(i + ". " + taskList.get(i).name);
            }
            viewList();
        }
        else if (func.equals("Specific task")) {
            System.out.println("\nHere is a list of your tasks: \n");
            for (int i = 0; i < taskList.size(); i++) {
                System.out.println(i + ". " + taskList.get(i).name);
        }
    }
        else if (func.equals("Categories")) {
            dashedLines();
            System.out.println("\nHere is a list of your tasks: \n");
            for (int i = 0; i < taskList.size(); i++) {
                System.out.println(i + ". " + taskList.get(i).name + " | " + taskList.get(i).category);
            }
            viewList();
        }
        else if (func.equals("Time")) {
            dashedLines();
            System.out.println("\nHere is a list of your tasks: \n");
            for (int i = 0; i < taskList.size(); i++) {
                System.out.println(i + ". " + taskList.get(i).name + " | " + taskList.get(i).dueTime);
            }
            viewList();
        }
        else if (func.equals("Date")) {
            dashedLines();
            System.out.println("\nHere is a list of your tasks (Earliest to Latest): \n");
            for (int i = 0; i <taskList.size(); i++) {
                System.out.println(i + ". " + taskList.get(i).name + " | " + taskList.get(i).dueDate);
            }
            viewList();
        }
        else if (func.equals("Status")) {
            dashedLines();
            System.out.println("\nHere is a list of your tasks: \n");
            for (int i = 0; i < taskList.size(); i++) {
                System.out.println(i + ". " + taskList.get(i).name + " | " + taskList.get(i).status);
            }
            viewList();
        }
    }

    //Prints out a summary of the task
    public static void summary(Task curr) {
        System.out.println("\nTask summary: \n");
        System.out.println("Name: " + curr.name);
        System.out.println("Description: " + curr.description);
        System.out.println("Due Date: " + curr.dueDate);
        System.out.println("Status: " + curr.status);
        System.out.println("Category: " + curr.category);
        System.out.println("Due Time: " + curr.dueTime);
        dashedLines();
    }

    //Modifying a task
    public static Task modify(int function, Task curr) {
        if (function == 1) {
            System.out.print("\nEnter new name here: ");
            input.nextLine();
            curr.name = input.nextLine();
        }
        else if (function == 2) {
            System.out.print("\nEnter new date here: ");
            curr.dueDate = input.next();
        }
        else if (function == 3) {
            System.out.print("\nEnter new time here: ");
            curr.dueTime = input.next();
        }
        else if (function == 4) {
            System.out.print("\nEnter new status here: ");
            curr.status = input.next();
        }
        else if (function == 5) {
            System.out.print("\nEnter new category here: ");
            curr.category = input.next();
            if (!categories.contains(curr.category)) {
                categories.add(curr.category);
            }
        }
        dashedLines();
        summary(curr);

        return curr;
    }

    //Sorts the tasks using alphabetical order
    public static void alphabetically() {
        for (int i = 0; i < taskList.size() ; i++) {
            for (int j = i + 1; j < taskList.size(); j++) {
                String name1 = taskList.get(i).name;
                String name2 = taskList.get(j).name;

                if (Character.compare(name1.charAt(0), name2.charAt(0)) > 0) {
                    Task temp = taskList.get(i);
                    taskList.set(i, taskList.get(j));
                    taskList.set(j, temp);
                }
            }
        }
        viewTasks("None");
        viewList();
    }

    //Sorts the tasks by their due date
    public static void sortDate() {
        for (int i = 0; i < taskList.size() ; i++) {
            for (int j = i + 1; j < taskList.size(); j++) {
                String date1 = taskList.get(i).dueDate;
                String date2 = taskList.get(j).dueDate;

                if (Integer.parseInt(date2.substring(6, 8)) > Integer.parseInt(date1.substring(6, 8))) {
                    Task temp = taskList.get(i);
                    taskList.set(i, taskList.get(j));
                    taskList.set(j, temp);
                }
                else if (date2.substring(6, 8).equals(date1.substring(6, 8))) {
                    if (Integer.parseInt(date2.substring(3,5)) > Integer.parseInt(date1.substring(3, 5))) {
                        Task temp = taskList.get(i);
                        taskList.set(i, taskList.get(j));
                        taskList.set(j, temp);
                    }
                }
                else {
                    if (Integer.parseInt(date2.substring(0, 2)) > Integer.parseInt(date1.substring(0, 2))) {
                        Task temp = taskList.get(i);
                        taskList.set(i, taskList.get(j));
                        taskList.set(j, temp);
                    }
                }
            }
        }
        Collections.reverse(taskList);
        viewTasks("Date");
    }

    //Sorts the tasks by the categories they are in
    public static void sortCategory() {
        ArrayList<Task> tempList = new ArrayList<Task>();
        for (int i = 0; i < categories.size(); i++) {
            for (int j = 0; j < taskList.size(); j++) {
                if (taskList.get(j).category.equals(categories.get(i))) {
                    tempList.add(taskList.get(j));
                }
            }
        }
        taskList = tempList;
        viewTasks("Categories");
    }

    //Sort the tasks by their status
    public static void sortStatus() {
        ArrayList<Task> tempList = new ArrayList<Task>();
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).status.equals("Not started")) {
                    tempList.add(taskList.get(i));
                }
            }
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).status.equals("In progress")) {
                    tempList.add(taskList.get(i));
                }
            }
        for (int i = 0; i < categories.size(); i++) {
            if (taskList.get(i).status.equals("Complete")) {
                    tempList.add(taskList.get(i));
                }
            }
        taskList = tempList;
        viewTasks("Status");
    }

    //Sorts the tasks by the time it's due
    public static ArrayList<Task> sortTime(ArrayList<Task> input) {
        for (int i = 0; i < input.size(); i++) {
            for (int j = i + 1; j < input.size(); j++) {
                String curr1 = input.get(i).dueTime;
                if (curr1.substring(0,2).compareTo(curr1.substring(0, 2)) > 0) {
                    Task temp = input.get(i);
                    input.set(i, input.get(j));
                    input.set(j, temp);
                }
                else if (curr1.substring(0,2).compareTo(curr1.substring(0, 2)) == 0) {
                    if (curr1.substring(3,5).compareTo(curr1.substring(3, 5)) > 0) {
                        Task temp = input.get(i);
                        input.set(i, input.get(j));
                        input.set(j, temp);
                    }
                }
            }
                }
        return input;
    }

    //Dashed lines make the code easier to read
    public static void dashedLines() {
        System.out.println();
        for (int i = 1; i <=100; i++) {
            System.out.print("-");
        }
        System.out.println();
    }
    }
