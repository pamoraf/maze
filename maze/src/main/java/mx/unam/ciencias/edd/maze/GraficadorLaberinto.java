package mx.unam.ciencias.edd.maze;

import mx.unam.ciencias.edd.Conjunto;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.IteradorLista;

/**
 * <p>Clase que proporciona una representación gráfica de un laberinto junto con su solución.</p>
 * <p>Genera el laberinto y su solución con svg donde :
 * - El cuarto de origen contiene un circulo de color azul.
 * - El cuarto de destino contiene un circulo de color rojo.
 * - EL camino de la solución es verde.
 * - Las paredes de los cuartos son negras.</p>
 * <p>Por la implementación de la clase laberinto podemos asegurar que el propocionado siempre tiene solución</p>
 */
public class GraficadorLaberinto {
    // Guarda las figuras svg generadas, evita repeticiones.
    private Conjunto<String> dibujo;
    private Laberinto laberinto;
    private Lista<Cuarto> solucion;
    // Permite crear las etiquetas svg.
    private FigurasSVG figuras;
    private int ancho;
    private int alto;
    /**
     * Constructor que inicializa un GraficadorLaberinto con un laberinto dado.
     * @param laberinto El laberinto que se va a representar gráficamente.
     * @throws IllegalArgumentException Si el laberinto es nulo.
     */
    public GraficadorLaberinto(Laberinto laberinto) throws IllegalArgumentException {
        if(laberinto == null)
            throw new IllegalArgumentException("Proporciona una laberinto no nulo.");
        this.laberinto = laberinto;
        dibujo = new Conjunto<>();
        figuras = new FigurasSVG();
        ancho = laberinto.obtenerAncho() * 20 + 20;
        alto = laberinto.obtenerAlto() * 20 + 20;
        solucion = laberinto.obtenerSolucion();
    }

    /**
     * Regresa una cadena con una serie de etiquetas svg, que representan
     * Al laberinto junto con su solución.
     * @return El dibujo del laberinto.
     */
    public String dibuja() {
        // Permite obtener información acerca de las coordenadas de cada cuarto.
        Cuarto[][] cuartos = laberinto.obtenerCuartos();
        // Las coordenadas de los cuartos de origen y de destino.
        int origenX = 0, destinoX = 0;
        int origenY = 0, destinoY = 0;
        for (int y = 0; y < laberinto.obtenerAlto(); y++) {
            for (int x = 0; x < laberinto.obtenerAncho(); x++) { 
                Cuarto cuarto = cuartos[y][x];
                if(cuarto == laberinto.obtenerOrigen()) {
                    origenX = x;
                    origenY = y;
                }
                if(cuarto == laberinto.obtenerDestino()) {
                    destinoX = x;
                    destinoY = y;
                }
                dibujaCuarto(cuarto, x, y);
            }
        }
        // Dado el cuarto origen empieza a reconstruir la trayectoria de la solución.
        dibujaSolucion(origenX, origenY);
        // Si se usa el toString de la clase conjunto salen caractéres no deseados ,
        // además de que a veces la trayectoria de la solución se sobrepone a 
        // un círculo que representa uno de los cuartos salida.
        StringBuilder temp = new StringBuilder();
        for(String etiqueta : dibujo)
            temp.append(etiqueta);
        // Agrega círculos para indicar un cuarto salida.
        figuras.establecerColor("blue");
        figuras.establecerColorRelleno("blue");
        temp.append(figuras.circulo(origenX * 20 + 20, origenY * 20 + 20, 5));
        figuras.establecerColor("red");
        figuras.establecerColorRelleno("red");
        temp.append(figuras.circulo(destinoX * 20 + 20, destinoY * 20 + 20, 5));
        return temp.toString();
    }

