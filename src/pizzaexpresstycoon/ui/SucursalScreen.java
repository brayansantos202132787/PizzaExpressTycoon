/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzaexpresstycoon.ui;

import pizzaexpresstycoon.dao.SucursalDAO;
import pizzaexpresstycoon.model.Sucursal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 *
 * @author braya
 */
public class SucursalScreen  extends JFrame{
    
    
    private JTable            tablaSucursales;
    private DefaultTableModel modeloTabla;
    private JTextField        fNombre, fDireccion;
    private JCheckBox         chkActiva;
    private SucursalDAO       sucursalDAO;
    private int               idSeleccionado = -1;

    private static final Color C_BG       = new Color(43, 20, 5);
    private static final Color C_PANEL    = new Color(60, 30, 10);
    private static final Color C_HEADER   = new Color(80, 40, 10);
    private static final Color C_FIELD    = new Color(30, 12, 3);
    private static final Color C_TEXT     = new Color(255, 220, 180);
    private static final Color C_LABEL    = new Color(200, 150, 100);
    private static final Color C_ACCENT   = new Color(200, 100, 20);
    private static final Color C_BTN_GUAR = new Color(160, 80, 10);
    private static final Color C_BTN_NUE  = new Color(100, 50, 10);
    private static final Color C_BTN_CERR = new Color(80, 35, 5);

    public SucursalScreen() {
        sucursalDAO = new SucursalDAO();
        configurarVentana();
        construirUI();
        cargarSucursales();
    }

    private void configurarVentana() {
        setTitle("Pizza Express Tycoon - Gestion de Sucursales");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 520);
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
        JLabel lbl = new JLabel("Gestion de Sucursales");
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

        JLabel titulo = new JLabel("Sucursales Registradas");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        titulo.setForeground(C_ACCENT);
        titulo.setBorder(new EmptyBorder(0, 2, 4, 0));

        String[] cols = {"ID", "Nombre", "Direccion", "Activa"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaSucursales = new JTable(modeloTabla);
        tablaSucursales.setBackground(C_FIELD);
        tablaSucursales.setForeground(C_TEXT);
        tablaSucursales.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tablaSucursales.setRowHeight(24);
        tablaSucursales.setShowGrid(false);
        tablaSucursales.setIntercellSpacing(new Dimension(0, 0));
        tablaSucursales.getTableHeader().setBackground(C_PANEL);
        tablaSucursales.getTableHeader().setForeground(C_ACCENT);
        tablaSucursales.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tablaSucursales.setSelectionBackground(C_ACCENT);
        tablaSucursales.setSelectionForeground(Color.WHITE);

        
        tablaSucursales.getSelectionModel().addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting()) cargarEnFormulario();
        });

        JScrollPane scroll = new JScrollPane(tablaSucursales);
        scroll.getViewport().setBackground(C_FIELD);
        scroll.setBorder(BorderFactory.createLineBorder(C_PANEL, 1));

        p.add(titulo, BorderLayout.NORTH);
        p.add(scroll,  BorderLayout.CENTER);
        return p;
    }

    private JPanel construirFormPanel() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(C_PANEL);
        form.setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel titulo = new JLabel("Datos de Sucursal");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        titulo.setForeground(C_TEXT);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        fNombre    = crearCampo();
        fDireccion = crearCampo();

        chkActiva = new JCheckBox("Activa");
        chkActiva.setBackground(C_PANEL);
        chkActiva.setForeground(C_TEXT);
        chkActiva.setFont(new Font("SansSerif", Font.PLAIN, 12));
        chkActiva.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkActiva.setSelected(true);

        form.add(titulo);
        form.add(Box.createVerticalStrut(14));
        agregar(form, "Nombre",    fNombre);
        agregar(form, "Direccion", fDireccion);
        form.add(chkActiva);
        form.add(Box.createVerticalStrut(6));

        // Indicador de modo (nueva / editando)
        JLabel lblModo = new JLabel("Modo: Nueva sucursal");
        lblModo.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblModo.setForeground(C_LABEL);
        lblModo.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lblModo);

        return form;
    }

    private JPanel construirBotones() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        p.setBackground(C_BG);
        p.add(crearBtn("Guardar",         C_BTN_GUAR, C_BTN_GUAR.brighter(), e -> guardar()));
        p.add(crearBtn("Nueva Sucursal",  C_BTN_NUE,  C_BTN_NUE.brighter(),  e -> limpiarForm()));
        p.add(crearBtn("Cerrar",          C_BTN_CERR, C_BTN_CERR.brighter(), e -> dispose()));
        return p;
    }

   
    private void cargarSucursales() {
        modeloTabla.setRowCount(0);
        List<Sucursal> lista = sucursalDAO.obtenerTodas();
        for (Sucursal s : lista) {
            modeloTabla.addRow(new Object[]{
                s.getId(), s.getNombre(), s.getDireccion(), s.isActiva() ? "Si" : "No"
            });
        }
    }

    private void cargarEnFormulario() {
        int fila = tablaSucursales.getSelectedRow();
        if (fila == -1) return;
        idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
        fNombre.setText((String) modeloTabla.getValueAt(fila, 1));
        fDireccion.setText((String) modeloTabla.getValueAt(fila, 2));
        chkActiva.setSelected("Si".equals(modeloTabla.getValueAt(fila, 3)));
    }

    private void guardar() {
        String nombre    = fNombre.getText().trim();
        String direccion = fDireccion.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.");
            return;
        }

        Sucursal s = new Sucursal();
        s.setNombre(nombre);
        s.setDireccion(direccion.isEmpty() ? null : direccion);
        s.setActiva(chkActiva.isSelected());

        if (idSeleccionado == -1) {
            // Nueva sucursal
            if (sucursalDAO.insertar(s)) {
                JOptionPane.showMessageDialog(this, "Sucursal creada correctamente.");
                limpiarForm();
                cargarSucursales();
            } else {
                JOptionPane.showMessageDialog(this, "Error al crear la sucursal.");
            }
        } else {
            // Editar existente
            s.setId(idSeleccionado);
            if (sucursalDAO.actualizar(s)) {
                JOptionPane.showMessageDialog(this, "Sucursal actualizada correctamente.");
                limpiarForm();
                cargarSucursales();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar la sucursal.");
            }
        }
    }

    private void limpiarForm() {
        idSeleccionado = -1;
        fNombre.setText("");
        fDireccion.setText("");
        chkActiva.setSelected(true);
        tablaSucursales.clearSelection();
    }

    // ── Helpers UI ────────────────────────────────────────────────────────────
    private void agregar(JPanel p, String lbl, JComponent c) {
        p.add(crearLabel(lbl));
        p.add(Box.createVerticalStrut(3));
        p.add(c);
        p.add(Box.createVerticalStrut(10));
    }

    private JTextField crearCampo() {
        JTextField c = new JTextField();
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
        return c;
    }

    private JLabel crearLabel(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("SansSerif", Font.BOLD, 11));
        l.setForeground(C_LABEL);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JButton crearBtn(String txt, Color bg, Color hov, ActionListener al) {
        JButton b = new JButton(txt);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(150, 34));
        b.addActionListener(al);
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(hov); }
            public void mouseExited(MouseEvent e)  { b.setBackground(bg); }
        });
        return b;
    }
    
}
