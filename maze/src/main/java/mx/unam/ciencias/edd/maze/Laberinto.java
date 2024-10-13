package mx.unam.ciencias.edd.maze;

import java.util.Random;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.Pila;
import mx.unam.ciencias.edd.Grafica;
import mx.unam.ciencias.edd.VerticeGrafica;

/**
 * <p>Clase que proporciona una representación gráfica de un laberinto junto con su solución.</p>
 * <p>Genera el laberinto y su solución con svg donde :
 * - El cuarto de origen contiene un circulo de color azul.
 * - El cuarto de destino contiene un circulo de color rojo.
 * - EL camino de la solución es verde.
 * - Las paredes de los cuartos son negras.</p>
 * <p>Por la implementación de la clase laberinto podemos asegurar que el propocionado siempre tiene solución</p>
 */
public class Laberinto {
    private Random generador;
    private Grafica<Cuarto> laberinto;
    private Lista<Cuarto> solucion;
    private Cuarto[][] cuartos;
    private Cuarto origen;
    private Cuarto destino;
    private final int ANCHO;
    private final int ALTO;

    /**
     * Constructor para inicializar un laberinto dado un arrglo de bytes.
     * Verifica que el laberinto sea válido de acuerdo con los requerimientos
     * del proyecto.
     * @param datos arreglo de bytes de dos dimensiones de donde se extrae la información de los cuartos.
     * @throws IllegalStateException Si el laberinto no es válido.
     */
    public Laberinto(byte[][] datos) throws IllegalStateException {
        ANCHO = datos[0].length;
        ALTO = datos.length;
        cuartos = new Cuarto[ALTO][ANCHO];
        laberinto = new Grafica<>();
        solucion = new Lista<>();
        validaLaberinto(datos);
        obtenerSolucion();
        if(solucion.esVacia())
            throw new IllegalStateException("El laberinto no tiene solución.");
    }

    /**
     * Constructor que permite crear un laberinto de forma aleatoria.
     * @param semilla Semilla del RNG.
     * @param ancho Ancho del laberinto.
     * @param alto Alto del laberinto.
     */
    public Laberinto(long semilla, int ancho, int alto) {
        ANCHO = ancho;
        ALTO = alto;
        cuartos = new Cuarto[ALTO][ANCHO];
        laberinto = new Grafica<>();
        generador = new Random(semilla);
        int x = generador.nextInt(ancho);
        int y = generador.nextInt(alto);
        int puntaje = generador.nextInt(15);
        cuartos[y][x] = new Cuarto();
        cuartos[y][x].establecerPuntaje((byte) puntaje);
        generaLaberinto(x, y);
        origen = obtenerSalida();
        destino = obtenerSalida();
    }

    /*
     * Regresa un cuarto salida aleatorio del laberinto, un cuarto salida es un cuarto que tiene un puerta que 
     * no conecta con otro cuarto.
     */
    private Cuarto obtenerSalida() {
        boolean centinela = true;
        Cuarto salida = null;
        // Recorre los bordes del laberinto de forma aleatoria para establecer un cuarto de salida.
        if(generador.nextBoolean()) {
            int limiteY = generador.nextBoolean() ? 0 : ALTO - 1;
            while(centinela) {
                for(int x = 0; x < ANCHO; x++) {
                    if(generador.nextInt(ANCHO) == 1 && !esSalida(cuartos[limiteY][x])) {
                        salida = cuartos[limiteY][x];
                        salida.establecerPuerta(limiteY == 0 ? Direccion.NORTE : Direccion.SUR);
                        centinela = false;
                        break;
                    }
                }
            }
        } else {
            int limiteX = generador.nextBoolean() ? 0 : ANCHO - 1;
            while(centinela) {
                for(int y = 0; y < ALTO; y++) {
                    if(generador.nextInt(ALTO) == 1 && !esSalida(cuartos[y][limiteX])) {
                        salida = cuartos[y][limiteX];
                        salida.establecerPuerta(limiteX == 0 ? Direccion.OESTE : Direccion.ESTE);
                        centinela = false;
                        break;
                    }
                }
            }
        }
        return salida;
    }

