package mx.unam.ciencias.edd;

import java.util.NoSuchElementException;

/**
 * <p>Clase abstracta para árboles binarios genéricos.</p>
 *
 * <p>La clase proporciona las operaciones básicas para árboles binarios, pero
 * deja la implementación de varias en manos de las subclases concretas.</p>
 */
public abstract class ArbolBinario<T> implements Coleccion<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class Vertice implements VerticeArbolBinario<T> {

        /** El elemento del vértice. */
        protected T elemento;
        /** El padre del vértice. */
        protected Vertice padre;
        /** El izquierdo del vértice. */
        protected Vertice izquierdo;
        /** El derecho del vértice. */
        protected Vertice derecho;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        protected Vertice(T elemento) {
            // Aquí va su código.
	        this.elemento = elemento;
        }

        /**
         * Nos dice si el vértice tiene un padre.
         * @return <code>true</code> si el vértice tiene padre,
         *         <code>false</code> en otro caso.
         */
        @Override public boolean hayPadre() {
            // Aquí va su código.
	        return padre != null;
        }

        /**
         * Nos dice si el vértice tiene un izquierdo.
         * @return <code>true</code> si el vértice tiene izquierdo,
         *         <code>false</code> en otro caso.
         */
        @Override public boolean hayIzquierdo() {
            // Aquí va su código.
	        return izquierdo != null;
        }

        /**
         * Nos dice si el vértice tiene un derecho.
         * @return <code>true</code> si el vértice tiene derecho,
         *         <code>false</code> en otro caso.
         */
        @Override public boolean hayDerecho() {
            // Aquí va su código.
	        return derecho != null;
        }

        /**
         * Regresa el padre del vértice.
         * @return el padre del vértice.
         * @throws NoSuchElementException si el vértice no tiene padre.
         */
        @Override public VerticeArbolBinario<T> padre() {
            // Aquí va su código.
            if(padre == null)
                throw new NoSuchElementException("El padre es nulo.");
	        return padre;
        }

        /**
         * Regresa el izquierdo del vértice.
         * @return el izquierdo del vértice.
         * @throws NoSuchElementException si el vértice no tiene izquierdo.
         */
        @Override public VerticeArbolBinario<T> izquierdo() {
            // Aquí va su código.
            if(izquierdo == null)
                throw new NoSuchElementException("El hijo izquierdo es nulo.");
            return izquierdo;
        }

        /**
         * Regresa el derecho del vértice.
         * @return el derecho del vértice.
         * @throws NoSuchElementException si el vértice no tiene derecho.
         */
        @Override public VerticeArbolBinario<T> derecho() {
            // Aquí va su código.
            if(derecho == null)
                throw new NoSuchElementException("El hijo derecho es nulo");
            return derecho;
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            // Aquí va su código.
            int izq = (izquierdo == null ? 0 : izquierdo.altura() + 1);
            int der = (derecho == null ? 0 : derecho.altura() + 1);
            return izq < der ? der : izq;
        }

        /**
         * Regresa la profundidad del vértice.
         * @return la profundidad del vértice.
         */
        @Override public int profundidad() {
            // Aquí va su código.
	        int contador = 0;
            for(Vertice n = padre; n != null; n = n.padre)
                contador++;
	        return contador;
        }

        /**
         * Regresa el elemento al que apunta el vértice.
         * @return el elemento al que apunta el vértice.
         */
        @Override public T get() {
            // Aquí va su código.
	        return elemento;
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>. Las clases que extiendan {@link Vertice} deben
         * sobrecargar el método {@link Vertice#equals}.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link Vertice}, su elemento es igual al elemento de éste
         *         vértice, y los descendientes de ambos son recursivamente
         *         iguales; <code>false</code> en otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") Vertice vertice = (Vertice)objeto;
            // Aquí va su código.
            boolean temp = elemento.equals(vertice.elemento);
            if(izquierdo != null && vertice.izquierdo != null)
                temp &= izquierdo.equals(vertice.izquierdo);
            else
                temp &= (izquierdo == null && vertice.izquierdo == null);
            if(derecho != null && vertice.derecho != null)
                temp &= derecho.equals(vertice.derecho);
            else
                temp &= (derecho == null && vertice.derecho == null);
            return temp;
        }

        /**
         * Regresa una representación en cadena del vértice.
         * @return una representación en cadena del vértice.
         */
        @Override public String toString() {
            // Aquí va su código.
	        return elemento.toString();
        }
    }

    /** La raíz del árbol. */
    protected Vertice raiz;
    /** El número de elementos */
    protected int elementos;

    /**
     * Constructor sin parámetros. Tenemos que definirlo para no perderlo.
     */
    public ArbolBinario() {}

