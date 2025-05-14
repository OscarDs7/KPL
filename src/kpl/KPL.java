import analizador.parser;
import java.awt.*;
import java.io.*;
import java_cup.Lexer;
import java_cup.runtime.Symbol;
import javax.swing.*;
import java_cup.runtime.ComplexSymbolFactory;

public class KPL extends JFrame {

    private JTextArea area, salidaArea;

    public KPL() {
        setTitle("Lenguaje Visual Tipo Scratch");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Área de entrada
        area = new JTextArea();
        area.setFont(new Font("Consolas", Font.PLAIN, 14));
        area.setBackground(new Color(245, 245, 245));

        // Área de salida
        salidaArea = new JTextArea();
        salidaArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        salidaArea.setBackground(new Color(230, 255, 230));
        salidaArea.setEditable(false);

        // Panel dividido: entrada y salida
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(area), new JScrollPane(salidaArea));
        splitPane.setResizeWeight(0.6);
        add(splitPane, BorderLayout.CENTER);

        // Redirigir System.out a salidaArea
        PrintStream printStream = new PrintStream(new TextAreaOutputStream(salidaArea));
        System.setOut(printStream);
        System.setErr(printStream);

        // Botones
        JPanel panelBotones = new JPanel();
        JButton moverBtn = new JButton("Mover 10 pasos");
        JButton decirBtn = new JButton("Decir 'Hola'");
        JButton repetirBtn = new JButton("Repetir 3 veces");
        JButton ejecutarBtn = new JButton("Ejecutar");

        moverBtn.addActionListener(e -> area.append("mover 10\n"));
        decirBtn.addActionListener(e -> area.append("decir \"Hola\"\n"));
        repetirBtn.addActionListener(e -> area.append("repetir 3 {\n  mover 5\n}\n"));
        ejecutarBtn.addActionListener(e -> ejecutarCodigo());

        panelBotones.add(moverBtn);
        panelBotones.add(decirBtn);
        panelBotones.add(repetirBtn);
        panelBotones.add(ejecutarBtn);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void ejecutarCodigo() {
        salidaArea.setText(""); // Limpiar salida anterior
        String codigo = area.getText(); // Obtener el código desde JTextArea

        new Thread(() -> {
            try {
                Reader reader = new StringReader(codigo);
                ComplexSymbolFactory sf = new ComplexSymbolFactory(); // Crear el Factory
                Lexer lexer = new Lexer(reader, sf); // Pasar el factory al lexer
                parser p = new parser(lexer, sf);
                p.parse(); // Llamada al método parse() para iniciar el análisis

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    salidaArea.append("Error: " + ex.getMessage());
                });
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KPL().setVisible(true));
    }

    // Clase auxiliar para redirigir System.out a un JTextArea
    static class TextAreaOutputStream extends OutputStream {
        private final JTextArea textArea;

        public TextAreaOutputStream(JTextArea area) {
            this.textArea = area;
        }

        @Override
        public void write(int b) {
            SwingUtilities.invokeLater(() -> textArea.append(String.valueOf((char) b)));
        }

        @Override
        public void write(byte[] b, int off, int len) {
            String texto = new String(b, off, len);
            SwingUtilities.invokeLater(() -> textArea.append(texto));
        }
    }
}
