/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzaexpresstycoon.dao;


import pizzaexpresstycoon.model.Pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import pizzaexpresstycoon.database.DataBaseConnection;

/**
 *
 * @author braya
 */
public class PedidoDAO {
     private Connection getConnection() {
        return DataBaseConnection.getInstance().getConnection();
    }

    public boolean insertar(Pedido pedido) {
        String sql = "INSERT INTO pedidos (id_partida, estado, nivel_al_generar, tiempo_limite_segundos) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, pedido.getIdPartida());
            stmt.setString(2, pedido.getEstado().name());
            stmt.setInt(3, pedido.getNivelAlCrear());
            stmt.setInt(4, pedido.getTiempoLimiteSegundos());
            int filas = stmt.executeUpdate();
            if (filas > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    pedido.setId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[PedidoDAO] Error al insertar: " + e.getMessage());
        }
        return false;
    }

    public boolean actualizarEstado(int idPedido, Pedido.Estado estadoAnterior, Pedido.Estado estadoNuevo) {
        String sqlPedido = "UPDATE pedidos SET estado = ? WHERE id_pedido = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sqlPedido)) {
            stmt.setString(1, estadoNuevo.name());
            stmt.setInt(2, idPedido);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[PedidoDAO] Error al actualizar estado: " + e.getMessage());
        }
        return false;
    }

    public boolean actualizar(Pedido pedido) {
        String sql = "UPDATE pedidos SET estado = ?, puntos_obtenidos = ?, fecha_entrega = ? WHERE id_pedido = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, pedido.getEstado().name());
            stmt.setInt(2, pedido.getPuntosObtenidos());
            if (pedido.getFechaFinalizacion() != null) {
                stmt.setTimestamp(3, Timestamp.valueOf(pedido.getFechaFinalizacion()));
            } else {
                stmt.setNull(3, Types.TIMESTAMP);
            }
            stmt.setInt(4, pedido.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[PedidoDAO] Error al actualizar pedido: " + e.getMessage());
        }
        return false;
    }

    public Pedido obtenerPorId(int id) {
        String sql = "SELECT * FROM pedidos WHERE id_pedido = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapearPedido(rs);
            }
        } catch (SQLException e) {
            System.err.println("[PedidoDAO] Error al obtener por id: " + e.getMessage());
        }
        return null;
    }

    public List<Pedido> obtenerPorPartida(int idPartida) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE id_partida = ? ORDER BY fecha_creacion ASC";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idPartida);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                pedidos.add(mapearPedido(rs));
            }
        } catch (SQLException e) {
            System.err.println("[PedidoDAO] Error al obtener por partida: " + e.getMessage());
        }
        return pedidos;
    }

    public List<Pedido> obtenerActivosPorPartida(int idPartida) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE id_partida = ? AND estado NOT IN ('ENTREGADA','CANCELADA','NO_ENTREGADO') ORDER BY fecha_creacion ASC";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idPartida);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                pedidos.add(mapearPedido(rs));
            }
        } catch (SQLException e) {
            System.err.println("[PedidoDAO] Error al obtener activos por partida: " + e.getMessage());
        }
        return pedidos;
    }

    public boolean marcarNoEntregados(int idPartida) {
        String sql = "UPDATE pedidos SET estado = 'NO_ENTREGADO', fecha_entrega = NOW() " +
                     "WHERE id_partida = ? AND estado NOT IN ('ENTREGADA','CANCELADA','NO_ENTREGADO')";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idPartida);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("[PedidoDAO] Error al marcar no entregados: " + e.getMessage());
        }
        return false;
    }

    public int contarActivosPorPartida(int idPartida) {
        String sql = "SELECT COUNT(*) FROM pedidos WHERE id_partida = ? AND estado NOT IN ('ENTREGADA','CANCELADA','NO_ENTREGADO')";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idPartida);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[PedidoDAO] Error al contar activos: " + e.getMessage());
        }
        return 0;
    }

    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setId(rs.getInt("id_pedido"));
        pedido.setIdPartida(rs.getInt("id_partida"));
        pedido.setEstado(Pedido.Estado.valueOf(rs.getString("estado")));
        pedido.setNivelAlCrear(rs.getInt("nivel_al_generar"));
        pedido.setTiempoLimiteSegundos(rs.getInt("tiempo_limite_segundos"));
        pedido.setPuntosObtenidos(rs.getInt("puntos_obtenidos"));
        Timestamp fechaCreacion = rs.getTimestamp("fecha_creacion");
        if (fechaCreacion != null) {
            pedido.setFechaCreacion(fechaCreacion.toLocalDateTime());
        }
        Timestamp fechaEntrega = rs.getTimestamp("fecha_entrega");
        if (fechaEntrega != null) {
            pedido.setFechaFinalizacion(fechaEntrega.toLocalDateTime());
        }
        return pedido;
    }

}
