package com.bazar.sistemabazar.adaptador.principal;

import com.bazar.sistemabazar.controllers.BazarController;

/**
 * Adapta la interfaz de usuario del frame principal (BazarController) para que solo
 * muestre las operacione permitidas para un gerente.
 */
public class AdaptadorUsuarioGUIGerente implements IAdaptadorUsuarioGUI {
    @Override
    public void ejecutar(BazarController controladorBazar) {
        // opciones de menu ventas
        //controladorBazar.menuVentas.setVisible(false);
        // opciones de menu productos
        controladorBazar.menuProductosAgregarProducto.setVisible(false);
        controladorBazar.menuProductosModificarProducto.setVisible(false);
        controladorBazar.menuProductosEliminarProducto.setVisible(false);
        // opciones de usuarios
        controladorBazar.menuUsuariosAgregarUsuario.setVisible(false);
        controladorBazar.menuUsuariosEliminarUsuario.setVisible(false);
        controladorBazar.menuUsuariosModificarUsuario.setVisible(false);
    }
}
