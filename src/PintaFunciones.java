import javax.swing.*;
import java.awt.*;

public class PintaFunciones extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Convertimos a Graphics2D para mejores opciones de renderizado
        Graphics2D g2d = (Graphics2D) g;

        // Suaviza los bordes (Antialiasing) para que el punto se vea redondo y no pixelado
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Obtener dimensiones de la ventana actual
        int ancho = getWidth();
        int alto = getHeight();

        // 2. Calcular el centro de la ventana (nuestro Origen Visual)
        int centroX = ancho / 2;
        int centroY = alto / 2;

        // --- Dibujar los Ejes (Opcional, para referencia) ---
        g2d.setColor(Color.LIGHT_GRAY);
        // Eje X (Horizontal)
        g2d.drawLine(0, centroY, ancho, centroY);
        // Eje Y (Vertical)
        g2d.drawLine(centroX, 0, centroX, alto);

        // --- Dibujar el Punto en el Origen ---
        g2d.setColor(Color.RED);
        int diametroPunto = 10; // Tamaño del punto en píxeles

        // IMPORTANTE: Para que el punto esté matemáticamente centrado,
        // restamos la mitad de su tamaño a las coordenadas.
        // fillOval(x, y, ancho, alto)
        g2d.fillOval(centroX - (diametroPunto / 2),
                centroY - (diametroPunto / 2),
                diametroPunto,
                diametroPunto);

        // Dibujar etiqueta de texto
        g2d.setColor(Color.BLACK);
        g2d.drawString("Origen (0,0)", centroX + 10, centroY - 10);
    }

    public static void main(String[] args) {
        // Crear el marco de la ventana (JFrame)
        JFrame ventana = new JFrame("Gráfica de Punto en el Origen");

        // Configuración básica
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(500, 500);
        ventana.setLocationRelativeTo(null); // Centrar ventana en pantalla

        // Agregar nuestro panel de dibujo
        ventana.add(new PintaFunciones());

        // Mostrar
        ventana.setVisible(true);
    }
}