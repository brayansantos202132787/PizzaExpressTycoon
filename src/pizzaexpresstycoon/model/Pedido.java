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
public class Pedido {
     public enum Estado {
        RECIBIDA,
        PREPARANDO,
        EN_HORNO,
        ENTREGADA,
        CANCELADA,
        NO_ENTREGADO
    }

    private int id;
    private int idPartida;
    private Estado estado;
    private int nivelAlCrear;
    private int tiempoLimiteSegundos;
    private int tiempoTranscurridoSegundos;
    private int puntosObtenidos;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaFinalizacion;

    public Pedido() {
        this.estado = Estado.RECIBIDA;
        this.nivelAlCrear = 1;
        this.tiempoTranscurridoSegundos = 0;
        this.puntosObtenidos = 0;
    }

    public Pedido(int id, int idPartida, Estado estado, int nivelAlCrear, int tiempoLimiteSegundos,
                  int tiempoTranscurridoSegundos, int puntosObtenidos,
                  LocalDateTime fechaCreacion, LocalDateTime fechaFinalizacion) {
        this.id = id;
        this.idPartida = idPartida;
        this.estado = estado;
        this.nivelAlCrear = nivelAlCrear;
        this.tiempoLimiteSegundos = tiempoLimiteSegundos;
        this.tiempoTranscurridoSegundos = tiempoTranscurridoSegundos;
        this.puntosObtenidos = puntosObtenidos;
        this.fechaCreacion = fechaCreacion;
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public boolean puedeAvanzarEstado() {
        return estado == Estado.RECIBIDA
                || estado == Estado.PREPARANDO
                || estado == Estado.EN_HORNO;
    }

    public boolean puedeCancelarse() {
        return estado == Estado.RECIBIDA || estado == Estado.PREPARANDO;
    }

    public boolean estaFinalizado() {
        return estado == Estado.ENTREGADA
                || estado == Estado.CANCELADA
                || estado == Estado.NO_ENTREGADO;
    }

    public boolean tiempoExpirado() {
        return tiempoTranscurridoSegundos >= tiempoLimiteSegundos;
    }

    public boolean esEficiente() {
        return tiempoTranscurridoSegundos <= (tiempoLimiteSegundos / 2);
    }

    public Estado getSiguienteEstado() {
        switch (estado) {
            case RECIBIDA:
                return Estado.PREPARANDO;
            case PREPARANDO:
                return Estado.EN_HORNO;
            case EN_HORNO:
                return Estado.ENTREGADA;
            default:
                return null;
        }
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

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public int getNivelAlCrear() {
        return nivelAlCrear;
    }

    public void setNivelAlCrear(int nivelAlCrear) {
        this.nivelAlCrear = nivelAlCrear;
    }

    public int getTiempoLimiteSegundos() {
        return tiempoLimiteSegundos;
    }

    public void setTiempoLimiteSegundos(int tiempoLimiteSegundos) {
        this.tiempoLimiteSegundos = tiempoLimiteSegundos;
    }

    public int getTiempoTranscurridoSegundos() {
        return tiempoTranscurridoSegundos;
    }

    public void setTiempoTranscurridoSegundos(int tiempoTranscurridoSegundos) {
        this.tiempoTranscurridoSegundos = tiempoTranscurridoSegundos;
    }

    public int getPuntosObtenidos() {
        return puntosObtenidos;
    }

    public void setPuntosObtenidos(int puntosObtenidos) {
        this.puntosObtenidos = puntosObtenidos;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(LocalDateTime fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", idPartida=" + idPartida +
                ", estado=" + estado +
                ", nivelAlCrear=" + nivelAlCrear +
                ", tiempoLimiteSegundos=" + tiempoLimiteSegundos +
                ", tiempoTranscurridoSegundos=" + tiempoTranscurridoSegundos +
                ", puntosObtenidos=" + puntosObtenidos +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaFinalizacion=" + fechaFinalizacion +
                '}';
    }
}
