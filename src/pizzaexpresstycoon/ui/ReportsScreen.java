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
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author braya
 */
public class ReportsScreen extends JFrame {

    private static final Color COLOR_FONDO        = new Color(30, 20, 10);
    private static final Color COLOR_PANEL        = new Color(50, 30, 15);
    private static final Color COLOR_ACENTO       = new Color(220, 60, 30);
    private static final Color COLOR_ACENTO_HOVER = new Color(255, 90, 50);
    private static final Color COLOR_TEXTO        = new Color(255, 240, 220);
    private static final Color COLOR_TABLA        = new Color(65, 40, 20);
    private static final Font  FUENTE_TITULO      = new Font("SansSerif", Font.BOLD, 20);
    private static final Font  FUENTE_NORMAL      = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font  FUENTE_BOTON       = new Font("SansSerif", Font.BOLD, 13);

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JLabel labelTotalPartidas;
    private JLabel labelPuntajePromedio;
    private PartidaDAO partidaDAO;
    private UsuarioDAO usuarioDAO;
    private List<Partida> partidas;

    public ReportsScreen() {
        partidaDAO = new PartidaDAO();
        usuarioDAO = new UsuarioDAO();
        configurarVentana();
        construirUI();
        cargarDatos();
    }

    private void configurarVentana() {
        setTitle("Pizza Express Tycoon - Reportes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 560);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(COLOR_FONDO);
    }

    private void construirUI() {
        setLayout(new BorderLayout(10, 10));

        add(construirPanelTitulo(), BorderLayout.NORTH);
        add(construirPanelTabla(), BorderLayout.CENTER);
        add(construirPanelInferior(), BorderLayout.SOUTH);
    }

    private JPanel construirPanelTitulo() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel titulo = new JLabel("Estadisticas y Reportes");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(COLOR_ACENTO);

        JPanel panelStats = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        panelStats.setBackground(COLOR_PANEL);

        labelTotalPartidas    = crearLabelStat("Partidas: 0");
        labelPuntajePromedio  = crearLabelStat("Promedio: 0");

        panelStats.add(labelTotalPartidas);
        panelStats.add(labelPuntajePromedio);

        panel.add(titulo, BorderLayout.WEST);
        panel.add(panelStats, BorderLayout.EAST);
        return panel;
    }

    private JPanel construirPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(0, 20, 0, 20));

        String[] columnas = {"ID", "Jugador", "Puntaje", "Nivel", "Completados", "Cancelados", "No Entregados", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tabla = new JTable(modeloTabla);
        tabla.setBackground(COLOR_TABLA);
        tabla.setForeground(COLOR_TEXTO);
        tabla.setFont(FUENTE_NORMAL);
        tabla.setRowHeight(26);
        tabla.setGridColor(COLOR_ACENTO);
        tabla.getTableHeader().setBackground(COLOR_ACENTO);
        tabla.getTableHeader().setForeground(Color.ORANGE);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tabla.setSelectionBackground(COLOR_ACENTO);
        tabla.setSelectionForeground(Color.ORANGE);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(COLOR_TABLA);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_ACENTO, 1));
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel construirPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(new EmptyBorder(6, 20, 6, 20));

        JButton botonExportar = crearBoton("Exportar CSV");
        JButton botonCerrar   = crearBoton("Cerrar");

        botonExportar.addActionListener(e -> exportarCSV());
        botonCerrar.addActionListener(e -> dispose());

        panel.add(botonExportar);
        panel.add(botonCerrar);
        return panel;
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        Usuario usuario = SessionManager.getInstance().getUsuarioActivo();

        if (usuario.getIdRol() == 1) {
            partidas = partidaDAO.obtenerTodas();
        } else {
            partidas = partidaDAO.obtenerPorSucursal(usuario.getIdSucursal());
        }

        int totalPuntaje = 0;
        for (Partida p : partidas) {
            Usuario jugador = usuarioDAO.obtenerPorId(p.getIdUsuario());
            String nombre   = jugador != null ? jugador.getNombreCompleto() : "Desconocido";
            modeloTabla.addRow(new Object[]{
                    p.getId(),
                    nombre,
                    p.getPuntajeTotal(),
                    p.getNivelAlcanzado(),
                    p.getPedidosCompletados(),
                    p.getPedidosCancelados(),
                    p.getPedidosNoEntregados(),
                    p.getEstado().name()
            });
            totalPuntaje += p.getPuntajeTotal();
        }

        int promedio = partidas.isEmpty() ? 0 : totalPuntaje / partidas.size();
        labelTotalPartidas.setText("Partidas: " + partidas.size());
        labelPuntajePromedio.setText("Promedio: " + promedio);
    }

    private void exportarCSV() {
        if (partidas == null || partidas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay datos para exportar.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new java.io.File("reporte_partidas.csv"));
        int resultado = fileChooser.showSaveDialog(this);

        if (resultado != JFileChooser.APPROVE_OPTION) return;

        try (FileWriter writer = new FileWriter(fileChooser.getSelectedFile())) {
            writer.write("ID,Jugador,Puntaje,Nivel,Completados,Cancelados,No Entregados,Estado\n");
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                StringBuilder fila = new StringBuilder();
                for (int j = 0; j < modeloTabla.getColumnCount(); j++) {
                    fila.append(modeloTabla.getValueAt(i, j));
                    if (j < modeloTabla.getColumnCount() - 1) fila.append(",");
                }
                writer.write(fila + "\n");
            }
            JOptionPane.showMessageDialog(this, "Reporte exportado correctamente.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al exportar: " + e.getMessage());
        }
    }

    private JLabel crearLabelStat(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(COLOR_TEXTO);
        return label;
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(FUENTE_BOTON);
        boton.setBackground(COLOR_ACENTO);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { boton.setBackground(COLOR_ACENTO_HOVER); }
            public void mouseExited(java.awt.event.MouseEvent e)  { boton.setBackground(COLOR_ACENTO); }
        });
        return boton;
    }
}
