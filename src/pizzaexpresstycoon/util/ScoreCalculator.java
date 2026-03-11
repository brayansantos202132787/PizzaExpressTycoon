/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzaexpresstycoon.util;

import pizzaexpresstycoon.model.Pedido;

/**
 *
 * @author braya
 */
public class ScoreCalculator {
    private static final int PUNTOS_COMPLETADO        =  100;
    private static final int PUNTOS_BONUS_EFICIENCIA  =   50;
    private static final int PENALIZACION_CANCELADO   =  -30;
    private static final int PENALIZACION_NO_ENTREGADO = -50;

    public int calcularPuntosEntrega(Pedido pedido) {
        int total = PUNTOS_COMPLETADO;

        if (pedido.esEficiente()) {
            total += PUNTOS_BONUS_EFICIENCIA;
        }

        return total;
    }

    public int calcularPenalizacionCancelado() {
        return PENALIZACION_CANCELADO;
    }

    public int calcularPenalizacionNoEntregado() {
        return PENALIZACION_NO_ENTREGADO;
    }

    public int getPuntosCompletado() {
        return PUNTOS_COMPLETADO;
    }

    public int getPuntosBonusEficiencia() {
        return PUNTOS_BONUS_EFICIENCIA;
    }

    public int getPenalizacionCancelado() {
        return PENALIZACION_CANCELADO;
    }

    public int getPenalizacionNoEntregado() {
        return PENALIZACION_NO_ENTREGADO;
    }
}
