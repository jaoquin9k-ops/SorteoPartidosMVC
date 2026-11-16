package main;

import controlador.TorneoController;
import modelo.Torneo;
import vista.Consola;

public class Main {
    public static void main(String[] args) {
        Torneo modelo = new Torneo();
        Consola vista = new Consola();
        TorneoController controlador = new TorneoController(modelo, vista);
        controlador.iniciar();
    }
}