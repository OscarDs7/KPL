import analizador.parser;
import analizador.LexerSimple;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.io.StringReader;
import java_cup.runtime.Symbol;

public class KPL extends JFrame {

    private JTextArea area, salidaArea;

    public KPL() {
        setTitle("Kids Programming Language");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); //centrar ventana
        
        // Área de entrada
        area = new JTextArea();
        area.setFont(new Font("Comic Sans MS", Font.PLAIN, 16)); // Fuente amigable
        area.setBackground(new Color(255, 255, 204)); // Amarillo pastel
        area.setForeground(Color.DARK_GRAY);
        area.setEditable(false);
        area.setBorder(BorderFactory.createTitledBorder("Entrada del programa ✏️"));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        // Área de salida
        salidaArea = new JTextArea();
        salidaArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        salidaArea.setBackground(new Color(204, 255, 229)); // Verde pastel
        salidaArea.setForeground(new Color(0, 102, 51));
        salidaArea.setEditable(false);
        salidaArea.setBorder(BorderFactory.createTitledBorder("Salida del programa 🚀"));
        salidaArea.setLineWrap(true);
        salidaArea.setWrapStyleWord(true);
        
        //Mejor apariencia de la ventana
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
        new JScrollPane(area), new JScrollPane(salidaArea));
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerSize(8);
        splitPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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
        
        //Colores y diseño de los botones
        // Cambiar fuente y colores en los botones:
        Font fuenteNiños = new Font("Comic Sans MS", Font.BOLD, 16);
        
        moverBtn.setFont(fuenteNiños);
        moverBtn.setBackground(new Color(135, 206, 250)); // Azul cielo claro
        moverBtn.setForeground(Color.WHITE);
        moverBtn.setFocusPainted(false);
        //moverBtn.setIcon(new ImageIcon("icons/move.jpg")); // icono representativo
        
        decirBtn.setFont(fuenteNiños);
        decirBtn.setBackground(new Color(255, 182, 193)); // Rosa claro
        decirBtn.setForeground(Color.WHITE);
        //decirBtn.setIcon(new ImageIcon("iconos/speak.png"));

        repetirBtn.setFont(fuenteNiños);
        repetirBtn.setBackground(new Color(144, 238, 144)); // Verde claro
        repetirBtn.setForeground(Color.WHITE);
        //repetirBtn.setIcon(new ImageIcon("iconos/repeat.png"));

        ejecutarBtn.setFont(fuenteNiños);
        ejecutarBtn.setBackground(new Color(255, 165, 0)); // Naranja
        ejecutarBtn.setForeground(Color.WHITE);
        //ejecutarBtn.setIcon(new ImageIcon("iconos/run.png"));

    }
    //Evento de Ejecutar código
    private void ejecutarCodigo() {
        salidaArea.setText("");
        String codigo = area.getText();

        new Thread(() -> {
            try {
                Reader reader = new StringReader(codigo);
                LexerSimple lexer = new LexerSimple(reader);
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
