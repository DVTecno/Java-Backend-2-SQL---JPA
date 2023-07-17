package com.mycompany.libreria.service;

import com.mycompany.libreria.entity.Autor;
import com.mycompany.libreria.persistece.ControladoraDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.mysql.cj.util.StringUtils;

public class AutorService {

    private ControladoraDAO persis;
    private Scanner leer;

    public AutorService() {
        this.persis = new ControladoraDAO();
        this.leer = new Scanner(System.in).useDelimiter("\n");
    }

    public void consultarAutores() {
        persis.consultarAutor();
    }

    public void crearAutor() {
        Autor autor = new Autor();
        persis.crearAutor(autor);
    }

    public void modificarAutor(Autor autor) {
        try {
            if (autor == null) {
                throw new Exception("El autor no tiene datos cargados");
            }
            //logica a realizar para la modificacion si hubiere alguna de lo contrario se persiste

            persis.modificarAutor(autor);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void eliminarAutor() {
        //Aqui para eliminar
    }

    public void consultaAutorPorNombre() throws Exception {
        String nombre = tomarNombre();
        try {
            Autor autor = persis.consultaAutorPorNombre(nombre);
            imprimirTablaAutores( autor);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public List<Autor> obtenerAutoresPorLote(int loteSize, int valorInicial) throws Exception {
        List<Autor> autores = new ArrayList<>();
        boolean finalDatos = false;

        try {
            while (!finalDatos) {
                List<Autor> lote = persis.consultarAutoresPorLotes(loteSize, valorInicial);
                if (lote.isEmpty()) {
                    System.err.println("La lista esta vacia");
                    finalDatos = true;
                } else {
                    autores.addAll(lote);
                    valorInicial += loteSize;
                }
            }
        } catch (Exception e) {
            throw new Exception("Error al obtener autores por lote");
        }

        return autores;
    }

    public int tomarId() throws Exception {
        System.out.print("Ingrese el ID del Autor: ");
        String input = leer.next();

        int codigo;
        try {
            codigo = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new Exception("Debe indicar un valor numerico valido para el ID");
        }

        if (codigo <= 0) {
            throw new Exception("Debe indicar un nuevo valor valido para el ID");
        } else {
            return codigo;
        }
    }

    private String tomarNombre() throws Exception {
        System.out.print("Ingrese el nombre: ");
        String nombre = leer.next();
        if (nombre == null) {
            throw new Exception("Este campo no puede ser nulo.");
        } else if (nombre.trim().isEmpty()) {
            throw new Exception("Este campo no puede ser vacio");
        } else if (nombre.contains("'") || nombre.contains("\"")) {
            throw new Exception("El nombre no puede contener comillas");
        } else if (StringUtils.isStrictlyNumeric(nombre)) {
            throw new Exception("El nombre no puede ser numerico");
        } else {
            return nombre;
        }
    }

    public void imprimirTablaAutores(Autor autor) {
        System.out.println("Tabla Autores");
        System.out.println("************************************");
        System.out.println(" _________________________________");
        System.out.println("| ID |         NOMBRE       |ALTA |");
        System.out.println("|____|______________________|_____|");

        System.out.printf("|%-4s|%-22s|%-5s|%n", autor.getId(), autor.getNombre(), autor.getAlta());
        System.out.println("|____|______________________|_____|");
        System.out.println("************************************");
    }

}
