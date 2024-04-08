package com.bazar.sistemabazar.components.tables;

import javafx.scene.control.TableView;

/**
 * Define las operaciones basicas de una tabla ligada a un objeto.
 * @param <T> Objeto que representara a la tabla.
 */
public interface IControlTabla<T> {

    /**
     * Prepara el formato de columnas y celdas a un formato definido por el modelo de la tabla en cuestion.
     */
    public void inicializar();

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
     * @param indice Posicion (partiendo desde 0) de la fila que se quiere obtener su dato.
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
     * @return true si se pudo eliminar la fila con el objeto especificado.
     */
    public boolean eliminarFilaObjeto(T objeto);

    /**
     * Elimina la fila en la tabla con el indice de posicion especificado (empezando desde 0).
     * @param indice posicion (partiendo desde 0) de la fila que se quiere eliminar.
     * @return true si se elimino la fila con el indica dado, false caso contrario.
     */
    public boolean eliminarFilaObjetoPorIndice(int indice);

    /**
     * Elimina todas las filas de la tabla.
     */
    public void eliminarTodasLasFilas();
}
