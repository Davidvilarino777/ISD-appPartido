package es.udc.ws.app.model.CompraEntrada;

import java.sql.*;

public class Jdbc3CcSqlCompraEntradaDao extends AbstractSqlCompraEntradaDao{

    @Override
    public CompraEntrada create(Connection connection, CompraEntrada compra) {
        /* Create "queryString". */
        String queryString = "INSERT INTO CompraEntrada"
                + " (IdPartido, Email, NumTarjetaBancaria, Unidades,"
                + " FechaCompra, recogida) VALUES (?, ?, ?, ?, ?, ?)";


        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, compra.getIdPartido());
            preparedStatement.setString(i++, compra.getEmail());
            preparedStatement.setString(i++, compra.getNumTarjetaBancaria());
            preparedStatement.setInt(i++, compra.getUnidades());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(compra.getFechaCompra()));
            preparedStatement.setBoolean(i++,compra.getrecogida());

            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long IdCompra = resultSet.getLong(1);

            /* Return sale. */
            return new CompraEntrada(IdCompra, compra.getIdPartido(), compra.getEmail()
                    ,compra.getNumTarjetaBancaria(),compra.getUnidades(),compra.getFechaCompra(),
                    compra.getrecogida());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
