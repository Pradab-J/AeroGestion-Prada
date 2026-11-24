package controlador;
import modelo.DAO.integridadDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.DAO.asientoDAO;
import modelo.DAO.avion_asientosDAO;
import modelo.DAO.equipajeDAO;
import modelo.DAO.pasajeroDAO;
import modelo.DAO.pasajeroGestionDAO;
import modelo.DAO.tiqueteDAO;
import modelo.DAO.vueloDAO;
import modelo.entidad.pasajero;
import vista.Inicio;
import vista.JpnlPasajeros;
import vista.JpnlSeleccionarAsiento;
/**
 * CONTROLADOR de los datos del pasajero (paso 2 de la reserva): busca el
 * pasajero por documento, lo registra si es nuevo y continúa a elegir asiento.
 */
public class pasajeroControlador implements ActionListener {
    // Referencia a la pantalla donde se escriben y muestran los datos del pasajero.
    private JpnlPasajeros vistaPasajero;
    // Dependencias para acceder a datos y coordinar la navegación entre pantallas.
    private pasajeroDAO daoPasajero;
    private Inicio inicio;
    private buscarVueloControlador controladorBuscarVuelo;
    private asientoDAO daoAsiento;
    private vueloDAO daoVuelo;
    private avion_asientosDAO daoConfiguracion;
    private equipajeDAO daoEquipaje;
    private tiqueteDAO daoTiquete;
    private pasajeroGestionDAO daoGestionPasajeros;
    // Indica si el pasajero que está en pantalla ya existe
    // Es true cuando el documento ingresado corresponde a un pasajero existente.
    private boolean pasajeroEncontrado = false;
    // Guarda el documento que se encontró para detectar si luego lo modifican.
    private String documentoEncontrado = "";
    
    
    
