package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            // Aquí va su código.
            iterador = vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            // Aquí va su código.
            return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            // Aquí va su código.
            return iterador.next().get();
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T>, Comparable<Vertice> {

        /* El elemento del vértice. */
        private T elemento;
        /* La distancia del vértice. */
        private double distancia;
        /* El diccionario de vecinos del vértice. */
        private Diccionario<T, Vecino> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            // Aquí va su código.
            this.elemento = elemento;
            vecinos = new Diccionario<>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            // Aquí va su código.
            return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            // Aquí va su código.
            return vecinos.getElementos();
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            // Aquí va su código.
            return vecinos;
        }

        /* Compara dos vértices por distancia. */
        @Override public int compareTo(Vertice vertice) {
            // Aquí va su código.
            if(vertice.distancia == Double.POSITIVE_INFINITY)
                return -1;
            double resta = distancia - vertice.distancia;
            if(resta < 0)
                return -1;
            if(resta > 0)
                return 1;
            return 0;
        }
    }

    /* Clase interna privada para vértices vecinos. */
    private class Vecino implements VerticeGrafica<T> {

        /* El vértice vecino. */
        public Vertice vecino;
        /* El peso de la arista conectando al vértice con su vértice vecino. */
        public double peso;

        /* Construye un nuevo vecino con el vértice recibido como vecino y el
         * peso especificado. */
        public Vecino(Vertice vecino, double peso) {
            // Aquí va su código.
            this.vecino = vecino;
            this.peso = peso;
        }

        /* Regresa el elemento del vecino. */
        @Override public T get() {
            // Aquí va su código.
            return vecino.elemento;
        }

        /* Regresa el grado del vecino. */
        @Override public int getGrado() {
            // Aquí va su código.
            return vecino.getGrado();
        }

        /* Regresa un iterable para los vecinos del vecino. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            // Aquí va su código.
            return vecino.vecinos();
        }
    }

    /* Interface para poder usar lambdas al buscar el elemento que sigue al
     * reconstruir un camino. */
    @FunctionalInterface
    private interface BuscadorCamino<T> {
        /* Regresa true si el vértice se sigue del vecino. */
        public boolean seSiguen(Grafica<T>.Vertice v, Grafica<T>.Vecino a);
    }

    /* Vértices. */
    private Diccionario<T, Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        // Aquí va su código.
        vertices = new Diccionario<>();
    }

    // Métodos privados que usé:
    private Lista<Vertice> getArista(T a, T b) {
        Lista<Vertice> arista  = new Lista<Vertice>();
        Vertice verticeA = (Vertice) vertice(a);
        Vertice verticeB = (Vertice) vertice(b);
        arista.agrega(verticeA);
        if(!verticeA.vecinos.contiene(verticeB.elemento))
            arista.agrega(new Vertice(null));
        arista.agrega(verticeB);
        return arista;
    }
    
    private int recorrido(T elemento, MeteSaca<Vertice>  meteSaca, AccionVerticeGrafica<T> accion) {
        Conjunto<Vertice> visitados = new Conjunto<>();
        Vertice pivote = (Vertice) vertice(elemento);
        meteSaca.mete(pivote);
        visitados.agrega(pivote);
        while(!meteSaca.esVacia()) {
            Vertice actual = meteSaca.saca();
            accion.actua(actual);
            for(Vecino v : actual.vecinos) {
                if(!visitados.contiene(v.vecino)) {
                    visitados.agrega(v.vecino);
                    meteSaca.mete(v.vecino);
                }
            }
        }
        return visitados.getElementos();
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        // Aquí va su código.
        return vertices.getElementos();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        // Aquí va su código.
        return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        if(elemento == null)
            throw new IllegalArgumentException("El elemento es nulo.");
        if(contiene(elemento))
            throw new IllegalArgumentException("El elemento ya está en la gráfica.");
        vertices.agrega(elemento, new Vertice(elemento));
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        // Aquí va su código.
        conecta(a, b, 1);
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva vecino.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, si a es
     *         igual a b, o si el peso es no positivo.
     */
    public void conecta(T a, T b, double peso) {
        // Aquí va su código.
        Lista<Vertice> arista = getArista(a, b);
        if(a.equals(b))
            throw new IllegalArgumentException("Los elementos son iguales.");
        if(arista.getLongitud() == 2)
            throw new IllegalArgumentException("Los elementos ya están conectados.");
        if(peso <= 0)
            throw new IllegalArgumentException("El peso debe ser positivo.");
        Vertice verticeA = arista.getPrimero();
        Vertice verticeB = arista.getUltimo();
        verticeA.vecinos.agrega(b, new Vecino(verticeB, peso));
        verticeB.vecinos.agrega(a, new Vecino(verticeA, peso));
        aristas++;
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        // Aquí va su código.
        Lista<Vertice> arista = getArista(a, b);
        if(arista.getElementos() != 2)
            throw new IllegalArgumentException("Los elementos no estan conectados.");
        arista.getPrimero().vecinos.elimina(b);
        arista.getUltimo().vecinos.elimina(a);
        aristas--;
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <code>true</code> si el elemento está contenido en la gráfica,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        // Aquí va su código.
        return vertices.contiene(elemento);
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        Vertice porBorrar = (Vertice) vertice(elemento);
        vertices.elimina(elemento);
        for(Vecino v : porBorrar.vecinos) {
            v.vecino.vecinos.elimina(elemento);
            aristas--;
        }
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <code>true</code> si a y b son vecinos, <code>false</code> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        // Aquí va su código.
        Lista<Vertice> arista = getArista(a, b);
        return arista.getLongitud() == 2;
    }

    /**
     * Regresa el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que contienen a
     *         los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public double getPeso(T a, T b) {
        // Aquí va su código.
        Lista<Vertice> arista = getArista(a, b);
        if(arista.getLongitud() != 2)
            throw new IllegalArgumentException("Los elementos no estan conectados.");
        return arista.getPrimero().vecinos.get(b).peso;
    }

    /**
     * Define el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @param peso el nuevo peso de la arista que comparten los vértices que
     *        contienen a los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados, o si peso
     *         es menor o igual que cero.
     */
    public void setPeso(T a, T b, double peso) {
        // Aquí va su código.
        Lista<Vertice> arista = getArista(a, b);
        if(arista.getLongitud() != 2)
            throw new IllegalArgumentException("Los elementos no estan conectados.");
        if(peso <= 0)
            throw new IllegalArgumentException("El peso debe ser positivo.");
        Vecino extremoA = arista.getUltimo().vecinos.get(a);
        Vecino extremoB = arista.getPrimero().vecinos.get(b);
        extremoA.peso = peso;
        extremoB.peso = peso;
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        // Aquí va su código.
        return vertices.get(elemento);
    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        // Aquí va su código.
        return vertices.esVacia() || recorrido(vertices.iteradorLlaves().next(), new Pila<>(), (u) -> {}) == vertices.getElementos();
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        // Aquí va su código.
        for(Vertice vertice : vertices)
            accion.actua(vertice);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        // Aquí va su código.
        recorrido(elemento, new Cola<>(), accion);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        // Aquí va su código.
        recorrido(elemento, new Pila<>(), accion);
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
        return vertices.esVacia();
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
        // Aquí va su código.
        aristas = 0;
        vertices.limpia();
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {
        // Aquí va su código.
        String conjuntoVertices = "";
        String conjuntoAristas = "";
        Conjunto<Vertice> visitados = new Conjunto<>();
        for(Vertice vertice : vertices) { 
            visitados.agrega(vertice);
            conjuntoVertices += String.format("%s, ", vertice.elemento);
            for(Vecino vecino : vertice.vecinos) {
                if(!visitados.contiene(vecino.vecino))
                    conjuntoAristas += String.format("(%s, %s), ", vertice.elemento, vecino.vecino.elemento);
            }
        }
        return "{" + conjuntoVertices + "}, {" + conjuntoAristas + "}";
    }

    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la gráfica es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Grafica<T> grafica = (Grafica<T>)objeto;
        // Aquí va su código.
        if(aristas != grafica.aristas || vertices.getElementos() != grafica.vertices.getElementos())
            return false;
        for(Vertice u : vertices) {
            Vertice v = null;
            for(Vertice w : grafica.vertices) {
                if(u.elemento.equals(w.elemento)) {
                    v = w;
                    break;
                }
            }
            if(v == null || u.vecinos.getElementos() != v.vecinos.getElementos())
                return false;
            for(Vecino vVecino : v.vecinos) {
                Vertice vecinoComun = null;
                for(Vecino uVecino : u.vecinos) {
                    if(vVecino.vecino.elemento.equals(uVecino.vecino.elemento))
                        vecinoComun = uVecino.vecino;  
                }
                if(vecinoComun == null)
                    return false;
            }
        }
        return true;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <code>a</code> y
     *         <code>b</code>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> trayectoriaMinima(T origen, T destino) {
        // Aquí va su código.
        Lista<VerticeGrafica<T>> trayectoriaMinima = new Lista<>();
        Vertice verticeOrigen = (Vertice) vertice(origen);
        Vertice verticeDestino = (Vertice) vertice(destino);
        if(origen.equals(destino)) {
            trayectoriaMinima.agrega(verticeOrigen);
            return trayectoriaMinima;
        }
        paraCadaVertice(v -> {
            Vertice vertice = (Vertice) v;
            vertice.distancia = Double.POSITIVE_INFINITY;
        });
        verticeOrigen.distancia = 0;
        Cola<Vertice> cola = new Cola<>();
        cola.mete(verticeOrigen);
        while(!cola.esVacia()) {
            Vertice actual = cola.saca();
            for(Vecino v : actual.vecinos) {
                if(v.vecino.distancia == Double.POSITIVE_INFINITY) {
                    v.vecino.distancia = actual.distancia + 1;
                    cola.mete(v.vecino);
                }
            }
        }
        return reconstruye((Vertice v,Vecino u) -> {
            return u.vecino.distancia == v.distancia - 1;
        }, verticeDestino);
    }

    private Lista<VerticeGrafica<T>> reconstruye(BuscadorCamino<T> buscaCamino, Vertice destino) {
        Lista<VerticeGrafica<T>> trayectoria = new Lista<>();
        if(destino.distancia == Double.POSITIVE_INFINITY)
            return trayectoria;
        trayectoria.agrega(destino);
        for(Vertice v = destino; v.distancia != 0;) {
            for(Vecino u : v.vecinos) {
                if(buscaCamino.seSiguen(v, u)) {
                    v = u.vecino;
                    trayectoria.agrega(v);
                    break;
                }
            }
        }
        return trayectoria.reversa();
    }

    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento
     * de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <code>origen</code> y
     *         el vértice <code>destino</code>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> dijkstra(T origen, T destino) {
        // Aquí va su código.
        Lista<VerticeGrafica<T>> trayectoriaPesoMinimo = new Lista<>();
        MonticuloDijkstra<Vertice> monticulo;
        Vertice verticeOrigen = (Vertice) vertice(origen);
        Vertice verticeDestino = (Vertice) vertice(destino);
        int n = vertices.getElementos();
        if(origen.equals(destino)) {
            trayectoriaPesoMinimo.agrega(verticeOrigen);
            return trayectoriaPesoMinimo;
        }
        paraCadaVertice(v -> {
            Vertice vertice = (Vertice) v;
            vertice.distancia = Double.POSITIVE_INFINITY;
        });
        verticeOrigen.distancia = 0;
        if(aristas > n * (n - 1) / 2 - n) 
            monticulo = new MonticuloArreglo<>(vertices, vertices.getElementos());
        else 
            monticulo = new MonticuloMinimo<>(vertices, vertices.getElementos());
        while(!monticulo.esVacia()) {
            Vertice actual = monticulo.elimina();
            for(Vecino v : actual.vecinos) {
                if(v.vecino.distancia > actual.distancia + v.peso) {
                    v.vecino.distancia = actual.distancia + v.peso;
                    monticulo.reordena(v.vecino);
                }
            }
        }
        return reconstruye((Vertice v, Vecino u) -> {
            return u.vecino.distancia == v.distancia - u.peso;
        }, verticeDestino);
    }
}
