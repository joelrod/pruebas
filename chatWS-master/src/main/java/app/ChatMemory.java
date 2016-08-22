package app;

import java.util.ArrayList;
import java.util.List;

/** 
 * Clase principal que se encarga de almacenar las sesiones 
 * FIXME : Mejorar esto haciendo mejor un BEAN de tipo Application 
 */
public class ChatMemory {
	
	// Lista con los nuevos usuarios
	public static List<String> allOnlines = new ArrayList<String>();
	
	public static String lastUserOnline = null;
	
}
