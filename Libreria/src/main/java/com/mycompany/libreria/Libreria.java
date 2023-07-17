package com.mycompany.libreria;
import com.mycompany.libreria.service.AutorService;
import com.mycompany.libreria.service.ClienteService;
import com.mycompany.libreria.service.LibroService;
import com.mycompany.libreria.service.PrestamoService;
import java.util.Scanner;

public class Libreria {

    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in).useDelimiter("\n");
        boolean salir = true;
        LibroService lS = new LibroService();
        PrestamoService pS = new PrestamoService();
        do {
            boolean seguir = true;

            try {
                System.out.println("""
                             ******************************************************************
                             *                        MENU                                    *
                             ******************************************************************
                             *         1) Generar las tablas con JPA                          *
                             *         2) Búsqueda de un Autor por nombre.                    *
                             *         3) Búsqueda de un libro por ISBN.                      *
                             *         4) Búsqueda de un libro por Título.                    *
                             *         5) Búsqueda de un libro/s por nombre de Autor.         *
                             *         6) Búsqueda de un libro/s por nombre de Editorial.     *
                             *         7) Creación de un Cliente nuevo                        *
                             *         8) Registrar el préstamo de un libro.                  *
                             *         9) Devolución de un libro                              *
                             *         10) Búsqueda de todos los préstamos de un Cliente.     *
                             *         11) SALIR.                                             *
                             *                                                                *
                             ******************************************************************
                           """);
                System.out.print("    Ingrese una opcion: ");
                int opcion = opcion();
                System.out.println("  *******************************");
                switch (opcion) {
                    case 1 -> {
                        lS.mostrarTablas();
                        System.out.println("Las tablas fueron generadas con éxito.");
                    }
                    case 2 -> {
                        AutorService aS = new AutorService();
                        do {

                            System.out.println("******* INGRESE UN NOMBRE *******");
                            System.out.println("""
                                                                            Jorge Luis Borges, Julio Cortázar, Héctor O.-Francisco S.
                                                                            Umberto Eco, Tomás Eloy Martínez, Ricardo Güiraldes, Gabriel García Márquez
                                                                            Ernesto Sabato, Adolfo Bioy Casares, Roberto Bolaño, José Hernández
                                                                            William Faulkner, José Ingenieros, Roberto Arlt
                                                                           """);
                            {
                                try {

                                    aS.consultaAutorPorNombre();

                                    seguir = false;
                                } catch (Exception ex) {
                                    System.err.println("Error al tomar el nombre: " + ex.getMessage());

                                }
                            }
                        } while (seguir);
                    }

                    case 3 -> {
                        do {
                            try {
                                lS.consultarLibro();
                                seguir = false;
                            } catch (Exception ex) {
                                System.out.println(ex.getMessage());
                            }
                        } while (seguir);
                    }
                    case 4 -> {
                        do {
                            System.out.println("******* INGRESE UN NOMBRE *******");
                            System.out.println("""
                                                                           Ficciones, Rayuela, El Aleph, Cien años de soledad
                                                                           El túnel, El amor en los tiempos del cólera, El nombre de la rosa
                                                                           Historias de cronopios y de famas, El Eternauta, Santa Evita
                                                                           Don Segundo Sombra, Sobre héroes y tumbas, La invención de Morel
                                                                           Los detectives salvajes, Martín Fierro, La muerte y la brújula
                                                                           El ruido y la furia, El hombre mediocre, El hacedor, El juguete rabioso
                                                                           """);

                            try {
                                lS.consultaLibroPorTitulo();

                                seguir = false;
                            } catch (Exception ex) {
                                System.err.println("Error al tomar el nombre: " + ex.getMessage());

                            }

                        } while (seguir);
                    }
                    case 5 -> {
                        System.out.println("""
                                                                    Jorge Luis Borges, Julio Cortázar, Héctor Oesterheld y Francisco Solano López
                                                                    Umberto Eco, Tomás Eloy Martínez, Ricardo Güiraldes, Gabriel García Márquez
                                                                    Ernesto Sabato, Adolfo Bioy Casares, Roberto Bolaño, José Hernández
                                                                    William Faulkner, José Ingenieros, Roberto Arlt
                                                                   """);
                        try {
                            lS.consultaLibroPorNombreAutor();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    case 6 -> {
                        System.out.println("""
                                                                   Emece, Sudamericana, Oveja Negra, Lumen, Frontera, Planeta,
                                                                   Losada, Seix Barral, Anagrama, Hachette, Latina, Claridad
                                                                   """);
                        do {

                            try {
                                lS.consultaLibroPorNombreEditorial();
                                seguir = false;
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        } while (seguir);
                    }
                    case 7 -> {
                        ClienteService cS = new ClienteService();

                        cS.crearCliente();
                        seguir = false;
                    }
                    case 8 -> pS.crearPrestamoLibro();
                    case 9 -> pS.devolverLibro();
                    case 10 -> pS.consultarPrestamoCliente();
                    case 11 -> {
                        System.out.println("Gracias por utilizar el sistema!");
                        salir = false;
                    }
                    default -> {
                        System.out.println("******************************************************************");
                        System.out.println("Por favor intente nuevante. Opcion incorrecta.");
                    }

                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        } while (salir);

    }

    public static int opcion() throws Exception {
        Scanner leer = new Scanner(System.in).useDelimiter("\n");
        //System.out.print("Ingrese el ID del Autor: ");
        String input = leer.next();

        int codigo;
        try {
            codigo = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new Exception("Debe indicar un valor numerico");
        }

        if (codigo <= 0) {
            throw new Exception("Debe indicar un nuevo valor Entero");
        } else {
            return codigo;
        }
    }

}
