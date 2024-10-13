package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<code>null</code>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            // Aquí va su código.
            super(elemento);
            color = Color.NINGUNO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        @Override public String toString() {
            // Aquí va su código.
            switch(color) {
                case NEGRO:
                    return "N{" + elemento.toString() + "}";
                case ROJO:
                    return "R{" + elemento.toString() + "}";
                default:
                    return elemento.toString();
            }
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)objeto;
            // Aquí va su código.
            return (color == vertice.color && super.equals(objeto));
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        // Aquí va su código.
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        // Aquí va su código.
        VerticeRojinegro nuevoVertice = new VerticeRojinegro(elemento);
        return nuevoVertice;
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
        VerticeRojinegro verticeRN = (VerticeRojinegro) vertice;
        return verticeRN.color;
    }

    private boolean esRojo(VerticeRojinegro vertice) {
        return vertice != null && vertice.color == Color.ROJO;
    }

    private VerticeRojinegro getAbuelo(VerticeRojinegro vertice) {
        if(vertice == null || vertice.padre == null)
            return null;
        return getPadre(getPadre(vertice));
    }

    private VerticeRojinegro getTio(VerticeRojinegro vertice) {
        VerticeRojinegro abuelo = getAbuelo(vertice);
        if(abuelo == null)
            return null;
        return abuelo.izquierdo == vertice.padre ? getHijoDerecho(abuelo) : getHijoIzquierdo(abuelo);
    }

    private VerticeRojinegro getHermano(VerticeRojinegro vertice) {
        VerticeRojinegro padre = getPadre(vertice);
        if(padre == null)
            return null;
        return padre.izquierdo == vertice ?  getHijoDerecho(padre) : getHijoIzquierdo(padre);
    }

    private VerticeRojinegro getPadre(VerticeRojinegro vertice) {
        return (VerticeRojinegro) vertice.padre;
    }

    private VerticeRojinegro getHijoIzquierdo(VerticeRojinegro vertice) {
        if(vertice == null)
            return null;
        return (VerticeRojinegro) vertice.izquierdo;
    }

    private VerticeRojinegro getHijoDerecho(VerticeRojinegro vertice) {
        if(vertice == null)
            return null;
        return (VerticeRojinegro) vertice.derecho;
    }

    private boolean tieneHijoDerecho(VerticeRojinegro vertice) {
        return vertice != null && vertice.derecho != null;
    }

    private boolean tieneHijoIzquierdo(VerticeRojinegro vertice) {
        return vertice != null && vertice.izquierdo != null;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        super.agrega(elemento);
        VerticeRojinegro vertice = (VerticeRojinegro) ultimoAgregado;
        vertice.color = Color.ROJO;
        rebalanceoAgrega(vertice);
    }

    private void rebalanceoAgrega(VerticeRojinegro vertice) {
        VerticeRojinegro padre = (VerticeRojinegro) vertice.padre;
        VerticeRojinegro tio = null;
        VerticeRojinegro abuelo = null;
        boolean esPadreIzquierdo = false;
        boolean esHijoIzquierdo = false;
        if(padre == null) {
            vertice.color = Color.NEGRO;
            return;
        }
        if(padre.color == Color.NEGRO)
            return;
        abuelo = getAbuelo(vertice);
        tio = getTio(vertice);
        esPadreIzquierdo = (abuelo.izquierdo == padre);
        esHijoIzquierdo = (padre.izquierdo == vertice);
        if(esRojo(tio)) {
            abuelo.color = Color.ROJO;
            padre.color = Color.NEGRO;
            tio.color = Color.NEGRO;
            rebalanceoAgrega(abuelo);
            return;
        } 
        if(esHijoIzquierdo != esPadreIzquierdo) {
            if(esPadreIzquierdo) {
                super.giraIzquierda(padre);
                padre = getHijoIzquierdo(abuelo);
                vertice = getHijoIzquierdo(padre);
            } else { 
                super.giraDerecha(padre);
                padre = getHijoDerecho(abuelo);
                vertice = getHijoDerecho(padre);
            }
        }
        padre.color = Color.NEGRO;
        abuelo.color = Color.ROJO;
        if(esPadreIzquierdo)
            super.giraDerecha(abuelo);
        else 
            super.giraIzquierda(abuelo);
    }


    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        VerticeRojinegro porBorrar = (VerticeRojinegro) busca(elemento);
        if(porBorrar == null)
            return;
        if(tieneHijoIzquierdo(porBorrar))
            porBorrar = (VerticeRojinegro) intercambiaEliminable(porBorrar);
        VerticeRojinegro fantasma = null;
        if(!tieneHijoDerecho(porBorrar) && !tieneHijoIzquierdo(porBorrar)) {
            fantasma = new VerticeRojinegro(null);
            fantasma.color = Color.NEGRO;
            porBorrar.izquierdo = fantasma;
            fantasma.padre = porBorrar;
        }
        VerticeRojinegro hijo = tieneHijoIzquierdo(porBorrar) ? getHijoIzquierdo(porBorrar) : getHijoDerecho(porBorrar);
        eliminaVertice(porBorrar);
        elementos--;
        if(esRojo(hijo))
            hijo.color = Color.NEGRO;
        else {
            if(!esRojo(porBorrar))
                rebalanceoElimina(hijo);
            eliminaVertice(fantasma);
        }
    }

    private void rebalanceoElimina(VerticeRojinegro vertice) {
        if(vertice.padre == null)
            return;
        VerticeRojinegro padre = getPadre(vertice);
        VerticeRojinegro hermano = getHermano(vertice);
        if(esRojo(hermano)) {
            padre.color = Color.ROJO;
            hermano.color = Color.NEGRO;
            if(padre.derecho == vertice) 
                super.giraDerecha(padre);
            else 
                super.giraIzquierda(padre);
            hermano = getHermano(vertice);
        }
        VerticeRojinegro sobrinoIzquierdo = getHijoIzquierdo(hermano);
        VerticeRojinegro sobrinoDerecho = getHijoDerecho(hermano);
        boolean familiaNegra = !esRojo(hermano) && !esRojo(sobrinoDerecho) && !esRojo(sobrinoIzquierdo);
        if(!esRojo(padre)) {
            if(familiaNegra) {
                hermano.color = Color.ROJO;
                rebalanceoElimina(padre);
                return;
            }
        } else {
            if(familiaNegra) {
                hermano.color = Color.ROJO;
                padre.color = Color.NEGRO;
                return;
            }
        }
        hermano.color = Color.ROJO;
        if(padre.izquierdo == vertice) {
            if(esRojo(sobrinoIzquierdo) && !esRojo(sobrinoDerecho)) {
                sobrinoIzquierdo.color = Color.NEGRO;
                super.giraDerecha(hermano);
            }
        } else {
            if(!esRojo(sobrinoIzquierdo) && esRojo(sobrinoDerecho)) {
                sobrinoDerecho.color = Color.NEGRO;
                super.giraIzquierda(hermano);
            }
        }
        hermano = getHermano(vertice);
        sobrinoIzquierdo = getHijoIzquierdo(hermano);
        sobrinoDerecho = getHijoDerecho(hermano);
        hermano.color = padre.color;
        padre.color = Color.NEGRO;
        if(padre.izquierdo == vertice) {
            sobrinoDerecho.color = Color.NEGRO;
            super.giraIzquierda(padre);
        } else {
            sobrinoIzquierdo.color = Color.NEGRO;
            super.giraDerecha(padre);
        }
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }
}
