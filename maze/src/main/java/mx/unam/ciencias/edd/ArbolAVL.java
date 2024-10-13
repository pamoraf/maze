package mx.unam.ciencias.edd;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeAVL extends Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
            // Aquí va su código.
            super(elemento);
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            // Aquí va su código.
            return altura;
        }

        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        @Override public String toString() {
            // Aquí va su código.
            return super.toString() + " " + altura + "/" + balance(this);
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)objeto;
            return altura == vertice.altura && super.equals(objeto);
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolAVL() { super(); }

    /**
     * Construye un árbol AVL a partir de una colección. El árbol AVL tiene los
     * mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol AVL.
     */
    public ArbolAVL(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        // Aquí va su código.
        return new VerticeAVL(elemento);
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        super.agrega(elemento);
        rebalancea(avl(ultimoAgregado.padre));
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        VerticeAVL porBorrar = (VerticeAVL) busca(elemento);
        if(porBorrar == null)
            return;
        if(porBorrar.izquierdo != null)
            porBorrar = avl(intercambiaEliminable(porBorrar));
        VerticeAVL padre = avl(porBorrar.padre);
        eliminaVertice(porBorrar);
        rebalancea(padre);
        elementos--;
    }
    
    private void rebalancea(VerticeAVL vertice) {
        if(vertice == null)
            return;
        calculaAltura(vertice);
        int balance = balance(vertice);
        if(balance == -2) {
            VerticeAVL hijoDerecho = avl(vertice.derecho);
            if(balance(hijoDerecho) > 0)
                super.giraDerecha(hijoDerecho);
            super.giraIzquierda(vertice);
            calculaAltura(hijoDerecho);
        }
        if(balance == 2) {
            VerticeAVL hijoIzquierdo = avl(vertice.izquierdo);
            if(balance(hijoIzquierdo) < 0)
                super.giraIzquierda(hijoIzquierdo);
            super.giraDerecha(vertice);
            calculaAltura(hijoIzquierdo);
        }
        calculaAltura(vertice);
        rebalancea(avl(vertice.padre));
    }

    private VerticeAVL avl(Vertice vertice) {
        return (VerticeAVL) vertice;
    }

    private void calculaAltura(VerticeAVL vertice) {
        int alturaHijoIzquierdo = getAltura(avl(vertice.izquierdo));
        int alturaHijoDerecho = getAltura(avl(vertice.derecho));
        vertice.altura = (alturaHijoDerecho < alturaHijoIzquierdo ? alturaHijoIzquierdo : alturaHijoDerecho) + 1;
    }

    private int getAltura(VerticeAVL vertice) {
        return vertice == null ? -1 : vertice.altura;
    }

    private int balance(VerticeAVL vertice) {
        return getAltura(avl(vertice.izquierdo)) - getAltura(avl(vertice.derecho));
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la izquierda por el " +
                                                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la derecha por el " +
                                                "usuario.");
    }
}
