package vista.util;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/** Imágenes generadas en código, para no depender de archivos externos. */
public final class Recursos {

    private Recursos() {
    }

    public static Image logo() {
        Canvas lienzo = new Canvas(160, 110);
        GraphicsContext g = lienzo.getGraphicsContext2D();
        g.clearRect(0, 0, 160, 110);
        g.setFill(Color.WHITE);
        g.setFont(Font.font("Segoe UI Bold", 58));
        g.setTextAlign(TextAlignment.CENTER);
        g.fillText("U", 45, 76);
        g.fillText("S", 115, 76);
        g.setFill(Color.web("#93C5FD"));
        g.fillRect(45, 88, 70, 3);
        return instantanea(lienzo);
    }

    public static Image producto(String categoria) {
        Canvas lienzo = new Canvas(225, 120);
        GraphicsContext g = lienzo.getGraphicsContext2D();
        g.setFill(colorCategoria(categoria));
        g.fillRoundRect(0, 0, 225, 120, 16, 16);
        g.setFill(Color.rgb(255, 255, 255, 0.20));
        g.fillRoundRect(12, 12, 201, 96, 12, 12);
        g.setTextAlign(TextAlignment.CENTER);
        g.setFill(Color.WHITE);
        g.setFont(Font.font("Segoe UI Bold", 26));
        g.fillText(etiqueta(categoria), 112, 64);
        g.setFont(Font.font("Segoe UI", 12));
        g.fillText(categoria, 112, 88);
        return instantanea(lienzo);
    }

    /** El fondo transparente evita que el ImageView muestre un rectángulo blanco. */
    private static Image instantanea(Canvas lienzo) {
        SnapshotParameters parametros = new SnapshotParameters();
        parametros.setFill(Color.TRANSPARENT);
        return lienzo.snapshot(parametros, null);
    }

    private static Color colorCategoria(String categoria) {
        if (categoria == null) {
            return Color.web("#64748B");
        }
        switch (categoria) {
            case "Tecnología": return Color.web("#2563EB");
            case "Hogar": return Color.web("#D97706");
            case "Accesorios": return Color.web("#7C3AED");
            case "Deportes": return Color.web("#059669");
            default: return Color.web("#64748B");
        }
    }

    private static String etiqueta(String categoria) {
        if (categoria == null) {
            return "GEN";
        }
        switch (categoria) {
            case "Tecnología": return "TEC";
            case "Hogar": return "HOG";
            case "Accesorios": return "ACC";
            case "Deportes": return "DEP";
            default: return "GEN";
        }
    }
}
