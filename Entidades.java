import java.time.LocalDate;

public class Entidades {

    /** Clase base para todos los tipos de usuarios del sistema */
    public abstract class Usuario {
        protected int idUsuario;
        protected String nombre;
        protected String primerApellido;
        protected String segundoApellido;
        protected String correo;
        protected String telefono;
        protected String contraseña; // Encriptada en SHA-1
        protected String tipo;

        public Usuario(int idUsuario, String nombre, String primerApellido, String segundoApellido,
                       String correo, String telefono, String contraseña, String tipo) {
            this.idUsuario = idUsuario;
            this.nombre = nombre;
            this.primerApellido = primerApellido;
            this.segundoApellido = segundoApellido;
            this.correo = correo;
            this.telefono = telefono;
            this.contraseña = contraseña;
            this.tipo = tipo;
        }

        public abstract void mostrarRol();

        // Getters y Setters
        public int getIdUsuario() {
            return idUsuario;
        }

        public void setIdUsuario(int idUsuario) {
            this.idUsuario = idUsuario;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getPrimerApellido() {
            return primerApellido;
        }

        public void setPrimerApellido(String primerApellido) {
            this.primerApellido = primerApellido;
        }

        public String getSegundoApellido() {
            return segundoApellido;
        }

        public void setSegundoApellido(String segundoApellido) {
            this.segundoApellido = segundoApellido;
        }

        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
        }

        public String getTelefono() {
            return telefono;
        }

        public void setTelefono(String telefono) {
            this.telefono = telefono;
        }

        public String getContraseña() {
            return contraseña;
        }

        public void setContraseña(String contraseña) {
            this.contraseña = contraseña;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }
    }

    /** Tipo de usuario: Administrador */
    public class Administrador extends Usuario {
        public Administrador(int idUsuario, String nombre, String primerApellido, String segundoApellido,
                             String correo, String telefono, String contraseña) {
            super(idUsuario, nombre, primerApellido, segundoApellido, correo, telefono, contraseña, "Administrador");
        }

        @Override
        public void mostrarRol() {
            System.out.println("Soy un administrador del sistema.");
        }
    }

    /** Tipo de usuario: Lector */
    public class Lector extends Usuario {
        public Lector(int idUsuario, String nombre, String primerApellido, String segundoApellido,
                      String correo, String telefono, String contraseña) {
            super(idUsuario, nombre, primerApellido, segundoApellido, correo, telefono, contraseña, "Lector");
        }

        @Override
        public void mostrarRol() {
            System.out.println("Soy un lector registrado.");
        }
    }

    /** Patrón Factory: crea instancias de Usuario según su tipo */
    public class UsuarioFactory {
        public static Usuario crearUsuario(String tipo, int idUsuario, String nombre, String primerApellido,
                                           String segundoApellido, String correo, String telefono, String contraseña) {
            switch (tipo.toLowerCase()) {
                case "administrador":
                    return new Administrador(idUsuario, nombre, primerApellido, segundoApellido, correo, telefono, contraseña);
                case "lector":
                    return new Lector(idUsuario, nombre, primerApellido, segundoApellido, correo, telefono, contraseña);
                default:
                    throw new IllegalArgumentException("Tipo de usuario no válido: " + tipo);
            }
        }
    } 

    /** Clase Libro */
    public class Libro {
        private int idLibro;
        private String tituloLibro;
        private String autor;
        private String isbn;
        private int añoPublicacion;
        private boolean disponible;

        public Libro(int idLibro, String tituloLibro, String autor, String isbn, int añoPublicacion) {
            this.idLibro = idLibro;
            this.tituloLibro = tituloLibro;
            this.autor = autor;
            this.isbn = isbn;
            this.añoPublicacion = añoPublicacion;
            this.disponible = true;
        }

        // Getters y Setters
        public int getIdLibro() {
            return idLibro;
        }

        public void setIdLibro(int idLibro) {
            this.idLibro = idLibro;
        }

        public String getTituloLibro() {
            return tituloLibro;
        }

        public void setTituloLibro(String tituloLibro) {
            this.tituloLibro = tituloLibro;
        }

        public String getAutor() {
            return autor;
        }

        public void setAutor(String autor) {
            this.autor = autor;
        }

        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        public int getAñoPublicacion() {
            return añoPublicacion;
        }

        public void setAñoPublicacion(int añoPublicacion) {
            this.añoPublicacion = añoPublicacion;
        }

        public boolean isDisponible() {
            return disponible;
        }

        public void setDisponible(boolean disponible) {
            this.disponible = disponible;
        }
    }

    /** Clase Prestamo */
    public class Prestamo {
        private int idPrestamo;
        private Usuario usuario;
        private Libro libro;
        private LocalDate fechaPrestamo;
        private LocalDate fechaLimitePrestamo;
        private LocalDate fechaDevolucion;

        public Prestamo(int idPrestamo, Usuario usuario, Libro libro,
                        LocalDate fechaPrestamo, LocalDate fechaLimitePrestamo, LocalDate fechaDevolucion) {
            this.idPrestamo = idPrestamo;
            this.usuario = usuario;
            this.libro = libro;
            this.fechaPrestamo = fechaPrestamo;
            this.fechaLimitePrestamo = fechaLimitePrestamo;
            this.fechaDevolucion = fechaDevolucion;
        }

        // Getters y Setters
        public int getIdPrestamo() {
            return idPrestamo;
        }

        public void setIdPrestamo(int idPrestamo) {
            this.idPrestamo = idPrestamo;
        }

        public Usuario getUsuario() {
            return usuario;
        }

        public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
        }

        public Libro getLibro() {
            return libro;
        }

        public void setLibro(Libro libro) {
            this.libro = libro;
        }

        public LocalDate getFechaPrestamo() {
            return fechaPrestamo;
        }

        public void setFechaPrestamo(LocalDate fechaPrestamo) {
            this.fechaPrestamo = fechaPrestamo;
        }

        public LocalDate getFechaLimitePrestamo() {
            return fechaLimitePrestamo;
        }

        public void setFechaLimitePrestamo(LocalDate fechaLimitePrestamo) {
            this.fechaLimitePrestamo = fechaLimitePrestamo;
        }

        public LocalDate getFechaDevolucion() {
            return fechaDevolucion;
        }

        public void setFechaDevolucion(LocalDate fechaDevolucion) {
            this.fechaDevolucion = fechaDevolucion;
        }
    }
}
