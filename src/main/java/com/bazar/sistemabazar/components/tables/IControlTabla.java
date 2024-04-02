package com.bazar.sistemabazar.components.tables;

import javafx.scene.control.TableView;

/**
 * Define las operaciones basicas de una tabla ligada a un objeto.
 * @param <T> Objeto.
 */
public interface IControlTabla<T> {

    /**
     * Regresa la informacion de la fila seleccionada en especificado.
     * @return null Si no se selecciono ningun elemento en la tabla.
     */
    public T obtenerFilaObjetoSeleccionado();

    /**
     * Obtiene el objeto de la que contenga informacion del objeto especificado.
     * @param objeto Objeto a buscar en la tabla.
     * @return null si no se encontro.
     */
    public T obtenerFilaObjeto(T objeto);

    /**
     * Obtiene el objeto en la fila especificada por el indice (empezando desde 0).
     * @param indice
     * @return null si no se encontro el objeto en la fila especificada.
     */
    public T obtenerFilaObjetoPorIndice(int indice);

    /**
     * Agrega un objeto como fila en la tabla.
     * @param objeto Objeto a agregar.
     */
    public void agregarFilaObjeto(T objeto);

    /**
     * Elimina la fila en la tabla que contenga informacion del objeto especificado.
     * @param objeto Objeto que se eliminara.
     * @return
     */
    public boolean eliminarFilaObjeto(T objeto);

    /**
     * Elimina la fila en la tabla con el indice de posicion especificado (empezando desde 0).
     * @param indice
     * @return true si se elimino la fila con el indica dado, false caso contrario.
     */
    public boolean eliminarFilaObjetoPorIndice(int indice);

    /**
     * Elimina todas las filas de la tabla.
     * @return true si se eliminaron filas, null en caso contrario.
     */
    public boolean eliminarTodasLasFilas();
}
