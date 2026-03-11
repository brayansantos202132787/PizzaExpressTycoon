/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzaexpresstycoon.dao;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import pizzaexpresstycoon.database.DataBaseConnection;
import pizzaexpresstycoon.model.Producto;
/**
 *
 * @author braya
 */
public class ProductoDAO {
    
    private Connection getConnection() {
        return DataBaseConnection.getInstance().getConnection();
    }

    public boolean insertar(Producto producto) {
        String sql = "INSERT INTO productos (nombre, descripcion, precio, id_sucursal, activo) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getDescripcion());
            stmt.setDouble(3, 0.0);
            stmt.setInt(4, producto.getIdSucursal());
            stmt.setBoolean(5, producto.isActivo());
            int filas = stmt.executeUpdate();
            if (filas > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    producto.setId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[ProductoDAO] Error al insertar: " + e.getMessage());
        }
        return false;
    }

    public boolean actualizar(Producto producto) {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, activo = ? WHERE id_producto = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getDescripcion());
            stmt.setBoolean(3, producto.isActivo());
            stmt.setInt(4, producto.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProductoDAO] Error al actualizar: " + e.getMessage());
        }
        return false;
    }

    public boolean desactivar(int id) {
        String sql = "UPDATE productos SET activo = false WHERE id_producto = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProductoDAO] Error al desactivar: " + e.getMessage());
        }
        return false;
    }

    public boolean activar(int id) {
        String sql = "UPDATE productos SET activo = true WHERE id_producto = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProductoDAO] Error al activar: " + e.getMessage());
        }
        return false;
    }

    public Producto obtenerPorId(int id) {
        String sql = "SELECT * FROM productos WHERE id_producto = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapearProducto(rs);
            }
        } catch (SQLException e) {
            System.err.println("[ProductoDAO] Error al obtener por id: " + e.getMessage());
        }
        return null;
    }

    public List<Producto> obtenerPorSucursal(int idSucursal) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE id_sucursal = ? ORDER BY nombre ASC";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idSucursal);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ProductoDAO] Error al obtener por sucursal: " + e.getMessage());
        }
        return productos;
    }

    public List<Producto> obtenerActivosPorSucursal(int idSucursal) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE id_sucursal = ? AND activo = true ORDER BY nombre ASC";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idSucursal);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ProductoDAO] Error al obtener activos por sucursal: " + e.getMessage());
        }
        return productos;
    }

    public List<Producto> obtenerTodos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos ORDER BY id_sucursal, nombre ASC";
        try (Statement stmt = getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ProductoDAO] Error al obtener todos: " + e.getMessage());
        }
        return productos;
    }

    public boolean existeNombreEnSucursal(String nombre, int idSucursal) {
        String sql = "SELECT COUNT(*) FROM productos WHERE nombre = ? AND id_sucursal = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setInt(2, idSucursal);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("[ProductoDAO] Error al verificar nombre en sucursal: " + e.getMessage());
        }
        return false;
    }

    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setId(rs.getInt("id_producto"));
        producto.setNombre(rs.getString("nombre"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setIdSucursal(rs.getInt("id_sucursal"));
        producto.setActivo(rs.getBoolean("activo"));
        return producto;
    }
}
