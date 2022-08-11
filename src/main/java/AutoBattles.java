
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import button.SimpleButton;
import button.SimpleButton.Form;
import checkbox.JCheckBoxCustom;
import combo_suggestion.ComboBoxSuggestion;
import textarea.TextArea;
import textarea.TextAreaScroll;

@SuppressWarnings("all")

public class AutoBattles extends JFrame {

	static int x = 0;

	static int y = 0;

	static int incremento;

	public static ArrayList<String> cards;

	public static ArrayList<String> movements;

	public static ArrayList<String> contenido;

	public static Robot robot;

	public static LinkedList<String> controles;

	private TextArea textField;

	private String archivo;

	private Timer t;

	private JCheckBoxCustom chckbxNewCheckBox;

	SimpleButton btnNewButton;

	TextAreaScroll textAreaScroll1;

	private void parar() {

		t.cancel();

		t = null;

	}

	private void iniciar() {

		t = new Timer();

		LeerTecla mTask = new LeerTecla();

		t.scheduleAtFixedRate(mTask, 0, 1000);

	}

	private void iconoStart(boolean start) {

		if (start) {

			btnNewButton.setBorderColor(new Color(136, 30, 30));

			btnNewButton.setIcon(new ImageIcon(AutoBattles.class.getResource("/image/stop.png")));

			btnNewButton.setText("Stop");

		}

		else {

			btnNewButton.setBorderColor(new Color(30, 136, 56));

			btnNewButton.setIcon(new ImageIcon(AutoBattles.class.getResource("/image/start.png")));

			btnNewButton.setText("Start");

		}

	}

	private void obtenerTexto(String string) {

		try {

			textField.setText("");

			contenido = (ArrayList<String>) leer(string, false);

			for (int i = 0; i < contenido.size(); i++) {

				if (contenido.get(i) != null && !contenido.get(i).isEmpty()) {

					textField.setText(textField.getText() + contenido.get(i) + "\n\n");

				}

			}

		}

		catch (Exception e) {

			e.printStackTrace();

		}

	}

	public static void escribir(String file, String text, boolean mode) {

		try {

			StandardOpenOption modo = StandardOpenOption.APPEND;

			if (mode) {

				modo = StandardOpenOption.TRUNCATE_EXISTING;

			}

			OutputStream os = Files.newOutputStream(Paths.get("./config/" + file), StandardOpenOption.CREATE,
					StandardOpenOption.WRITE, modo);

			PrintWriter writer = new PrintWriter(os);

			writer.print("\n" + text + "\n");

			writer.flush();

		}

		catch (Exception e) {

		}

	}

	public static void moveMouse(Robot r, int X, int Y) {

		r.mouseMove(X, Y);

	}

	public static void clickMouse(Robot r) {

		r.mousePress(16);

		r.mouseRelease(16);

	}

	public static void hacia(int posicionFicha) {

		if (posicionFicha == 1) {

			y = Integer.parseInt(movements.get(posicionFicha));

			posicionFicha--;

			x = Integer.parseInt(movements.get(posicionFicha));

		}

		else {

			incremento = posicionFicha;

			y = Integer.parseInt(movements.get(posicionFicha + (incremento - 1)));

			incremento = posicionFicha;

			x = Integer.parseInt(movements.get(incremento + (incremento - 2)));

		}

		escribir("Log.txt", "MOVE " + posicionFicha + " - " + x + " - " + y, false);

		robot.mouseMove(x, y);

		clickMouse(robot);

	}

