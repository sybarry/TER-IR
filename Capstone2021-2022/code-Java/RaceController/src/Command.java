

public class Command {
	public static final String FINISH = "Finish:finish";
	public static final String START = "Start:start";
	public static final String WANTBONUS = "Wantbonus::Wantbonus";
	public static final String READY = "Ready:ready";
	
	public static String keyWordInCommand(String command) {
		return command.split(":")[0];
	}
	
	public static String messageInCommand(String command) {
		return command.split(":")[1];
	}
}
