import analizador.parser;
//import analizador.Lexer;
import java.awt.*;
import java.io.*;
import java_cup.Lexer;
import java_cup.runtime.Symbol;
import javax.swing.*;

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

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(area), new JScrollPane(salidaArea));
        splitPane.setResizeWeight(0.6);
        add(splitPane, BorderLayout.CENTER);

        // Redirigir salida
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
        salidaArea.setText("");
        String codigo = area.getText();

        new Thread(() -> {
            try {
                Reader reader = new StringReader(codigo);
                Lexer lexer = new Lexer(reader);
                parser p = new parser(lexer);
                Symbol resultado = p.parse();

                if (resultado.value instanceof Runnable runnable) {
                    runnable.run();
                }

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

    // Clase auxiliar
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
