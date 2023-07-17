package com.mycompany.libreria.service;

import com.mycompany.libreria.entity.Cliente;
import com.mycompany.libreria.entity.Libro;
import com.mycompany.libreria.entity.Prestamo;
import com.mycompany.libreria.persistece.ControladoraDAO;
import com.mycompany.libreria.persistece.exceptions.NonexistentEntityException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrestamoService {

    private ControladoraDAO persis;
    private ClienteService cS;
    private LibroService lS;
    private Scanner leer;

    public PrestamoService() {
        this.cS = new ClienteService();
        this.leer = new Scanner(System.in).useDelimiter("\n");
        this.lS = new LibroService();
        this.persis = new ControladoraDAO();
    }

    public void crearPrestamoLibro() {
        try {
            Libro libro = lS.consultaLibroPorTituloPrestamo();
            Long isbn = libro.getIsbn();
            if (libro != null) {

                if (libro.getAlta() && libro.getEjemplaresRestantes() < 1 && libro != null) {
                    System.out.println("Sin Stock para este libro");

                    System.out.println("Quiere consultar fecha de disponibilidad? SI/NO");
                    String respuesta = leer.next();
                    while (respuesta.equalsIgnoreCase("SI")) {//cambiar segun la opcion asi no me olvido jajaja
                        //hay que mejorar esta logica de si acepta esperar o va por otro titulo
                            //Aca desarrollar logica por si o no
                            Prestamo prestamo = persis.BuscarprestamoPorIsbn(isbn);
                            if (prestamo != null) {
                                imprimirPrestamos(prestamo);
                                
                                crearPrestamoLibro();
                                break;
                            }
                        }
                        System.out.println("Lamentamos no tener disponible el libro.");
                    }else if (!libro.getAlta()) {
                    System.out.println("El libro no esta disponible");
                } else {

                    Cliente cliente = cS.crearCliente();
                    System.out.print("Ingrese cuantos dias desea el libro: ");
                    long dias = leer.nextLong();
                    Prestamo prestamoLibro = new Prestamo(LocalDate.now(), LocalDate.now().plusDays(dias), cliente, libro);
                    libro.setEjemplaresPrestados(libro.getEjemplaresPrestados() + 1);
                    libro.setEjemplaresRestantes(libro.getEjemplaresRestantes() - 1);

                    lS.modificarLibro(libro);
                    persis.crearPrestamos(prestamoLibro);
                    imprimirPrestamos(prestamoLibro);
                    //System.out.println(prestamoLibro.toString());
                    System.out.println("""
                                       *******************************
                                       * Libro prestado exitosamente *
                                       *******************************
                                       """);
                }
                } else {
                    System.out.println("El libro no se encuntra");
                    System.out.println("Deseas gestionar otro libro? SI/NO");
                    String opcion = leer.next();
                    while (opcion.equalsIgnoreCase("SI")) {
                        crearPrestamoLibro();
                    }
                    System.out.println("""
                                   *************************
                                   * Gracias por consulta. *
                                   *************************
                                   """);
                }
            }catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        }

    

    

    private String tomarNombreApellido(String variable) throws Exception {
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

    private Long tomarDocumento() {
        while (true) {
            System.out.print("Ingrese el documento del Cliente: ");
            String documentoStr = leer.next();
            try {
                Long documento = Long.parseLong(documentoStr);
                return documento;
            } catch (NumberFormatException e) {
                System.out.println("Debe indicar un valor numerico valido para el documento");
            }
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

    public void imprimirPrestamos(Prestamo prestamo) {
        System.out.println("****************************************************************");
        System.out.println(" ______________________________________________________________");
        System.out.println("|ID  |Fecha Prestamo|Fecha Devolucion|         Cliente         |");
        System.out.println("|____|______________|________________|_________________________|");

        System.out.printf("|%-4s|%-14s|%-16s|%-25s|%n",
                prestamo.getId(), prestamo.getFechaPrestamo(), prestamo.getFechaDevolucion(),
                prestamo.getCliente().getNombre() + " " + prestamo.getCliente().getApellido());
        System.out.println("|____|______________|________________|_________________________|");
        System.out.println("****************************************************************");
    }

    public void imprimirPrestamos(List<Prestamo> prestamos) {
        System.out.println("**********************************************************************");
        System.out.println(" _____________________________________________________________________________________");
        System.out.println("|ID  |Fecha Prestamo|Fecha Devolucion|ISBN   |                 TITULO                 |");
        System.out.println("|____|______________|________________|_______|________________________________________|");
        for (Prestamo prestamo : prestamos) {
            System.out.printf("|%-4s|%-14s|%-16s|%-7s|%-40s|%n",
                    prestamo.getId(), prestamo.getFechaPrestamo(), prestamo.getFechaDevolucion(),
                    prestamo.getLibro().getIsbn(), prestamo.getLibro().getTitulo());
            System.out.println("|____|______________|________________|_______|________________________________________|");

        }
        System.out.println("**********************************************************************");
    }

    public List<Prestamo> consultarPrestamoCliente() {
        int intentos = 0;
        final int MAX_INTENTOS = 3;

        do {
            try {
                Cliente cliente = cS.traerClientePorDocumento();
                if (cliente == null) {
                    intentos++;
                    if (intentos >= MAX_INTENTOS) {
                        System.out.println("Se alcanzo el numero maximo de intentos. ");
                        break;
                    }
                    System.out.println("Intente nuevamente.");
                    continue;
                }
                cS.imprimirCliente(cliente);
                List<Prestamo> prestamosPorCliente = persis.consultarPrestamosPorCliente(cliente);
                if (prestamosPorCliente.isEmpty()) {
                    System.out.println("No se registran pretamos para este cliente");
                }
                System.out.println("Cantidad de prestamos encontrados: " + prestamosPorCliente.size());
                imprimirPrestamos(prestamosPorCliente);
                return prestamosPorCliente;

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while (true);
        return null;
    }

    public void devolverLibro() {
        List<Prestamo> prestamos = consultarPrestamoCliente();
        System.out.print("Ingrese el isbn del libro que desea devolver: ");
        Long isbn = leer.nextLong();
        Libro libro = lS.consultaLibroPorIsbn(isbn);
        libro.setEjemplaresPrestados(libro.getEjemplaresPrestados() - 1);
        libro.setEjemplaresRestantes(libro.getEjemplaresRestantes() + 1);
        lS.modificarLibro(libro);
        for (Prestamo prestamo : prestamos) {
            if (prestamo.getLibro().getIsbn() == libro.getIsbn()) {
                Integer id = prestamo.getId();
                try {
                    persis.eliminarPrestamos(id);

                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(PrestamoService.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

    }
}
