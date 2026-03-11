/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzaexpresstycoon.ui;

import pizzaexpresstycoon.dao.UsuarioDAO;
import pizzaexpresstycoon.model.Usuario;
import pizzaexpresstycoon.util.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 *
 * @author braya
 */
public class LoginScreen extends JFrame {

    private static final Color COLOR_FONDO        = new Color(30, 20, 10);
    private static final Color COLOR_PANEL        = new Color(50, 30, 15);
    private static final Color COLOR_ACENTO       = new Color(220, 60, 30);
    private static final Color COLOR_ACENTO_HOVER = new Color(255, 90, 50);
    private static final Color COLOR_TEXTO        = new Color(255, 240, 220);
    private static final Color COLOR_CAMPO        = new Color(70, 45, 25);
    private static final Font  FUENTE_TITULO      = new Font("SansSerif", Font.BOLD, 26);
    private static final Font  FUENTE_LABEL       = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font  FUENTE_BOTON       = new Font("SansSerif", Font.BOLD, 14);

    private JTextField campoUsername;
    private JPasswordField campoPassword;
    private JButton botonIngresar;
    private JLabel labelMensaje;
    private UsuarioDAO usuarioDAO;

    public LoginScreen() {
        usuarioDAO = new UsuarioDAO();
        configurarVentana();
        construirUI();
    }

    private void configurarVentana() {
        setTitle("Pizza Express Tycoon - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 480);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(COLOR_FONDO);
    }

    private void construirUI() {
        setLayout(new BorderLayout());

        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBackground(COLOR_PANEL);
        panelCentral.setBorder(new EmptyBorder(40, 50, 40, 50));

        JLabel labelTitulo = new JLabel("Pizza Express Tycoon");
        labelTitulo.setFont(FUENTE_TITULO);
        labelTitulo.setForeground(COLOR_ACENTO);
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel labelSubtitulo = new JLabel("Iniciar Sesion");
        labelSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        labelSubtitulo.setForeground(COLOR_TEXTO);
        labelSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        campoUsername = crearCampoTexto();
        campoPassword = new JPasswordField();
        estilizarCampo(campoPassword);

        labelMensaje = new JLabel(" ");
        labelMensaje.setFont(new Font("SansSerif", Font.PLAIN, 12));
        labelMensaje.setForeground(COLOR_ACENTO);
        labelMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        botonIngresar = crearBoton("Ingresar");
        botonIngresar.addActionListener(e -> intentarLogin());

        panelCentral.add(labelTitulo);
        panelCentral.add(Box.createVerticalStrut(6));
        panelCentral.add(labelSubtitulo);
        panelCentral.add(Box.createVerticalStrut(30));
        panelCentral.add(crearLabel("Usuario"));
        panelCentral.add(Box.createVerticalStrut(6));
        panelCentral.add(campoUsername);
        panelCentral.add(Box.createVerticalStrut(16));
        panelCentral.add(crearLabel("Contrasena"));
        panelCentral.add(Box.createVerticalStrut(6));
        panelCentral.add(campoPassword);
        panelCentral.add(Box.createVerticalStrut(24));
        panelCentral.add(botonIngresar);
        panelCentral.add(Box.createVerticalStrut(12));
        panelCentral.add(labelMensaje);

        JPanel panelWrapper = new JPanel(new GridBagLayout());
        panelWrapper.setBackground(COLOR_FONDO);
        panelWrapper.add(panelCentral);

        add(panelWrapper, BorderLayout.CENTER);
    }

    private void intentarLogin() {
        String username = campoUsername.getText().trim();
        String password = new String(campoPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            labelMensaje.setText("Completa todos los campos.");
            return;
        }

        Usuario usuario = usuarioDAO.obtenerPorCredenciales(username, password);

        if (usuario == null) {
            labelMensaje.setText("Usuario o contrasena incorrectos.");
            campoPassword.setText("");
            return;
        }

        SessionManager.getInstance().setUsuarioActivo(usuario);
        labelMensaje.setText(" ");
        abrirPantallaSegunRol(usuario);
    }

    private void abrirPantallaSegunRol(Usuario usuario) {
        dispose();
        switch (usuario.getIdRol()) {
            case 1:
                new SuperAdminScreen().setVisible(true);
                break;
            case 2:
                new MainMenu().setVisible(true);
                break;
            case 3:
                new MainMenu().setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Rol no reconocido.");
        }
    }

    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        estilizarCampo(campo);
        return campo;
    }

    private void estilizarCampo(JTextField campo) {
        campo.setBackground(COLOR_CAMPO);
        campo.setForeground(COLOR_TEXTO);
        campo.setCaretColor(COLOR_TEXTO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_ACENTO, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        campo.setFont(FUENTE_LABEL);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    }

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FUENTE_LABEL);
        label.setForeground(COLOR_TEXTO);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
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
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
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
