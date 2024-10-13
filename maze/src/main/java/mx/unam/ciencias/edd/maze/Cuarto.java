package mx.unam.ciencias.edd.maze;

import mx.unam.ciencias.edd.Conjunto;

/**
 * <p>Clase que representa un cuarto en el laberinto.</p>
 * <p>Se maneja la representación del cuarto en un byte.</p>
 */
public class Cuarto {
    // Permite examinar la información del byte.
    private final byte[] MASCARAS_BITS = {0b1110, 0b1101, 0b1011, 0b0111};
    // Evita repeticiones.
    private Conjunto<Direccion> puertas;
    private byte puntaje;
    private byte dato;
    /**
     *Constructor por omisión, establece el dato del cuarto que 
     * se interpreta como puntaje cero y ninguna puerta.
     */
    public Cuarto () {
        puertas = new Conjunto<>(4);
        dato = 0b00001111;
    }

    /**
     * Constructor que se le proporciona el dato del cuarto y lo interpreta.
     * @param dato El dato del cuarto.
     */
    public Cuarto(byte dato) {
        this.dato = dato;
        puertas = new Conjunto<>();
        // Establece las puertas y el puntaje del cuarto.
        for(byte mascara : MASCARAS_BITS) {
            // Examina los primeros cuatro bytes.
            // Si no cambio el valor al aplicar la máscara indica la presencia
            // de una puerta.
            if(!cambioValor(mascara))
                puertas.agrega(Direccion.obtenerDireccion(mascara));
        }
        // Prepara los 4 bits mas significativos para su análisis.
        establecerPuntaje((byte) ((dato >> 4) & 0b00001111));
    }

    /*
     * Dice si el valor del dato se modificó al aplicar la mascara.
     */
    private boolean cambioValor(byte mascara) {
        byte pivote = (byte) (dato & 0b00001111);
        byte temp = (byte) (pivote & mascara);
        return pivote != temp;
    }

    /**
     * Agrega una nueva puerta al cuarto y modifica su dato asociado.
     * @param direccion La dirección de la puerta a agregar.
     */
    public void establecerPuerta(Direccion direccion) {
        puertas.agrega(direccion);
        // Evita modificar los últimos cuatro bits.
        // Agrega la puerta al byte pivote (0b11110000).
        // Actualiza al dato.
        dato &= (0b11110000 | direccion.obtenerDato());
    }

    /**
     * Establece el puntaje del cuarto.
     * @param puntaje El byte del puntaje.
     * @throws IllegalArgumentException Si el puntaje no esta en un rango del 0 al 15. 
     */
    public void establecerPuntaje(byte puntaje) throws IllegalArgumentException {
        if(puntaje < 0 || puntaje > 15)
            throw new IllegalArgumentException("El puntaje debe ser un número del 0 al 15.");
        this.puntaje = puntaje;
        byte temp = (byte) (puntaje << 4);
        dato |= temp;
    }

    /**
     * Regresa el puntaje del cuarto.
     * @return El puntaje del cuarto.
     */
    public int obtenerPuntaje() {
        return puntaje;
    }

    /**
     * Regresa el dato del cuarto.
     * @return El dato del cuarto.
     */
    public byte obtenerDato() {
        return dato;
    }

    /**
     * Regresa el conjunto de las direcciones de las puertas del cuarto.
     * @return El conjutno de las direcciones de las puertas del cuarto.
     */
    public Conjunto<Direccion> obtenerPuertas() {
        return puertas;
    }
}
