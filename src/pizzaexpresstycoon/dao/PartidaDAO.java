/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzaexpresstycoon.dao;

import pizzaexpresstycoon.model.Partida;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import pizzaexpresstycoon.database.DataBaseConnection;

/**
 *
 * @author braya
 */
public class PartidaDAO {
    private Connection getConnection() {
        return DataBaseConnection.getInstance().getConnection();
    }

    public boolean insertar(Partida partida) {
        String sql = "INSERT INTO partidas (id_usuario, id_sucursal, puntaje_final, nivel_alcanzado, completada) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, partida.getIdUsuario());
            stmt.setInt(2, partida.getIdSucursal());
            stmt.setInt(3, partida.getPuntajeTotal());
            stmt.setInt(4, partida.getNivelAlcanzado());
            stmt.setBoolean(5, false);
            int filas = stmt.executeUpdate();
            if (filas > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    partida.setId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[PartidaDAO] Error al insertar: " + e.getMessage());
        }
        return false;
    }

    public boolean actualizar(Partida partida) {
        String sql = "UPDATE partidas SET puntaje_final = ?, nivel_alcanzado = ?, completada = ? WHERE id_partida = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, partida.getPuntajeTotal());
            stmt.setInt(2, partida.getNivelAlcanzado());
            stmt.setBoolean(3, partida.getEstado() == Partida.Estado.FINALIZADA);
            stmt.setInt(4, partida.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[PartidaDAO] Error al actualizar: " + e.getMessage());
        }
        return false;
    }

    public Partida obtenerPorId(int id) {
        String sql = "SELECT * FROM partidas WHERE id_partida = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapearPartida(rs);
            }
        } catch (SQLException e) {
            System.err.println("[PartidaDAO] Error al obtener por id: " + e.getMessage());
        }
        return null;
    }

    public List<Partida> obtenerPorUsuario(int idUsuario) {
        List<Partida> partidas = new ArrayList<>();
        String sql = "SELECT * FROM partidas WHERE id_usuario = ? ORDER BY fecha_inicio DESC";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                partidas.add(mapearPartida(rs));
            }
        } catch (SQLException e) {
            System.err.println("[PartidaDAO] Error al obtener por usuario: " + e.getMessage());
        }
        return partidas;
    }

    public List<Partida> obtenerPorSucursal(int idSucursal) {
        List<Partida> partidas = new ArrayList<>();
        String sql = "SELECT * FROM partidas WHERE id_sucursal = ? ORDER BY fecha_inicio DESC";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idSucursal);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                partidas.add(mapearPartida(rs));
            }
        } catch (SQLException e) {
            System.err.println("[PartidaDAO] Error al obtener por sucursal: " + e.getMessage());
        }
        return partidas;
    }

    public List<Partida> obtenerTodas() {
        List<Partida> partidas = new ArrayList<>();
        String sql = "SELECT * FROM partidas ORDER BY fecha_inicio DESC";
        try (Statement stmt = getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                partidas.add(mapearPartida(rs));
            }
        } catch (SQLException e) {
            System.err.println("[PartidaDAO] Error al obtener todas: " + e.getMessage());
        }
        return partidas;
    }

    public boolean finalizar(int idPartida) {
        String sql = "UPDATE partidas SET completada = true, fecha_fin = NOW() WHERE id_partida = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idPartida);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[PartidaDAO] Error al finalizar partida: " + e.getMessage());
        }
        return false;
    }

    private Partida mapearPartida(ResultSet rs) throws SQLException {
        Partida partida = new Partida();
        partida.setId(rs.getInt("id_partida"));
        partida.setIdUsuario(rs.getInt("id_usuario"));
        partida.setIdSucursal(rs.getInt("id_sucursal"));
        partida.setPuntajeTotal(rs.getInt("puntaje_final"));
        partida.setNivelAlcanzado(rs.getInt("nivel_alcanzado"));
        boolean completada = rs.getBoolean("completada");
        partida.setEstado(completada ? Partida.Estado.FINALIZADA : Partida.Estado.EN_CURSO);
        Timestamp fechaInicio = rs.getTimestamp("fecha_inicio");
        if (fechaInicio != null) {
            partida.setFechaInicio(fechaInicio.toLocalDateTime());
        }
        Timestamp fechaFin = rs.getTimestamp("fecha_fin");
        if (fechaFin != null) {
            partida.setFechaFin(fechaFin.toLocalDateTime());
        }
        return partida;
    }
}
