package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para montículos mínimos (<i>min heaps</i>).
 */
public class MonticuloMinimo<T extends Comparable<T>>
    implements Coleccion<T>, MonticuloDijkstra<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            // Aquí va su código.
            return indice < elementos;
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            // Aquí va su código.
            if(indice >= elementos)
                throw new NoSuchElementException();
            return arbol[indice++];
        }
    }

    /* El número de elementos en el arreglo. */
    private int elementos;
    /* Los índices. */
    private Diccionario<T, Integer> indices;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new Comparable[n]);
    }

    /*
     * Métodos privados que usé:
     */

    /* Acomoda hacia abajo (min-heapify); ve si el nodo actual (i) es mayor que
     * alguno de sus hijos (2*i+1, 2*i+2). Si así es, reemplaza el nodo con el
     * hijo menor, y recursivamente hace lo mismo con el hijo menor (que tiene
     * el valor del que era su padre). */
    private  void acomodaHaciaAbajo(int indice) {
        T actual = arbol[indice];
        int indiceMenor = indice;
        int indiceHijoIzquierdo = indiceMenor * 2 + 1;
        int indiceHijoDerecho = indiceMenor * 2 + 2;
        if(indiceHijoIzquierdo < elementos && actual.compareTo(arbol[indiceHijoIzquierdo]) > 0)
            indiceMenor = indiceHijoIzquierdo;
        if(indiceHijoDerecho < elementos && arbol[indiceMenor].compareTo(arbol[indiceHijoDerecho]) > 0)
            indiceMenor = indiceHijoDerecho;
        if(indiceMenor != indice) {
            intercambia(indice, indiceMenor);
            acomodaHaciaAbajo(indiceMenor);
        }
    }

    private void acomodaHaciaArriba(int indice) {
        T actual = arbol[indice];
        int indicePadre = (indice - 1) / 2;
        T padre = arbol[indicePadre];
        if(indice != 0 && actual.compareTo(padre) < 0) {
            intercambia(indice, indicePadre);
            acomodaHaciaArriba(indicePadre);
        }
    }

    private void intercambia(int i, int j) {
        T temp = arbol[i];
        arbol[i] = arbol[j];
        arbol[j] = temp;
        indices.agrega(arbol[i], i);
        indices.agrega(arbol[j], j);
    }    

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Coleccion)} o {@link #MonticuloMinimo(Iterable,int)},
     * pero se ofrece este constructor por completez.
     */
    public MonticuloMinimo() {
        // Aquí va su código.
        arbol = nuevoArreglo(100);
        indices = new Diccionario<>();
    }

    /**
     * Constructor para montículo mínimo que recibe una colección. Es más barato
     * construir un montículo con todos sus elementos de antemano (tiempo
     * <i>O</i>(<i>n</i>)), que el insertándolos uno por uno (tiempo
     * <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloMinimo(Coleccion<T> coleccion) {
        this(coleccion, coleccion.getElementos());
    }

    /**
     * Constructor para montículo mínimo que recibe un iterable y el número de
     * elementos en el mismo. Es más barato construir un montículo con todos sus
     * elementos de antemano (tiempo <i>O</i>(<i>n</i>)), que el insertándolos
     * uno por uno (tiempo <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param iterable el iterable a partir de la cuál queremos construir el
     *                 montículo.
     * @param n el número de elementos en el iterable.
     */
    public MonticuloMinimo(Iterable<T> iterable, int n) {
        // Aquí va su código.
        arbol = nuevoArreglo(n);
        indices = new Diccionario<>();
        elementos = n;
        int indice = 0;
        for(T elemento : iterable) {
            indices.agrega(elemento, indice);
            arbol[indice++] = elemento; 
        }
        for(int i = n / 2 - 1; i >= 0; i--)
            acomodaHaciaAbajo(i);
    }

    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        if(elementos == arbol.length) {
            T[] arbolAuxiliar = arbol;
            arbol = nuevoArreglo(arbol.length * 2);
            for(int i = 0; i < arbolAuxiliar.length; i++)
                arbol[i] = arbolAuxiliar[i];
        }
        arbol[elementos] = elemento;
        indices.agrega(elemento, elementos);
        elementos++;
        acomodaHaciaArriba(indices.get(elemento));
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    @Override public T elimina() {
        // Aquí va su código.
        if(elementos == 0)
            throw new IllegalStateException("El montículo es vacío.");
        return eliminaElemento(0);
    }

    /**
     * Elimina un elemento del montículo.
     * @param elemento a eliminar del montículo.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        if(!indices.contiene(elemento))
            return;
        eliminaElemento(indices.get(elemento));
    }

    /* Elimina el elemento en el índice. */
    private T eliminaElemento(int indice) {
        // Aquí va su código.
        T eliminado = arbol[indice];
        intercambia(indice, --elementos);
        arbol[elementos] = null;
        indices.elimina(eliminado);
        if (indice < elementos) {
            acomodaHaciaAbajo(indice);
            acomodaHaciaArriba(indice);
        }
        return eliminado;
    }

    /**
     * Nos dice si un elemento está contenido en el montículo.
     * @param elemento el elemento que queremos saber si está contenido.
     * @return <code>true</code> si el elemento está contenido,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        // Aquí va su código.
        return indices.contiene(elemento);
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <code>true</code> si ya no hay elementos en el montículo,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
        return elementos == 0;
    }

    /**
     * Limpia el montículo de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        // Aquí va su código.
        for(int i = 0; i < arbol.length; i++)
            arbol[i] = null;
        elementos = 0;
        indices.limpia();
    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    @Override public void reordena(T elemento) {
        // Aquí va su código.
        acomodaHaciaArriba(indices.get(elemento));
        acomodaHaciaAbajo(indices.get(elemento));
    }

    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    @Override public int getElementos() {
        // Aquí va su código.
        return elementos;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @param i el índice del elemento que queremos, en <em>in-order</em>.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    @Override public T get(int i) {
        // Aquí va su código.
        if(i < 0 || i >= elementos)
            throw new NoSuchElementException("El índice es inválido.");
        return arbol[i];
    }

    /**
     * Regresa una representación en cadena del montículo mínimo.
     * @return una representación en cadena del montículo mínimo.
     */
    @Override public String toString() {
        // Aquí va su código.
        String cadena = "";
        for(T elemento : arbol)
            cadena += elemento.toString() + ", ";
        return cadena;
    }

    /**
     * Nos dice si el montículo mínimo es igual al objeto recibido.
     * @param objeto el objeto con el que queremos comparar el montículo mínimo.
     * @return <code>true</code> si el objeto recibido es un montículo mínimo
     *         igual al que llama el método; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") MonticuloMinimo<T> monticulo =
            (MonticuloMinimo<T>)objeto;
        // Aquí va su código.
        if(monticulo.elementos != elementos)
            return false;
        for(int i = 0; i < elementos; i++) {
            if(!arbol[i].equals(monticulo.arbol[i]))
                return false;
        }
        return true;
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El montículo se
     * itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Ordena la colección usando HeapSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param coleccion la colección a ordenar.
     * @return una lista ordenada con los elementos de la colección.
     */
    public static <T extends Comparable<T>>
    Lista<T> heapSort(Coleccion<T> coleccion) {
        // Aquí va su código.
        Lista<T> lista = new Lista<>();
        MonticuloMinimo<T> monticulo = new MonticuloMinimo<>(coleccion, coleccion.getElementos());
        while(!monticulo.esVacia())
            lista.agrega(monticulo.elimina());
        return lista;
    }
}
