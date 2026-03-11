/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzaexpresstycoon.util;

import pizzaexpresstycoon.model.Usuario;

/**
 *
 * @author braya
 */
public class SessionManager {
    private static SessionManager instance;
    private Usuario usuarioActivo;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public Usuario getUsuarioActivo() {
        return usuarioActivo;
    }

    public void setUsuarioActivo(Usuario usuario) {
        this.usuarioActivo = usuario;
    }

    public boolean haySesionActiva() {
        return usuarioActivo != null;
    }

    public void cerrarSesion() {
        usuarioActivo = null;
    }
}
