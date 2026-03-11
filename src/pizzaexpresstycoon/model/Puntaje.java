/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzaexpresstycoon.model;

import java.time.LocalDateTime;

/**
 *
 * @author braya
 */
public class Puntaje {
       public enum Tipo {
        COMPLETADO,
        BONUS_EFICIENCIA,
        CANCELADO,
        NO_ENTREGADO
    }

    private int id;
    private int idPartida;
    private Integer idPedido;
    private Tipo tipo;
    private int puntos;
    private LocalDateTime fecha;

    public Puntaje() {}

    public Puntaje(int id, int idPartida, Integer idPedido, Tipo tipo, int puntos, LocalDateTime fecha) {
        this.id = id;
        this.idPartida = idPartida;
        this.idPedido = idPedido;
        this.tipo = tipo;
        this.puntos = puntos;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(int idPartida) {
        this.idPartida = idPartida;
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public boolean esPositivo() {
        return puntos > 0;
    }

    @Override
    public String toString() {
        return "Puntaje{" +
                "id=" + id +
                ", idPartida=" + idPartida +
                ", idPedido=" + idPedido +
                ", tipo=" + tipo +
                ", puntos=" + puntos +
                ", fecha=" + fecha +
                '}';
    }
}
