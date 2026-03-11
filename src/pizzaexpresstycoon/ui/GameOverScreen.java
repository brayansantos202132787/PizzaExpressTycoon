/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzaexpresstycoon.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import pizzaexpresstycoon.model.Partida;

public class GameOverScreen extends JFrame {

    private static final Color COLOR_FONDO        = new Color(30, 20, 10);
    private static final Color COLOR_PANEL        = new Color(50, 30, 15);
    private static final Color COLOR_ACENTO       = new Color(220, 60, 30);
    private static final Color COLOR_ACENTO_HOVER = new Color(255, 90, 50);
    private static final Color COLOR_TEXTO        = new Color(255, 240, 220);
    private static final Color COLOR_VERDE        = new Color(80, 180, 80);
    private static final Font  FUENTE_TITULO      = new Font("SansSerif", Font.BOLD, 26);
    private static final Font  FUENTE_DATO        = new Font("SansSerif", Font.PLAIN, 15);
    private static final Font  FUENTE_BOTON       = new Font("SansSerif", Font.BOLD, 14);

    private Partida partida;

    public GameOverScreen(Partida partida) {
        this.partida = partida;
        configurarVentana();
        construirUI();
    }

    private void configurarVentana() {
        setTitle("Pizza Express Tycoon - Fin de Partida");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(460, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(COLOR_FONDO);
    }

    private void construirUI() {
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(new EmptyBorder(40, 50, 40, 50));

        JLabel labelTitulo = new JLabel("Partida Finalizada");
        labelTitulo.setFont(FUENTE_TITULO);
        labelTitulo.setForeground(COLOR_ACENTO);
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator separador = new JSeparator();
        separador.setForeground(COLOR_ACENTO);
        separador.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));

        panel.add(labelTitulo);
        panel.add(Box.createVerticalStrut(20));
        panel.add(separador);
        panel.add(Box.createVerticalStrut(24));
        panel.add(crearFilaDato("Puntaje Total",       String.valueOf(partida.getPuntajeTotal()),        COLOR_VERDE));
        panel.add(Box.createVerticalStrut(12));
        panel.add(crearFilaDato("Nivel Alcanzado",     String.valueOf(partida.getNivelAlcanzado()),      COLOR_TEXTO));
        panel.add(Box.createVerticalStrut(12));
        panel.add(crearFilaDato("Pedidos Completados", String.valueOf(partida.getPedidosCompletados()),  COLOR_VERDE));
        panel.add(Box.createVerticalStrut(12));
        panel.add(crearFilaDato("Pedidos Cancelados",  String.valueOf(partida.getPedidosCancelados()),   COLOR_ACENTO));
        panel.add(Box.createVerticalStrut(12));
        panel.add(crearFilaDato("No Entregados",       String.valueOf(partida.getPedidosNoEntregados()), COLOR_ACENTO));
        panel.add(Box.createVerticalStrut(32));
        panel.add(crearBoton("Ver Ranking",    e -> abrirRanking()));
        panel.add(Box.createVerticalStrut(12));
        panel.add(crearBoton("Menu Principal", e -> volverMenu()));

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(COLOR_FONDO);
        wrapper.add(panel);
        add(wrapper, BorderLayout.CENTER);
    }

    private JPanel crearFilaDato(String etiqueta, String valor, Color colorValor) {
        JPanel fila = new JPanel(new BorderLayout());
        fila.setBackground(COLOR_PANEL);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel labelEtiqueta = new JLabel(etiqueta);
        labelEtiqueta.setFont(FUENTE_DATO);
        labelEtiqueta.setForeground(COLOR_TEXTO);

        JLabel labelValor = new JLabel(valor);
        labelValor.setFont(new Font("SansSerif", Font.BOLD, 15));
        labelValor.setForeground(colorValor);
        labelValor.setHorizontalAlignment(SwingConstants.RIGHT);

        fila.add(labelEtiqueta, BorderLayout.WEST);
        fila.add(labelValor,    BorderLayout.EAST);
        return fila;
    }

    private void abrirRanking() {
        new RankingScreen().setVisible(true);
    }

    private void volverMenu() {
        dispose();
        new MainMenu().setVisible(true);
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
            public void mouseEntered(java.awt.event.MouseEvent e) { boton.setBackground(COLOR_ACENTO_HOVER); }
            public void mouseExited(java.awt.event.MouseEvent e)  { boton.setBackground(COLOR_ACENTO); }
        });
        return boton;
    }
}
