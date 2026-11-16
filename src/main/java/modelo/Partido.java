package modelo;

import java.io.Serializable;

public class Partido implements Serializable {

    private static final long serialVersionUID = 1L;
    private final Equipo local;
    private final Equipo visitante;
    private Equipo ganador;

    public Partido(Equipo local, Equipo visitante) {
        this.local = local;
        this.visitante = visitante;
        this.ganador = null;
    }

    public Equipo getLocal() {
        return local;
    }

    public Equipo getVisitante() {
        return visitante;
    }

    public Equipo getGanador() {
        return ganador;
    }

    public void setGanador(Equipo ganador) {
        this.ganador = ganador;
    }

    @Override
    public String toString() {
        String infoGanador = (ganador != null) ? "  -->  [Clasificado: " + ganador.getNombre() + "]" : "";
        return String.format("%-28s vs  %-28s%s", local.getNombre(), visitante.getNombre(), infoGanador);
    }
}