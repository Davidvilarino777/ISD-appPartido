package es.udc.ws.app.model.Partido;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public interface SqlPartidoDao {
    public Partido create(Connection connection, Partido partido);
    public List<Partido> findbetweenDates(Connection connection, LocalDateTime fechacelebración1, LocalDateTime fechacelebración2) throws InputValidationException;
    public Partido findId(Connection connection, Long IdPartido)
            throws InstanceNotFoundException;

    public void update(Connection connection, Partido partido)
            throws InstanceNotFoundException;

    public void remove(Connection connection, Long IdPartido)
            throws InstanceNotFoundException;
}

