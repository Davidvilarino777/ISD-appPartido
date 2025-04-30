package es.udc.ws.app.model.Partido;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlPartidoDao implements SqlPartidoDao {

    @Override
    public List<Partido> findbetweenDates(Connection connection, LocalDateTime fechacelebracion1, LocalDateTime fechacelebracion2) {
        String queryString = "SELECT IdPartido, NomEquipoVis,"
                + " Precio, NumMaxEntradasDisp, NumEntradasVendidas, FechaCelebracion, FechaCreacion  FROM Partido"
                + " WHERE FechaCelebracion >= ? AND FechaCelebracion <= ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            int i = 1;
            Timestamp fechacelebraciontimestamp1 = Timestamp.valueOf(fechacelebracion1);
            Timestamp fechacelebraciontimestamp2 = Timestamp.valueOf(fechacelebracion2);
            preparedStatement.setTimestamp(i++, fechacelebraciontimestamp1);
            preparedStatement.setTimestamp(i++, fechacelebraciontimestamp2);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Partido> partidos = new ArrayList<>();
            Partido PartidoEncontrado;
            /* Get results. */
            while (resultSet.next()) {
                i = 1;
                Long Id = resultSet.getLong(i++);
                String NomEquipoVis = resultSet.getString(i++);
                float Precio = resultSet.getFloat(i++);
                int NumMaxEntradasDisp = resultSet.getInt(i++);
                int NumEntradasVendidas = resultSet.getInt(i++);
                LocalDateTime FechaCelebracion = resultSet.getTimestamp(i++).toLocalDateTime();
                LocalDateTime FechaCreacion = resultSet.getTimestamp(i++).toLocalDateTime();
                PartidoEncontrado=new Partido(Id, NomEquipoVis, Precio, NumMaxEntradasDisp, NumEntradasVendidas, FechaCelebracion, FechaCreacion);
                partidos.add(PartidoEncontrado);
            }
            /* Return movies. */
            return partidos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public Partido findId(Connection connection, Long IdPartido) throws InstanceNotFoundException {
        String queryString = "SELECT NomEquipoVis, Precio, NumMaxEntradasDisp," +
                "NumEntradasVendidas, FechaCelebracion, FechaCreacion FROM Partido WHERE IdPartido = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setLong(i++, IdPartido.longValue());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(IdPartido, Partido.class.getName());
            }

            i = 1;
            String NomEquipoVis = resultSet.getString(i++);
            float Precio = resultSet.getFloat(i++);
            int NumMaxEntradasDisp = resultSet.getInt(i++);
            int NumEntradasVendidas = resultSet.getInt(i++);
            Timestamp FechaCelebracionStamp = resultSet.getTimestamp(i++);
            LocalDateTime FechaCelebracion = FechaCelebracionStamp.toLocalDateTime();
            Timestamp FechaCreacionStamp = resultSet.getTimestamp(i++);
            LocalDateTime FechaCreacion = FechaCreacionStamp.toLocalDateTime();

            return new Partido(IdPartido, NomEquipoVis, Precio, NumMaxEntradasDisp, NumEntradasVendidas, FechaCelebracion, FechaCreacion);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Connection connection, Partido partido) throws InstanceNotFoundException {
        /* Create "queryString". */
        String queryString = "UPDATE Partido"
                + " SET NomEquipoVis = ?, Precio = ?, NumMaxEntradasDisp = ?, "
                + "NumEntradasVendidas = ?, FechaCelebracion = ? WHERE IdPartido = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, partido.getNomEquipoVis());
            preparedStatement.setFloat(i++, partido.getPrecio());
            preparedStatement.setInt(i++, partido.getNumMaxEntradasDisp());
            preparedStatement.setInt(i++, partido.getNumEntradasVendidas());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(partido.getFechaCelebracion()));
            preparedStatement.setLong(i++, partido.getIdPartido());

            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(partido.getIdPartido(),
                        Partido.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long IdPartido) throws InstanceNotFoundException {
        String queryString = "DELETE FROM Partido WHERE" + " IdPartido = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            int i = 1;
            preparedStatement.setLong(i++, IdPartido);

            int filasborradas = preparedStatement.executeUpdate();

            if (filasborradas == 0) {
                throw new InstanceNotFoundException(IdPartido, Partido.class.getName());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}