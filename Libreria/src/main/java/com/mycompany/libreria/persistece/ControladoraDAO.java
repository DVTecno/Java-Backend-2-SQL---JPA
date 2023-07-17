package com.mycompany.libreria.persistece;

import com.mycompany.libreria.entity.Autor;
import com.mycompany.libreria.entity.Cliente;
import com.mycompany.libreria.entity.Editorial;
import com.mycompany.libreria.entity.Libro;
import com.mycompany.libreria.entity.Prestamo;
import com.mycompany.libreria.persistece.exceptions.NonexistentEntityException;
import java.util.List;

public class ControladoraDAO {

    private AutorJpaController daoAutor;
    private ClienteJpaController daoCliente;
    private EditorialJpaController daoEditorial;
    private LibroJpaController daoLibro;
    private PrestamoJpaController daoPrestamo;

    public ControladoraDAO() {
        this.daoAutor = new AutorJpaController();
        this.daoCliente = new ClienteJpaController();
        this.daoEditorial = new EditorialJpaController();
        this.daoLibro = new LibroJpaController();
        this.daoPrestamo = new PrestamoJpaController();
    }
    //************ Luego de instanciar se secciona por clase 1ra Prestamos ************
    public void eliminarPrestamos(Integer id) throws NonexistentEntityException {
        daoPrestamo.destroy(id);
    }

    public void modificarPrestamos(Prestamo prestamo) throws Exception {
        daoPrestamo.edit(prestamo);
    }

    public void crearPrestamos(Prestamo prestamo) {
        daoPrestamo.create(prestamo);
    }

    public List<Prestamo> consultarPrestamos() {
        return daoPrestamo.findPrestamoEntities();
    }

    public List<Prestamo> consultaPrestamosPorLote(int loteSize, int valorInicial) throws Exception {
        return daoPrestamo.findPrestamoEntities(loteSize, valorInicial);
    }

    public List<Prestamo> consultarPrestamosPorCliente(Cliente cliente) {
        return daoPrestamo.buscarPrestamosPorCliente(cliente);
    }

    public Prestamo BuscarprestamoPorIsbn(Long isbn) {
        return daoPrestamo.buscarPrestamoPorLibro(isbn);
    }
    // ********************** Segunda seccion Libros ************************************
    public void eliminarLibro(Long id) throws NonexistentEntityException {
        daoLibro.destroy(id);
    }

    public void modificarLibro(Libro libro) throws Exception {
        daoLibro.edit(libro);
    }

    public void crearLibro(Libro libro) {
        daoLibro.create(libro);
    }

    public List<Libro> consultarLibro() {
        return daoLibro.findLibroEntities();
    }

    public List<Libro> consultarLibroPorLote(int loteSize, int valorInicial) throws Exception {
        return daoLibro.findLibroEntities(loteSize, valorInicial);
    }

    public List<Libro> consultaLibroPorNombreEditorial(String nombre) {
        return daoLibro.buscarLibrosPorNombreEditorial(nombre);
    }

    public Libro buscarLibroPorId(Long id) {
        return daoLibro.findLibro(id);
    }

    public Libro consultarLibroPorTitulo(String titulo) {
        return daoLibro.buscarLibroPorTitulo(titulo);
    }

    public List<Libro> consultaLibroPorNombreAutor(String nombre) {
        return daoLibro.buscarLibrosPorNombreAutor(nombre);
    }
    // ************************** 3ra Seccion Editorial *********************************
    public void eliminarEditorial(Integer id) throws NonexistentEntityException {
        daoEditorial.destroy(id);
    }

    public void modificarEditorial(Editorial editorial) throws Exception {
        daoEditorial.edit(editorial);
    }

    public void crearEditorial(Editorial editorial) {
        daoEditorial.create(editorial);
    }

    public List<Editorial> consultarEditorial() {
        return daoEditorial.findEditorialEntities();
    }

    public List<Editorial> consultarAutoresPorLote(int loteSize, int valorInicial) throws Exception {
        return daoEditorial.findEditorialEntities(loteSize, valorInicial);
    }

    public List<Editorial> consultaEditorialPorNombre(String nombre) {
        return daoEditorial.findEditorialPorNombre(nombre);
    }
    // **************************** 4ta Seccion Cliente **********************************
    public void eliminarClientes(Integer id) throws NonexistentEntityException {
        daoCliente.destroy(id);
    }

    public void modificarClientes(Cliente Cliente) throws Exception {
        daoCliente.edit(Cliente);
    }

    public Cliente crearClientes(Cliente cliente) {
        try {

            List<Cliente> clientes = consultarClientes();
            if (clientes.isEmpty()) {
                System.out.println("""
                                   ***********************************
                                   * Primer Cliente en base de datos *
                                   ***********************************
                                   """);
                daoCliente.create(cliente);
            } else {

                daoCliente.create(cliente);
                return cliente;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return cliente;

    }

    public Cliente verificarExistenciaCliente(Cliente cliente) throws Exception {
        List<Cliente> clientes = daoCliente.findClientePorDocumento(cliente.getDocumento());
        for (Cliente c : clientes) {
            if (c.getDocumento().equals(cliente.getDocumento())) {
                System.out.println("Cliente con el mismo documento");
                return c;
            }

        }
        return null;
    }

    public List<Cliente> consultarClientes() {
        return daoCliente.findClienteEntities();
    }

    public List<Cliente> consultaClientesPorLote(int loteSize, int valorInicial) throws Exception {
        return daoCliente.findClienteEntities(loteSize, valorInicial);
    }

    public List<Cliente> consultaClientesPorNombre(String nombre) throws Exception {
        return daoCliente.findClientePorNombre(nombre);
    }
    // ********************************* 5ta Seccion Autores ******************************
    public void eliminarAutor(Integer id) throws NonexistentEntityException {
        daoAutor.destroy(id);
    }

    public void modificarAutor(Autor autor) throws Exception {
        daoAutor.edit(autor);
    }

    public void crearAutor(Autor autor) {
        daoAutor.create(autor);
    }

    public List<Autor> consultarAutor() {
        return daoAutor.findAutorEntities();
    }

    public List<Autor> consultarAutoresPorLotes(int loteSize, int valorInicial) throws Exception {
        return daoAutor.findAutorEntities(loteSize, valorInicial);
    }

    public Autor consultaAutorPorNombre(String nombre) throws Exception {
        return daoAutor.findAutorPorNombre(nombre);
    }
}
