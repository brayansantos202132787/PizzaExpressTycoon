/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pizzaexpresstycoon;


import pizzaexpresstycoon.database.DataBaseConnection;
import pizzaexpresstycoon.ui.LoginScreen;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

/**
 *
 * @author braya
 */
public class Main {

   public static void main(String[] args) {
        // Verificar la conexión a la base de datos antes de iniciar la UI
        if (!initializeDatabaseConnection()) {
            JOptionPane.showMessageDialog(
                null,
                "No se pudo conectar a la base de datos.\n"
                + "Verifique que MySQL esté en ejecución y que la base de datos\n"
                + "'pizza_express_tycoon' exista.\n\n"
                + "Host: " + DataBaseConnection.DB_HOST + "\n"
                + "Puerto: " + DataBaseConnection.DB_PORT,
                "Error de Conexión",
                JOptionPane.ERROR_MESSAGE
            );
            System.exit(1);
        }

        // Lanzar la interfaz gráfica en el Event Dispatch Thread (EDT)
        // Esto garantiza la seguridad de hilos en Swing
        SwingUtilities.invokeLater(() -> {
            applyLookAndFeel();
            launchLoginScreen();
        });
    }

    /**
     * Intenta establecer la conexión con la base de datos MySQL.
     * Usa el patrón Singleton de DatabaseConnection para reutilizar la conexión.
     *
     * @return true si la conexión fue exitosa, false en caso contrario.
     */
    private static boolean initializeDatabaseConnection() {
        System.out.println("[DB] Intentando conectar a la base de datos...");
        try {
            Connection conn = DataBaseConnection.getInstance().getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("[DB] Conexión establecida correctamente.");
                return true;
            }
        } catch (Exception e) {
            System.err.println("[DB] Error al conectar: " + e.getMessage());
        }
        return false;
    }

    /**
     * Aplica el Look and Feel del sistema operativo para una apariencia nativa.
     * Si no está disponible, se usa el L&F por defecto de Swing.
     */
    private static void applyLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException
                 | InstantiationException | IllegalAccessException e) {
            System.err.println("[UI] No se pudo aplicar el Look and Feel del sistema: " + e.getMessage());
            // Se continuará con el Look and Feel por defecto de Swing (Metal)
        }
    }

    /**
     * Instancia y muestra la pantalla de Login como primera ventana de la aplicación.
     * LoginScreen deberá extender JFrame y gestionar la autenticación del usuario.
     *
     * Una vez autenticado, LoginScreen será responsable de:
     * - Determinar el rol del usuario (JUGADOR, ADMIN_TIENDA, SUPERADMIN)
     * - Redirigir a la pantalla correspondiente según el rol
     */
    private static void launchLoginScreen() {
        System.out.println("[UI] Iniciando pantalla de Login...");

        // ---------------------------------------------------------------
        // PUNTO DE INTEGRACIÓN: Pantalla de Login
        // Descomenta la línea de abajo cuando LoginScreen esté implementada:
        new LoginScreen().setVisible(true);
        // ---------------------------------------------------------------

        // Ventana temporal de placeholder mientras se desarrolla LoginScreen
        //JFrame placeholder = buildPlaceholderFrame();
        //placeholder.setVisible(true);
    }

    /**
     * Ventana temporal de placeholder para validar el arranque correcto
     * de la aplicación antes de que LoginScreen esté implementada.
     * ELIMINAR este método cuando LoginScreen esté lista.
     *
     * @return JFrame de placeholder
     */
    private static JFrame buildPlaceholderFrame() {
        JFrame frame = new JFrame("Pizza Express Tycoon");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(480, 300);
        frame.setLocationRelativeTo(null); // Centrar en pantalla
        frame.setResizable(false);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("🍕 Pizza Express Tycoon", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));

        JLabel subtitle = new JLabel(
            "<html><center>Aplicación iniciada correctamente.<br>"
            + "Base de datos conectada.<br><br>"
            + "<i>Implementa LoginScreen.java para continuar.</i></center></html>",
            SwingConstants.CENTER
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JButton exitBtn = new JButton("Cerrar");
        exitBtn.addActionListener(e -> {
            DataBaseConnection.getInstance().closeConnection();
            System.exit(0);
        });

        panel.add(title, BorderLayout.NORTH);
        panel.add(subtitle, BorderLayout.CENTER);
        panel.add(exitBtn, BorderLayout.SOUTH);

        frame.setContentPane(panel);
        return frame;
    }
    
}
