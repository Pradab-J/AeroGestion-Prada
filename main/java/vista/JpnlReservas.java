package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.List;

import javax.swing.JScrollPane;

/**
 * PANEL administrativo de Reservas: busca por código o documento, muestra la
 * tabla de reservas y permite cancelar la reserva seleccionada.
 */
public class JpnlReservas extends JPanel {

    private final Color azulOscuro = new Color(23, 42, 58);
    private final Color azulActivo = new Color(29, 140, 248);
    private final Color fondo = new Color(244, 247, 250);
    private final Color blanco = Color.WHITE;
    private final Color texto = new Color(55, 65, 81);

    // Públicos para que reservaControlador conecte los botones.
    public JTextField txtBuscar;
    public JButton btnBuscar;
    public JButton btnLimpiar;
    public JButton btnCancelar;

    private JTable tblReservas;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> ordenador;

    public JpnlReservas() {
        configurarPanel();
        crearInterfaz();
    }

    /** Texto escrito en el buscador (sin espacios sobrantes). */
    public String getTextoBusqueda() {
        return txtBuscar.getText().trim();
    }

    /** Vacía el buscador. */
    public void limpiarBusqueda() {
        txtBuscar.setText("");
    }

    /**
     * Devuelve {codigo, estado} de la reserva seleccionada en la tabla, o null
     * si no hay ninguna seleccionada.
     */
    public String[] getReservaSeleccionada() {
        int fila = tblReservas.getSelectedRow();
        if (fila < 0) {
            return null;
        }
        int m = tblReservas.convertRowIndexToModel(fila);
        return new String[]{
            String.valueOf(modelo.getValueAt(m, 0)),
            String.valueOf(modelo.getValueAt(m, 6))
        };
    }

    /** Reemplaza el contenido de la tabla con la lista recibida. */
    public void mostrarReservas(List<Object[]> reservas) {
    modelo.setRowCount(0);

    for (Object[] reserva : reservas) {
        modelo.addRow(reserva);
    }
}

    /** Configura el diseño base del panel (margen y color de fondo). */
    private void configurarPanel() {
        setLayout(new BorderLayout(0, 18));
        setBackground(fondo);
        setBorder(BorderFactory.createEmptyBorder(24, 26, 24, 26));
    }

    

    /** Construye encabezado, buscador, tabla y botón de cancelar. */
    private void crearInterfaz() {

        // ENCABEZADO
        JPanel encabezado = new JPanel(new BorderLayout(0, 6));
        encabezado.setOpaque(false);

        JLabel lblTitulo = new JLabel("GESTIÓN DE RESERVAS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 25));
        lblTitulo.setForeground(azulOscuro);

        JLabel lblDescripcion = new JLabel(
                "Consulta y administra las reservas de los pasajeros."
        );
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDescripcion.setForeground(texto);

        encabezado.add(lblTitulo, BorderLayout.NORTH);
        encabezado.add(lblDescripcion, BorderLayout.CENTER);

        add(encabezado, BorderLayout.NORTH);

        // CONTENIDO CENTRAL
        JPanel contenido = new JPanel(new BorderLayout(0, 16));
        contenido.setOpaque(false);

        // PANEL DE BUSQUEDA
        JPanel panelBusqueda = new JPanel(new GridBagLayout());
        panelBusqueda.setBackground(blanco);
        panelBusqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(225, 231, 238)
                ),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblBuscar = new JLabel(
                "Buscar por código o documento del pasajero"
        );
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblBuscar.setForeground(azulOscuro);

        txtBuscar = new JTextField();
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBuscar.setPreferredSize(new Dimension(250, 36));
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(210, 218, 228)
                ),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        btnBuscar = crearBoton("Buscar", azulActivo, blanco);
        btnLimpiar = crearBoton(
                "Limpiar", new Color(232, 237, 243), azulOscuro
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        panelBusqueda.add(lblBuscar, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        panelBusqueda.add(txtBuscar, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0;
        panelBusqueda.add(btnBuscar, gbc);

        gbc.gridx = 2;
        panelBusqueda.add(btnLimpiar, gbc);

        contenido.add(panelBusqueda, BorderLayout.NORTH);

        // TABLA DE RESERVAS
        String[] columnas = {
            "Código",
            "Fecha",
            "Documento",
            "Pasajero",
            "Vuelo",
            "Asiento",
            "Estado"
        };

        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tblReservas = new JTable(modelo);
        tblReservas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblReservas.setForeground(texto);
        tblReservas.setBackground(blanco);
        tblReservas.setRowHeight(34);
        tblReservas.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
        tblReservas.setShowVerticalLines(false);
        tblReservas.setGridColor(new Color(235, 239, 244));
        tblReservas.setFillsViewportHeight(true);
        tblReservas.setAutoCreateRowSorter(true);

        tblReservas.getTableHeader().setBackground(azulOscuro);
        tblReservas.getTableHeader().setForeground(blanco);
        tblReservas.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );
        tblReservas.getTableHeader().setPreferredSize(
                new Dimension(0, 38)
        );

        ordenador = new TableRowSorter<>(modelo);
        tblReservas.setRowSorter(ordenador);

        JScrollPane scroll = new JScrollPane(tblReservas);
        scroll.setBorder(BorderFactory.createLineBorder(
                new Color(225, 231, 238)
        ));
        scroll.getViewport().setBackground(blanco);

        JPanel panelTabla = new JPanel(new BorderLayout(0, 10));
        panelTabla.setOpaque(false);

        JLabel lblTabla = new JLabel("Reservas registradas");
        lblTabla.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTabla.setForeground(azulOscuro);

        panelTabla.add(lblTabla, BorderLayout.NORTH);
        panelTabla.add(scroll, BorderLayout.CENTER);

        contenido.add(panelTabla, BorderLayout.CENTER);

        // BOTON CANCELAR RESERVA (admin)
        btnCancelar = crearBoton(
                "Cancelar reserva", new Color(200, 55, 55), blanco
        );
        btnCancelar.setPreferredSize(new Dimension(150, 36));
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelAcciones.setOpaque(false);
        panelAcciones.add(btnCancelar);
        panelTabla.add(panelAcciones, BorderLayout.SOUTH);

        add(contenido, BorderLayout.CENTER);

    }

    /** Crea un botón con el estilo del panel. */
    private JButton crearBoton(
            String nombre, Color colorFondo, Color colorTexto) {

        JButton boton = new JButton(nombre);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setForeground(colorTexto);
        boton.setBackground(colorFondo);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setCursor(
                new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)
        );
        boton.setPreferredSize(new Dimension(105, 36));

        return boton;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
