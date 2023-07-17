package com.mycompany.libreria.service;

import com.mycompany.libreria.entity.Libro;
import com.mycompany.libreria.persistece.ControladoraDAO;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LibroService {

    private Scanner leer;
    private ControladoraDAO persis;

    public LibroService() {
        this.persis = new ControladoraDAO();
        this.leer = new Scanner(System.in).useDelimiter("\n");
    }

    public void consultarLibro() {
        Long id;
        try {
            id = tomarId();
            Libro libro = persis.buscarLibroPorId(id);
            if (libro == null) {
                throw new NullPointerException("No se encontro el libro con el isbn especificado.");
            } else {
                System.out.println("El libro pedido: ");
                imprimirTabla(libro);
            }
        } catch (Exception ex) {
            throw new NullPointerException("Error al consultar el libro por isbn: ");
        }
    }

    public void crearLibro() {
        //Logica de crear libro no la hice ya que la inserto
    }

    public void modificarLibro(Libro libro) {
        try {
            persis.modificarLibro(libro);
        } catch (Exception ex) {
            Logger.getLogger(LibroService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarLibro() {
        //Logica de eliminar libro
    }

    public void consultaLibroPorNombreEditorial() throws Exception {
        String nombre = "Nombre";
        try {
            nombre = tomarNombre(nombre);
            List<Libro> libros = persis.consultaLibroPorNombreEditorial(nombre);
            imprimirLibros(libros);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    private Long tomarId() throws Exception {
        System.out.print("Ingrese isbn para consultar el Libro: ");
        String input = leer.next();

        Long codigo;
        try {
            codigo = Long.parseLong(input);
        } catch (NumberFormatException e) {
            throw new Exception("Debe indicar un valor numerico valido para el isbn");
        }

        if (codigo <= 0) {
            throw new Exception("Debe indicar un nuevo valor valido para el isbn");
        } else {
            return codigo;
        }
    }

    public void imprimirTabla(Libro libro) {

        System.out.println("******************************************************************************************************************");
        System.out.println(" __________________________________________________________________________________________________________________");
        System.out.println("|ISBN|                Titulo                  | Año |Prestados|Restantes| Alta|         Autor        |  Editorial  |");
        System.out.println("|____|________________________________________|_____|_________|_________|_____|______________________|_____________|");
        System.out.printf("|%-4s|%-40s|%-5s|%-9s|%-9s|%-5s|%-22s|%-13s|%n",
                libro.getIsbn(), libro.getTitulo(), libro.getAnio(), libro.getEjemplaresPrestados(),
                libro.getEjemplaresRestantes(), libro.getAlta(), libro.getAutor().getNombre(), libro.getEditorial().getNombre());
        System.out.println("|____|________________________________________|_____|_________|_________|_____|______________________|_____________|");
        System.out.println("******************************************************************************************************************");
    }

    private String tomarNombre(String variable) throws Exception {
        System.out.print("Ingrese el " + variable + ": ");
        String nombre = leer.next();
        if (nombre == null) {
            throw new Exception("Este campo no puede ser nulo.");
        } else if (nombre.trim().isEmpty()) {
            throw new Exception("Este campo no puede ser vacio");
        } else if (nombre.matches("^[0-9]+$")) {
            throw new Exception("El " + variable + " no puede ser exclusivamente numerico");
        } else if (nombre.contains(
                "/") || nombre.contains("?") || nombre.contains("!")) {
            throw new Exception("El " + variable + " no puede contener los simbolos /, ? o !");
        } else {
            return nombre;
        }
    }

    public void consultaLibroPorTitulo() throws Exception {
        String titulo = "Titulo";
        try {
            titulo = tomarNombre(titulo);
            Libro libro = persis.consultarLibroPorTitulo(titulo);
            imprimirTabla(libro);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void mostrarTablas() {
        
        
        System.out.println("*****************************************************************************");
        System.out.println("Tabla Libros");
        System.out.println("****************************************************************************(");
        System.out.println(" ___________________________________________________________________________");
        System.out.println("|ISBN|   TITULO   |ANIO|PRESTADOS|RESTANTES| ALTA|    AUTOR     | EDITORIAL |");
        System.out.println("|____|____________|____|_________|_________|_____|______________|___________|");
        System.out.println("*****************************************************************************");
        System.out.println("Tabla Autores");
        System.out.println("************************");
        System.out.println(" ______________________");
        System.out.println("| ID |  NOMBRE  | ALTA |");
        System.out.println("|____|__________|______|");
        System.out.println("************************");
        System.out.println("Tabla Editoriales");
        System.out.println("************************");
        System.out.println(" ______________________");
        System.out.println("| ID |  NOMBRE  | ALTA |");
        System.out.println("|____|__________|______|");
        System.out.println("************************");
        System.out.println("Tabla Clientes");
        System.out.println("*****************************************");
        System.out.println(" _______________________________________");
        System.out.println("| ID |DOCUMENTO|NOMBRE|APELLIDO|TELEFONO|");
        System.out.println("|____|_________|______|________|________|");
        System.out.println("*****************************************");
        System.out.println("Tabla Prestamos");
        System.out.println("**********************************************");
        System.out.println(" ____________________________________________");
        System.out.println("| ID |FECHA_PRESTAMO|FECHA_DEVOLUCION|CLIENTE|");
        System.out.println("|____|______________|________________|_______|");
        System.out.println("**********************************************");
        System.out.println("*****************************************************************************");
    }

    public void consultaLibroPorNombreAutor() throws Exception {
        String nombre = "Nombre";
        try {
            nombre = tomarNombre(nombre);
            List<Libro> libros = persis.consultaLibroPorNombreAutor(nombre);
            if (libros.isEmpty()) {
                System.out.println("No se encontraron libros del autor especificado.");
            } else {
                imprimirLibros(libros);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void imprimirLibros(List<Libro> libros) {
        System.out.println("******************************************************************************************************************");
        System.out.println(" __________________________________________________________________________________________________________________");
        System.out.println("|ISBN|                Título                  | Año |Prestados|Restantes| Alta|         Autor        |  Editorial  |");
        System.out.println("|____|________________________________________|_____|_________|_________|_____|______________________|_____________|");

        for (Libro libro : libros) {
            System.out.printf("|%-4s|%-40s|%-5s|%-9s|%-9s|%-5s|%-22s|%-13s|%n",
                    libro.getIsbn(), libro.getTitulo(), libro.getAnio(), libro.getEjemplaresPrestados(),
                    libro.getEjemplaresRestantes(), libro.getAlta(), libro.getAutor().getNombre(), libro.getEditorial().getNombre());
        }

        System.out.println("|____|________________________________________|_____|_________|_________|_____|______________________|_____________|");
        System.out.println("******************************************************************************************************************");
    }

    public Libro consultaLibroPorTituloPrestamo() throws Exception {
        String titulo = "Titulo";
        try {
            titulo = tomarNombre(titulo);
            Libro libro = persis.consultarLibroPorTitulo(titulo);
            if (libro == null) {
                throw new Exception("No se encontro el libro: " + titulo);
            }
            imprimirTabla(libro);
            return libro;
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return null;

    }

    public Libro consultaLibroPorIsbn(Long isbn) {
        Libro libro = persis.buscarLibroPorId(isbn);
        if (libro == null) {
            System.out.println("No se encontro ningun libro con ese isbn.");
        }
        return libro;
    }

    

}