    /*
     * Verifica si un cuarto es una salida.
     */
    private boolean esSalida(Cuarto cuarto) {
        return cuarto == destino || cuarto == origen;
    }

    /*
     * Genera el laberinto utilizando el recorrido dfs-random.
     * Empieza a generar apartir del cuarto origen.
     */
    private void generaLaberinto(int origenX, int origenY) {
        Pila<int[]> pila = new Pila<>();
        int[] origen = {origenX, origenY};
        pila.mete(origen);
        while (!pila.esVacia()) {
            int[] actuales = pila.mira();
            Direccion direccion = obtenerCuartoVacio(actuales[0], actuales[1]);
            // Si no hay cuarto vacío se empieza a retroceder en la trayectoria.
            if (direccion == Direccion.NINGUNA)
                pila.saca();
            else {
                // Crea la conexión de puertas con su cuarto adyacente.
                Cuarto actual = cuartos[actuales[1]][actuales[0]];
                actual.establecerPuerta(direccion);
                // Genera un puntaje aleatoria del 1 al 15.
                int puntaje = generador.nextInt(15) + 1;
                // Inicizliza el cuarto adyacente y crea la conexión de puertas.
                int adyacenteX = actuales[0] + direccion.deltaX;
                int adyacenteY = actuales[1] + direccion.deltaY;
                int[] adyacentes = {adyacenteX, adyacenteY};
                Cuarto adyacente = cuartos[adyacenteY][adyacenteX] = new Cuarto();
                adyacente.establecerPuntaje((byte) puntaje);
                adyacente.establecerPuerta(direccion.opuesta());
                // Agrega los elementos a la gráfica.
                if(!laberinto.contiene(actual))
                    laberinto.agrega(actual);
                if(!laberinto.contiene(adyacente))
                    laberinto.agrega(adyacente);
                if(!laberinto.sonVecinos(actual, adyacente)) {
                    int peso = actual.obtenerPuntaje() + adyacente.obtenerPuntaje() + 1;
                    laberinto.conecta(actual, adyacente, peso);
                }
                // Continua el recorrido.
                pila.mete(adyacentes);
            }
        }
    }

    /*
     * Obtiene la dirección de un cuarto nulo el cual es adyacente al cuarto
     * de la coordenadas dadas x , y.
     */
    private Direccion obtenerCuartoVacio(int x, int y) {
        Lista<Direccion> direcciones = new Lista<>();
        // Verifica los cuatro cuartos adyacentes posibles.
        for(Direccion direccion : Direccion.obtenerCardinales()) {
            int adyacenteX = x + direccion.deltaX;
            int adyacenteY = y + direccion.deltaY;
            // Comprueba que las coordenadas esten dentro de los límites del laberinto.
            if(adyacenteX >= 0 && adyacenteX < ANCHO && adyacenteY >= 0 && adyacenteY < ALTO) {
                // Se agrega la dirección si el cuarto es nulo.
                if(cuartos[adyacenteY][adyacenteX] == null)
                    direcciones.agrega(direccion);
            }
        }
        if(direcciones.esVacia())
            return Direccion.NINGUNA;
        // Regresa la dirección de un cuarto nulo de manera aleatoria.
        return direcciones.get(generador.nextInt(direcciones.getElementos()));
    }

