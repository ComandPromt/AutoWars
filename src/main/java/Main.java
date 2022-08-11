import org.jnativehook.GlobalScreen;

public class Main {

	public static void main(String[] args) {

		try {

			GlobalScreen.registerNativeHook();

			GlobalScreen.addNativeKeyListener(new KeyLogger());

			new AutoBattles().setVisible(true);

		}

		catch (Exception e) {

		}

	}

}