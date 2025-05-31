import analizador.parser;
import analizador.LexerSimple;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.io.StringReader;
import java_cup.runtime.Symbol;

public class KPL extends JFrame {
    public static java.util.Map<String, Integer> memoria = new java.util.HashMap<>(); //memoria compartida 

    private JTextArea area, salidaArea;

    public KPL() {
        setTitle("Kids Programming Language");
        setSize(800, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); //centrar ventana
        
        // Área de entrada
        area = new JTextArea();
        area.setFont(new Font("Comic Sans MS", Font.PLAIN, 16)); // Fuente amigable
        area.setBackground(new Color(255, 255, 204)); // Amarillo pastel
        area.setForeground(Color.DARK_GRAY);
        area.setEditable(true);
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
        // Panel de botones de operaciones
        JPanel panelBotonesOperaciones = new JPanel(new GridLayout(3, 4, 8, 8));
        panelBotonesOperaciones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de botones de ejecución/limpieza
        JPanel panelBotonesEjecucion = new JPanel(new GridLayout(1, 3, 8, 8));
        panelBotonesEjecucion.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
     
        JButton moverBtn = new JButton("Mover 10 pasos"); // Texto en los botones
        JButton decirBtn = new JButton("Decir 'Hola'");
        JButton repetirBtn = new JButton("Repetir 3 veces");
        JButton asignarBtn = new JButton("Asignar variable");
        JButton sumaBtn = new JButton("Suma");
        JButton restaBtn = new JButton("Resta");
        JButton multiplicacionBtn = new JButton("Multiplicación");
        JButton divisionBtn = new JButton("División");
        JButton ifIgualBtn = new JButton("If-Else-Igualdad");
        JButton ifDiferenteBtn = new JButton("If-Else-Diferente");
        JButton ifMenorBtn = new JButton("If-Else-Menor");
        JButton ifMayorBtn = new JButton("If-Else-Mayor");
        JButton ejecutarBtn = new JButton("Ejecutar");
        JButton limpiarEntradaBtn = new JButton("Limpiar Entrada");
        JButton limpiarSalidaBtn = new JButton("Limpiar Salida");

        
        //Agregar acciones a realizar
        moverBtn.addActionListener(e -> area.append("mover 10\n"));
        decirBtn.addActionListener(e -> area.append("decir \"Hola\"\n"));
        repetirBtn.addActionListener(e -> area.append("repetir 3 {\n  mover 5\n}\n"));
        asignarBtn.addActionListener(e -> {
            String linea = "x = 8";
            area.append(linea + "\n");
            ejecutarLinea(linea);
        });

        sumaBtn.addActionListener(e -> {
            String linea = "x = x + 1";
            area.append(linea + "\n");
            //ejecutarLinea(linea);
        });

        restaBtn.addActionListener(e -> {
            String linea = "x = x - 1";
            area.append(linea + "\n");
            //ejecutarLinea(linea);
        });
        
        multiplicacionBtn.addActionListener(e -> {
            String linea = "x = x * 2";
            area.append(linea + "\n");
            //ejecutarLinea(linea);
        });
        
        divisionBtn.addActionListener(e -> {
            String linea = "x = x / 2";
            area.append(linea + "\n");
            //ejecutarLinea(linea);
        });
        
        ifIgualBtn.addActionListener(e -> area.append(
            "if (x == 5) {\n" +
            "  decir \"X es Igual que 5 \"\n" +
            "} else {\n" +
            "  decir \"X no es Igual que 5\"\n" +
            "}\n"));
        
        ifDiferenteBtn.addActionListener(e -> area.append(
            "if (x != 5) {\n" +
            "  decir \"X es Diferente que 5 \"\n" +
            "} else {\n" +
            "  decir \"X no es Diferrente que 5\"\n" +
            "}\n"));
        
         ifMayorBtn.addActionListener(e -> area.append(
            "if (x > 5) {\n" +
            "  decir \"X es Mayor que 5 \"\n" +
            "} else {\n" +
            "  decir \"X no es Mayor que 5\"\n" +
            "}\n"));

        ifMenorBtn.addActionListener(e -> area.append(
            "if (x < 5) {\n" +
            "  decir \"X es Menor que 5 \"\n" +
            "} else {\n" +
            "  decir \"X no es Menor que 5\"\n" +
            "}\n"));
        
        ejecutarBtn.addActionListener(e -> ejecutarCodigo());
        limpiarEntradaBtn.addActionListener(e -> area.setText(""));
        limpiarSalidaBtn.addActionListener(e -> salidaArea.setText(""));

        
        //Se muestran los botones en el panel de Botones de la ventana
        panelBotonesOperaciones.add(moverBtn);
        panelBotonesOperaciones.add(decirBtn);
        panelBotonesOperaciones.add(repetirBtn);
        panelBotonesOperaciones.add(asignarBtn);
        panelBotonesOperaciones.add(sumaBtn);
        panelBotonesOperaciones.add(restaBtn);
        panelBotonesOperaciones.add(multiplicacionBtn);
        panelBotonesOperaciones.add(divisionBtn);
        panelBotonesOperaciones.add(ifIgualBtn);
        panelBotonesOperaciones.add(ifDiferenteBtn);
        panelBotonesOperaciones.add(ifMayorBtn);
        panelBotonesOperaciones.add(ifMenorBtn);
        
        // Sub-panel que contiene los tres botones
        JPanel panelExecute = new JPanel(new BorderLayout(10, 0));
        panelExecute.add(limpiarEntradaBtn, BorderLayout.WEST);
        panelExecute.add(ejecutarBtn, BorderLayout.CENTER);
        panelExecute.add(limpiarSalidaBtn, BorderLayout.EAST);

        // Añadir el sub-panel al panel de botones de ejecución
        panelBotonesEjecucion.add(panelExecute);

        // Panel contenedor que organiza los botones en vertical
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.add(panelBotonesOperaciones);
        panelBotones.add(panelBotonesEjecucion);

        // Añadir este panel combinado a la parte sur de la ventana
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
        
        asignarBtn.setFont(fuenteNiños);
        asignarBtn.setBackground(new Color(255, 140, 0)); // Naranja oscuro
        asignarBtn.setForeground(Color.WHITE);

        sumaBtn.setFont(fuenteNiños);
        sumaBtn.setBackground(new Color(30, 144, 255)); // Azul dodger
        sumaBtn.setForeground(Color.WHITE);

        restaBtn.setFont(fuenteNiños);
        restaBtn.setBackground(new Color(220, 20, 60)); // Rojo
        restaBtn.setForeground(Color.WHITE);
        
        multiplicacionBtn.setFont(fuenteNiños);
        multiplicacionBtn.setBackground(new Color(30, 144, 255)); // Azul dodger
        multiplicacionBtn.setForeground(Color.WHITE);

        divisionBtn.setFont(fuenteNiños);
        divisionBtn.setBackground(new Color(220, 20, 60)); // Rojo
        divisionBtn.setForeground(Color.WHITE);
        
        ifIgualBtn.setFont(fuenteNiños);
        ifIgualBtn.setBackground(new Color(123, 104, 238)); // Azul medio púrpura
        ifIgualBtn.setForeground(Color.WHITE);
        
        ifDiferenteBtn.setFont(fuenteNiños);
        ifDiferenteBtn.setBackground(new Color(123, 104, 238)); // Azul medio púrpura
        ifDiferenteBtn.setForeground(Color.WHITE);

        ifMayorBtn.setFont(fuenteNiños);
        ifMayorBtn.setBackground(new Color(123, 104, 238)); // Azul medio púrpura
        ifMayorBtn.setForeground(Color.WHITE);
        
        ifMenorBtn.setFont(fuenteNiños);
        ifMenorBtn.setBackground(new Color(123, 104, 238)); // Azul medio púrpura
        ifMenorBtn.setForeground(Color.WHITE);

        ejecutarBtn.setFont(fuenteNiños);
        ejecutarBtn.setBackground(new Color(255, 165, 0)); // Naranja Vibrante
        ejecutarBtn.setForeground(Color.WHITE);
        
        limpiarEntradaBtn.setFont(fuenteNiños);
        limpiarEntradaBtn.setBackground(new Color(255, 215, 0)); // Dorado
        limpiarEntradaBtn.setForeground(Color.BLACK);

        limpiarSalidaBtn.setFont(fuenteNiños);
        limpiarSalidaBtn.setBackground(new Color(176, 224, 230)); // Azul claro pastel
        limpiarSalidaBtn.setForeground(Color.BLACK);
        
        // Agregar iconos en los botones
        // BOTÓN DE EJECUTAR
        ImageIcon iconoEjecutar = new ImageIcon(getClass().getResource("/icons/ejecutar.png"));
        Image img = iconoEjecutar.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ejecutarBtn.setIcon(new ImageIcon(img));
        
    }
    