    /**
     * Construye un árbol binario a partir de una colección. El árbol binario
     * tendrá los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario.
     */
    public ArbolBinario(Coleccion<T> coleccion) {
        // Aquí va su código.
        for(T elemento : coleccion)
            agrega(elemento);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link Vertice}. Para
     * crear vértices se debe utilizar este método en lugar del operador
     * <code>new</code>, para que las clases herederas de ésta puedan
     * sobrecargarlo y permitir que cada estructura de árbol binario utilice
     * distintos tipos de vértices.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    protected Vertice nuevoVertice(T elemento) {
        // Aquí va su código.
	    return new Vertice(elemento);
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol es la altura de su
     * raíz.
     * @return la altura del árbol.
     */
    public int altura() {
        // Aquí va su código.
	    return (raiz == null ? -1 : raiz.altura());
    }

    /**
     * Regresa el número de elementos que se han agregado al árbol.
     * @return el número de elementos en el árbol.
     */
    @Override public int getElementos() {
        // Aquí va su código.
	    return elementos;
    }

    /**
     * Nos dice si un elemento está en el árbol binario.
     * @param elemento el elemento que queremos comprobar si está en el árbol.
     * @return <code>true</code> si el elemento está en el árbol;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        // Aquí va su código.
	    return busca(elemento) != null;
    }

    /**
     * Busca el vértice de un elemento en el árbol. Si no lo encuentra regresa
     * <code>null</code>.
     * @param elemento el elemento para buscar el vértice.
     * @return un vértice que contiene el elemento buscado si lo encuentra;
     *         <code>null</code> en otro caso.
     */
    public VerticeArbolBinario<T> busca(T elemento) {
        // Aquí va su código.
	    if(raiz == null)
	        return null;
        Vertice buscado = null;
        Pila<Vertice> pila = new Pila<Vertice>();
        pila.mete(raiz);
        while(!(pila.esVacia())) {
            Vertice actual = pila.saca();
            if(actual.elemento.equals(elemento)) {
                buscado = actual;
                break;
            }
            if(actual.izquierdo != null)
                pila.mete(actual.izquierdo);
            if(actual.derecho != null)
                pila.mete(actual.derecho);
        }
	    return buscado;
    }

    /**
     * Regresa el vértice que contiene la raíz del árbol.
     * @return el vértice que contiene la raíz del árbol.
     * @throws NoSuchElementException si el árbol es vacío.
     */
    public VerticeArbolBinario<T> raiz() {
        // Aquí va su código.
        if(raiz == null)
            throw new NoSuchElementException("La raiz es nula.");
        return raiz;
    }

    /**
     * Nos dice si el árbol es vacío.
     * @return <code>true</code> si el árbol es vacío, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
	    return raiz == null;
    }

    /**
     * Limpia el árbol de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        // Aquí va su código.
        raiz = null;
        elementos = 0;
    }

    /**
     * Compara el árbol con un objeto.
     * @param objeto el objeto con el que queremos comparar el árbol.
     * @return <code>true</code> si el objeto recibido es un árbol binario y los
     *         árboles son iguales; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked")
            ArbolBinario<T> arbol = (ArbolBinario<T>)objeto;
        // Aquí va su código.
	    if(raiz == null)
	        return arbol.raiz == null;
	    return raiz.equals(arbol.raiz);
    }

    private String dibujaEspacios(int nivel, boolean[] arregloBinario) {
	    String espacios = "";
        for(int i = 0; i < nivel; i++) {
            if(arregloBinario[i])
                espacios += "│  ";
            else
                espacios += "   ";
        }
        return espacios;
    }

    private String toString(Vertice v, int nivel, boolean[] arregloBinario) {
        String lineaVertices = v.toString() + "\n";
        arregloBinario[nivel] = true;
        if(v.izquierdo != null && v.derecho != null) {
            lineaVertices += dibujaEspacios(nivel, arregloBinario);
            lineaVertices += "├─›";
            lineaVertices += toString(v.izquierdo, nivel + 1, arregloBinario);
            lineaVertices += dibujaEspacios(nivel, arregloBinario);
            lineaVertices += "└─»";
            arregloBinario[nivel] = false;
            lineaVertices += toString(v.derecho, nivel + 1, arregloBinario);
        } else if(v.izquierdo != null) {
            lineaVertices += dibujaEspacios(nivel, arregloBinario);
            lineaVertices += "└─›";
            arregloBinario[nivel] = false;
            lineaVertices += toString(v.izquierdo, nivel + 1, arregloBinario);
        } else if(v.derecho != null) {
            lineaVertices += dibujaEspacios(nivel, arregloBinario);
            lineaVertices += "└─»";
            arregloBinario[nivel] = false;
            lineaVertices += toString(v.derecho, nivel + 1, arregloBinario);
        }
        return lineaVertices;
    }

    /**
     * Regresa una representación en cadena del árbol.
     * @return una representación en cadena del árbol.
     */
    @Override public String toString() {
        // Aquí va su código.
	    if(raiz == null)
	        return "";
        boolean[] arregloBinario = new boolean[altura() + 1];
        for(int i = 0; i < altura() + 1; i++)
            arregloBinario[i] = false;
        return toString(raiz, 0, arregloBinario);
    }

    /**
     * Convierte el vértice (visto como instancia de {@link
     * VerticeArbolBinario}) en vértice (visto como instancia de {@link
     * Vertice}). Método auxiliar para hacer esta audición en un único lugar.
     * @param vertice el vértice de árbol binario que queremos como vértice.
     * @return el vértice recibido visto como vértice.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         Vertice}.
     */
    protected Vertice vertice(VerticeArbolBinario<T> vertice) {
        return (Vertice)vertice;
    }
}
