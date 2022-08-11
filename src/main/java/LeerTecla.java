import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.TimerTask;

public class LeerTecla extends TimerTask {

	private String texto = "";

	private FileReader flE;

	private BufferedReader fE;

	private String tecla = "";

	private int indice;

	private int cartas;

	@Override

	public void run() {

		try {

			flE = new FileReader(Paths.get("./config/keys.txt").toString());

			fE = new BufferedReader(flE);

			texto = fE.readLine();

			cartas = AutoBattles.cards.size() / 2;

			if (!texto.equals(tecla)) {

				if (AutoBattles.controles.contains(texto)) {

					indice = AutoBattles.controles.indexOf(texto);

					++indice;

					AutoBattles.escribir("Log.txt", " KEY " + texto, false);

					if (indice <= cartas) {

						tecla = texto;

						AutoBattles.carta(indice);

					}

					else {

						tecla = "----";

						AutoBattles.hacia(indice - cartas);

					}

					AutoBattles.robot.keyPress(KeyEvent.VK_CANCEL);

				}

				fE.close();

				flE.close();

			}

		}

		catch (Exception e) {

			e.printStackTrace();

		}

	}

}
