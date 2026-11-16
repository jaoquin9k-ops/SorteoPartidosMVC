package controlador;

import excepciones.NumeroEquiposInvalidoException;
import excepciones.SorteoInvalidoException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import modelo.Partido;
import modelo.Torneo;
import vista.Consola;

public class TorneoController {

    private Torneo modelo;
    private final Consola vista;
    private static final String ARCHIVO_SERIALIZADO = "torneo.ser";

    public TorneoController(Torneo modelo, Consola vista) {
        this.modelo = modelo;
        this.vista = vista;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = vista.mostrarMenuPrincipal(modelo.obtenerNombreEtapaActual(), modelo.getCampeon());
            procesarOpcion(opcion);
        } while (opcion != 0);
    }

    private void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                vista.mostrarEquipos(modelo.getEquiposActivos(), modelo.obtenerNombreEtapaActual());
                break;
            case 2:
                ejecutarSorteoEtapa();
                break;
            case 3:
                ejecutarSimulacionEtapa();
                break;
              case 4:
                guardarEstado();
                break;
            case 5:
                cargarEstado();
                break;
            case 6:
                modelo.cargarEquiposIniciales();
                vista.mostrarMensaje("[OK] Torneo reiniciado con los 16 equipos originales.");
                break;
            case 0:
                vista.mostrarMensaje("Saliendo de la aplicación...");
                break;
            default:
                vista.mostrarError("Opción no válida. Ingrese un número del 0 al 7.");
        }
    }

    private void ejecutarSorteoEtapa() {
        try {
            String nombreEtapa = modelo.obtenerNombreEtapaActual();
            List<Partido> partidos = modelo.sortearEtapaActual();
            vista.mostrarPartidos(nombreEtapa, partidos);
        } catch (NumeroEquiposInvalidoException | SorteoInvalidoException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    private void ejecutarSimulacionEtapa() {
        try {
            String etapaJugada = modelo.obtenerNombreEtapaActual();
            List<Partido> resultados = modelo.simularResultadosEtapa();
            vista.mostrarPartidos("RESULTADOS - " + etapaJugada, resultados);
            if (modelo.getCampeon() != null) {
                vista.mostrarMensaje("¡TENEMOS CAMPEÓN DEL TORNEO!: " + modelo.getCampeon().getNombre().toUpperCase());
            } else {
                vista.mostrarMensaje("[OK] Clasificados listos para: " + modelo.obtenerNombreEtapaActual());
            }
        } catch (SorteoInvalidoException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    private void guardarEstado() {
        try {
            modelo.guardarTorneo(ARCHIVO_SERIALIZADO);
            vista.mostrarMensaje("[OK] Estado del torneo serializado correctamente en '" + ARCHIVO_SERIALIZADO + "'.");
        } catch (IOException e) {
            vista.mostrarError("Error de E/S al guardar el torneo: " + e.getMessage());
        }
    }

    private void cargarEstado() {
        try {
            this.modelo = Torneo.cargarTorneo(ARCHIVO_SERIALIZADO);
            vista.mostrarMensaje("[OK] Torneo cargado exitosamente desde '" + ARCHIVO_SERIALIZADO + "'.");
        } catch (FileNotFoundException e) {
            vista.mostrarError("No se encontró el archivo '" + ARCHIVO_SERIALIZADO + "'. Guarda un torneo primero.");
        } catch (IOException e) {
            vista.mostrarError("Error de E/S al leer el archivo: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            vista.mostrarError("Error de compatibilidad de clases al deserializar: " + e.getMessage());
        }
    }
}