    //Evento de Ejecutar código
    private void ejecutarCodigo() {
        salidaArea.setText("");
        memoria.clear(); // Limpiar variables para cada ejecución

        String codigo = area.getText();

        new Thread(() -> {
            try {
                if (codigo == null || codigo.trim().isEmpty()) {
                    SwingUtilities.invokeLater(() -> 
                        salidaArea.append("Presiona algún botón con información.\n")
                    );
                    return;
                }

                Reader reader = new StringReader(codigo); // Fuente de entrada
                LexerSimple lexer = new LexerSimple(reader); // Lexer de JFlex
                parser p = new parser(lexer); // Parser de JavaCUP
                Symbol resultado = p.parse(); // Parseo del código

                // Ejecutar resultado si es un Runnable
                if (resultado.value instanceof Runnable runnable) {
                      SwingUtilities.invokeLater(runnable); // Ejecuta en el hilo de Swing
                      //runnable.run();
                }

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> 
                    salidaArea.append("Error: " + ex.getMessage() + "\n")
                );
            }
        }).start();

    }//ejecutarCódigo
    
    private void ejecutarLinea(String linea) {
    new Thread(() -> {
        try {
            Reader reader = new StringReader(linea);
            LexerSimple lexer = new LexerSimple(reader);
            parser p = new parser(lexer);
            Symbol resultado = p.parse();

            if (resultado.value instanceof Runnable runnable) {
                runnable.run();
                // Mostrar memoria si querés:
                //System.out.println("🧠 Estado actual:");
                memoria.forEach((k, v) -> System.out.println(k + " = " + v));
            }

        } catch (Exception ex) {
            SwingUtilities.invokeLater(() -> salidaArea.append("Error: " + ex.getMessage() + "\n"));
        }
    }).start();
}//ejecutarLinea


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
