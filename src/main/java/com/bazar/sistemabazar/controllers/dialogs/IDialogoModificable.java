package com.bazar.sistemabazar.controllers.dialogs;

/**
 * Define las operaciones basicas que efecuta un dialogo el cual se puede
 * modificar sus campos y datos del objeto afiliado a este.
 * @param <T> Objeto al cual el dialogo esta sujeto.
 */
public interface IDialogoModificable<T> {
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
     * Asigna el modo de operacion que realizara el dialogo.
     * @param modo Modo de operacion
     */
    public void asignarModoOperacion(DialogoOperacion modo);

    /**
     * Asigna un nuevo valor del objeto del dialogo.
     * @param objeto Objeto nuevo.
     */
    public void asignarObjeto(T objeto);
}
