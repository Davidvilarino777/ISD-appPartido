package es.udc.ws.app.model.Partido;

import java.sql.*;

import java.sql.*;

public class Jdbc3CcSqlPartidoDao extends AbstractSqlPartidoDao {

    @Override
    public Partido create(Connection connection, Partido partido) {
        /* Create "queryString". */
        String queryString = "INSERT INTO Partido"
                + " (NomEquipoVis, Precio, NumMaxEntradasDisp, NumEntradasVendidas," +
                " FechaCelebracion, FechaCreacion)"
                + " VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, partido.getNomEquipoVis());
            preparedStatement.setDouble(i++, partido.getPrecio());
            preparedStatement.setInt(i++, partido.getNumMaxEntradasDisp());
            preparedStatement.setFloat(i++, partido.getNumEntradasVendidas());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(partido.getFechaCelebracion()));
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(partido.getFechaCreacion()));

            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) { //no se creó la clave del partido
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long partidoId = resultSet.getLong(1);

            /* Return Partido. */
            return new Partido(partidoId, partido.getNomEquipoVis(), partido.getPrecio(),
                    partido.getNumMaxEntradasDisp(), partido.getNumEntradasVendidas(),
                    partido.getFechaCelebracion(), partido.getFechaCreacion());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}