import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class PintaFunciones extends JPanel {

    // Variables para controlar el estado inicial de las graficas
    private Color colorSeno = Color.BLUE;
    private Color colorCoseno = Color.RED;
    private boolean dibujarSeno = true;
    private boolean dibujarCoseno = true;

    // Métodos setters para comunicar el Menú con el Panel
    public void setColorSeno(Color c) {
        this.colorSeno = c; repaint();
    }
    public void setColorCoseno(Color c) {
        this.colorCoseno = c; repaint();
    }
    public void setDibujarSeno(boolean b) {
        this.dibujarSeno = b; repaint();
    }
    public void setDibujarCoseno(boolean b) {
        this.dibujarCoseno = b; repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Añadimos el antialiasing para que se vea mejor
        RenderingHints hints = g2.getRenderingHints();
        hints.put(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        hints.put(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.addRenderingHints(hints);

        int ancho = getWidth();
        int alto = getHeight();

        // Aqui le damos los valores a Transform, siendo de 0 a 2PI en "x",
        // y en "y" de -1.5 a 1.5 para darle un pequeño margen
        Transform T = new Transform(0.0, -1.5, 2 * Math.PI, 1.5, 0, 0, ancho, alto);

        // Graficamos los ejes coordenados de negro
        g2.setColor(Color.BLACK);
        // Punto inicial de la linea
        Point p1 = T.project(0.0, 0.0);
        // Punto final de la linea
        Point p2 = T.project(2 * Math.PI, 0.0);
        // Pintamos la linea horizontal del eje "x"
        g2.drawLine(p1.x, p1.y, p2.x, p2.y);

        // Punto inicial de la linea
        Point p3 = T.project(0.0, -1.5);
        // Punto final de la linea
        Point p4 = T.project(0.0, 1.5);
        // Pintamos la linea vertical del eje "y"
        g2.drawLine(p3.x, p3.y, p4.x, p4.y);

        // Si el checkbox está marcado, pintamos el seno
        if (dibujarSeno) {
            g2.setColor(colorSeno);
            drawFunction(g2, T, "seno");
        }

        // Si el checkbox está marcado, pintamos el coseno
        if (dibujarCoseno) {
            g2.setColor(colorCoseno);
            drawFunction(g2, T, "coseno");
        }
    }

    private void drawFunction(Graphics2D g2, Transform T, String funcion) {
        double paso = 0.01;
        double x = 0.0;

        // Si es "seno", dibuja solo el seno y no toca al coseno
        if (funcion.equals("seno")) {

            double y = Math.sin(x);
            // Transforma el punto inicial a la proyección
            Point p1 = T.project(x, y);


            for (x = paso; x <= 2 * Math.PI; x += paso) {
                y = Math.sin(x);
                Point p2 = T.project(x, y);

                // Dibuja una linea entre el punto pasado y el nuevo
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                p1 = p2;
            }
        // Si es "coseno", dibuja al coseno e ignora al seno
        } else if (funcion.equals("coseno")) {

            double y = Math.cos(x);
            // Transforma el punto inicial a la proyección
            Point p1 = T.project(x, y);


            for (x = paso; x <= 2 * Math.PI; x += paso) {
                y = Math.cos(x);
                Point p2 = T.project(x, y);

                // Dibuja una linea entre el punto pasado y el nuevo
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                p1 = p2;
            }
        }
    }

    public void guarda()
    {
        File directorioActual = new File(System.getProperty("user.dir"));
        JFileChooser chooser = new JFileChooser(directorioActual);
        chooser.setDialogTitle("Guardar imagen");
        chooser.setSelectedFile(new File("graficas.png"));

        int selection = chooser.showSaveDialog(this);
        if (selection == JFileChooser.CANCEL_OPTION) {
            return;
        }
        File file = chooser.getSelectedFile();
        String nombre = file.getName().toLowerCase();
        if (!nombre.endsWith(".png")) {
            file = new File(file.getAbsolutePath() + ".png");
        }

        if (file.exists()) {
            int r = JOptionPane.showConfirmDialog(null,
                    "El archivo " + nombre + "ya existe.\n¿Desea sobreescribirlo?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (r == JOptionPane.NO_OPTION) {
                return;
            }
        }
        BufferedImage imagen = new BufferedImage(this.getWidth(),
                this.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imagen.createGraphics();
        this.paint(g);
        g.dispose();

        try {
            ImageIO.write(imagen, "png", file);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    // Estructura main solicitada
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Crear JFrame
            JFrame frame = new JFrame("Tarea 3");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            PintaFunciones panel = new PintaFunciones();
            frame.add(panel);

            //Creamos el JMenuBar
            JMenuBar menuBar = new JMenuBar();

            // Menú de opciones
            JMenu menuOpciones = new JMenu("Opciones");

            //Color del seno
            JMenuItem itemColorSeno = new JMenuItem("Color Seno");
            itemColorSeno.addActionListener(e -> {
                Color c = JColorChooser.showDialog(frame, "Seleccionar color Seno", panel.colorSeno);
                if (c != null) panel.setColorSeno(c);
            });

            //Color del coseno
            JMenuItem itemColorCoseno = new JMenuItem("Color Coseno");
            itemColorCoseno.addActionListener(e -> {
                Color c = JColorChooser.showDialog(frame, "Seleccionar color Coseno", panel.colorCoseno);
                if (c != null) panel.setColorCoseno(c);
            });

            // Checkbox para el seno
            JCheckBoxMenuItem checkSeno = new JCheckBoxMenuItem("Ver Seno", true);
            checkSeno.addActionListener(e -> panel.setDibujarSeno(checkSeno.isSelected()));

            // Checkbox para el coseno
            JCheckBoxMenuItem checkCoseno = new JCheckBoxMenuItem("Ver Coseno", true);
            checkCoseno.addActionListener(e -> panel.setDibujarCoseno(checkCoseno.isSelected()));

            //Guardar imagen
            JMenuItem itemGuardar = new JMenuItem("Guardar Imagen");
            itemGuardar.addActionListener(e -> panel.guarda());

            //Salir
            JMenuItem itemSalir = new JMenuItem("Salir");
            itemSalir.addActionListener(e -> System.exit(0));

            //Agregamos los items al menú de opciones
            menuOpciones.add(itemColorSeno);
            menuOpciones.add(itemColorCoseno);
            menuOpciones.addSeparator();
            menuOpciones.add(checkSeno);
            menuOpciones.add(checkCoseno);
            menuOpciones.addSeparator();
            menuOpciones.add(itemGuardar);
            menuOpciones.add(itemSalir);

            //Acerca de
            JMenu menuAyuda = new JMenu("Ayuda");
            JMenuItem itemAcerca = new JMenuItem("Acerca");
            itemAcerca.addActionListener(e -> {
                JOptionPane.showMessageDialog(frame,
                        "Estudiantes:\n- Paulina Bojórquez Trujillo \n- María Fernanda Hernández García \n- Mario Alberto Ocejo Quijada \n- Denzel Omar Rivera Urias",
                        "Acerca de", JOptionPane.INFORMATION_MESSAGE);
            });
            menuAyuda.add(itemAcerca);

            // Agregamos los menús a la barra
            menuBar.add(menuOpciones);
            menuBar.add(menuAyuda);

            // Colocamos la barra al frame
            frame.setJMenuBar(menuBar);

            frame.setVisible(true);
        });
    }
}