	public static void carta(int origen) {

		try {

			if (origen == 1) {

				y = Integer.parseInt(cards.get(origen));

				x = Integer.parseInt(cards.get(--origen));

			}

			else {

				incremento = origen;

				y = Integer.parseInt(cards.get(origen + (incremento - 1)));

				incremento = origen;

				x = Integer.parseInt(cards.get(incremento + (incremento - 2)));

			}

			escribir("Log.txt", "FROM  " + x + " - " + y, false);

			moveMouse(robot, x, y);

			clickMouse(robot);

			Thread.sleep(300);

		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static List<String> leer(String file, boolean filtro) {

		ArrayList<String> list = new ArrayList<>();

		try {

			String texto = "";

			FileReader flE = null;

			BufferedReader fE = null;

			flE = new FileReader(file);

			fE = new BufferedReader(flE);

			texto = "";

			while (texto != null) {

				texto = fE.readLine();

				if (texto != null && !texto.isEmpty()) {

					if (filtro && texto.contains("-->")) {

						texto = texto.substring(texto.indexOf("-->") + 3, texto.length()).trim();

						if (Integer.parseInt(texto) >= 0) {

							list.add(texto);

						}

					}

					if (!filtro) {

						list.add(texto.trim());

					}

				}

			}

			fE.close();

			flE.close();

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	public AutoBattles() throws IOException, AWTException {
		setIconImage(Toolkit.getDefaultToolkit().getImage(AutoBattles.class.getResource("/image/file011099.png")));

		setAlwaysOnTop(true);

		setTitle("AutoWars");

		setType(Type.NORMAL);

		initComponents();

		this.setVisible(true);

	}

	public void initComponents() throws IOException, AWTException {

		textAreaScroll1 = new TextAreaScroll();

		btnNewButton = new SimpleButton("New button");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 16));

		chckbxNewCheckBox = new JCheckBoxCustom("Start on load", SwingConstants.LEFT);
		chckbxNewCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 16));

		chckbxNewCheckBox.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {

				if (chckbxNewCheckBox.isSelected()) {

					escribir("config.txt", "1", true);

				}

				else {

					escribir("config.txt", "0", true);

				}

			}

		});

		robot = new Robot();

		cards = (ArrayList<String>) leer(Paths.get("./config/cards.txt").toString(), true);

		movements = (ArrayList<String>) leer(Paths.get("./config/positions.txt").toString(), true);

		controles = new LinkedList<>();

		String texto = "";

		FileReader flE = null;

		BufferedReader fE = null;

		try {

			flE = new FileReader(Paths.get("./config/controls.txt").toString());

			fE = new BufferedReader(flE);

			texto = "1";

			while (texto != null) {

				texto = fE.readLine();

				if (!texto.trim().isEmpty() && texto.contains("-->")) {

					texto = texto.substring(texto.indexOf("-->") + 3, texto.length()).trim();

					controles.add(texto);

				}

			}

			fE.close();

			flE.close();

		}

		catch (Exception e) {
			//
		}

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

		setResizable(false);

		ComboBoxSuggestion comboBox = new ComboBoxSuggestion();
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 14));

		comboBox.addItem("Controls");

		comboBox.addItem("Cards");

		comboBox.addItem("Targets");

		comboBox.addItem("Log");

		comboBox.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {

				textAreaScroll1.setLabelText(comboBox.getSelectedItem().toString());

				switch (comboBox.getSelectedIndex()) {

				case 0:

					obtenerTexto(Paths.get("./config/controls.txt").toString());

					break;

				case 1:

					obtenerTexto(Paths.get("./config/cards.txt").toString());

					break;

				case 2:

					obtenerTexto(Paths.get("./config/positions.txt").toString());

					break;

				case 3:

					obtenerTexto(Paths.get("./config/Log.txt").toString());

					break;

				}

			}

		});

		textAreaScroll1.setLabelText("Controls");

		btnNewButton.setText("");
		btnNewButton.setColorClick(Color.WHITE);
		btnNewButton.setColorOver(Color.WHITE);

		btnNewButton.setForm(Form.RECTANGLE);

		btnNewButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (!(t instanceof Timer)) {

					iconoStart(true);

					iniciar();

				}

				else {
					iconoStart(false);

					parar();

				}

			}

		});

		SimpleButton btnNewButton_1 = new SimpleButton("Save");
		btnNewButton_1.setBorderColor(new Color(113, 156, 244));
		btnNewButton_1.setIcon(new ImageIcon(AutoBattles.class.getResource("/image/save.png")));
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnNewButton_1.setColorOver(Color.WHITE);
		btnNewButton_1.setColorClick(Color.WHITE);

		btnNewButton_1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (!textField.getText().trim().isEmpty()) {

					archivo = comboBox.getSelectedItem().toString().toLowerCase();

					if (archivo.equals("targets")) {

						archivo = "positions";

					}

					escribir(archivo + ".txt", textField.getText().trim(), true);

				}

			}

		});

		try {

			if (Integer.parseInt(leer(Paths.get("./config/config.txt").toString(), false).get(0)) == 1) {

				chckbxNewCheckBox.setSelected(true);

				iniciar();

				iconoStart(true);

			}

			else {

				iconoStart(false);

				chckbxNewCheckBox.setSelected(false);

			}

		}

		catch (Exception e) {

		}

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.TRAILING).addGroup(layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(comboBox, 0, 506, Short.MAX_VALUE)
						.addComponent(textAreaScroll1, GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE)
						.addGroup(layout.createSequentialGroup()
								.addComponent(chckbxNewCheckBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addGap(51)
								.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, 70, Short.MAX_VALUE).addComponent(
										btnNewButton_1, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap()));
		layout.setVerticalGroup(
				layout.createParallelGroup(Alignment.LEADING)
						.addGroup(
								layout.createSequentialGroup().addGap(15)
										.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 31,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addGroup(layout
												.createParallelGroup(Alignment.LEADING).addGroup(layout
														.createParallelGroup(Alignment.BASELINE)
														.addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 42,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(chckbxNewCheckBox, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
												.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 42,
														GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(textAreaScroll1, GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
										.addContainerGap()));

		textField = new TextArea();

		textAreaScroll1.setViewportView(textField);

		textField.setFont(new Font("Tahoma", Font.PLAIN, 16));

		textField.setColumns(10);

		getContentPane().setLayout(layout);

		setSize(new Dimension(532, 596));

		setLocationRelativeTo(null);

	}
}