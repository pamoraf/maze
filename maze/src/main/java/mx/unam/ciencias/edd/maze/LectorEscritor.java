package mx.unam.ciencias.edd.maze;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import mx.unam.ciencias.edd.Lista;

/**
 * <p>Clase para gestionar la entrada y la salida de bytes.</p>
 * <p>Propociona comportamiento para leer y escribir datos.</p>
 */
public class LectorEscritor {

	/* No es deseable hacer objetos de esta clase. */
	private LectorEscritor() {};

	/**
	 * Lee los bytes de la entrada estándar.
	 * @throws IOException Si ocurrió un error al leer.
	 * @return Ls lista de bytes leída.
	 */
	public static Lista<Byte> leer() throws IOException {
		InputStream lector = System.in;
		Lista<Byte> entrada = new Lista<>();
		for(byte dato : lector.readAllBytes())
			entrada.agrega(dato);
		lector.close();
		return entrada;
	}

	/**
	 * Permite escribir los bytes generados en la salida estándar.
	 * @param datos Los bytes a escribir.
	 * @throws IOException Si ocurrió un error al escribir.
	 */
	public static void escribir(Lista<Byte> datos) throws IOException {
		OutputStream escritor = System.out;
		for(byte dato : datos)
			escritor.write(dato);
		// Limpia la salida estándar.
		escritor.flush();
	}
}
