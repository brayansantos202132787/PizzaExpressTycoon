/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzaexpresstycoon.ui;

import pizzaexpresstycoon.dao.UsuarioDAO;
import pizzaexpresstycoon.dao.PartidaDAO;
import pizzaexpresstycoon.model.Usuario;
import pizzaexpresstycoon.util.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 *
 * @author braya
 */
public class SuperAdminScreen extends JFrame {

    
   private JTable            tablaUsuarios;
    private DefaultTableModel modeloTabla;
    private JTextField        fUsername, fNombre, fApellido, fEmail, fSucursal;
    private JPasswordField    fPassword;
    private JComboBox<String> comboRol;
    private UsuarioDAO        usuarioDAO;

    private static final Color C_BG       = new Color(43, 20, 5);
    private static final Color C_PANEL    = new Color(60, 30, 10);
    private static final Color C_HEADER   = new Color(80, 40, 10);
    private static final Color C_FIELD    = new Color(30, 12, 3);
    private static final Color C_TEXT     = new Color(255, 220, 180);
    private static final Color C_LABEL    = new Color(200, 150, 100);
    private static final Color C_ACCENT   = new Color(200, 100, 20);
    private static final Color C_BTN_DEAC = new Color(180, 60, 20);
    private static final Color C_BTN_ACT  = new Color(60, 140, 60);
    private static final Color C_BTN_RANK = new Color(160, 80, 10);
    private static final Color C_BTN_REP  = new Color(140, 70, 10);
    private static final Color C_BTN_CERR = new Color(100, 50, 10);

    public SuperAdminScreen() {
        usuarioDAO = new UsuarioDAO();
        configurarVentana();
        construirUI();
        cargarUsuarios();
    }

    private void configurarVentana() {
        setTitle("Pizza Express Tycoon - Super Administrador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(C_BG);
    }

    private void construirUI() {
        setLayout(new BorderLayout(0, 0));
        add(construirTitulo(),  BorderLayout.NORTH);
        add(construirCentro(),  BorderLayout.CENTER);
        add(construirBotones(), BorderLayout.SOUTH);
    }

    private JPanel construirTitulo() {
        JPanel p = new JPanel();
        p.setBackground(C_HEADER);
        p.setBorder(new EmptyBorder(10, 0, 10, 0));
        JLabel lbl = new JLabel("Panel Super Administrador");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        lbl.setForeground(C_TEXT);
        p.add(lbl);
        return p;
    }

    private JPanel construirCentro() {
        JPanel p = new JPanel(new GridLayout(1, 2, 8, 0));
        p.setBackground(C_BG);
        p.setBorder(new EmptyBorder(10, 10, 6, 10));
        p.add(construirTablaPanel());
        p.add(construirFormPanel());
        return p;
    }

    private JPanel construirTablaPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setBackground(C_BG);

        JLabel titulo = new JLabel("Usuarios Registrados");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        titulo.setForeground(C_ACCENT);
        titulo.setBorder(new EmptyBorder(0, 2, 4, 0));

        String[] cols = {"ID", "Username", "Nombre", "Rol", "Sucursal", "Activo"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setBackground(C_FIELD);
        tablaUsuarios.setForeground(C_TEXT);
        tablaUsuarios.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tablaUsuarios.setRowHeight(24);
        tablaUsuarios.setShowGrid(false);
        tablaUsuarios.setIntercellSpacing(new Dimension(0, 0));
        tablaUsuarios.getTableHeader().setBackground(C_PANEL);
        tablaUsuarios.getTableHeader().setForeground(C_ACCENT);
        tablaUsuarios.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tablaUsuarios.setSelectionBackground(C_ACCENT);
        tablaUsuarios.setSelectionForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(tablaUsuarios);
        scroll.getViewport().setBackground(C_FIELD);
        scroll.setBorder(BorderFactory.createLineBorder(C_PANEL, 1));

        p.add(titulo, BorderLayout.NORTH);
        p.add(scroll,  BorderLayout.CENTER);
        return p;
    }

    private JScrollPane construirFormPanel() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(C_PANEL);
        form.setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel titulo = new JLabel("Registrar Nuevo Usuario");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        titulo.setForeground(C_TEXT);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        fUsername = crearCampo();
        fNombre   = crearCampo();
        fApellido = crearCampo();
        fEmail    = crearCampo();
        fPassword = new JPasswordField();
        estilizarCampo(fPassword);
        fSucursal = crearCampo();

        comboRol = new JComboBox<>(new String[]{"ADMIN_TIENDA", "JUGADOR"});
        comboRol.setBackground(C_FIELD);
        comboRol.setForeground(C_TEXT);
        comboRol.setFont(new Font("SansSerif", Font.PLAIN, 12));
        comboRol.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        comboRol.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnReg = crearBtn("Registrar Usuario", C_ACCENT, C_ACCENT.brighter());
        btnReg.addActionListener(e -> registrarUsuario());

        form.add(titulo);
        form.add(Box.createVerticalStrut(10));
        agregar(form, "Username",    fUsername);
        agregar(form, "Nombre",      fNombre);
        agregar(form, "Apellido",    fApellido);
        agregar(form, "Email",       fEmail);
        agregar(form, "Contrasena",  fPassword);
        form.add(crearLabel("Rol"));
        form.add(Box.createVerticalStrut(3));
        form.add(comboRol);
        form.add(Box.createVerticalStrut(8));
        agregar(form, "ID Sucursal", fSucursal);
        form.add(Box.createVerticalStrut(10));
        form.add(btnReg);

        JScrollPane sc = new JScrollPane(form);
        sc.setBorder(BorderFactory.createLineBorder(C_HEADER, 1));
        sc.getViewport().setBackground(C_PANEL);
        sc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sc.getVerticalScrollBar().setUnitIncrement(10);
        return sc;
    }

