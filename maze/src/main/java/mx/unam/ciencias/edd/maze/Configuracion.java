package mx.unam.ciencias.edd.maze;

import java.io.IOException;
import mx.unam.ciencias.edd.Lista;

/**
 * <p>Clase para gestionar las acciones y datos del programa.</p>
 */
public class Configuracion {
    private final String FORMATO = "MAZE";
    private Accion accion;
    private byte ancho;
    private byte alto;
    private long semilla;
    private byte[][] datos;
    private Lista<Byte> maze;

    /**
     * Inicializa la configuración con los argumentos proporcionados.
     * @param args Argumentos propocionados.
     * @throws IOException Si ocurrió un error al leer de la entrada estándar.
     */
    public Configuracion(String[] args) throws IOException {
        // Establece la semilla por defecto en la hora de la computadora.
        semilla = System.currentTimeMillis();
        // Acción por defecto.
        accion = Accion.RESUELVE;
        maze = new Lista<>();
        // Si no se propocionaron argumentos lee de la entrada estándar.
        if(args.length != 0)
            validaArgumentos(args);
        else {
            maze = LectorEscritor.leer();
            validaFormato(maze);
            byte altura = maze.eliminaPrimero();
            byte ancho = maze.eliminaPrimero();
            establecerAltura((altura & 0xFF));
            establecerAncho((ancho & 0xFF));
            establecerDatos(maze);
        }
    }

    /*
     * Valida los argumentos propocionados siguiendo el siguiente cirterio.
     * -g Indica que se va a generar un laberinto.
     * -s Indica que se debe propocionar una semilla.
     * -h Indica la altura del laberinto.
     * -w Indica el ancho del laberinto.
     * Todos los parámetros deben ir acompañados de un número excepto -g.
     * Si no se introdujo alguno de los mencionados manda una excpción.
     */
    private void validaArgumentos(String[] args) {
        boolean[] argumentosDisponibles = new boolean[3];
        for(int i = 0; i < args.length; i++) {
            String actual = args[i].trim();
            String siguiente = obtenerCadena(args, ++i).trim();
            switch(actual) {
                case "-g":
                    i--;
                    accion = Accion.GENERA;
                    argumentosDisponibles[0] = true;
                    break;
                case "-s":
                    establecerSemilla(Long.parseLong(siguiente));
                    break;
                case "-h":
                    establecerAltura(Integer.parseInt(siguiente));
                    argumentosDisponibles[1] = true;
                    break;
                case "-w":
                    establecerAncho(Integer.parseInt(siguiente));
                    argumentosDisponibles[2] = true;
                    break;
                default:
                    throw new IllegalArgumentException("Función inválida");
            }
        }
        boolean pivote = true;
        for(boolean argumento : argumentosDisponibles)
            pivote &= argumento;
        if(!pivote) {
            uso();
            throw new IllegalStateException("La forma en la que se introdujeron los argumentos es inválida.");
        }
    }

    /*
     * Obtiene una cadena sin espacios, en un arreglo de forma segura.
     */
    private String obtenerCadena(String[] args, int indice) {
        if(indice < args.length && indice >= 0)
            return args[indice].trim();
        return "";
    }

    /*
     * Verica que el archivo empiece con la frase "MAZE".
     */
    private void validaFormato(Lista<Byte> entrada) {
        String formato = "";
        for(int i = 0; i < 4; i++)
            formato += (char) (entrada.eliminaPrimero() & 0xFF);
        if(!formato.equals(FORMATO))
            throw new IllegalArgumentException("Formato inválido");
    }

    /*
     * Establece el valor de la semilla del generador.
     */
    private void establecerSemilla(long semilla) {
        this.semilla = semilla;
    }

    /*
     * Establece la altura en bytes dado un entero.
     * Si la altura no esta en el rango del 2 al 255 manda una excepción.
     */
    private void establecerAltura(int alto) {
        if(alto < 2 || alto > 255)
            throw new IllegalArgumentException("El alto debe de estar entre 2 y 255.");
        this.alto = (byte) alto;
    }

    /*
     * Establece la anchura en bytes dado un entero.
     * Si la anchura no esta en el rango del 2 al 255 manda una excepción.
     */
    private void establecerAncho(int ancho) {
        if(ancho < 2 || ancho > 255)
            throw new IllegalArgumentException("El ancho debe de estar entre 2 y 255.");  
        this.ancho = (byte) ancho;
    }

    /*
     * Establece el arreglo de dos dimensiones de los datos de los cuartos
     * teniendo el cuenta el orden de estos en el laberinto.
     * Si la cantidad de datos no es congruente con los datos del laberinto,
     * manda una excepción.
     */
    private void establecerDatos(Lista<Byte> maze) {
        if(maze.getElementos() != obtenerAncho() * obtenerAlto())
            throw new IllegalArgumentException("Ancho o alto incoherentes.");
        datos = new byte[obtenerAlto()][obtenerAncho()];
        int x = 0; 
        int y = 0;
        for(byte dato : maze) {
            if(x == obtenerAncho()) {
                x = 0;
                y++;
            }
            datos[y][x] = dato;
            x++;
        }
    }

    /**
     * Muestra como debe usarse el programa.
     */
    public static void uso() {
        System.err.println("Uso genera (-s es opcional) : java -jar target/maze.jar" +
			   " -g -s <numero> -w <numero> -h <numero> > <recurso salida>");
        System.err.println("Uso resuelve : java -jar target/maze.jar < <recurso entrada> > <recurso salida>");
    }

    /**
     * Establecce el arreglo de dos dimensiones de los datos de los cuartos
     * teniendo en cuenta un arreglo de dos dimensiones de cuartos.
     * @param cuartos El arreglo de dos dimensiones de cuartos.
     */
    public void establecerDatos(Cuarto[][] cuartos) {
        ancho = (byte) cuartos[0].length;
        alto = (byte) cuartos.length;
        // Borra los datos actuales.
        maze.limpia();
        // Establece al formato.
        for(int i = 0; i < FORMATO.length(); i++)
            maze.agrega((byte) FORMATO.charAt(i));
        maze.agrega(alto);
        maze.agrega(ancho);
        // Agrega los datos de los cuartos.
        for(int y = 0; y < obtenerAlto(); y++) {
            for(int x = 0; x < obtenerAncho(); x++)
                maze.agrega(cuartos[y][x].obtenerDato());
        }
    }
    
    /**
     * Regresa los datos de los cuartos del laberinto.
     * @return Un arreglo de bytes de dos dimensiones con los datos del laberinto.
     */
    public byte[][] obtenerDatos() {
        return datos;
    }

    /**
     * Regresa los datos del laberinto con toda la información requerida por el programa.
     * @return Una lista de los datos del laberinto.
     */
    public Lista<Byte> obenerFormatoDatos() {
        return maze;
    }

    /**
     * Regresa la acción por realizar.
     * @return La acción por realizar.
     */
    public Accion obtenerAccion() {
        return accion;
    }

    /**
     * Regresa la semilla dada por el programa.
     * @return La semilla dada por el programa.
     */
    public long obtenerSemilla() {
        return semilla;
    }
    
    /**
     * Regresa el ancho del laberinto convierte el byte a entero.
     * @return El ancho del laberinto.
     */
    public int obtenerAncho() {
        return ancho & 0xFF;
    }

    /**
     * Regresa el alto del laberinto convierte el byte a un entero.
     * @return El alto del laberinto.
     */
    public int obtenerAlto() {
        return alto & 0xFF;
    }
}
