/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzaexpresstycoon.ui;


import pizzaexpresstycoon.dao.PartidaDAO;
import pizzaexpresstycoon.dao.PedidoDAO;
import pizzaexpresstycoon.dao.ProductoDAO;
import pizzaexpresstycoon.model.Partida;
import pizzaexpresstycoon.model.Pedido;
import pizzaexpresstycoon.model.Producto;
import pizzaexpresstycoon.util.ScoreCalculator;
import pizzaexpresstycoon.util.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author braya
 */

public class GameScreen extends JFrame {

    
    private static final Color COLOR_FONDO        = new Color(30, 20, 10);
    private static final Color COLOR_PANEL        = new Color(50, 30, 15);
    private static final Color COLOR_ACENTO       = new Color(220, 60, 30);
    private static final Color COLOR_ACENTO_HOVER = new Color(255, 90, 50);
    private static final Color COLOR_TEXTO        = new Color(255, 240, 220);
    private static final Color COLOR_VERDE        = new Color(80, 180, 80);
    private static final Color COLOR_TARJETA      = new Color(65, 40, 20);
    private static final Font  FUENTE_TITULO      = new Font("SansSerif", Font.BOLD, 18);
    private static final Font  FUENTE_NORMAL      = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font  FUENTE_BOTON       = new Font("SansSerif", Font.BOLD, 12);

    private static final int DURACION_PARTIDA    = 180;
    private static final int MAX_PEDIDOS_ACTIVOS = 4;
    private static final int INTERVALO_PEDIDO    = 8;

    private Partida partida;
    private List<Pedido> pedidosActivos;
    private List<Producto> productosDisponibles;

    private JLabel labelTiempoPartida;
    private JLabel labelPuntaje;
    private JLabel labelNivel;
    private JPanel panelPedidos;

    private Timer timerPartida;
    private Timer timerPedidos;
    private int segundosRestantes;
    private int segundosSiguientePedido;

    private PartidaDAO partidaDAO;
    private PedidoDAO pedidoDAO;
    private ProductoDAO productoDAO;
    private ScoreCalculator scoreCalculator;
    private Random random;

    public GameScreen() {
        partidaDAO      = new PartidaDAO();
        pedidoDAO       = new PedidoDAO();
        productoDAO     = new ProductoDAO();
        scoreCalculator = new ScoreCalculator();
        pedidosActivos  = new ArrayList<>();
        random          = new Random();

        iniciarPartida();
        configurarVentana();
        construirUI();
        iniciarTimers();
    }

    private void configurarVentana() {
        setTitle("Pizza Express Tycoon - Jugando");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(900, 620);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(COLOR_FONDO);
    }

    private void iniciarPartida() {
        partida = new Partida();
        partida.setIdUsuario(SessionManager.getInstance().getUsuarioActivo().getId());
        partida.setIdSucursal(SessionManager.getInstance().getUsuarioActivo().getIdSucursal());
        partida.setFechaInicio(LocalDateTime.now());
        partidaDAO.insertar(partida);

        productosDisponibles    = productoDAO.obtenerActivosPorSucursal(partida.getIdSucursal());
        segundosRestantes       = DURACION_PARTIDA;
        segundosSiguientePedido = 3;
    }

    private void construirUI() {
        setLayout(new BorderLayout(10, 10));
        add(construirPanelSuperior(), BorderLayout.NORTH);
        add(construirPanelPedidos(), BorderLayout.CENTER);
        add(construirPanelInferior(), BorderLayout.SOUTH);
    }

    private JPanel construirPanelSuperior() {
        JPanel panel = new JPanel(new GridLayout(1, 3));
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(new EmptyBorder(12, 20, 12, 20));

        labelTiempoPartida = crearLabelInfo("Tiempo: " + segundosRestantes + "s");
        labelPuntaje       = crearLabelInfo("Puntaje: 0");
        labelNivel         = crearLabelInfo("Nivel: 1");

        panel.add(labelTiempoPartida);
        panel.add(labelPuntaje);
        panel.add(labelNivel);
        return panel;
    }

