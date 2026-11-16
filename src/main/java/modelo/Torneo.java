package modelo;

import excepciones.NumeroEquiposInvalidoException;
import excepciones.SorteoInvalidoException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Torneo implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<Equipo> equiposActivos;
    private final Map<String, List<Partido>> historialEtapas;
    private List<Partido> partidosEtapaActual;
    private boolean etapaSorteada;
    private Equipo campeon;
    private final Random random;

    public Torneo() {
        this.equiposActivos = new ArrayList<>();
        this.historialEtapas = new LinkedHashMap<>();
        this.partidosEtapaActual = new ArrayList<>();
        this.etapaSorteada = false;
        this.campeon = null;
        this.random = new Random();
        cargarEquiposIniciales();
    }

    public final void cargarEquiposIniciales() {
        equiposActivos.clear();
        historialEtapas.clear();
        partidosEtapaActual.clear();
        etapaSorteada = false;
        campeon = null;

        String[] nombres = {
            "chancla fc",
            "calientabancas",
            "los profesionales",
            "casi gol",
            "deportivo aguateros",
            "90 minutos fc",
            "lesionados",
            "los mas queridos",
            "arbitros cf",
            "solteros fc",
            "amarrados fc",
            "deportivo mandarinas",
            "sociedad deportiva anonimos",
            "balon desinflado fc",
            "deportivo tachuela",
            "la redonda"
        };

        for (String nombre : nombres) {
            equiposActivos.add(new Equipo(nombre));
        }
    }

    public String obtenerNombreEtapaActual() {
        switch (equiposActivos.size()) {
            case 16:
                return "OCTAVOS DE FINAL";
            case 8:
                return "CUARTOS DE FINAL";
            case 4:
                return "SEMIFINALES";
            case 2:
                return "FINAL";
            case 1:
                return "TORNEO FINALIZADO";
            default:
                return "ETAPA DESCONOCIDA (" + equiposActivos.size() + " equipos)";
        }
    }

    private boolean esPotenciaDeDos(int n) {
        return n >= 2 && (n & (n - 1)) == 0;
    }

    public List<Partido> sortearEtapaActual() throws NumeroEquiposInvalidoException, SorteoInvalidoException {
        if (campeon != null) {
            throw new SorteoInvalidoException("Torneo finalizado. El campeón es: " + campeon.getNombre());
        }

        if (etapaSorteada) {
            throw new SorteoInvalidoException("La etapa actual (" + obtenerNombreEtapaActual() + ") ya fue sorteada. Debes disputar los partidos antes de sortear la siguiente.");
        }

        int cantidad = equiposActivos.size();
        if (!esPotenciaDeDos(cantidad)) {
            throw new NumeroEquiposInvalidoException(
                "Cantidad inválida de equipos (" + cantidad + "). Se requiere una potencia de 2 (16, 8, 4 o 2) para eliminación directa."
            );
        }

        List<Equipo> disponibles = new ArrayList<>(equiposActivos);
        List<Partido> nuevosPartidos = new ArrayList<>();
        Set<Equipo> emparejadosEnEtapa = new HashSet<>();

        emparejarRecursivo(disponibles, nuevosPartidos, emparejadosEnEtapa);

        this.partidosEtapaActual = nuevosPartidos;
        this.etapaSorteada = true;
        this.historialEtapas.put(obtenerNombreEtapaActual(), new ArrayList<>(nuevosPartidos));

        return nuevosPartidos;
    }

    /**
     * empareja aleatoriamente los equipos disponibles
     * garantizando que ningún equipo se enfrente a sí mismo.
     */
    private void emparejarRecursivo(List<Equipo> disponibles, List<Partido> partidosGenerados, Set<Equipo> yaEmparejados)
            throws SorteoInvalidoException {

        if (disponibles.isEmpty()) {
            return;
        }

        if (disponibles.size() % 2 != 0) {
            throw new SorteoInvalidoException("Error lógico en el sorteo recursivo: quedó un número impar de equipos sin pareja.");
        }

        int indiceLocal = random.nextInt(disponibles.size());
        Equipo local = disponibles.remove(indiceLocal);

        int indiceVisitante = random.nextInt(disponibles.size());
        Equipo visitante = disponibles.remove(indiceVisitante);

        if (local == null || visitante == null || local.equals(visitante)) {
            throw new SorteoInvalidoException("Error en asignación: un equipo fue emparejado consigo mismo o es nulo.");
        }

        if (!yaEmparejados.add(local) || !yaEmparejados.add(visitante)) {
            throw new SorteoInvalidoException("Error de duplicidad: un equipo intentó jugar más de un partido en la misma etapa.");
        }

        partidosGenerados.add(new Partido(local, visitante));

        emparejarRecursivo(disponibles, partidosGenerados, yaEmparejados);
    }

    public List<Partido> simularResultadosEtapa() throws SorteoInvalidoException {
        if (!etapaSorteada || partidosEtapaActual.isEmpty()) {
            throw new SorteoInvalidoException("Primero debes realizar el sorteo de la etapa actual antes de simular resultados.");
        }

        List<Equipo> ganadores = new ArrayList<>();
        for (Partido partido : partidosEtapaActual) {
            Equipo ganador = random.nextBoolean() ? partido.getLocal() : partido.getVisitante();
            partido.setGanador(ganador);
            ganadores.add(ganador);
        }

        this.equiposActivos = ganadores;
        this.etapaSorteada = false;

        if (equiposActivos.size() == 1) {
            this.campeon = equiposActivos.get(0);
        }

        return partidosEtapaActual;
    }

    public void guardarTorneo(String rutaArchivo) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
            oos.writeObject(this);
        }
    }
    public static Torneo cargarTorneo(String rutaArchivo) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rutaArchivo))) {
            return (Torneo) ois.readObject();
        }
    }

    public List<Equipo> getEquiposActivos() {
        return equiposActivos;
    }

    public Map<String, List<Partido>> getHistorialEtapas() {
        return historialEtapas;
    }

    public Equipo getCampeon() {
        return campeon;
    }
}