    /*
     * Dibuja la solución del laberinto con líneas verdes, dadas
     * las coordenadas del cuarto de origen y haciendo uso de la lista
     * de cuartos solución propocionada por el laberinto.
     */
    private void dibujaSolucion(int x, int y) {
        // Permite que las líneas luzcan continuas.
        double ajuste = figuras.obtenerGrosor() / 2;
        IteradorLista<Cuarto> iterador = solucion.iteradorLista();
        // Los cuartos actual y siguiente se posicionan en el cuarto origen.
        Cuarto actual =  iterador.next();
        Cuarto siguiente = actual;
        Direccion direccionActual = Direccion.NINGUNA;
        Direccion direccionSiguiente = Direccion.NINGUNA;
        boolean centinela = true;
        figuras.establecerColor("green");
        while(centinela) {
            // Ajusta los índices del arreglo de dos dimensiones de cuartos.
            x += direccionActual.deltaX;
            y += direccionActual.deltaY;
            // Cada pared o puerta tiene longitud de 20
            // Se le suma 20 para que quede justo en medio de dos paredes del cuarto.
            double ajusteX = x * 20 + 20;
            double ajusteY = y * 20 + 20;
            // Posiciona los cuartos actual y siguiente conforme al orden de la lista de cuartos solución.
            if(iterador.hasNext()) {
                actual = siguiente;
                siguiente = iterador.next();
                // Obtiene la dirección del cuarto solución respecto al cuarto actual.
                direccionSiguiente = obtenerDireccionCuarto(siguiente, x, y);
                dibujo.agrega(figuras.linea(ajusteX - ajuste * direccionSiguiente.deltaX,
					    ajusteY - ajuste * direccionSiguiente.deltaY,
					    ajusteX + 10 * direccionSiguiente.deltaX,
					    ajusteY + 10 * direccionSiguiente.deltaY));
            } else 
                centinela = false;
            dibujo.agrega(figuras.linea(ajusteX, ajusteY, ajusteX - 10 * direccionActual.deltaX, ajusteY - 10 * direccionActual.deltaY));
            direccionActual = direccionSiguiente;
        }
    }
 
    /*
     * Dados la coordenadas de un cuarto y su cuarto adyacente regresa la dirección donde
     * se encuetra este.
     */
    private Direccion obtenerDireccionCuarto(Cuarto siguiente, int x, int y) {
        for(Direccion direccion : Direccion.obtenerCardinales()) {
            if(obtenerCuarto(x + direccion.deltaX, y + direccion.deltaY) == siguiente)
                return direccion;
        }
        return Direccion.NINGUNA;
    }
    
    /*
     * Agrega la representación del cuarto en svg al conjunto de cadenas,
     * Recibe un cuarto y las coordenadas de este.
     */
    private void dibujaCuarto(Cuarto cuarto, int x, int y) {
        // Cada pared o puerta tiene longitud de 20
        // Se le suma 10 para ajustar al margen.
        double ajusteX = x * 20 + 10;
        double ajusteY = y * 20 + 10;
        // Para que las líneas luzcan continuas.
        double ajuste = figuras.obtenerGrosor() / 2;
        figuras.establecerColor("black");
        for (Direccion direccion : Direccion.obtenerCardinales()) {
            // Si el cuarto no tiene la puerta en la dirección proporcionada dibuja una pared.
            if (!cuarto.obtenerPuertas().contiene(direccion)) {
                switch (direccion) {
                    case NORTE:
                            dibujo.agrega(figuras.linea(ajusteX - ajuste, ajusteY, ajusteX + 20 + ajuste, ajusteY));
                        break;
                    case SUR:
                            dibujo.agrega(figuras.linea(ajusteX - ajuste, ajusteY + 20, ajusteX + 20 + ajuste, ajusteY + 20));
                        break;
                    case ESTE:
                            dibujo.agrega(figuras.linea(ajusteX + 20, ajusteY - ajuste, ajusteX + 20, ajusteY + 20 + ajuste));
                        break;
                    case OESTE:
                            dibujo.agrega(figuras.linea(ajusteX, ajusteY - ajuste, ajusteX, ajusteY + 20 + ajuste));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /*
     * Permite obtener un cuarto de manera segura del arreglo, dados los dos índices 
     * correspondientes.
     */
    private Cuarto obtenerCuarto(int x, int y) {
        if(x >= 0 && x < laberinto.obtenerAncho() && y >= 0 && y < laberinto.obtenerAlto())
            return laberinto.obtenerCuartos()[y][x];
        return null;
    }

    /**
     * Obtiene el ancho del laberinto.
     * @return el ancho del laberinto.
     */
    public int obtenerAncho() {
        return ancho;
    }

    /**
     * Obtiene el alto del laberinto.
     * @return el alto del laberinto.
     */
    public int obtenerAlto() {
        return alto;
    }

}
