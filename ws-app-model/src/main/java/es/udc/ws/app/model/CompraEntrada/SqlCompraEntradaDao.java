package es.udc.ws.app.model.CompraEntrada;

import es.udc.ws.app.model.Partido.Partido;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.util.List;

public interface SqlCompraEntradaDao {
    public CompraEntrada create(Connection connection, CompraEntrada compraEntrada);

    public List<CompraEntrada> ComprasUsuario(Connection connection, String Email);

    public CompraEntrada findId(Connection connection, Long IdCompra)
            throws InstanceNotFoundException;

    public void update(Connection connection, CompraEntrada compra)
            throws InstanceNotFoundException;

    public void remove(Connection connection, Long IdCompra)
            throws InstanceNotFoundException;
}