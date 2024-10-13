package mx.unam.ciencias.edd;

import java.util.Comparator;

/**
 * Clase para ordenar y buscar arreglos genéricos.
 */
public class Arreglos {

    /* Constructor privado para evitar instanciación. */
    private Arreglos() {}

    /**
     * Intercambia dos elementos en un arreglo dados dos índices.
     * @param arreglo
     * @param primer índice.
     * @param segundo índice.
     */
    private static <T> void intercambia(T[] arreglo, int a, int b) {
        T temp = arreglo[a];
        arreglo[a] = arreglo[b];
        arreglo[b] = temp;
    }

    /**
     * Ordena el arreglo recibido usando QuickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordenar el arreglo.
     */
    public static <T> void quickSort(T[] arreglo, Comparator<T> comparador) {
        Pila<Integer> p = new Pila<>();
        p.mete(0);
        p.mete(arreglo.length - 1);
        while (!(p.esVacia())) {
            int b = p.saca();
            int a = p.saca();
            int i = a + 1;
            int j = b;
            if (b <= a)
                continue;
            while (i < j) {
                if (comparador.compare(arreglo[i], arreglo[a]) > 0 && comparador.compare(arreglo[j], arreglo[a]) <= 0) {
                    intercambia(arreglo, i, j);
                    i++;
                    j--;
                } else if (comparador.compare(arreglo[i], arreglo[a]) <= 0)
                    i++;
                else
                    j--;
            }
            if (comparador.compare(arreglo[i], arreglo[a]) > 0)
                i--;
            intercambia(arreglo, a, i);
            p.mete(a);
            p.mete(i - 1);
            p.mete(i + 1);
            p.mete(b);
        }
    }

    /**
     * Ordena el arreglo recibido usando QuickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void quickSort(T[] arreglo) {
        quickSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordernar el arreglo.
     */
    public static <T> void selectionSort(T[] arreglo, Comparator<T> comparador) {
        for (int i = 0; i < arreglo.length; i++) {
            int m = i;
            for (int j = i + 1; j < arreglo.length; j++) {
                if (comparador.compare(arreglo[j], arreglo[m]) < 0)
                    m = j;
            }
            intercambia(arreglo, i, m);
        }
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void selectionSort(T[] arreglo) {
        selectionSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo dónde buscar.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador para hacer la búsqueda.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T> int busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador) {
        return busquedaBinaria(arreglo, elemento, comparador, 0, arreglo.length - 1);
    }

    private static <T> int busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador, int a, int b) {
        int prom = a + (b - a) / 2;
        int comparacion = comparador.compare(arreglo[prom], elemento);
        if (a > b)
            return -1;
        if (comparacion == 0)
            return prom;
        if (comparacion < 0)
            return busquedaBinaria(arreglo, elemento, comparador, prom, b);
        return busquedaBinaria(arreglo, elemento, comparador, a, prom);
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     * @param elemento el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int busquedaBinaria(T[] arreglo, T elemento) {
        return busquedaBinaria(arreglo, elemento, (a, b) -> a.compareTo(b));
    }
}
