package es.udc.ws.app.model.CompraEntrada;

import es.udc.ws.app.model.Partido.Partido;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.exceptions.InputValidationException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlCompraEntradaDao implements SqlCompraEntradaDao{


    public List<CompraEntrada> ComprasUsuario(Connection connection, String Email) {
        String querystring = "SELECT IdCompra, IdPartido, Email, NumTarjetaBancaria, " +
                "Unidades, FechaCompra, recogida FROM CompraEntrada WHERE email= ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(querystring)){
            int i = 1;
            preparedStatement.setString(i,Email);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<CompraEntrada> compraentradaslist = new ArrayList<>();
            CompraEntrada nuevacompra;

            while (resultSet.next()) {
                i = 1;
                Long idCompra = resultSet.getLong(i++);
                Long idPartido = resultSet.getLong(i++);
                String email = resultSet.getString(i++);
                String numTarjetaBancaria = resultSet.getString(i++);
                int unidades = resultSet.getInt(i++);
                LocalDateTime fechacompra = resultSet.getTimestamp(i++).toLocalDateTime();
                boolean recogida = resultSet.getBoolean(i);

                nuevacompra = new CompraEntrada(idCompra,idPartido,email,numTarjetaBancaria,unidades,fechacompra,recogida);
                compraentradaslist.add(nuevacompra);
            }
            return compraentradaslist;

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompraEntrada findId(Connection connection, Long IdCompra) throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "SELECT IdPartido, Email, NumTarjetaBancaria,"
                + " Unidades, FechaCompra, recogida FROM CompraEntrada WHERE IdCompra = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, IdCompra.longValue());

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(IdCompra,
                        CompraEntrada.class.getName());
            }

            /* Get results. */
            i = 1;
            Long IdPartido = resultSet.getLong(i++);
            String Email = resultSet.getString(i++);
            String NumTarjetaBancaria = resultSet.getString(i++);
            int Unidades = resultSet.getInt(i++);
            Timestamp FechaCompraAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime FechaCompra = FechaCompraAsTimestamp.toLocalDateTime();
            boolean recogida = resultSet.getBoolean(i++);

            /* Return sale. */
            return new CompraEntrada(IdCompra, IdPartido, Email,NumTarjetaBancaria,
                    Unidades, FechaCompra, recogida);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(Connection connection, CompraEntrada compra) throws InstanceNotFoundException {
        String queryString = "UPDATE CompraEntrada"
                + " SET IdPartido = ?, Email = ?,"
                + " NumTarjetaBancaria = ?, Unidades = ?,"
                + " FechaCompra = ?, recogida = ? WHERE IdCompra = ?";
        try(PreparedStatement preparedStatement= connection.prepareStatement(queryString)) {
            int i=1;
            preparedStatement.setLong(i++, compra.getIdPartido());
            preparedStatement.setString(i++, compra.getEmail());
            preparedStatement.setString(i++, compra.getNumTarjetaBancaria());
            preparedStatement.setInt(i++, compra.getUnidades());
            Timestamp date = compra.getFechaCompra() != null ? Timestamp.valueOf(compra.getFechaCompra()) : null;
            preparedStatement.setTimestamp(i++, date);
            preparedStatement.setBoolean(i++, compra.getrecogida());
            preparedStatement.setLong(i++,compra.getIdCompra());

            int updatedRows=preparedStatement.executeUpdate();
            if (updatedRows == 0) {
                throw new InstanceNotFoundException(compra.getIdCompra(),
                        CompraEntrada.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long IdCompra) throws InstanceNotFoundException {
        String queryString = "DELETE FROM CompraEntrada WHERE" + " IdCompra = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
            int i = 1;
            preparedStatement.setLong(i++, IdCompra);

            int filasborradas = preparedStatement.executeUpdate();

            if (filasborradas == 0){
                throw  new InstanceNotFoundException(IdCompra,CompraEntrada.class.getName());
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}