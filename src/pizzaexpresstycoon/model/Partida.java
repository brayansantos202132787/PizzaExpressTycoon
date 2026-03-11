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
public class Partida {
    public enum Estado {
        EN_CURSO,
        FINALIZADA
    }

    private int id;
    private int idUsuario;
    private int idSucursal;
    private int puntajeTotal;
    private int nivelAlcanzado;
    private int pedidosCompletados;
    private int pedidosCancelados;
    private int pedidosNoEntregados;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Estado estado;

    public Partida() {
        this.puntajeTotal = 0;
        this.nivelAlcanzado = 1;
        this.pedidosCompletados = 0;
        this.pedidosCancelados = 0;
        this.pedidosNoEntregados = 0;
        this.estado = Estado.EN_CURSO;
    }

    public Partida(int id, int idUsuario, int idSucursal, int puntajeTotal, int nivelAlcanzado,
                   int pedidosCompletados, int pedidosCancelados, int pedidosNoEntregados,
                   LocalDateTime fechaInicio, LocalDateTime fechaFin, Estado estado) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idSucursal = idSucursal;
        this.puntajeTotal = puntajeTotal;
        this.nivelAlcanzado = nivelAlcanzado;
        this.pedidosCompletados = pedidosCompletados;
        this.pedidosCancelados = pedidosCancelados;
        this.pedidosNoEntregados = pedidosNoEntregados;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public int getPuntajeTotal() {
        return puntajeTotal;
    }

    public void setPuntajeTotal(int puntajeTotal) {
        this.puntajeTotal = puntajeTotal;
    }

    public int getNivelAlcanzado() {
        return nivelAlcanzado;
    }

    public void setNivelAlcanzado(int nivelAlcanzado) {
        this.nivelAlcanzado = nivelAlcanzado;
    }

    public int getPedidosCompletados() {
        return pedidosCompletados;
    }

    public void setPedidosCompletados(int pedidosCompletados) {
        this.pedidosCompletados = pedidosCompletados;
    }

    public int getPedidosCancelados() {
        return pedidosCancelados;
    }

    public void setPedidosCancelados(int pedidosCancelados) {
        this.pedidosCancelados = pedidosCancelados;
    }

    public int getPedidosNoEntregados() {
        return pedidosNoEntregados;
    }

    public void setPedidosNoEntregados(int pedidosNoEntregados) {
        this.pedidosNoEntregados = pedidosNoEntregados;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public boolean isEnCurso() {
        return this.estado == Estado.EN_CURSO;
    }

    public void agregarPuntaje(int puntos) {
        this.puntajeTotal += puntos;
    }

    @Override
    public String toString() {
        return "Partida{" +
                "id=" + id +
                ", idUsuario=" + idUsuario +
                ", idSucursal=" + idSucursal +
                ", puntajeTotal=" + puntajeTotal +
                ", nivelAlcanzado=" + nivelAlcanzado +
                ", pedidosCompletados=" + pedidosCompletados +
                ", pedidosCancelados=" + pedidosCancelados +
                ", pedidosNoEntregados=" + pedidosNoEntregados +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", estado=" + estado +
                '}';
    }
}
