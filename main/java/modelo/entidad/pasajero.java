package modelo.entidad;

/**
 * Clase de entidad que representa a un pasajero del sistema AeroGestión Prada.
 * Contiene los datos personales que se almacenan para identificar a la persona
 * que va a realizar un viaje.
 *
 * @author Aprendiz
 */
public class pasajero {

    // Atributos privados que almacenan la información personal del pasajero.
    // El tipo String permite manejar estos datos como texto.
    private String documento;
    private String tipoDocumento;
    private String nombre;
    private String apellido;
    private String genero;
    private String fechaNacimiento;
    private String nacionalidad;
    private String telefono;
    private String correo;

    /**
     * Constructor vacío.
     * Permite crear un objeto pasajero sin asignar sus datos inicialmente.
     * Los valores se pueden establecer después mediante los métodos set.
     */
    public pasajero() {

    }

    /**
     * Constructor con todos los atributos.
     * Permite crear un objeto pasajero y asignar sus datos en el momento
     * de construirlo.
     *
     * @param documento número del documento de identidad del pasajero.
     * @param tipoDocumento tipo de documento de identidad.
     * @param nombre nombre del pasajero.
     * @param apellido apellido del pasajero.
     * @param genero género registrado para el pasajero.
     * @param fechaNacimiento fecha de nacimiento del pasajero.
     * @param nacionalidad nacionalidad del pasajero.
     * @param telefono número telefónico de contacto.
     * @param correo correo electrónico de contacto.
     */
    public pasajero(String documento, String tipoDocumento, String nombre, String apellido, String genero,
            String fechaNacimiento, String nacionalidad, String telefono, String correo) {

        // Guarda cada parámetro recibido en el atributo correspondiente del objeto.
        this.documento = documento;
        this.tipoDocumento = tipoDocumento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.genero = genero;
        this.fechaNacimiento = fechaNacimiento;
        this.nacionalidad = nacionalidad;
        this.telefono = telefono;
        this.correo = correo;
    }

    /**
     * Obtiene el documento de identidad del pasajero.
     *
     * @return el documento almacenado.
     */
    public String getDocumento() {
        return documento;
    }

    /**
     * Asigna el documento de identidad del pasajero.
     *
     * @param documento documento que se va a guardar en el objeto.
     */
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    /**
     * Obtiene el tipo de documento del pasajero.
     *
     * @return el tipo de documento almacenado.
     */
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * Asigna el tipo de documento del pasajero.
     *
     * @param tipoDocumento tipo de documento que se va a guardar.
     */
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * Obtiene el nombre del pasajero.
     *
     * @return el nombre almacenado.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el nombre del pasajero.
     *
     * @param nombre nombre que se va a guardar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el apellido del pasajero.
     *
     * @return el apellido almacenado.
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Asigna el apellido del pasajero.
     *
     * @param apellido apellido que se va a guardar.
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Obtiene el género registrado para el pasajero.
     *
     * @return el género almacenado.
     */
    public String getGenero() {
        return genero;
    }

    /**
     * Asigna el género del pasajero.
     *
     * @param genero género que se va a guardar.
     */
    public void setGenero(String genero) {
        this.genero = genero;
    }

    /**
     * Obtiene la fecha de nacimiento del pasajero.
     *
     * @return la fecha de nacimiento almacenada.
     */
    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Asigna la fecha de nacimiento del pasajero.
     *
     * @param fechaNacimiento fecha que se va a guardar.
     */
    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Obtiene la nacionalidad del pasajero.
     *
     * @return la nacionalidad almacenada.
     */
    public String getNacionalidad() {
        return nacionalidad;
    }

    /**
     * Asigna la nacionalidad del pasajero.
     *
     * @param nacionalidad nacionalidad que se va a guardar.
     */
    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    /**
     * Obtiene el teléfono de contacto del pasajero.
     *
     * @return el teléfono almacenado.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Asigna el teléfono de contacto del pasajero.
     *
     * @param telefono teléfono que se va a guardar.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene el correo electrónico del pasajero.
     *
     * @return el correo almacenado.
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Asigna el correo electrónico del pasajero.
     *
     * @param correo correo que se va a guardar.
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
