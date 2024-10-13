package mx.unam.ciencias.edd.maze;
/**
 * <p>Clase para realizar las operaciones del laberinto.</p>
 */
public class AdministradorLaberinto {
    /* No es deseable hacer objetos de esta clase. */
    private AdministradorLaberinto() {}

    /**
     * Genera un laberinto dada una configuración y la establece los datos del 
     * laberinto en la configuración.
     * @param configuracion De donde se obtienen y establecen los datos del laberinto.
     * @throws IllegalStateException Si el laberinto no es coherente. 
     */
    public static void genera(Configuracion configuracion) throws IllegalStateException {   
        long semilla = configuracion.obtenerSemilla();
        int ancho = configuracion.obtenerAncho();
        int alto = configuracion.obtenerAlto();
        Laberinto laberinto = new Laberinto(semilla, ancho, alto);
        Cuarto[][] cuartos = laberinto.obtenerCuartos();
        configuracion.establecerDatos(cuartos);
    }

    /**
     * Crea el código svg del laberinto con su solución.
     * @param laberinto Por dibujar.
     * @return Dibujo del laberinto en svg.
     */
    public static String dibujaSolucion(Laberinto laberinto) {
        GraficadorLaberinto dibujo = new GraficadorLaberinto(laberinto);
        return FigurasSVG.estructuraSVG(dibujo.obtenerAncho(), dibujo.obtenerAlto(), dibujo.dibuja());
    }
}
