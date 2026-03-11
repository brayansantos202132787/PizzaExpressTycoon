/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzaexpresstycoon.ui;

import pizzaexpresstycoon.dao.PartidaDAO;
import pizzaexpresstycoon.dao.UsuarioDAO;
import pizzaexpresstycoon.model.Partida;
import pizzaexpresstycoon.model.Usuario;
import pizzaexpresstycoon.util.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

/**
 *
 * @author braya
 */
public class RankingScreen extends JFrame {

    private static final Color COLOR_FONDO   = new Color(30, 20, 10);
    private static final Color COLOR_PANEL   = new Color(50, 30, 15);
    private static final Color COLOR_ACENTO  = new Color(220, 60, 30);
    private static final Color COLOR_TEXTO   = new Color(255, 240, 220);
    private static final Color COLOR_TABLA   = new Color(65, 40, 20);
    private static final Color COLOR_HEADER  = new Color(220, 60, 30);
    private static final Font  FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 20);
    private static final Font  FUENTE_TABLA  = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font  FUENTE_BOTON  = new Font("SansSerif", Font.BOLD, 13);

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private PartidaDAO partidaDAO;
    private UsuarioDAO usuarioDAO;

    public RankingScreen() {
        partidaDAO = new PartidaDAO();
        usuarioDAO = new UsuarioDAO();
        configurarVentana();
        construirUI();
        cargarDatos();
    }

    private void configurarVentana() {
        setTitle("Pizza Express Tycoon - Ranking");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(COLOR_FONDO);
    }

    private void construirUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTitulo.setBackground(COLOR_PANEL);
        panelTitulo.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel labelTitulo = new JLabel("Ranking de Jugadores");
        labelTitulo.setFont(FUENTE_TITULO);
        labelTitulo.setForeground(COLOR_ACENTO);
        panelTitulo.add(labelTitulo);

        String[] columnas = {"#", "Jugador", "Sucursal", "Puntaje", "Nivel", "Completados"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tabla = new JTable(modeloTabla);
        tabla.setBackground(COLOR_TABLA);
        tabla.setForeground(COLOR_TEXTO);
        tabla.setFont(FUENTE_TABLA);
        tabla.setRowHeight(28);
        tabla.setGridColor(COLOR_ACENTO);
        tabla.getTableHeader().setBackground(COLOR_HEADER);
        tabla.getTableHeader().setForeground(Color.ORANGE);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tabla.setSelectionBackground(COLOR_ACENTO);
        tabla.setSelectionForeground(Color.ORANGE);

        DefaultTableCellRenderer centradorCeldas = new DefaultTableCellRenderer();
        centradorCeldas.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < columnas.length; i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centradorCeldas);
        }

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(COLOR_TABLA);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_ACENTO, 1));

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelInferior.setBackground(COLOR_PANEL);
        panelInferior.setBorder(new EmptyBorder(10, 20, 10, 20));

        JButton botonCerrar = crearBoton("Cerrar");
        botonCerrar.addActionListener(e -> dispose());
        panelInferior.add(botonCerrar);

        add(panelTitulo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        Usuario usuario = SessionManager.getInstance().getUsuarioActivo();

        List<Partida> partidas;
        if (usuario.getIdRol() == 1) {
            partidas = partidaDAO.obtenerTodas();
        } else {
            partidas = partidaDAO.obtenerPorSucursal(usuario.getIdSucursal());
        }

        partidas.sort((a, b) -> b.getPuntajeTotal() - a.getPuntajeTotal());

        int posicion = 1;
        for (Partida p : partidas) {
            Usuario jugador = usuarioDAO.obtenerPorId(p.getIdUsuario());
            String nombre   = jugador != null ? jugador.getNombreCompleto() : "Desconocido";
            modeloTabla.addRow(new Object[]{
                    posicion++,
                    nombre,
                    p.getIdSucursal(),
                    p.getPuntajeTotal(),
                    p.getNivelAlcanzado(),
                    p.getPedidosCompletados()
            });
        }
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(FUENTE_BOTON);
        boton.setBackground(COLOR_ACENTO);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
}
