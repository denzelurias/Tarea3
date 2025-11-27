import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class PintaFunciones extends JPanel {

    // Variables para controlar el estado del dibujo
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
    protected void paintComponent(Graphics g) {
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

        // 1. Graficar los ejes de coordenadas en color negro
        g2.setColor(Color.BLACK);
        // Eje X (de 0 a 2PI en y=0)
        Point p1 = T.project(0.0, 0.0);
        Point p2 = T.project(2 * Math.PI, 0.0);
        g2.drawLine(p1.x, p1.y, p2.x, p2.y);

        // Eje Y (de -1.5 a 1.5 en x=0)
        Point p3 = T.project(0.0, -1.5);
        Point p4 = T.project(0.0, 1.5);
        g2.drawLine(p3.x, p3.y, p4.x, p4.y);

        // 2. Graficar Seno si el checkbox está marcado
        if (dibujarSeno) {
            g2.setColor(colorSeno);
            drawFunction(g2, T, "sin");
        }

        // 3. Graficar Coseno si el checkbox está marcado
        if (dibujarCoseno) {
            g2.setColor(colorCoseno);
            drawFunction(g2, T, "cos");
        }
    }

    // Método auxiliar para dibujar la curva punto a punto
    private void drawFunction(Graphics2D g2, Transform T, String type) {
        double step = 0.01; // Paso para la suavidad de la curva
        double xOld = 0.0;
        double yOld = (type.equals("sin")) ? Math.sin(xOld) : Math.cos(xOld);
        Point pOld = T.project(xOld, yOld);

        for (double x = step; x <= 2 * Math.PI; x += step) {
            double y = (type.equals("sin")) ? Math.sin(x) : Math.cos(x);
            Point pNew = T.project(x, y);

            g2.drawLine(pOld.x, pOld.y, pNew.x, pNew.y);

            pOld = pNew;
        }
    }

    // Método para guardar la imagen
    public void guardarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                // Asegurar extensión .png
                if (!file.getName().toLowerCase().endsWith(".png")) {
                    file = new File(file.getAbsolutePath() + ".png");
                }

                BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = image.createGraphics();
                this.print(g2); // Pinta el panel en la imagen
                g2.dispose();
                ImageIO.write(image, "png", file);
                JOptionPane.showMessageDialog(this, "Imagen guardada exitosamente.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
            }
        }
    }

    // Estructura main solicitada
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Crear JFrame
            JFrame frame = new JFrame("Tarea 3: Graficador de Funciones");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            // Crear la instancia de nuestra clase PintaFunciones
            PintaFunciones panel = new PintaFunciones();
            frame.add(panel);

            // --- Creación del JMenuBar ---
            JMenuBar menuBar = new JMenuBar();

            // 1. Menú Opciones
            JMenu menuOpciones = new JMenu("Opciones");

            // Ítem: Color Seno
            JMenuItem itemColorSeno = new JMenuItem("Color Seno");
            itemColorSeno.addActionListener(e -> {
                // JColorChooser
                Color c = JColorChooser.showDialog(frame, "Seleccionar color Seno", panel.colorSeno);
                if (c != null) panel.setColorSeno(c);
            });

            // Ítem: Color Coseno
            JMenuItem itemColorCoseno = new JMenuItem("Color Coseno");
            itemColorCoseno.addActionListener(e -> {
                Color c = JColorChooser.showDialog(frame, "Seleccionar color Coseno", panel.colorCoseno);
                if (c != null) panel.setColorCoseno(c);
            });

            // Checkbox: Gráfica del Seno
            JCheckBoxMenuItem checkSeno = new JCheckBoxMenuItem("Ver Seno", true);
            checkSeno.addActionListener(e -> panel.setDibujarSeno(checkSeno.isSelected()));

            // Checkbox: Gráfica del Coseno
            JCheckBoxMenuItem checkCoseno = new JCheckBoxMenuItem("Ver Coseno", true);
            checkCoseno.addActionListener(e -> panel.setDibujarCoseno(checkCoseno.isSelected()));

            // Ítem: Guardar Imagen
            JMenuItem itemGuardar = new JMenuItem("Guardar Imagen");
            itemGuardar.addActionListener(e -> panel.guardarImagen());

            // Ítem: Salir
            JMenuItem itemSalir = new JMenuItem("Salir");
            itemSalir.addActionListener(e -> System.exit(0));

            menuOpciones.add(itemColorSeno);
            menuOpciones.add(itemColorCoseno);
            menuOpciones.addSeparator();
            menuOpciones.add(checkSeno);
            menuOpciones.add(checkCoseno);
            menuOpciones.addSeparator();
            menuOpciones.add(itemGuardar);
            menuOpciones.add(itemSalir);

            // 2. Menú Ayuda
            JMenu menuAyuda = new JMenu("Ayuda");
            JMenuItem itemAcerca = new JMenuItem("Acerca");
            itemAcerca.addActionListener(e -> {
                JOptionPane.showMessageDialog(frame,
                        "Estudiantes:\n- Paulina Bojórquez Trujillo \n- María Fernanda Hernández García \n- Mario Alberto Ocejo Quijada \n- Denzel Omar Rivera Urias",
                        "Acerca", JOptionPane.INFORMATION_MESSAGE);
            });
            menuAyuda.add(itemAcerca);

            // Agregar menús a la barra
            menuBar.add(menuOpciones);
            menuBar.add(menuAyuda);

            // Setear la barra al frame
            frame.setJMenuBar(menuBar);

            frame.setVisible(true);
        });
    }
}