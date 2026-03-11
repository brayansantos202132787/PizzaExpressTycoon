/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzaexpresstycoon.ui;

import pizzaexpresstycoon.model.Usuario;
import pizzaexpresstycoon.util.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 *
 * @author braya
 */
public class MainMenu extends JFrame {

    private static final Color COLOR_FONDO        = new Color(30, 20, 10);
    private static final Color COLOR_PANEL        = new Color(50, 30, 15);
    private static final Color COLOR_ACENTO       = new Color(220, 60, 30);
    private static final Color COLOR_ACENTO_HOVER = new Color(255, 90, 50);
    private static final Color COLOR_TEXTO        = new Color(255, 240, 220);
    private static final Font  FUENTE_TITULO      = new Font("SansSerif", Font.BOLD, 24);
    private static final Font  FUENTE_SUBTITULO   = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font  FUENTE_BOTON       = new Font("SansSerif", Font.BOLD, 14);

    private Usuario usuarioActivo;

    public MainMenu() {
        usuarioActivo = SessionManager.getInstance().getUsuarioActivo();
        configurarVentana();
        construirUI();
    }

    private void configurarVentana() {
        setTitle("Pizza Express Tycoon - Menu Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 520);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(COLOR_FONDO);
    }

    private void construirUI() {
        setLayout(new BorderLayout());

        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBackground(COLOR_PANEL);
        panelCentral.setBorder(new EmptyBorder(40, 60, 40, 60));

        JLabel labelBienvenida = new JLabel("Bienvenido, " + usuarioActivo.getNombre());
        labelBienvenida.setFont(FUENTE_TITULO);
        labelBienvenida.setForeground(COLOR_ACENTO);
        labelBienvenida.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel labelRol = new JLabel(obtenerNombreRol(usuarioActivo.getIdRol()));
        labelRol.setFont(FUENTE_SUBTITULO);
        labelRol.setForeground(COLOR_TEXTO);
        labelRol.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelCentral.add(labelBienvenida);
        panelCentral.add(Box.createVerticalStrut(6));
        panelCentral.add(labelRol);
        panelCentral.add(Box.createVerticalStrut(36));

        if (usuarioActivo.getIdRol() == 3) {
            panelCentral.add(crearBoton("Jugar", e -> abrirJuego()));
            panelCentral.add(Box.createVerticalStrut(14));
            panelCentral.add(crearBoton("Ver Ranking", e -> abrirRanking()));
        }

        if (usuarioActivo.getIdRol() == 2) {
            panelCentral.add(crearBoton("Gestionar Productos", e -> abrirProductos()));
            panelCentral.add(Box.createVerticalStrut(14));
            panelCentral.add(crearBoton("Estadisticas y Reportes", e -> abrirReportes()));
            panelCentral.add(Box.createVerticalStrut(14));
            panelCentral.add(crearBoton("Ver Ranking", e -> abrirRanking()));
        }

        panelCentral.add(Box.createVerticalStrut(24));
        panelCentral.add(crearBoton("Cerrar Sesion", e -> cerrarSesion()));

        JPanel panelWrapper = new JPanel(new GridBagLayout());
        panelWrapper.setBackground(COLOR_FONDO);
        panelWrapper.add(panelCentral);

        add(panelWrapper, BorderLayout.CENTER);
    }

    private void abrirJuego() {
        dispose();
        new GameScreen().setVisible(true);
    }

    private void abrirRanking() {
        new RankingScreen().setVisible(true);
    }

    private void abrirProductos() {
        new ProductManagementScreen().setVisible(true);
    }

    private void abrirReportes() {
        new ReportsScreen().setVisible(true);
    }

    private void cerrarSesion() {
        SessionManager.getInstance().cerrarSesion();
        dispose();
        new LoginScreen().setVisible(true);
    }

    private String obtenerNombreRol(int idRol) {
        switch (idRol) {
            case 1: return "Super Administrador";
            case 2: return "Administrador de Tienda";
            case 3: return "Jugador";
            default: return "Rol desconocido";
        }
    }

    private JButton crearBoton(String texto, java.awt.event.ActionListener accion) {
        JButton boton = new JButton(texto);
        boton.setFont(FUENTE_BOTON);
        boton.setBackground(COLOR_ACENTO);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.addActionListener(accion);
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setBackground(COLOR_ACENTO_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBackground(COLOR_ACENTO);
            }
        });
        return boton;
    }
}