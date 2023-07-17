package com.mycompany.libreria.service;

import com.mycompany.libreria.entity.Cliente;
import com.mycompany.libreria.persistece.ControladoraDAO;
import java.util.List;
import java.util.Scanner;

public class ClienteService {

    private Scanner leer;
    private ControladoraDAO persis;

    public ClienteService() {
        this.leer = new Scanner(System.in).useDelimiter("\n");
        this.persis = new ControladoraDAO();
    }

    public Cliente crearCliente() {
        boolean salir = false;

        try {
            Long documento = tomarDocumento();
            Cliente clienteEncontrado = persis.verificarExistenciaCliente(new Cliente(documento));

            if (clienteEncontrado == null) {
                while (!salir) {
                    String nombre = "Nombre";
                    String apellido = "Apellido";
                    try {
                        nombre = tomarNombreApellido(nombre);
                        apellido = tomarNombreApellido(apellido);
                        String telefono = tomarTelefono();
                        Cliente cliente = new Cliente(documento, nombre, apellido, telefono);
                        validarCamposObligatorios(cliente);

                        System.out.println("""
                                       *************************************************
                                       * Agregando el nuevo cliente a la base de datos *
                                       *************************************************
                                       """);

                        return persis.crearClientes(cliente);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            } else {
                imprimirCliente(clienteEncontrado);
                return clienteEncontrado;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    private void validarCamposObligatorios(Cliente cliente) throws Exception {
        if (cliente.getDocumento() == null || cliente.getNombre() == null || cliente.getApellido() == null) {
            throw new Exception("Faltan campos obligatorios");
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

    private String tomarNombreApellido(String variable) {
        while (true) {

            System.out.print("Ingrese el " + variable + ": ");
            String nombre = leer.next();
            if (nombre == null) {
                System.out.println("Este campo no puede ser nulo.");
            } else if (nombre.trim().isEmpty()) {
                System.out.println("Este campo no puede ser vacio");
            } else if (nombre.matches("^[0-9]+$")) {
                System.out.println("El " + variable + " no puede ser exclusivamente numerico");
            } else if (nombre.contains(
                    "/") || nombre.contains("?") || nombre.contains("!")) {
                System.out.println("El " + variable + " no puede contener los simbolos /, ? o !");
            } else {
                return nombre;
            }
        }
    }

    public void imprimirClientes() {
        List<Cliente> clientes = persis.consultarClientes();
        System.out.println("**********************************************************************************************");
        System.out.println(" _________________________________________________________________");
        System.out.println("|   ID  |   Documento  |   Nombre   |   Apellido   |   Teléfono   |");
        System.out.println("|_______|______________|____________|______________|______________|");

        for (Cliente cliente : clientes) {
            System.out.printf("|%-7s|%-14s|%-12s|%-14s|%-14s|%n",
                    cliente.getId(), cliente.getDocumento(), cliente.getNombre(), cliente.getApellido(), cliente.getTelefono());
        }

        System.out.println("|_______|______________|____________|______________|______________|");
        System.out.println("Consulta realizada con éxito.");
        System.out.println("**********************************************************************************************");
    }

    public void imprimirCliente(Cliente cliente) {
        System.out.println("**********************************************************************************************");
        System.out.println(" _________________________________________________________________");
        System.out.println("|   ID  |   Documento  |   Nombre   |   Apellido   |   Teléfono   |");
        System.out.println("|_______|______________|____________|______________|______________|");

        System.out.printf("|%-7s|%-14s|%-12s|%-14s|%-14s|%n",
                cliente.getId(), cliente.getDocumento(), cliente.getNombre(), cliente.getApellido(), cliente.getTelefono());

        System.out.println("|_______|______________|____________|______________|______________|");
        System.out.println("**********************************************************************************************");
    }

    private Long tomarDocumento() {
        while (true) {
            System.out.print("Ingrese el documento del Cliente: ");
            String documentoStr = leer.next();
            leer.nextLine();
            try {
                Long documento = Long.parseLong(documentoStr);
                return documento;
            } catch (NumberFormatException e) {
                System.out.println("Debe indicar un valor numerico valido para el documento");
            }
        }
    }

    private String tomarTelefono() {
        while (true) {
            System.out.print("Ingrese el telefono del Cliente: ");
            String telefono = leer.next();

            // Validar si el telefono contiene solo numeros con expresiones regulares
            if (telefono.matches("\\d+")) {
                return telefono;
            } else {
                System.out.println("Debe indicar un valor numérico válido para el telefono");

            }
        }
    }

    public Cliente traerClientePorDocumento() {
        try {
            Long documento = tomarDocumento();
            Cliente clienteEncontrado = persis.verificarExistenciaCliente(new Cliente(documento));
            if (clienteEncontrado == null) {
                System.out.println("No se encontró un cliente con el documento ingresado.");
                return null;
            }
            return clienteEncontrado;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

}
