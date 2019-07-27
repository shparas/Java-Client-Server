import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {

	public static void main(String[] args) {
		AtomicInteger numThreads = new AtomicInteger(0);
		ArrayList<Thread> list = new ArrayList<Thread>();
		
		try {
			ServerSocket socket = new ServerSocket(8080);
			System.out.println("Server listening on port 8080");

			while(true) {
				Socket client = socket.accept();
				Thread thrd = new Thread(new ServerThread(client));
				list.add(thrd);
				thrd.start();
				numThreads.incrementAndGet();
				System.out.println("Thread " + numThreads.get() + " started.");
			}
		}
		catch (IOException ioe){
			ioe.printStackTrace();
		}
	}
}

