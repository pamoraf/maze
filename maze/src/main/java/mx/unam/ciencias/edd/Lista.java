package mx.unam.ciencias.edd;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas no aceptan a <code>null</code> como elemento.</p>
 *
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase interna privada para nodos. */
    private class Nodo {
        /* El elemento del nodo. */
        private T elemento;
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nodo con un elemento. */
        private Nodo(T elemento) {
            // Aquí va su código.
            this.elemento = elemento;
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nuevo iterador. */
        private Iterador() {
            // Aquí va su código.
            start();
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
            return siguiente != null;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
            // Aquí va su código.
            if(siguiente == null)
                throw new NoSuchElementException("No hay elemento siguiente.");
            anterior = siguiente;
            siguiente = siguiente.siguiente;
            return anterior.elemento;
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            // Aquí va su código.
            return anterior != null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            // Aquí va su código.
            if(anterior == null)
                throw new NoSuchElementException("No hay elemento anterior.");
            siguiente = anterior;
            anterior = anterior.anterior;
            return siguiente.elemento;
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            // Aquí va su código.
            siguiente = cabeza;
            anterior = null;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            // Aquí va su código.
            anterior = rabo;
            siguiente = null;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Métodos privados auxiliares.
     */
    private Nodo getNodo(T elemento) {
        Nodo n = cabeza;
        while(n != null) {
            if(n.elemento.equals(elemento))
                return n;

            n = n.siguiente;
        }
        return null;
    }

    private Nodo getNodo(int i) {
        Nodo n = cabeza;
        while(n != null){
            if(i == 0)
                return n;
            n = n.siguiente;
            i--;
        }
        return null;
    }

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        // Aquí va su código.
        return longitud;
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    @Override public int getElementos() {
        // Aquí va su código.
        return longitud;
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
        return longitud == 0;
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. El método es idéntico a
     * {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        agregaFinal(elemento);
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        // Aquí va su código.
        if(elemento == null)
            throw new IllegalArgumentException("El elemento es nulo.");
        Nodo porAgregar = new Nodo(elemento);
        if(longitud == 0)
            cabeza = rabo = porAgregar;
        else {
            rabo.siguiente = porAgregar;
            porAgregar.anterior = rabo;
            rabo = porAgregar;
        }
        longitud++;
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
        // Aquí va su código.
        if(elemento == null)
            throw new IllegalArgumentException("El elemento es nulo.");
        Nodo porAgregar = new Nodo(elemento);
        if(longitud == 0)
            cabeza = rabo = porAgregar;
        else {
            cabeza.anterior = porAgregar;
            porAgregar.siguiente = cabeza;
            cabeza = porAgregar;
        }
        longitud++;
    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) {
        // Aquí va su código.
        if(elemento == null)
            throw new IllegalArgumentException("El elemento es nulo.");
        if(i <= 0)
            agregaInicio(elemento);
        else if(i >= longitud)
            agregaFinal(elemento);
        else {
            Nodo n = getNodo(i - 1);
            Nodo nSiguiente = n.siguiente;
            n.siguiente = new Nodo(elemento);
            nSiguiente.anterior = n.siguiente;
            n.siguiente.anterior = n;
            n.siguiente.siguiente = nSiguiente;
            longitud++;
        }
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        Nodo porBorrar = getNodo(elemento);
        if(porBorrar != null) {
            Nodo anterior = porBorrar.anterior;
            Nodo siguiente = porBorrar.siguiente;
            longitud--;
            porBorrar.anterior = null;
            porBorrar.siguiente = null;
            if(anterior != null && siguiente != null) {
                anterior.siguiente = siguiente;
                siguiente.anterior = anterior;
            } else if(anterior != null) {
                anterior.siguiente = null;
                rabo = anterior;
            } else if(siguiente != null) {
                siguiente.anterior = null;
                cabeza = siguiente;
            } else
                cabeza = rabo = null;
        }
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
        // Aquí va su código.
        if(longitud == 0)
            throw new NoSuchElementException("La lista es vacía.");
        T elemento = cabeza.elemento;
        if(longitud == 1)
            rabo = cabeza = null;
        else {
            cabeza = cabeza.siguiente;
            cabeza.anterior.siguiente = null;
            cabeza.anterior = null;
        }
        longitud--;
        return elemento;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        // Aquí va su código.
        if(longitud == 0)
            throw new NoSuchElementException("La lista es vacía.");
        T elemento = rabo.elemento;
        if(longitud == 1)
            rabo = cabeza = null;
        else {
            rabo = rabo.anterior;
            rabo.siguiente.anterior = null;
            rabo.siguiente = null;
        }
        longitud--;
        return elemento;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <code>true</code> si <code>elemento</code> está en la lista,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        // Aquí va su código.
        return getNodo(elemento) != null;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        // Aquí va su código.
        Lista<T> reversa = new Lista<T>();
        Nodo n = rabo;
        while(n != null) {
            reversa.agregaFinal(n.elemento);
            n = n.anterior;
        }
        return reversa;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        // Aquí va su código.
        Lista<T> copia = new Lista<T>();
        Nodo n = cabeza;
        while(n != null) {
            copia.agregaFinal(n.elemento);
            n = n.siguiente;
        }
        return copia;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    @Override public void limpia() {
        // Aquí va su código.
        rabo = cabeza = null;
        longitud = 0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
        // Aquí va su código.
        if(longitud == 0)
            throw new NoSuchElementException();
        return cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
        // Aquí va su código.
        if(longitud == 0)
            throw new NoSuchElementException("La lista es vacía.");
        return rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
        // Aquí va su código.
        if(i < 0 || i >= longitud)
            throw new ExcepcionIndiceInvalido("El indice es inválido.");
        return getNodo(i).elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        // Aquí va su código.
        int i = 0;
        for(Nodo n = cabeza; n != null; n = n.siguiente, i++) {
            if(n.elemento.equals(elemento))
                return i;
        }
        return -1;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        // Aquí va su código.
        String lista = "[";
        if(longitud == 0)
            lista += "]";
        else {
            Nodo n = cabeza;
            lista += n.elemento;
            n = n.siguiente;
            while(n != null) {
                lista += ", " + n.elemento;
                n = n.siguiente;
            }
            lista += "]";
        }
        return lista;
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la lista es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)objeto;
        // Aquí va su código.
        if(lista.getLongitud() != longitud)
            return false;
        Nodo n = cabeza;
        for(T elemento : lista) {
            if(!(elemento.equals(n.elemento)))
                return false;
            n = n.siguiente;
        }
        return true;
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }
    
    /**
     * Regresa una copia de la lista, pero ordenada. Para poder hacer el
     * ordenamiento, el método necesita una instancia de {@link Comparator} para
     * poder comparar los elementos de la lista.
     * @param comparador el comparador que la lista usará para hacer el
     *                   ordenamiento.
     * @return una copia de la lista, pero ordenada.
     */
    public Lista<T> mergeSort(Comparator<T> comparador) {
        // Aquí va su código.
        return mergeSort(this, comparador);
    }

    private Lista<T> mergeSort(Lista<T> lista, Comparator<T> comparador) {
        if(lista.longitud <= 1)
            return lista.copia();
        int centro = lista.longitud / 2;
        Lista<T> izq = new Lista<>();
        Lista<T> der = new Lista<>();
        Nodo n = lista.cabeza;
        for(int i = 0; i < centro; i++, n = n.siguiente)
            izq.agrega(n.elemento);
        for(int i = centro; i < lista.longitud; i++, n = n.siguiente)
            der.agrega(n.elemento);
        return mezcla(mergeSort(izq, comparador), mergeSort(der, comparador), comparador);
    }

    private Lista<T> mezcla(Lista<T> izq, Lista<T> der, Comparator<T> comparador) {
        Nodo nIzq = izq.cabeza;
        Nodo nDer = der.cabeza;
        Lista<T> ordenada = new Lista<>();
        while(nIzq != null && nDer != null) {
            if(comparador.compare(nIzq.elemento, nDer.elemento) <= 0) {
                ordenada.agrega(nIzq.elemento);
                nIzq = nIzq.siguiente;
            } else {
                ordenada.agrega(nDer.elemento);
                nDer = nDer.siguiente;
            }
        }
        while(nDer != null) {
            ordenada.agrega(nDer.elemento);
            nDer = nDer.siguiente;
        }
        while(nIzq != null) {
            ordenada.agrega(nIzq.elemento);
            nIzq = nIzq.siguiente;
        }
        return ordenada;
    }

    /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    public static <T extends Comparable<T>>
    Lista<T> mergeSort(Lista<T> lista) {
        return lista.mergeSort((a, b) -> a.compareTo(b));
    }

    /**
     * Busca un elemento en la lista ordenada, usando el comparador recibido. El
     * método supone que la lista está ordenada usando el mismo comparador.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador con el que la lista está ordenada.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public boolean busquedaLineal(T elemento, Comparator<T> comparador) {
        // Aquí va su código.
        int min = comparador.compare(cabeza.elemento, elemento);
        int max = comparador.compare(rabo.elemento, elemento);
        if(max == 0 || min == 0)
            return true;
        if(max > 0 && min < 0){
            Nodo n = cabeza;
            int comp = 0;
            while(n != null) {
                comp = comparador.compare(n.elemento, elemento);
                if(comp == 0)
                    return true;
                if(comp > 0)
                    return false;
                n = n.siguiente;
            }
        }
        return false;
    }

    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista donde se buscará.
     * @param elemento el elemento a buscar.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public static <T extends Comparable<T>>
    boolean busquedaLineal(Lista<T> lista, T elemento) {
        return lista.busquedaLineal(elemento, (a, b) -> a.compareTo(b));
    }
}
