import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandExecutor {

	static String run(String commandString) {
		String result = "";
		String line;
		try {
			Process child = Runtime.getRuntime().exec(parseCommand(commandString));

			BufferedReader output = new BufferedReader(new InputStreamReader(child.getInputStream()));
			while ((line = output.readLine()) != null) {
				result = result.concat(line);
				result = result.concat("\n");
			}

			result = result.concat("\n");
			result = result.concat("END_MESSAGE");
			output.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	static String parseCommand(String inputString) {
		int inputInt = Integer.parseInt(inputString);
		String commandString = "";
		switch (inputInt) {
			case 1:
				commandString = "date";
				break;
			case 2:
				commandString = "uptime";
				break;
			case 3:
				commandString = "free";
				break;
			case 4:
				commandString = "netstat";
				break;
			case 5:
				commandString = "who";
				break;
			case 6:
				commandString = "ps -e";
				break;
		}
		return commandString;
	}
}
