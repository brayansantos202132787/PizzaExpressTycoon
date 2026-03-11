/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzaexpresstycoon.dao;

import pizzaexpresstycoon.database.DataBaseConnection;
import pizzaexpresstycoon.model.Sucursal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author braya
 */
public class SucursalDAO {
    private Connection getConnection() {
        return DataBaseConnection.getInstance().getConnection();
    }

    public boolean insertar(Sucursal sucursal) {
        String sql = "INSERT INTO sucursales (nombre, direccion, activa) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, sucursal.getNombre());
            stmt.setString(2, sucursal.getDireccion());
            stmt.setBoolean(3, sucursal.isActiva());
            int filas = stmt.executeUpdate();
            if (filas > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) sucursal.setId(keys.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[SucursalDAO] Error al insertar: " + e.getMessage());
        }
        return false;
    }

    public boolean actualizar(Sucursal sucursal) {
        String sql = "UPDATE sucursales SET nombre = ?, direccion = ?, activa = ? WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, sucursal.getNombre());
            stmt.setString(2, sucursal.getDireccion());
            stmt.setBoolean(3, sucursal.isActiva());
            stmt.setInt(4, sucursal.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[SucursalDAO] Error al actualizar: " + e.getMessage());
        }
        return false;
    }

    public List<Sucursal> obtenerTodas() {
        List<Sucursal> sucursales = new ArrayList<>();
        String sql = "SELECT * FROM sucursales ORDER BY id ASC";
        try (Statement stmt = getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                sucursales.add(mapearSucursal(rs));
            }
        } catch (SQLException e) {
            System.err.println("[SucursalDAO] Error al obtener todas: " + e.getMessage());
        }
        return sucursales;
    }

    public Sucursal obtenerPorId(int id) {
        String sql = "SELECT * FROM sucursales WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapearSucursal(rs);
        } catch (SQLException e) {
            System.err.println("[SucursalDAO] Error al obtener por id: " + e.getMessage());
        }
        return null;
    }

    private Sucursal mapearSucursal(ResultSet rs) throws SQLException {
        Sucursal s = new Sucursal();
        s.setId(rs.getInt("id"));
        s.setNombre(rs.getString("nombre"));
        s.setDireccion(rs.getString("direccion"));
        s.setActiva(rs.getBoolean("activa"));
        Timestamp fecha = rs.getTimestamp("fecha_creacion");
        if (fecha != null) s.setFechaCreacion(fecha.toLocalDateTime());
        return s;
    }
}
