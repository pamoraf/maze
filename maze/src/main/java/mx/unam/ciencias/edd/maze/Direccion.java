package mx.unam.ciencias.edd.maze;

/**
 * Enumeración para 4 direcciones que maneja el programa.
 */
public enum Direccion {
    /**
     * Dirección por omisión.
     */
    NINGUNA(0, 0),
    /**
     * Dirección norte que resta una unidad a la coordenada y.
     */
    NORTE(0, -1),
    /**
     * Dirección sur que suma una unidad a la coordenada y.
     */
    SUR(0, 1),
    /**
     * Dirección este que suma una unidad a la coordenada x.
     */
    ESTE(1, 0),
    /**
     * Dirección oeste que resta una unidad a la coordenada x.
     */
    OESTE(-1, 0);
    // Desplazamientos en el eje X e Y para cada dirección.
    int deltaX;
    int deltaY;

    /**
     * Obtiene la dirección correspondiente a un valor en binario.
     * @param valor El valor en binario.
     * @return La dirección correspondiente al valor, o NINGUNA si el valor no
     *         corresponde a ninguna dirección.
     */
    public static Direccion obtenerDireccion(byte valor) {
        switch (valor) {
            case 0b1110:
                return Direccion.ESTE;
            case 0b1101:
                return Direccion.NORTE;
            case 0b1011:
                return Direccion.OESTE;
            case 0b0111:
                return Direccion.SUR;
            default:
                return Direccion.NINGUNA;
        }
    }

    /**
     * Obtiene un arreglo con las direcciones cardinales (NORTE, SUR, ESTE y OESTE).
     * @return Un arreglo con las direcciones cardinales.
     */
    public static Direccion[] obtenerCardinales() {
        Direccion[] direcciones = { Direccion.NORTE, Direccion.SUR, Direccion.ESTE, Direccion.OESTE };
        return direcciones;
    }

    /**
     * Constructor privado para la enumeración.
     * @param deltaX El desplazamiento en el eje X para la dirección.
     * @param deltaY El desplazamiento en el eje Y para la dirección.
     */
    Direccion(int deltaX, int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    /**
     * Obtiene la dirección opuesta a la dirección actual.
     * @return La dirección opuesta.
     */
    public Direccion opuesta() {
        switch (this) {
            case NORTE:
                return Direccion.SUR;
            case SUR:
                return Direccion.NORTE;
            case ESTE:
                return Direccion.OESTE;
            case OESTE:
                return Direccion.ESTE;
            default:
                return Direccion.NINGUNA;
        }
    }

    /**
     * Obtiene el valor en binario correspondiente a la dirección.
     * @return El valor en binario correspondiente a la dirección.
     */
    public byte obtenerDato() {
        switch (this) {
            case ESTE:
                return 0b1110;
            case NORTE:
                return 0b1101;
            case OESTE:
                return 0b1011;
            case SUR:
                return 0b0111;
            default:
                return 0;
        }
    }

    /**
     * Regresa una representación en cadena de la dirección.
     * @return Una representación en cadena de la dirección.
     */
    @Override public String toString() {
        switch (this) {
            case NORTE:
                return "NORTE";
            case SUR:
                return "SUR";
            case ESTE:
                return "ESTE";
            case OESTE:
                return "OESTE";
            default:
                return "NINGUNA";
        }
    }
}
