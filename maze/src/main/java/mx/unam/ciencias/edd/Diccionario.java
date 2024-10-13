package mx.unam.ciencias.edd;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para diccionarios (<em>hash tables</em>). Un diccionario generaliza el
 * concepto de arreglo, mapeando un conjunto de <em>llaves</em> a una colección
 * de <em>valores</em>.
 */
public class Diccionario<K, V> implements Iterable<V> {

    /* Clase interna privada para entradas. */
    private class Entrada {

        /* La llave. */
        private K llave;
        /* El valor. */
        private V valor;

        /* Construye una nueva entrada. */
        private Entrada(K llave, V valor) {
            // Aquí va su código.
            this.llave = llave;
            this.valor = valor;
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador {

        /* En qué lista estamos. */
        private int indice;
        /* Iterador auxiliar. */
        private Iterator<Entrada> iterador;

        /* Construye un nuevo iterador, auxiliándose de las listas del
         * diccionario. */
        private Iterador() {
            // Aquí va su código.
            indice = -1;
            mueveIterador();
        }

        /* Nos dice si hay una siguiente entrada. */
        public boolean hasNext() {
            return iterador != null;
        }

        /* Regresa la siguiente entrada. */
        protected Entrada siguiente() {
            // Aquí va su código.
            if(iterador == null) 
                throw new NoSuchElementException("No hay un elemento siguiente.");
            Entrada siguiente = iterador.next();
            if(!iterador.hasNext())
                mueveIterador();
            return siguiente;
        }

        /* Mueve el iterador a la siguiente entrada válida. */
        private void mueveIterador() {
            // Aquí va su código.
            iterador = null;
            for(int i = indice + 1; i < entradas.length; i++) {
                if(entradas[i] != null) {
                    Iterator<Entrada> actual = entradas[i].iterator();
                    if(actual.hasNext()) {
                        indice = i;
                        iterador = entradas[i].iterator();
                        break;
                    }
                }
            }
        }
    }

    /* Clase interna privada para iteradores de llaves. */
    private class IteradorLlaves extends Iterador
        implements Iterator<K> {

        /* Regresa el siguiente elemento. */
        @Override public K next() {
            // Aquí va su código.
            return siguiente().llave;
        }
    }

    /* Clase interna privada para iteradores de valores. */
    private class IteradorValores extends Iterador
        implements Iterator<V> {

        /* Regresa el siguiente elemento. */
        @Override public V next() {
            // Aquí va su código.
            return siguiente().valor;
        }
    }

    /** Máxima carga permitida por el diccionario. */
    public static final double MAXIMA_CARGA = 0.72;

    /* Capacidad mínima; decidida arbitrariamente a 2^6. */
    private static final int MINIMA_CAPACIDAD = 64;

    /* Dispersor. */
    private Dispersor<K> dispersor;
    /* Nuestro diccionario. */
    private Lista<Entrada>[] entradas;
    /* Número de valores. */
    private int elementos;

    /* Truco para crear un arreglo genérico. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked")
    private Lista<Entrada>[] nuevoArreglo(int n) {
        return (Lista<Entrada>[])Array.newInstance(Lista.class, n);
    }

    /**
     * Construye un diccionario con una capacidad inicial y dispersor
     * predeterminados.
     */
    public Diccionario() {
        this(MINIMA_CAPACIDAD, (K llave) -> llave.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial definida por el
     * usuario, y un dispersor predeterminado.
     * @param capacidad la capacidad a utilizar.
     */
    public Diccionario(int capacidad) {
        this(capacidad, (K llave) -> llave.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial predeterminada, y un
     * dispersor definido por el usuario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(Dispersor<K> dispersor) {
        this(MINIMA_CAPACIDAD, dispersor);
    }

    /**
     * Construye un diccionario con una capacidad inicial y un método de
     * dispersor definidos por el usuario.
     * @param capacidad la capacidad inicial del diccionario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(int capacidad, Dispersor<K> dispersor) {
        // Aquí va su código.
        this.dispersor = dispersor;
        capacidad = capacidad < MINIMA_CAPACIDAD ? MINIMA_CAPACIDAD : capacidad;
        capacidad *= 2;
        int i = 1;
        while(1 << i < capacidad)
            i++;
        capacidad = 1 << i;
        entradas = nuevoArreglo(capacidad);
    }

    /**
     * Agrega un nuevo valor al diccionario, usando la llave proporcionada. Si
     * la llave ya había sido utilizada antes para agregar un valor, el
     * diccionario reemplaza ese valor con el recibido aquí.
     * @param llave la llave para agregar el valor.
     * @param valor el valor a agregar.
     * @throws IllegalArgumentException si la llave o el valor son nulos.
     */
    public void agrega(K llave, V valor) {
        // Aquí va su código.
        if(llave == null) 
            throw new IllegalArgumentException("La llave es nula.");
        if(valor == null)
            throw new IllegalArgumentException("El valor es nulo.");
        int i = dispersor.dispersa(llave) & (entradas.length - 1);
        boolean centinela = false;
        if(entradas[i] == null)
            entradas[i] = new Lista<>();
        for(Entrada entrada : entradas[i]) {
            if(entrada.llave.equals(llave)) {
                entrada.valor = valor;
                centinela = true;
            }
        }
        if(!centinela) {
            entradas[i].agrega(new Entrada(llave, valor));
            elementos++;
        }
        if(carga() >= MAXIMA_CARGA) {
            Lista<Entrada>[] temp = nuevoArreglo(entradas.length * 2);
            Iterador iterador = new Iterador();
            while (iterador.hasNext()) {
                Entrada entrada = iterador.siguiente();
                int indice = dispersor.dispersa(entrada.llave) & (temp.length - 1);
                if (temp[indice] == null)
                    temp[indice] = new Lista<Entrada>();
                temp[indice].agrega(entrada);
            }
            entradas = temp;
        }
    }

    /**
     * Regresa el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor.
     * @return el valor correspondiente a la llave.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException si la llave no está en el diccionario.
     */
    public V get(K llave) {
        // Aquí va su código.
        if(llave == null)
            throw new IllegalArgumentException("La llave es nula.");
        V valor = null;
        int indiceValor = dispersor.dispersa(llave) & (entradas.length - 1);
        if(entradas[indiceValor] != null) {
            for(Entrada entrada : entradas[indiceValor]){
                if(entrada.llave.equals(llave))
                    valor = entrada.valor;
            }
        }
        if(valor == null)
            throw new NoSuchElementException("La llave no está en el diccionario.");
        return valor;
    }

    /**
     * Nos dice si una llave se encuentra en el diccionario.
     * @param llave la llave que queremos ver si está en el diccionario.
     * @return <code>true</code> si la llave está en el diccionario,
     *         <code>false</code> en otro caso.
     */
    public boolean contiene(K llave) {
        // Aquí va su código.
        if(llave == null)
            return false;
        int indiceValor = dispersor.dispersa(llave) & (entradas.length - 1);
        if(entradas[indiceValor] != null) {
            for(Entrada entrada : entradas[indiceValor]){
                if(entrada.llave.equals(llave))
                    return true;
            }
        }
        return false;
    }

    /**
     * Elimina el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor a eliminar.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException si la llave no se encuentra en
     *         el diccionario.
     */
    public void elimina(K llave) {
        // Aquí va su código.
        if(llave == null)
            throw new IllegalArgumentException("La llave es nula.");
        if(!contiene(llave))
            throw new IllegalArgumentException("La llave no se encuentra en el diccinoario.");
        int i = dispersor.dispersa(llave) & (entradas.length - 1);
        for(Entrada entrada : entradas[i]) {
            if(entrada.llave.equals(llave))
                entradas[i].elimina(entrada);
        }
        elementos--;
    }

    /**
     * Nos dice cuántas colisiones hay en el diccionario.
     * @return cuántas colisiones hay en el diccionario.
     */
    public int colisiones() {
        // Aquí va su código.
        int suma = 0;
        for(Lista<Entrada> lista : entradas) {
            if(lista != null)
                suma += lista.getLongitud() - 1;
        }
        return suma;
    }

    /**
     * Nos dice el máximo número de colisiones para una misma llave que tenemos
     * en el diccionario.
     * @return el máximo número de colisiones para una misma llave.
     */
    public int colisionMaxima() {
        // Aquí va su código.
        int maximo = 0;
        for(Lista<Entrada> lista : entradas) {
            if(lista != null && lista.getElementos() > maximo)
                maximo = lista.getElementos();
        }
        return maximo - 1;
    }

    /**
     * Nos dice la carga del diccionario.
     * @return la carga del diccionario.
     */
    public double carga() {
        // Aquí va su código.
        return (double) elementos / entradas.length;
    }

    /**
     * Regresa el número de entradas en el diccionario.
     * @return el número de entradas en el diccionario.
     */
    public int getElementos() {
        // Aquí va su código.
        return elementos;
    }

    /**
     * Nos dice si el diccionario es vacío.
     * @return <code>true</code> si el diccionario es vacío, <code>false</code>
     *         en otro caso.
     */
    public boolean esVacia() {
        // Aquí va su código.
        return elementos == 0;
    }

    /**
     * Limpia el diccionario de elementos, dejándolo vacío.
     */
    public void limpia() {
        // Aquí va su código.
        elementos = 0;
        entradas = nuevoArreglo(MINIMA_CAPACIDAD);
    }

    /**
     * Regresa una representación en cadena del diccionario.
     * @return una representación en cadena del diccionario.
     */
    @Override public String toString() {
        // Aquí va su código.
        String diccionario = "{ ";
        Iterador iterador = new Iterador();
        while(iterador.hasNext()) {
            Entrada actual = iterador.siguiente();
            diccionario += obtenerEntrada(actual);
        }
        diccionario += "}";
        return esVacia() ? "{}" : diccionario;
    }

    private String obtenerEntrada(Entrada entrada) {
        return String.format("'%s': '%s', ", entrada.llave.toString(), entrada.valor.toString());
    }

    /**
     * Nos dice si el diccionario es igual al objeto recibido.
     * @param o el objeto que queremos saber si es igual al diccionario.
     * @return <code>true</code> si el objeto recibido es instancia de
     *         Diccionario, y tiene las mismas llaves asociadas a los mismos
     *         valores.
     */
    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") Diccionario<K, V> d =
            (Diccionario<K, V>)o;
        // Aquí va su código.
        if (d.elementos != elementos)
            return false;
        Iterador iterador = new Iterador();
        while (iterador.hasNext()) {
            Entrada entrada = iterador.siguiente();
            if (!d.contiene(entrada.llave) || !d.get(entrada.llave).equals(entrada.valor))
                return false;
        }
        return true;
    }

    /**
     * Regresa un iterador para iterar las llaves del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar las llaves del diccionario.
     */
    public Iterator<K> iteradorLlaves() {
        return new IteradorLlaves();
    }

    /**
     * Regresa un iterador para iterar los valores del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar los valores del diccionario.
     */
    @Override public Iterator<V> iterator() {
        return new IteradorValores();
    }
}