    /*
     * Verifica que el laberinto sea válido de acuerdo con los criterios del proyecto:
     * - Solo dos cuartos salida (origen y destino).
     * - Cuartos coherentes.
     */
    private void validaLaberinto(byte[][] datos) {
        for(int y = 0; y < ALTO; y++) {
            for(int x = 0; x < ANCHO; x++) {
                Cuarto actual = obtenerCuarto(datos, x, y);
                // Revisa todos los cuartos adyacentes al cuarto actual.
                for(Direccion direccion : Direccion.obtenerCardinales()) {
                    Cuarto adyacente = obtenerCuarto(datos, x + direccion.deltaX, y + direccion.deltaY);
                    // Si el cuarto actual esta en el borde del laberinto si adyacente es nulo
                    // esta es una de las características de los cuartos salida.
                    if(adyacente == null) {
                        // Verifica si tiene una puerta que no conecta con otro cuarto. 
                        // Es posible que un cuarto salida tenga dos puertas que no contecten con
                        // dos cuartos.
                        if(!esSalida(actual) && actual.obtenerPuertas().contiene(direccion)) {
                            // Establece el cuarto salida.
                            if(origen == null)
                                origen = actual;
                            else if(destino == null)
                                destino = actual;
                            else 
                                throw new IllegalStateException("Hay más de dos cuartos salida.");
                        }
                        continue;
                    }
                    // Calcula el peso de la arista.
                    int peso = actual.obtenerPuntaje() + adyacente.obtenerPuntaje() + 1;
                    if(!laberinto.contiene(adyacente))
                        laberinto.agrega(adyacente);
                    if(!laberinto.contiene(actual))
                        laberinto.agrega(actual);
                    boolean contienenPuerta = actual.obtenerPuertas().contiene(direccion)
			&& adyacente.obtenerPuertas().contiene(direccion.opuesta());
                    boolean noContienenPuerta = !actual.obtenerPuertas().contiene(direccion)
			&& !adyacente.obtenerPuertas().contiene(direccion.opuesta());
                    if(!laberinto.sonVecinos(adyacente, actual) && contienenPuerta)
                        laberinto.conecta(actual, adyacente, peso);
                    // Si los cuartos tiene puertas que llevan a una pared lanza una excepción.
                    if(!contienenPuerta && !noContienenPuerta)
                        throw new IllegalStateException("Los cuartos del laberinto no son coherentes.");
                }

            }
        }
    }

    /*
     * Permite acceder a los cuartos del laberinto de forma segura, dado un arreglo de bytes
     * llamado datos si en el arreglo los indices x, y llevan a un cuarto nulo se inicializa
     * un nuevo cuarto dado el byte en el arreglo de datos con los índices mencionados.
     */
    private Cuarto obtenerCuarto(byte[][] datos ,int x, int y) {
        Cuarto cuarto = null;
        if(x >= 0 && x < ANCHO && y >= 0 && y < ALTO) {
            if(cuartos[y][x] == null)
                cuartos[y][x] = new Cuarto(datos[y][x]);
            cuarto = cuartos[y][x];
        }
        return cuarto;
    }

    /**
     * Obtiene la matriz de cuartos del laberinto.
     * @return La matriz de cuartos del laberinto.
     */
    public Cuarto[][] obtenerCuartos() {
        return cuartos;
    }

    /**
     * Obtiene el cuarto de origen del laberinto.
     * @return El cuarto de origen del laberinto.
     */
    public Cuarto obtenerOrigen() {
        return origen;
    }

    /**
     * Obtiene el cuarto de destino del laberinto.
     * @return El cuarto de destino del laberinto.
     */
    public Cuarto obtenerDestino() {
        return destino;
    }

    /**
     * Obtiene el ancho del laberinto.
     * @return El ancho del laberinto.
     */
    public int obtenerAncho() {
        return ANCHO;
    }

    /**
     * Obtiene el alto del laberinto.
     * @return El alto del laberinto.
     */
    public int obtenerAlto() {
        return ALTO;
    }

    /**
     * Obtiene la solución del laberinto como una lista de cuartos.
     * @return Una lista de cuartos que representan la solución del laberinto.
     */
    public Lista<Cuarto> obtenerSolucion() {
        if (solucion.esVacia()) {
            for (VerticeGrafica<Cuarto> cuarto : laberinto.dijkstra(origen, destino))
                solucion.agrega(cuarto.get());
        }
        return solucion;
    }
}
