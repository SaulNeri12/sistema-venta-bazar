package com.bazar.sistemabazar.controllers.dialogs;

/**
 * Define las operaciones basicas que efecuta un dialogo al que se puede acceder
 * a su objeto afiliado.
 * @param <T> Objeto al que esta sujeto el dialogo.
 */
public interface IDialogoAccesible<T> {
    /**
     * Prepara el dialogo segun el modo de operacion del mismo.
     */
    public void prepararModoOperacion();

    /**
     * Regresa el modo de operacion.
     * @return
     */
    public DialogoOperacion obtenerModoOperacion();

    /**
     * Obtiene el objeto ligado al dialogo.
     * @return
     */
    public T obtenerObjeto();
}