    private JPanel construirBotones() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        p.setBackground(C_BG);
        p.add(crearBtn("Ver Ranking Global", C_BTN_RANK, C_BTN_RANK.brighter(), e -> new RankingScreen().setVisible(true)));
        p.add(crearBtn("Ver Reportes",       C_BTN_REP,  C_BTN_REP.brighter(),  e -> new ReportsScreen().setVisible(true)));
        p.add(crearBtn("Activar Usuario",    C_BTN_ACT,  C_BTN_ACT.brighter(),  e -> activarSeleccionado()));
        p.add(crearBtn("Desactivar Usuario", C_BTN_DEAC, C_BTN_DEAC.brighter(), e -> desactivarSeleccionado()));
        p.add(crearBtn("Cerrar Sesion",      C_BTN_CERR, C_BTN_CERR.brighter(), e -> cerrarSesion()));
        return p;
    }

    private void cargarUsuarios() {
        modeloTabla.setRowCount(0);
        List<Usuario> lista = usuarioDAO.obtenerTodos();
        for (Usuario u : lista) {
            modeloTabla.addRow(new Object[]{
                u.getId(), u.getUsername(), u.getNombreCompleto(),
                u.getIdRol(), u.getIdSucursal(), u.isActivo() ? "Si" : "No"
            });
        }
    }

    private void registrarUsuario() {
        String username = fUsername.getText().trim();
        String nombre   = fNombre.getText().trim();
        String apellido = fApellido.getText().trim();
        String email    = fEmail.getText().trim();
        String pass     = new String(fPassword.getPassword()).trim();
        String sucTxt   = fSucursal.getText().trim();

        if (username.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username, nombre, apellido y contrasena son obligatorios.");
            return;
        }
        if (usuarioDAO.existeUsername(username)) {
            JOptionPane.showMessageDialog(this, "El username ya esta en uso.");
            return;
        }

        int idRol = comboRol.getSelectedIndex() == 0 ? 2 : 3;
        Integer idSuc = null;
        if (!sucTxt.isEmpty()) {
            try { idSuc = Integer.parseInt(sucTxt); }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID de sucursal debe ser un numero.");
                return;
            }
        }

        Usuario u = new Usuario();
        u.setUsername(username); u.setPassword(pass);
        u.setNombre(nombre);     u.setApellido(apellido);
        u.setEmail(email.isEmpty() ? null : email);
        u.setIdRol(idRol);       u.setIdSucursal(idSuc);
        u.setActivo(true);

        if (usuarioDAO.insertar(u)) {
            limpiarForm(); cargarUsuarios();
            JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar el usuario.");
        }
    }

    private void desactivarSeleccionado() {
        int fila = tablaUsuarios.getSelectedRow();
        if (fila == -1) { JOptionPane.showMessageDialog(this, "Selecciona un usuario."); return; }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        if (JOptionPane.showConfirmDialog(this, "Desactivar este usuario?", "Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (usuarioDAO.desactivar(id)) cargarUsuarios();
        }
    }

    private void activarSeleccionado() {
        int fila = tablaUsuarios.getSelectedRow();
        if (fila == -1) { JOptionPane.showMessageDialog(this, "Selecciona un usuario."); return; }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        if (JOptionPane.showConfirmDialog(this, "Activar este usuario?", "Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (usuarioDAO.activar(id)) cargarUsuarios();
        }
    }

    private void limpiarForm() {
        fUsername.setText(""); fNombre.setText(""); fApellido.setText("");
        fEmail.setText(""); fPassword.setText(""); fSucursal.setText("");
        comboRol.setSelectedIndex(0);
    }

    private void cerrarSesion() {
        SessionManager.getInstance().cerrarSesion();
        dispose();
        new LoginScreen().setVisible(true);
    }

    private void agregar(JPanel p, String lbl, JComponent c) {
        p.add(crearLabel(lbl));
        p.add(Box.createVerticalStrut(3));
        p.add(c);
        p.add(Box.createVerticalStrut(8));
    }

    private JTextField crearCampo() {
        JTextField c = new JTextField();
        estilizarCampo(c);
        return c;
    }

    private void estilizarCampo(JTextField c) {
        c.setBackground(C_FIELD);
        c.setForeground(C_TEXT);
        c.setCaretColor(C_TEXT);
        c.setFont(new Font("SansSerif", Font.PLAIN, 12));
        c.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_PANEL, 1),
            new EmptyBorder(4, 8, 4, 8)
        ));
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JLabel crearLabel(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("SansSerif", Font.BOLD, 11));
        l.setForeground(C_LABEL);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JButton crearBtn(String txt, Color bg, Color hov) {
        JButton b = new JButton(txt);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(160, 34));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(hov); }
            public void mouseExited(MouseEvent e)  { b.setBackground(bg); }
        });
        return b;
    }

    private JButton crearBtn(String txt, Color bg, Color hov, ActionListener al) {
        JButton b = crearBtn(txt, bg, hov);
        b.addActionListener(al);
        return b;
    }
}
