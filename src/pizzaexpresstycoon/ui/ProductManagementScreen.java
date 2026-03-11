/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzaexpresstycoon.ui;

import pizzaexpresstycoon.dao.ProductoDAO;
import pizzaexpresstycoon.model.Producto;
import pizzaexpresstycoon.util.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 *
 * @author braya
 */
public class ProductManagementScreen extends JFrame {

    private static final Color COLOR_FONDO        = new Color(30, 20, 10);
    private static final Color COLOR_PANEL        = new Color(50, 30, 15);
    private static final Color COLOR_ACENTO       = new Color(220, 60, 30);
    private static final Color COLOR_ACENTO_HOVER = new Color(255, 90, 50);
    private static final Color COLOR_TEXTO        = new Color(255, 240, 220);
    private static final Color COLOR_TABLA        = new Color(65, 40, 20);
    private static final Color COLOR_CAMPO        = new Color(70, 45, 25);
    private static final Font  FUENTE_TITULO      = new Font("SansSerif", Font.BOLD, 20);
    private static final Font  FUENTE_NORMAL      = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font  FUENTE_BOTON       = new Font("SansSerif", Font.BOLD, 13);

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField campoNombre;
    private JTextField campoDescripcion;
    private ProductoDAO productoDAO;
    private int idSucursal;

    public ProductManagementScreen() {
        productoDAO = new ProductoDAO();
        idSucursal  = SessionManager.getInstance().getUsuarioActivo().getIdSucursal();
        configurarVentana();
        construirUI();
        cargarProductos();
    }

    private void configurarVentana() {
        setTitle("Pizza Express Tycoon - Gestion de Productos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(750, 560);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(COLOR_FONDO);
    }

    private void construirUI() {
        setLayout(new BorderLayout(10, 10));

        add(construirPanelTitulo(), BorderLayout.NORTH);
        add(construirPanelTabla(), BorderLayout.CENTER);
        add(construirPanelFormulario(), BorderLayout.SOUTH);
    }

    private JPanel construirPanelTitulo() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel titulo = new JLabel("Gestion de Productos");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(COLOR_ACENTO);
        panel.add(titulo);
        return panel;
    }

    private JPanel construirPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(0, 20, 0, 20));

        String[] columnas = {"ID", "Nombre", "Descripcion", "Activo"};
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

    private JPanel construirPanelFormulario() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(new EmptyBorder(16, 24, 16, 24));

        campoNombre      = crearCampo();
        campoDescripcion = crearCampo();

        JPanel filaInputs = new JPanel(new GridLayout(2, 2, 10, 8));
        filaInputs.setBackground(COLOR_PANEL);
        filaInputs.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        filaInputs.add(crearLabel("Nombre"));
        filaInputs.add(crearLabel("Descripcion"));
        filaInputs.add(campoNombre);
        filaInputs.add(campoDescripcion);

        JPanel filaBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filaBotones.setBackground(COLOR_PANEL);

        JButton botonAgregar    = crearBoton("Agregar");
        JButton botonDesactivar = crearBoton("Desactivar");
        JButton botonActivar    = crearBoton("Activar");
        JButton botonCerrar     = crearBoton("Cerrar");

        botonAgregar.addActionListener(e -> agregarProducto());
        botonDesactivar.addActionListener(e -> cambiarEstadoProducto(false));
        botonActivar.addActionListener(e -> cambiarEstadoProducto(true));
        botonCerrar.addActionListener(e -> dispose());

        filaBotones.add(botonAgregar);
        filaBotones.add(botonActivar);
        filaBotones.add(botonDesactivar);
        filaBotones.add(botonCerrar);

        panel.add(filaInputs);
        panel.add(Box.createVerticalStrut(10));
        panel.add(filaBotones);
        return panel;
    }

    private void cargarProductos() {
        modeloTabla.setRowCount(0);
        List<Producto> productos = productoDAO.obtenerPorSucursal(idSucursal);
        for (Producto p : productos) {
            modeloTabla.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(),
                    p.getDescripcion(),
                    p.isActivo() ? "Si" : "No"
            });
        }
    }

    private void agregarProducto() {
        String nombre      = campoNombre.getText().trim();
        String descripcion = campoDescripcion.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del producto es obligatorio.");
            return;
        }

        if (productoDAO.existeNombreEnSucursal(nombre, idSucursal)) {
            JOptionPane.showMessageDialog(this, "Ya existe un producto con ese nombre en esta sucursal.");
            return;
        }

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setIdSucursal(idSucursal);
        producto.setActivo(true);

        if (productoDAO.insertar(producto)) {
            campoNombre.setText("");
            campoDescripcion.setText("");
            cargarProductos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al agregar el producto.");
        }
    }

    private void cambiarEstadoProducto(boolean activar) {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto de la tabla.");
            return;
        }
        int idProducto = (int) modeloTabla.getValueAt(fila, 0);
        boolean exito  = activar ? productoDAO.activar(idProducto) : productoDAO.desactivar(idProducto);
        if (exito) {
            cargarProductos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al cambiar el estado del producto.");
        }
    }

    private JTextField crearCampo() {
        JTextField campo = new JTextField();
        campo.setBackground(COLOR_CAMPO);
        campo.setForeground(COLOR_TEXTO);
        campo.setCaretColor(COLOR_TEXTO);
        campo.setFont(FUENTE_NORMAL);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_ACENTO, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        return campo;
    }

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FUENTE_NORMAL);
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




