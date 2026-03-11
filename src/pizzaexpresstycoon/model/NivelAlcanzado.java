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
public class NivelAlcanzado {
    
    private int id;
    private int idPartida;
    private int nivel;
    private LocalDateTime fechaAlcanzado;

    public NivelAlcanzado() {}

    public NivelAlcanzado(int id, int idPartida, int nivel, LocalDateTime fechaAlcanzado) {
        this.id = id;
        this.idPartida = idPartida;
        this.nivel = nivel;
        this.fechaAlcanzado = fechaAlcanzado;
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

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public LocalDateTime getFechaAlcanzado() {
        return fechaAlcanzado;
    }

    public void setFechaAlcanzado(LocalDateTime fechaAlcanzado) {
        this.fechaAlcanzado = fechaAlcanzado;
    }

    @Override
    public String toString() {
        return "NivelAlcanzado{" +
                "id=" + id +
                ", idPartida=" + idPartida +
                ", nivel=" + nivel +
                ", fechaAlcanzado=" + fechaAlcanzado +
                '}';
    }
}
