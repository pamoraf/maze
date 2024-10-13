package mx.unam.ciencias.edd.maze;

/**
 * <p>Clase para dibujar figuras con svg.</p>
 */
public class FigurasSVG {
    // Figuras disponibles.
    private final String RECTANGULO = "<rect x='%.1f' y='%.1f' width='%.1f' height='%.1f' style=\"fill:%s; stroke:%s; stroke-width:%.1f;\" />\n";
    private final String POLIGONO = "<polygon points='%.1f, %.1f %.1f, %.1f  %.1f, %.1f'/>\n";
    private final String LINEA = "<line x1='%.1f' y1='%.1f' x2='%.1f' y2='%.1f' stroke='%s' stroke-width='%.1f' />\n";
    private final String CIRCULO = "<circle cx='%.1f' cy='%.1f' r='%.1f' stroke='%s' stroke-width='%.1f' fill='%s' />\n";
    private final String TEXTO = "<text fill='%s' font-family='%s' font-size='%.1f' x='%.1f' y='%.1f' text-anchor='%s'>%s</text>\n";
    private final static String ESTRCTURA_SVG = "<?xml version='1.0' encoding='UTF-8' ?>\n<svg width='%.1f' height='%.1f'>\n%s</svg>";
    private final static String DIBUJO = "<g>\n%s</g>";
    private String color;
    private String colorRelleno;
    private String fuente;
    private String anchoTexto;
    private double grosor;
    private double tamanioFuente;

    /**
     * Inicializa los atributos de las figuras.
     */
    public FigurasSVG() {
        this.color = "black";
        this.colorRelleno = "black";
        this.fuente = "sans-serif";
        this.anchoTexto = "middle";
        this.grosor = 5;
        this.tamanioFuente = 22;
    }

    /**
     * Regresa una etiqueta svg de un rectángulo.
     * @param x Coordenada x.
     * @param y Coordenada y.
     * @param ancho Ancho del rectángulo.
     * @param alto Alto del rectángulo.
     * @return Una etiqueta svg de un rectángulo.
     */
    public String rectangulo(double x, double y, double ancho, double alto) {
        return String.format(RECTANGULO, x, y, ancho, alto, colorRelleno, color, grosor);
    }

    /**
     * Regresa una etiqueta svg de una línea.
     * @param x1 Coordenada x inicial.
     * @param y1 Coordenada y inicial.
     * @param x2 Coordenada x final.
     * @param y2 Coordenada y final.
     * @return Una etiqueta svg de una línea.
     */
    public String linea(double x1, double y1, double x2, double y2) {
        return String.format(LINEA, x1, y1, x2, y2, color, grosor);
    }

    /**
     * Regresa una etiqueta svg de un círculo.
     * @param x Coordenada x del centro.
     * @param y Coordenada y del centro.
     * @param radio El radio del círculo.
     * @return Una etiqueta svg de un círculo.
     */
    public String circulo(double x, double y, double radio) {
        return String.format(CIRCULO, x, y, radio, color, grosor, colorRelleno);
    }

    /**
     * Regresa una etiqueta svg de un texto.
     * @param texto Texto que se muestra.
     * @param x Coordenada x de la esquina superior izquierda del cuadro de texto.
     * @param y Coordenada y de la esquina superior izquierda del cuadro de texto.
     * @return Una etiqueta svg de un texto.
     */
    public String texto(String texto, double x, double y) {
        return String.format(TEXTO, color, fuente, tamanioFuente, x, y, anchoTexto, texto);
    }

    /**
     * Regresa una serie de etiquetas svg que representan una flecha doble.
     * @param x Coordenada x inicial.
     * @param y Coordenada y inicial.
     * @param x1 Coordenada x final.
     * @param y1 Coordenada y final.
     * @return Una serie de etiquetas svg que representan una flecha doble.
     */
    public String flecha(double x, double y, double x1, double y1) {
        return triangulo(x, y, x + 2, y + 2, x + 2, y - 2) + linea(x + 2, y, x1 - 2, y1) + triangulo(x1, y1, x1 - 2, y1 + 2, x1 - 2, y1 - 2);
    }

