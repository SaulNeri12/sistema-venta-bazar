package com.bazar.sistemabazar.controllers.dialogs;

/**
 * Define las operaciones basicas de acceso y asignacion al objeto de un dialogo.
 * @param <T> Clase controladora de un archivo FXML.
 */
public interface IControlDialogo<T> {

    /**
     * Realiza las operaciones necesarias para guardar la informacion recibida
     * en el dialogo para ser transferida al objeto.
     */
    public void guardarCambios();

    /**
     * Restaura cualquier cambio hecho a la informacion del dialogo y la vuelve a su
     * estado original de cuando se creo el dialogo.
     */
    public void restaurarCambios();

    /**
     * Obtiene el objeto ligado al dialogo.
     * @return
     */
    public T obtenerObjeto();

    /**
     * Asigna un nuevo valor del objeto del dialogo.
     * @param objeto Objeto nuevo.
     */
    public void asignarObjeto(T objeto);
}
