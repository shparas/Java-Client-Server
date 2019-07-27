import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.ConnectException;
import java.util.concurrent.atomic.AtomicLong;


public class ClientThread extends Thread {
	int menuSelection;
	String hostName;
	Socket socket = null; 
	
	AtomicLong totalTime;

	AtomicLong runningThreads;

	boolean printOutput;

	long startTime;
	long endTime;

	ClientThread(String hostName, int menuSelection, AtomicLong totalTime, boolean printOutput, AtomicLong runningThreads) {
		this.menuSelection = menuSelection;
		this.hostName = hostName;
		this.totalTime = totalTime;
		this.printOutput = printOutput;
		this.runningThreads = runningThreads;
	}

	public void run() {
		PrintWriter out = null;
		BufferedReader input = null;
		try {
			socket = new Socket(hostName, 15432);
			if (printOutput) {
				System.out.print("Establishing connection.");
			}
			out = new PrintWriter(socket.getOutputStream(), true);

			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			if (printOutput) System.out.println("\nRequesting output for the '" + menuSelection + "' command from " + hostName);
			
			startTime = System.currentTimeMillis();

			out.println(Integer.toString(menuSelection));
			if (printOutput) System.out.println("Sent output");

			String outputString;
			while (((outputString = input.readLine()) != null) && (!outputString.equals("END_MESSAGE"))) {
				if (printOutput) System.out.println(outputString);
			}

			endTime = System.currentTimeMillis();
			totalTime.addAndGet(endTime - startTime);

		}
		catch (UnknownHostException e) {
			System.err.println("Unknown host: " + hostName);
			System.exit(1);
		}
		catch (ConnectException e) {
			System.err.println("Connection refused by host: " + hostName);
			System.exit(1);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (printOutput) System.out.println("closing");
			try {
				socket.close();
				runningThreads.decrementAndGet();
				System.out.flush();
			}
			catch (IOException e ) {
				System.out.println("Couldn't close socket");
			}
		}
	}
}