    private JPanel construirPanelPedidos() {
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(COLOR_FONDO);
        contenedor.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel titulo = new JLabel("Pedidos Activos");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(COLOR_ACENTO);
        titulo.setBorder(new EmptyBorder(0, 0, 10, 0));

        panelPedidos = new JPanel();
        panelPedidos.setLayout(new BoxLayout(panelPedidos, BoxLayout.Y_AXIS));
        panelPedidos.setBackground(COLOR_FONDO);

        JScrollPane scroll = new JScrollPane(panelPedidos);
        scroll.setBackground(COLOR_FONDO);
        scroll.getViewport().setBackground(COLOR_FONDO);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_ACENTO, 1));
        scroll.getVerticalScrollBar().setUnitIncrement(12);

        contenedor.add(titulo, BorderLayout.NORTH);
        contenedor.add(scroll, BorderLayout.CENTER);
        return contenedor;
    }

    private JPanel construirPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JButton botonRendirse = crearBoton("Abandonar Partida", COLOR_ACENTO);
        botonRendirse.addActionListener(e -> confirmarAbandono());
        panel.add(botonRendirse);
        return panel;
    }

    private void iniciarTimers() {
        timerPartida = new Timer(1000, e -> tickPartida());
        timerPartida.start();

        timerPedidos = new Timer(1000, e -> tickPedidos());
        timerPedidos.start();
    }

    private void tickPartida() {
        segundosRestantes--;
        labelTiempoPartida.setText("Tiempo: " + segundosRestantes + "s");
        if (segundosRestantes <= 0) {
            finalizarPartida();
        }
    }

    private void tickPedidos() {
        segundosSiguientePedido--;

        for (Pedido pedido : new ArrayList<>(pedidosActivos)) {
            pedido.setTiempoTranscurridoSegundos(pedido.getTiempoTranscurridoSegundos() + 1);
            if (pedido.tiempoExpirado() && !pedido.estaFinalizado()) {
                expirarPedido(pedido);
            }
        }

        if (segundosSiguientePedido <= 0 && segundosRestantes > 0
                && pedidosActivos.size() < MAX_PEDIDOS_ACTIVOS
                && !productosDisponibles.isEmpty()) {
            generarNuevoPedido();
            segundosSiguientePedido = INTERVALO_PEDIDO;
        }

        verificarSubidaNivel();
        actualizarPanelPedidos();
    }

    private void generarNuevoPedido() {
        int tiempoLimite = obtenerTiempoLimitePorNivel(partida.getNivelAlcanzado());

        Pedido pedido = new Pedido();
        pedido.setIdPartida(partida.getId());
        pedido.setNivelAlCrear(partida.getNivelAlcanzado());
        pedido.setTiempoLimiteSegundos(tiempoLimite);
        pedido.setFechaCreacion(LocalDateTime.now());

        if (pedidoDAO.insertar(pedido)) {
            pedidosActivos.add(pedido);
        } else {
            System.err.println("[GameScreen] Error al insertar pedido en DB.");
        }
    }

    private void expirarPedido(Pedido pedido) {
        Pedido.Estado estadoAnterior = pedido.getEstado();
        pedido.setEstado(Pedido.Estado.NO_ENTREGADO);
        pedido.setFechaFinalizacion(LocalDateTime.now());
        pedidoDAO.actualizarEstado(pedido.getId(), estadoAnterior, Pedido.Estado.NO_ENTREGADO);
        pedidoDAO.actualizar(pedido);

        int penalizacion = scoreCalculator.calcularPenalizacionNoEntregado();
        partida.agregarPuntaje(penalizacion);
        partida.setPedidosNoEntregados(partida.getPedidosNoEntregados() + 1);
        partidaDAO.actualizar(partida);

        pedidosActivos.remove(pedido);
        labelPuntaje.setText("Puntaje: " + partida.getPuntajeTotal());
    }

    public void avanzarEstadoPedido(Pedido pedido) {
        if (pedido.estaFinalizado() || !pedido.puedeAvanzarEstado()) return;

        Pedido.Estado estadoAnterior = pedido.getEstado();
        Pedido.Estado estadoNuevo    = pedido.getSiguienteEstado();

        if (estadoNuevo == null) return;

        pedido.setEstado(estadoNuevo);
        pedidoDAO.actualizarEstado(pedido.getId(), estadoAnterior, estadoNuevo);

        if (estadoNuevo == Pedido.Estado.ENTREGADA) {            pedido.setFechaFinalizacion(LocalDateTime.now());
            pedidoDAO.actualizar(pedido);

            int puntos = scoreCalculator.calcularPuntosEntrega(pedido);
            partida.agregarPuntaje(puntos);
            partida.setPedidosCompletados(partida.getPedidosCompletados() + 1);
            partidaDAO.actualizar(partida);

            pedidosActivos.remove(pedido);
            labelPuntaje.setText("Puntaje: " + partida.getPuntajeTotal());
        }

        actualizarPanelPedidos();
    }

    public void cancelarPedido(Pedido pedido) {
        if (!pedido.puedeCancelarse()) {
            JOptionPane.showMessageDialog(this, "Este pedido no puede cancelarse en su estado actual.");
            return;
        }

        Pedido.Estado estadoAnterior = pedido.getEstado();
        pedido.setEstado(Pedido.Estado.CANCELADA);
        pedido.setFechaFinalizacion(LocalDateTime.now());
        pedidoDAO.actualizarEstado(pedido.getId(), estadoAnterior, Pedido.Estado.CANCELADA);
        pedidoDAO.actualizar(pedido);

        int penalizacion = scoreCalculator.calcularPenalizacionCancelado();
        partida.agregarPuntaje(penalizacion);
        partida.setPedidosCancelados(partida.getPedidosCancelados() + 1);
        partidaDAO.actualizar(partida);

        pedidosActivos.remove(pedido);
        labelPuntaje.setText("Puntaje: " + partida.getPuntajeTotal());
        actualizarPanelPedidos();
    }

    private void verificarSubidaNivel() {
        int nivelActual = partida.getNivelAlcanzado();
        if (nivelActual >= 3) return;

        boolean subir = false;
        if (nivelActual == 1 && partida.getPedidosCompletados() >= 5)  subir = true;
        if (nivelActual == 2 && partida.getPedidosCompletados() >= 10) subir = true;

        if (subir) {
            partida.setNivelAlcanzado(nivelActual + 1);
            partidaDAO.actualizar(partida);
            labelNivel.setText("Nivel: " + partida.getNivelAlcanzado());
            JOptionPane.showMessageDialog(this, "Subiste al nivel " + partida.getNivelAlcanzado() + "!");
        }
    }

    private void actualizarPanelPedidos() {
        panelPedidos.removeAll();
        for (Pedido pedido : pedidosActivos) {
            panelPedidos.add(construirTarjetaPedido(pedido));
            panelPedidos.add(Box.createVerticalStrut(8));
        }
        panelPedidos.revalidate();
        panelPedidos.repaint();
    }

    private JPanel construirTarjetaPedido(Pedido pedido) {
        JPanel tarjeta = new JPanel(new BorderLayout(10, 5));
        tarjeta.setBackground(COLOR_TARJETA);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_ACENTO, 1),
                new EmptyBorder(10, 14, 10, 14)
        ));
        tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        int tiempoRestante = pedido.getTiempoLimiteSegundos() - pedido.getTiempoTranscurridoSegundos();
        Color colorTiempo  = tiempoRestante > 15 ? COLOR_VERDE : COLOR_ACENTO;

        JLabel labelInfo = new JLabel(
                "Pedido #" + pedido.getId()
                + "   Estado: " + pedido.getEstado().name()
                + "   Tiempo restante: " + Math.max(0, tiempoRestante) + "s"
        );
        labelInfo.setFont(FUENTE_NORMAL);
        labelInfo.setForeground(colorTiempo);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        panelBotones.setBackground(COLOR_TARJETA);

        JButton botonAvanzar  = crearBoton("Avanzar",  COLOR_VERDE);
        JButton botonCancelar = crearBoton("Cancelar", COLOR_ACENTO);

        botonAvanzar.addActionListener(e -> avanzarEstadoPedido(pedido));
        botonCancelar.addActionListener(e -> cancelarPedido(pedido));

        if (pedido.estaFinalizado() || !pedido.puedeAvanzarEstado()) botonAvanzar.setEnabled(false);
        if (!pedido.puedeCancelarse()) botonCancelar.setEnabled(false);

        panelBotones.add(botonAvanzar);
        panelBotones.add(botonCancelar);

        tarjeta.add(labelInfo,    BorderLayout.CENTER);
        tarjeta.add(panelBotones, BorderLayout.EAST);
        return tarjeta;
    }

    private void finalizarPartida() {
        timerPartida.stop();
        timerPedidos.stop();

        pedidoDAO.marcarNoEntregados(partida.getId());
        partidaDAO.finalizar(partida.getId());
        partida.setEstado(Partida.Estado.FINALIZADA);

        dispose();
        new GameOverScreen(partida).setVisible(true);
    }

    private void confirmarAbandono() {
        int opcion = JOptionPane.showConfirmDialog(this,
                "Seguro que deseas abandonar la partida?",
                "Abandonar", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            finalizarPartida();
        }
    }

    private int obtenerTiempoLimitePorNivel(int nivel) {
        switch (nivel) {
            case 1:  return 60;
            case 2:  return 50;
            case 3:  return 40;
            default: return 60;
        }
    }

    private JLabel crearLabelInfo(String texto) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setFont(FUENTE_TITULO);
        label.setForeground(COLOR_TEXTO);
        return label;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(FUENTE_BOTON);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
}