import java.util.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicLong;

public class Client {
	private static String hostName;

	private static Thread thrd = null;

	private static LinkedList<Thread> list = new LinkedList<Thread>();

	private static AtomicLong totalTime = new AtomicLong(0);

	private static AtomicLong runningThreads = new AtomicLong(0);
	private static boolean printOutput = true;

	public static void main(String[] args) {
		int menuSelection = 0;
		int numProcesses = 1;
		if (args.length == 0) { 
			System.out.println("User did not enter a host name. Client program exiting.");
			System.exit(1);
		}

		else while (menuSelection != 8) {
			menuSelection = mainMenu();

			if (menuSelection == 8) {
				System.out.println("Quitting.");
				System.exit(0);
			}

			if (menuSelection == 7) {
				printOutput = false;
				menuSelection = benchmarkMenu();
				numProcesses = numProcessesMenu();
			}
			totalTime.set(0);
			runningThreads.set(numProcesses);
			for (int i = 0; i < numProcesses; i++) {
				thrd = new Thread(new ClientThread(args[0], menuSelection, totalTime, printOutput, runningThreads));
				thrd.start(); // start the thread
				list.add(thrd); // add the thread to the end of the linked list

			}

			for (int i = 0; i < numProcesses; i++) {
				try {
					list.get(i).join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			while (runningThreads.get() != 0) {}

			System.out.println("Average response time: " + (totalTime.get() / numProcesses) + " ms\n");
			numProcesses = 1;
			printOutput = true;
		}

	}
	public static int mainMenu() {
		int menuSelection = 0;
		while ((menuSelection <= 0) || (menuSelection > 8)) {
			System.out.println("The menu provides the following choices to the user: ");
			System.out.println("1. Host current Date and Time \n2. Host uptime\n"
					+ "3. Host memory use \n4. Host Netstat \n5. Host current users "
					+ "\n6. Host running processes \n7. Benchmark (measure mean response time)\n8. Quit ");
			System.out.print("Please provide number corresponding to the action you want to be performed: ");
			Scanner sc = new Scanner(System.in);
			if (sc.hasNextInt()) menuSelection = sc.nextInt();
		}
		return menuSelection;
	}

	public static int benchmarkMenu() {
		int menuSelection = 0;
		while ((menuSelection <= 0) || (menuSelection > 6)) {
			System.out.println("Which command would you like to benchmark? ");
			System.out.println("1. Host current Date and Time \n2. Host uptime\n"
					+ "3. Host memory use \n4. Host Netstat \n5. Host current users "
					+ "\n6. Host running processes");
			System.out.print("Please provide number corresponding to the action you want to be performed: ");
			Scanner sc = new Scanner(System.in);
			if (sc.hasNextInt()) menuSelection = sc.nextInt();
		}
		return menuSelection;
	}   

	public static int numProcessesMenu() {
		int menuSelection = 0;
		while ((menuSelection <= 0) || (menuSelection > 100)) {
			System.out.print("How many connections to the server would you like to open? [1-100]: ");
			Scanner sc = new Scanner(System.in);
			if (sc.hasNextInt()) menuSelection = sc.nextInt();
		}
		return menuSelection;
	}

}
