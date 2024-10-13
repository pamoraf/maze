package mx.unam.ciencias.edd;

/**
 * Clase para métodos estáticos con dispersores de bytes.
 */
public class Dispersores {

    /* Constructor privado para evitar instanciación. */
    private Dispersores() {}

    /*
     * Métodos privados que usé:
     */
    private static int bigEndian(byte a, byte b, byte c, byte d) {
        return ((a & 0xFF) << 24) | ((b & 0xFF) << 16) | ((c & 0xFF) << 8) | (d & 0xFF);
    }

    private static int littleEndian(byte a, byte b, byte c, byte d) {
        return ((d & 0xFF) << 24) | ((c & 0xFF) << 16) | ((b & 0xFF) << 8) | (a & 0xFF);
    }

    private static int[] mezcla(int a, int b, int c) {
        int[] resultado = new int[3];
        a -= b; 
        a -= c; 
        a ^= (c >>> 13);
        b -= c; 
        b -= a; 
        b ^= (a << 8);
        c -= a; 
        c -= b; 
        c ^= (b >>> 13);
        a -= b; 
        a -= c; 
        a ^= (c >>> 12);
        b -= c; 
        b -= a; 
        b ^= (a << 16);
        c -= a; 
        c -= b; 
        c ^= (b >>> 5);
        a -= b; 
        a -= c; 
        a ^= (c >>> 3);
        b -= c;
        b -= a; 
        b ^= (a << 10);
        c -= a; 
        c -= b; 
        c ^= (b >>> 15);
        resultado[0] = a;
        resultado[1] = b;
        resultado[2] = c;
        return resultado;
    }

    /**
     * Función de dispersión XOR.
     * @param llave la llave a dispersar.
     * @return la dispersión de XOR de la llave.
     */
    public static int dispersaXOR(byte[] llave) {
        // Aquí va su código.
        int l = 0;
        int r = 0;
        int k = llave.length;
        while (l + 4 < k)
            r ^= bigEndian(llave[l++], llave[l++], llave[l++], llave[l++]);
        int t = 0;
        byte cero = 0;
        switch (k - l) {
            case 4:
                t = bigEndian(llave[k - 4], llave[k - 3], llave[k - 2], llave[k - 1]);
                break;
            case 3:
                t = bigEndian(llave[k - 3], llave[k - 2], llave[k - 1], cero);
                break;
            case 2:
                t = bigEndian(llave[k - 2], llave[k - 1], cero, cero);
                break;
            case 1:
                t = bigEndian(llave[k - 1], cero, cero, cero);
                break;
        }
        return r ^ t;
    }

    /**
     * Función de dispersión de Bob Jenkins.
     * @param llave la llave a dispersar.
     * @return la dispersión de Bob Jenkins de la llave.
     */
    public static int dispersaBJ(byte[] llave) {
        // Aquí va su código.
        int a = 0x9E3779B9;
        int b = 0x9E3779B9;
        int c = 0xFFFFFFFF;
        int l = 0;
        int k = llave.length;
        int[] resultado = null;
        while (l + 12 <= k) {
            a += littleEndian(llave[l++], llave[l++], llave[l++], llave[l++]);
            b += littleEndian(llave[l++], llave[l++], llave[l++], llave[l++]);
            c += littleEndian(llave[l++], llave[l++], llave[l++], llave[l++]);
            resultado = mezcla(a, b, c);
            a = resultado[0];
            b = resultado[1];
            c = resultado[2];
        }
        byte cero = 0;
        c += k;
        switch (k - l) {
            case 12:
                c -= k;
                a += littleEndian(llave[k - 12], llave[k - 11], llave[k - 10], llave[k - 9]);
                b += littleEndian(llave[k - 8], llave[k - 7], llave[k - 6], llave[k - 5]);
                c += littleEndian(llave[k - 4], llave[k - 3], llave[k - 2], llave[k - 1]);
                break;
            case 11:
                a += littleEndian(llave[k - 11], llave[k - 10], llave[k - 9], llave[k - 8]);
                b += littleEndian(llave[k - 7], llave[k - 6], llave[k - 5], llave[k - 4]);
                c += littleEndian(cero, llave[k - 3], llave[k - 2], llave[k - 1]);
                break;
            case 10:
                a += littleEndian(llave[k - 10], llave[k - 9], llave[k - 8], llave[k - 7]);
                b += littleEndian(llave[k - 6], llave[k - 5], llave[k - 4], llave[k - 3]);
                c += littleEndian(cero, llave[k - 2], llave[k - 1], cero);
                break;
            case 9:
                a += littleEndian(llave[k - 9], llave[k - 8], llave[k - 7], llave[k - 6]);
                b += littleEndian(llave[k - 5], llave[k - 4], llave[k - 3], llave[k - 2]);
                c += littleEndian(cero, llave[k - 1], cero, cero);
                break;
                
            case 8:
                a += littleEndian(llave[k - 8], llave[k - 7], llave[k - 6], llave[k - 5]);
                b += littleEndian(llave[k - 4], llave[k - 3], llave[k - 2], llave[k - 1]);
                break;
            case 7:
                a += littleEndian(llave[k - 7], llave[k - 6], llave[k - 5], llave[k - 4]);
                b += littleEndian(llave[k - 3], llave[k - 2], llave[k - 1], cero);
                break;
            case 6:
                a += littleEndian(llave[k - 6], llave[k - 5], llave[k - 4], llave[k - 3]);
                b += littleEndian(llave[k - 2], llave[k - 1], cero, cero);
                break;
            case 5:
                a += littleEndian(llave[k - 5], llave[k - 4], llave[k - 3], llave[k - 2]);
                b += littleEndian(llave[k - 1], cero, cero, cero);
                break;
            case 4:
                a += littleEndian(llave[k - 4], llave[k - 3], llave[k - 2], llave[k - 1]);
                break;
            case 3:
                a += littleEndian(llave[k - 3], llave[k - 2], llave[k - 1], cero);
                break;
            case 2:
                a += littleEndian(llave[k - 2], llave[k - 1], cero, cero);
                break;
            case 1:
                a += littleEndian(llave[k - 1], cero, cero, cero);
                break;
        }
        resultado = mezcla(a, b, c);
        a = resultado[0];
        b = resultado[1];
        c = resultado[2];
        return c;
    }

    /**
     * Función de dispersión Daniel J. Bernstein.
     * @param llave la llave a dispersar.
     * @return la dispersión de Daniel Bernstein de la llave.
     */
    public static int dispersaDJB(byte[] llave) {
        // Aquí va su código.
        int h = 5381;
        for(int i = 0; i < llave.length; i++)
            h += (h << 5) + (llave[i] & 0xFF);
        return h;
    }
}
