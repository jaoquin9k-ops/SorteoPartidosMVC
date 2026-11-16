package vista;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import modelo.Equipo;
import modelo.Partido;

public class Consola {

    private final Scanner scanner;

    public Consola() {
        this.scanner = new Scanner(System.in);
    }

    public int mostrarMenuPrincipal(String etapaActual, Equipo campeon) {
        System.out.println("       SISTEMA DE SORTEO DE TORNEO (MVC + RECURSIVO)");
        if (campeon != null) {
            System.out.println(" ESTADO: TORNEO FINALIZADO | CAMPEÓN: " + campeon.getNombre().toUpperCase());
        } else {
            System.out.println(" ETAPA ACTUAL: " + etapaActual);
        }
        System.out.println(" 1. Ver equipos en competencia");
        System.out.println(" 2. Sortear partidos de la etapa actual (Recursivo)");
        System.out.println(" 3. Disputar partidos y avanzar ganadores");
        System.out.println(" 4. Ver historial completo de llaves");
        System.out.println(" 5. Guardar progreso del torneo (torneo.ser)");
        System.out.println(" 6. Cargar progreso del torneo (torneo.ser)");
        System.out.println(" 7. Reiniciar torneo con los 16 equipos");
        System.out.println(" 0. Salir");
        System.out.print("Seleccione una opción: ");

        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void mostrarEquipos(List<Equipo> equipos, String etapa) {
        System.out.println("EQUIPOS ACTIVOS (" + etapa + " - Total: " + equipos.size() + ") ");
        for (int i = 0; i < equipos.size(); i++) {
            System.out.printf(" %2d. %s%n", (i + 1), equipos.get(i).getNombre());
        }
    }

    public void mostrarPartidos(String tituloEtapa, List<Partido> partidos) {
        System.out.println("LLAVES DE " + tituloEtapa + " ");
        for (int i = 0; i < partidos.size(); i++) {
            System.out.printf(" Partido %d: %s%n", (i + 1), partidos.get(i));
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println("\n" + mensaje);
    }

    public void mostrarError(String error) {
        System.err.println("\n[ERROR] " + error);
    }
}