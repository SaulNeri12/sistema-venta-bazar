package com.bazar.sistemabazar.adaptador.principal;

import com.bazar.sistemabazar.controllers.BazarController;

/**
 * Encapsula una operacion especifica para adaptar la interfaz del controlador BazarController
 * segun el puesto de un usuario, permitiendo mostrar solo las opciones habilitadas para este.
 */
public interface IAdaptadorUsuarioGUI {

    /**
     * Ejecuta la operacion definida para el usuario.
     * @param controladorBazar Controlador del frame principal que contiene los elementos de la interfaz grafica
     * necesarios para la operacion de adaptamiento.
     */
    public void ejecutar(BazarController controladorBazar);
}
