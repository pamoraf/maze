package mx.unam.ciencias.edd.maze;

import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * <p>Clase para realizar las operaciones del laberinto.</p>
 * <p>Esta clase se encarga de ejecutar la generación y resolución de laberintos 
 * de acuerdo a los argumentos proporcionados en la línea de comandos.</p>
 */
public class Maze {
    /* No es deseable hacer objecto de esta clase. */
    private Maze() {}

    /**
     * Generea o resuelve un laberinto dada una configuración propocionada por el usuario.
     * @param args los argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        try {
            Configuracion configuracion = new Configuracion(args);
            switch (configuracion.obtenerAccion()) {
                case RESUELVE:
                    Laberinto laberinto = new Laberinto(configuracion.obtenerDatos());
                    System.out.println(AdministradorLaberinto.dibujaSolucion(laberinto));
                    break;
                case GENERA:
                    AdministradorLaberinto.genera(configuracion);
                    LectorEscritor.escribir(configuracion.obenerFormatoDatos());
                    break;
                default:
                    break;
            }
        } catch(NumberFormatException nfe) { 
            Configuracion.uso();
        } catch (IllegalArgumentException iae) {
            System.err.println(iae.getMessage());
        } catch(IOException ioe) {
            System.err.println(ioe.getMessage());
        } catch(NoSuchElementException nsee) {
            System.err.println(nsee.getMessage());
        } catch(IllegalStateException ise) {
            System.err.println(ise.getMessage());
        }
    }
}
