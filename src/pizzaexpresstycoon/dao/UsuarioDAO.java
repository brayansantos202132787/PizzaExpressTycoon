/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzaexpresstycoon.dao;

import pizzaexpresstycoon.database.DataBaseConnection;
import pizzaexpresstycoon.model.Usuario;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author braya
 */
public class UsuarioDAO {
    
    
    private Connection getConnection() {
        return DataBaseConnection.getInstance().getConnection();
    }

    public Usuario obtenerPorCredenciales(String username, String password) {
        String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ? AND activo = true";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapearUsuario(rs);
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al obtener por credenciales: " + e.getMessage());
        }
        return null;
    }

    public Usuario obtenerPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapearUsuario(rs);
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al obtener por id: " + e.getMessage());
        }
        return null;
    }

    public List<Usuario> obtenerTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (Statement stmt = getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al obtener todos: " + e.getMessage());
        }
        return usuarios;
    }

    public List<Usuario> obtenerPorSucursal(int idSucursal) {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE id_sucursal = ? AND activo = true";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idSucursal);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al obtener por sucursal: " + e.getMessage());
        }
        return usuarios;
    }

    public boolean insertar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (username, password, nombre, apellido, email, id_rol, id_sucursal, activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getUsername());
            stmt.setString(2, usuario.getPassword());
            stmt.setString(3, usuario.getNombre());
            stmt.setString(4, usuario.getApellido());
            stmt.setString(5, usuario.getEmail());
            stmt.setInt(6, usuario.getIdRol());
            if (usuario.getIdSucursal() != null) {
                stmt.setInt(7, usuario.getIdSucursal());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            stmt.setBoolean(8, usuario.isActivo());
            int filas = stmt.executeUpdate();
            if (filas > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    usuario.setId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al insertar: " + e.getMessage());
        }
        return false;
    }

    public boolean actualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET username = ?, password = ?, nombre = ?, apellido = ?, email = ?, id_rol = ?, id_sucursal = ?, activo = ? WHERE id_usuario = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, usuario.getUsername());
            stmt.setString(2, usuario.getPassword());
            stmt.setString(3, usuario.getNombre());
            stmt.setString(4, usuario.getApellido());
            stmt.setString(5, usuario.getEmail());
            stmt.setInt(6, usuario.getIdRol());
            if (usuario.getIdSucursal() != null) {
                stmt.setInt(7, usuario.getIdSucursal());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            stmt.setBoolean(8, usuario.isActivo());
            stmt.setInt(9, usuario.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al actualizar: " + e.getMessage());
        }
        return false;
    }

    public boolean desactivar(int id) {
        String sql = "UPDATE usuarios SET activo = 0 WHERE id_usuario = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al desactivar: " + e.getMessage());
        }
        return false;
    }

    public boolean activar(int id) {
        String sql = "UPDATE usuarios SET activo = 1 WHERE id_usuario = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al activar: " + e.getMessage());
        }
        return false;
    }

    public boolean existeUsername(String username) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE username = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al verificar username: " + e.getMessage());
        }
        return false;
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id_usuario"));
        usuario.setUsername(rs.getString("username"));
        usuario.setPassword(rs.getString("password"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setApellido(rs.getString("apellido"));
        usuario.setEmail(rs.getString("email"));
        usuario.setIdRol(rs.getInt("id_rol"));
        int idSucursal = rs.getInt("id_sucursal");
        usuario.setIdSucursal(rs.wasNull() ? null : idSucursal);
        usuario.setActivo(rs.getBoolean("activo"));
        Timestamp fechaCreacion = rs.getTimestamp("fecha_creacion");
        if (fechaCreacion != null) {
            usuario.setFechaCreacion(fechaCreacion.toLocalDateTime());
        }
        return usuario;
    }
}