    /**
     * Regresa una etiqueta svg que representa a un triángulo.
     * @param x Coordenada x de un vértice del triángulo.
     * @param y Coordenada y de un vértice del triángulo.
     * @param x1 Coordenada x de un vértice del triángulo.
     * @param y1 Coordenada y de un vértice del triángulo.
     * @param x2 Coordenada x de un vértice del triángulo.
     * @param y2 Coordenada y de un vértice del triángulo.
     * @return Una etiqueta svg que representa un triángulo.
     */
    public String triangulo(double x, double y, double x1, double y1, double x2, double y2) {
        return String.format(POLIGONO, x, y, x1, y1, x2, y2);
    }

    /**
     * Dadas una serie de etiquetas svg las regresa la etiqueta svg del dibujo.
     * @param dibujo Una serie de etiquetas svg.
     * @return La etiqueta svg del dibujo.
     */
    public static String dibujo(String dibujo) {
        return String.format(DIBUJO, dibujo);
    }

    /**
     * Regresa el formato básico para que se interprete un archivo svg.
     * @param ancho El ancho de la imagen.
     * @param alto El alto de la imagen.
     * @param elemento Secuencia de etiquetas svg.
     * @return El formato básico para que se interprete un archivo svg.
     */
    public static String estructuraSVG(double ancho, double alto, String elemento) {
        return String.format(ESTRCTURA_SVG, ancho, alto, elemento);
    }

    /**
     * Establece el color del objeto.
     * @param color El color a establecer.
     */
    public void establecerColor(String color) {
        this.color = color;
    }

    /**
     * Establece el color de relleno del objeto.
     * @param colorRelleno El color de relleno a establecer.
     */
    public void establecerColorRelleno(String colorRelleno) {
        this.colorRelleno = colorRelleno;
    }

    /**
     * Establece la fuente del texto del objeto.
     * @param fuente La fuente a establecer.
     */
    public void establecerFuente(String fuente) {
        this.fuente = fuente;
    }

    /**
     * Establece el ancho del texto del objeto.
     * @param anchoTexto El ancho del texto a establecer.
     */
    public void establecerAnchoTexto(String anchoTexto) {
        this.anchoTexto = anchoTexto;
    }

    /**
     * Establece el grosor del trazo del objeto.
     * @param grosor El grosor a establecer.
     */
    public void establecerGrosor(double grosor) {
        this.grosor = grosor;
    }

    /**
     * Establece el tamaño de la fuente del texto del objeto.
     * @param tamanioFuente El tamaño de la fuente a establecer.
     */
    public void establecerTamanioFuente(double tamanioFuente) {
        this.tamanioFuente = tamanioFuente; 
    }

    /**
     * Obtiene el color del objeto.
     * @return El color del objeto.
     */
    public String obtenerColor() {
        return color;
    }

    /**
     * Obtiene el color de relleno del objeto.
     * @return El color de relleno del objeto.
     */
    public String obtenerColorRelleno() {
        return colorRelleno;
    }

    /**
     * Obtiene la fuente del texto del objeto.
     * @return La fuente del texto del objeto.
     */
    public String obtenerFuente() {
        return fuente;
    }

    /**
     * Obtiene el ancho del texto del objeto.
     * @return El ancho del texto del objeto.
     */
    public String obtenerAnchoTexto() {
        return anchoTexto;
    }

    /**
     * Obtiene el grosor del trazo del objeto.
     * @return El grosor del trazo del objeto.
     */
    public double obtenerGrosor() {
        return grosor;
    }

    /**
     * Obtiene el tamaño de la fuente del texto del objeto.
     * @return El tamaño de la fuente del texto del objeto.
     */
    public double obtenerTamanioFuente() {
        return tamanioFuente;
    }


}