    /** Constructor: guarda la vista y los DAO y conecta los botones con sus acciones. */
    public pasajeroControlador(
            JpnlPasajeros vistaPasajero,
            pasajeroDAO daoPasajero,
            Inicio inicio,
            buscarVueloControlador controladorBuscarVuelo,
            asientoDAO daoAsiento,
            vueloDAO daoVuelo,
            avion_asientosDAO daoConfiguracion,
            equipajeDAO daoEquipaje,
            tiqueteDAO daoTiquete,
            pasajeroGestionDAO daoGestionPasajeros) {
        this.vistaPasajero = vistaPasajero;
        this.daoPasajero = daoPasajero;
        this.inicio = inicio;
        this.controladorBuscarVuelo = controladorBuscarVuelo;
        this.daoAsiento = daoAsiento;
        this.daoVuelo = daoVuelo;
        this.daoConfiguracion = daoConfiguracion;
        this.daoEquipaje = daoEquipaje;
        this.daoTiquete = daoTiquete;
        // IMPORTANTE:
        // El parámetro se llama igual que el atributo: daoGestionPasajeros
        this.daoGestionPasajeros = daoGestionPasajeros;
        // Conecta cada botón con este controlador para procesar sus clics.
        vistaPasajero.btnBuscarDocumento.addActionListener(this);
        vistaPasajero.btnGuardarPasajero.addActionListener(this);
        vistaPasajero.btnContinuar.addActionListener(this);
        vigilarDocumento();
    }
    /** Recibe los clics de los botones de la vista y llama a la acción que corresponde. */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Identifica cuál botón originó el evento y ejecuta la operación correspondiente.
        // =========================
        // BUSCAR DOCUMENTO
        // =========================
        if (e.getSource() == vistaPasajero.btnBuscarDocumento) {
            buscarPorDocumento();
        }
        // =========================
        // GUARDAR
        // =========================
        if (e.getSource() == vistaPasajero.btnGuardarPasajero) {
            guardarPasajero();
        }
        // =========================
        // CONTINUAR
        // =========================
        if (e.getSource() == vistaPasajero.btnContinuar) {
            continuarFormAsiento();
        }
    }
    /** Busca en la base de datos el documento escrito y, si existe, rellena el formulario. */
    // =========================
    // BUSCAR PASAJERO
    // =========================
    private void buscarPorDocumento() {
        // Obtiene el documento escrito y elimina espacios al inicio y al final.
        String documento =
                vistaPasajero.txtDocumento
                        .getText()
                        .trim();
        // No se consulta la base de datos si el campo está vacío.
        if (documento.isEmpty()) {
            JOptionPane.showMessageDialog(
                    vistaPasajero,
                    "Ingrese el documento del pasajero."
            );
            vistaPasajero.txtDocumento.requestFocus();
            return;
        }
        // Consulta si ya existe un pasajero con ese documento.
        pasajero p =
                daoGestionPasajeros
                        .buscarPorDocumento(documento);
        // Si la consulta devuelve un pasajero, carga sus datos en el formulario.
        if (p != null) {
            marcarEncontrado();
            cargarDatos(p);
            JOptionPane.showMessageDialog(
                    vistaPasajero,
                    "El pasajero ya está registrado."
            );
        } else {
            pasajeroEncontrado = false;
            limpiarDatosExceptoDocumento();
            JOptionPane.showMessageDialog(
                    vistaPasajero,
                    "El pasajero no está registrado. "
                    + "Puede ingresar sus datos y guardarlo."
            );
            vistaPasajero.txtNombre.requestFocus();
        }
    }
    /** Rellena el formulario con los datos de un pasajero ya registrado. */
    // =========================
    // CARGAR DATOS
    // =========================
    private void cargarDatos(pasajero p) {
        // Copia los datos de la entidad a los campos visibles de la pantalla.
        vistaPasajero.txtDocumento.setText(
                p.getDocumento()
        );
        vistaPasajero.cmbTipoDocumento.setSelectedItem(
                p.getTipoDocumento()
        );
        vistaPasajero.txtNombre.setText(
                p.getNombre()
        );
        vistaPasajero.txtApellido.setText(
                p.getApellido()
        );
        vistaPasajero.cmbGenero.setSelectedItem(
                p.getGenero()
        );
        vistaPasajero.txtNacionalidad.setText(
                p.getNacionalidad()
        );
        vistaPasajero.txtTelefono.setText(
                p.getTelefono()
        );
        vistaPasajero.txtCorreo.setText(
                p.getCorreo()
        );
        // Convierte la fecha almacenada como texto al formato que acepta el selector de fecha.
        try {
            if (p.getFechaNacimiento() != null
                    && !p.getFechaNacimiento().isEmpty()) {
                java.util.Date fecha =
                        new SimpleDateFormat(
                                "yyyy-MM-dd"
                        ).parse(
                                p.getFechaNacimiento()
                        );
                vistaPasajero.txtFechaNacimiento.setDate(fecha);
            }
        } catch (Exception e) {
            System.out.println(
                    "No se pudo cargar la fecha: "
                    + e
            );
        }
    }
    /** Borra los datos del formulario pero conserva el documento escrito. */
    // =========================
    // LIMPIAR DATOS
    // =========================
    private void limpiarDatosExceptoDocumento() {
        // Limpia los campos personales, pero deja el documento para que pueda completarse el registro.
        vistaPasajero.txtNombre.setText("");
        vistaPasajero.txtApellido.setText("");
        vistaPasajero.txtNacionalidad.setText("");
        vistaPasajero.txtTelefono.setText("");
        vistaPasajero.txtCorreo.setText("");
        vistaPasajero.txtFechaNacimiento.setDate(null);
    }
    /** Valida y registra al pasajero (si es nuevo) antes de seguir con la reserva. */
    // =========================
    // GUARDAR
    // =========================
    private boolean guardarPasajero() {
        // Devuelve true si el pasajero ya existe o se guarda correctamente; false si falla.
        // Si ya encontramos al pasajero,
        // no lo volvemos a insertar.
        if (pasajeroEncontrado) {
            JOptionPane.showMessageDialog(
                    vistaPasajero,
                    "El pasajero ya está registrado."
            );
            return true;
        }
        // Detiene el registro cuando algún dato no cumple las validaciones.
        if (!validarRegistro()) {
            return false;
        }
        // Crea el objeto que reunirá la información antes de guardarla.
        pasajero p = new pasajero();
        try {
            String documento =
                    vistaPasajero.txtDocumento
                            .getText()
                            .trim();
            if (documento.isEmpty()) {
                JOptionPane.showMessageDialog(
                        vistaPasajero,
                        "Ingrese el documento del pasajero."
                );
                return false;
            }
            if (vistaPasajero.txtFechaNacimiento.getDate() == null) {
                JOptionPane.showMessageDialog(
                        vistaPasajero,
                        "Seleccione la fecha de nacimiento."
                );
                return false;
            }
            // Convierte la fecha seleccionada al formato año-mes-día para almacenarla.
            SimpleDateFormat formato =
                    new SimpleDateFormat(
                            "yyyy-MM-dd"
                    );
            String fecha =
                    formato.format(
                            vistaPasajero.txtFechaNacimiento.getDate()
                    );
            // Asigna al objeto pasajero los valores obtenidos del formulario.
            p.setDocumento(documento);
            p.setTipoDocumento(
                    vistaPasajero.cmbTipoDocumento
                            .getSelectedItem()
                            .toString()
            );
            p.setNombre(
                    vistaPasajero.txtNombre
                            .getText()
                            .trim()
            );
            p.setApellido(
                    vistaPasajero.txtApellido
                            .getText()
                            .trim()
            );
            p.setGenero(
                    vistaPasajero.cmbGenero
                            .getSelectedItem()
                            .toString()
            );
            p.setFechaNacimiento(fecha);
            p.setNacionalidad(
                    vistaPasajero.txtNacionalidad
                            .getText()
                            .trim()
            );
            p.setTelefono(
                    vistaPasajero.txtTelefono
                            .getText()
                            .trim()
            );
            p.setCorreo(
                    vistaPasajero.txtCorreo
                            .getText()
                            .trim()
            );
            // Segunda comprobación para reducir el riesgo de registrar un documento duplicado.
            pasajero existente =
                    daoGestionPasajeros
                            .buscarPorDocumento(documento);
            if (existente != null) {
                marcarEncontrado();
                cargarDatos(existente);
                JOptionPane.showMessageDialog(
                        vistaPasajero,
                        "El pasajero ya estaba registrado."
                );
                return true;
            }
            // Intenta insertar el pasajero en la base de datos.
            if (daoGestionPasajeros.guardar(p)) {
                marcarEncontrado();
                JOptionPane.showMessageDialog(
                        vistaPasajero,
                        "Pasajero guardado correctamente."
                );
                reiniciarFormulario();
                return true;
            } else {
                JOptionPane.showMessageDialog(
                        vistaPasajero,
                        "No se pudo guardar el pasajero."
                );
                return false;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    vistaPasajero,
                    "Complete todos los datos del pasajero."
            );
            System.out.println(
                    "Error en guardar: "
                    + ex
            );
            return false;
        }
    }
    /** Valida el formulario y avanza a la selección de asiento. */
    // =========================
    // CONTINUAR
    // =========================
    private void continuarFormAsiento() {
        // Comprueba que exista un documento antes de avanzar al siguiente paso de la reserva.
        String documento =
                vistaPasajero.txtDocumento
                        .getText()
                        .trim();
        if (documento.isEmpty()) {
            JOptionPane.showMessageDialog(
                    vistaPasajero,
                    "Primero ingrese el documento."
            );
            return;
        }
        // Si todavía no se confirmó si el pasajero existe, realiza la consulta ahora.
        // Si no se ha buscado el documento,
        // primero lo verificamos.
        if (!pasajeroEncontrado) {
            pasajero p =
                    daoGestionPasajeros
                            .buscarPorDocumento(documento);
            if (p != null) {
                marcarEncontrado();
                cargarDatos(p);
            } else {
                // Si no existe, intenta validarlo y guardarlo antes de continuar.
                // Es un pasajero nuevo,
                // entonces debemos guardarlo.
                if (!guardarPasajero()) {
                    return;
                }
            }
        }
        abrirSeleccionAsiento();
    }
    /** Abre la pantalla de selección de asiento del vuelo elegido. */
    // =========================
    // ABRIR SELECCIÓN ASIENTO
    // =========================
    private void abrirSeleccionAsiento() {
        // Recupera el identificador del vuelo que el usuario seleccionó anteriormente.
        int idVuelo =
                controladorBuscarVuelo
                        .getIdVueloSeleccionado();
        if (idVuelo <= 0) {
            JOptionPane.showMessageDialog(
                    vistaPasajero,
                    "No se encontró el vuelo seleccionado."
            );
            return;
        }
        // Crea la pantalla donde se mostrarán los asientos del vuelo.
        JpnlSeleccionarAsiento panelAsiento =
                new JpnlSeleccionarAsiento();
        // Crea el controlador de asientos y le entrega las dependencias necesarias,
        // el vuelo seleccionado y el documento del pasajero.
        seleccionarAsientoControlador controladorAsiento =
                new seleccionarAsientoControlador(
                        panelAsiento,
                        daoAsiento,
                        daoVuelo,
                        daoConfiguracion,
                        idVuelo,
                        inicio,
                        daoEquipaje,
                        vistaPasajero.txtDocumento.getText(),
                        daoTiquete,
                        daoGestionPasajeros
                );
        // Muestra la pantalla de selección de asiento dentro de la ventana principal.
        inicio.mostrarPanel(panelAsiento);
    }
    /** Deja el formulario como nuevo para empezar otra reserva. */
    // =========================
    // REINICIAR (nueva reserva)
    // =========================
    public void reiniciarFormulario() {
        // Restablece la marca para que el siguiente proceso vuelva a comprobar al pasajero.
        pasajeroEncontrado = false;
    }

    /** Carga en memoria/pantalla la lista de pasajeros registrados. */
    // =========================
    // LISTAR
    // =========================
    public void listarPasajeros() {
        // Obtiene todos los pasajeros registrados mediante el DAO.
        ArrayList<pasajero> lista =
                daoGestionPasajeros.listar();
        // Define las columnas que aparecerán en la tabla de la interfaz.
        DefaultTableModel modelo =
                new DefaultTableModel();
        modelo.addColumn("Documento");
        modelo.addColumn("Tipo Documento");
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("Genero");
        modelo.addColumn("Fecha Nacimiento");
        modelo.addColumn("Nacionalidad");
        modelo.addColumn("Telefono");
        modelo.addColumn("Correo");
        // Recorre la lista y crea una fila con los datos de cada pasajero.
        for (pasajero p : lista) {
            Object[] datos = {
                p.getDocumento(),
                p.getTipoDocumento(),
                p.getNombre(),
                p.getApellido(),
                p.getGenero(),
                p.getFechaNacimiento(),
                p.getNacionalidad(),
                p.getTelefono(),
                p.getCorreo()
            };
            modelo.addRow(datos);
        }
        // Asigna el modelo construido a la tabla visible.
        vistaPasajero.tblPasajero.setModel(modelo);
    }

    /** Recuerda que el pasajero ya existe y con qué documento fue encontrado. */
    // =========================================================
    // VALIDACIONES DEL REGISTRO
    // =========================================================
    private void marcarEncontrado() {
        // Marca el pasajero como existente y conserva el documento que se verificó.
        pasajeroEncontrado = true;
        documentoEncontrado = vistaPasajero.txtDocumento.getText().trim();
    }

    /** Si despues de buscar un pasajero se cambia el documento, ya no es el mismo pasajero. */
    private void vigilarDocumento() {
        // Escucha cambios en el campo documento para invalidar la búsqueda anterior
        // si el usuario escribe un documento diferente.
        vistaPasajero.txtDocumento.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            // Comprueba si el texto actual dejó de coincidir con el documento encontrado.
            private void verificarCambioDocumento() {
                if (pasajeroEncontrado
                        && !vistaPasajero.txtDocumento.getText().trim().equals(documentoEncontrado)) {
                    pasajeroEncontrado = false;
                }
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                verificarCambioDocumento();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                verificarCambioDocumento();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                verificarCambioDocumento();
            }
        });
    }

    /** Revisa los campos del formulario; avisa del primer error y devuelve false si hay datos inválidos. */
    private boolean validarRegistro() {
        // Normaliza los textos antes de validarlos: quita espacios sobrantes y
        // estandariza teléfono y correo electrónico.
        String doc = Validar.limpiar(vistaPasajero.txtDocumento.getText());
        String nombre = Validar.limpiar(vistaPasajero.txtNombre.getText());
        String apellido = Validar.limpiar(vistaPasajero.txtApellido.getText());
        String nacionalidad = Validar.limpiar(vistaPasajero.txtNacionalidad.getText());
        String telefono = Validar.limpiar(vistaPasajero.txtTelefono.getText()).replace(" ", "").replace("-", "");
        String correo = Validar.limpiar(vistaPasajero.txtCorreo.getText()).toLowerCase();
        vistaPasajero.txtDocumento.setText(doc);
        vistaPasajero.txtNombre.setText(nombre);
        vistaPasajero.txtApellido.setText(apellido);
        vistaPasajero.txtNacionalidad.setText(nacionalidad);
        vistaPasajero.txtTelefono.setText(telefono);
        vistaPasajero.txtCorreo.setText(correo);

        // Obtiene el tipo de documento seleccionado y valida conjuntamente los campos.
        Object tipo = vistaPasajero.cmbTipoDocumento.getSelectedItem();
        String error = Validar.pasajero(tipo == null ? "" : tipo.toString(), doc, nombre, apellido,
                vistaPasajero.cmbGenero.getSelectedItem(), nacionalidad, telefono, correo,
                vistaPasajero.txtFechaNacimiento.getDate());
        // Si las validaciones anteriores pasaron, comprueba también que el documento
        // no esté registrado en la tabla de pasajeros.
        if (error == null && integridadDAO.existe("pasajeros", "documento", doc)) {
            error = "Ese documento ya está registrado.\nPulse «Buscar» para cargar los datos del pasajero.";
        }
        return !Validar.mostrar(vistaPasajero, error);
    }
}
