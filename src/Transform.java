import java.awt.*;

/**
 * Window to Viewport Transformation
 *
 * @author TESI
 */
public class Transform {
    public double xMin, xMax, yMin, yMax;  // Window limits
    public double uMin, uMax, vMin, vMax;  // Viewport limits

    /*
     * Constructor
     */
    public Transform(double x1, double y1, double x2, double y2,
                     double u1, double v1, double u2, double v2)
    {
        // Window
        xMin = x1;
        yMin = y1;
        xMax = x2;
        yMax = y2;

        // Viewport
        uMin = u1;
        vMin = v1;
        uMax = u2;
        vMax = v2;
    }

    /*
     * Project a point, p, from window coordinates to viewport coordinates
     */
    public Point project(double x, double y)
    {
        Point q;

        int u = (int) ((uMax - uMin) / (xMax - xMin) * (x - xMin) + uMin);
        int v = (int) (vMax - ((vMax - vMin) / (yMax - yMin)) * (y - yMin));

        q = new Point(u, v);

        return q;
    }
}