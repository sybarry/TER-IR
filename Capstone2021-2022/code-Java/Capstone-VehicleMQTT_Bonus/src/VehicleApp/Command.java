package VehicleApp;

public class Command {
	public static final String FINISH = "Finish:finish";
	public static final String START = "Start:start";
	public static final String WANTBONUS = "Wantbonus::Wantbonus";
	public static final String COUNTDOWN1 = "COUNTDOWN1:1";
	public static final String COUNTDOWN2 = "COUNTDOWN2:2";
	public static final String COUNTDOWN3 = "COUNTDOWN3:3";
	
	public static String keyWordInCommand(String command) {
		return command.split(":")[0];
	}
	
	public static String messageInCommand(String command) {
		return command.split(":")[1];
	}
}
