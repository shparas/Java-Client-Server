import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerThread extends Thread {
	Socket client = null;
	PrintWriter output;
	BufferedReader input;
	
	public ServerThread(Socket client) {
		this.client = client;
	}
	
	public void run() {
		System.out.print("Accepted connection. ");

		try {
			output = new PrintWriter(client.getOutputStream(), true);
			input = new BufferedReader(new InputStreamReader(client.getInputStream()));
			System.out.print("Reader and writer created. ");

			String inString;
		        while  ((inString = input.readLine()) == null);
			System.out.println("Read command " + inString);

			String outString = CommandExecutor.run(inString);
			System.out.println("Server sending result to client");
			output.println(outString);
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
		finally {
			try {
				client.close();
			}
			catch (IOException e) {
				e.printStackTrace();	
			}			
			System.out.println("Output closed.");
		}
	}
}