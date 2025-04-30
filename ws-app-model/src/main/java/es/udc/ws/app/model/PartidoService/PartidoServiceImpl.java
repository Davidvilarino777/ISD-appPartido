package es.udc.ws.app.model.PartidoService;

import es.udc.ws.app.model.CompraEntrada.CompraEntrada;
import es.udc.ws.app.model.CompraEntrada.SqlCompraEntradaDao;
import es.udc.ws.app.model.CompraEntrada.SqlCompraEntradaDaoFactory;
import es.udc.ws.app.model.Partido.Partido;
import es.udc.ws.app.model.Partido.SqlPartidoDao;
import es.udc.ws.app.model.Partido.SqlPartidoDaoFactory;
import es.udc.ws.app.model.PartidoService.Exceptions.NonEqualCreditCardException;
import es.udc.ws.app.model.PartidoService.Exceptions.NotEnoughUnitsException;
import es.udc.ws.app.model.PartidoService.Exceptions.TicketsAlreadyTakenException;
import es.udc.ws.app.model.PartidoService.Exceptions.TicketsNotOnSaleException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static es.udc.ws.app.model.util.ModelConstants.*;

public class PartidoServiceImpl implements PartidoService{

    private final DataSource dataSource;
    private SqlPartidoDao partidoDao = null;
    private SqlCompraEntradaDao compraEntradaDAO = null;

    public PartidoServiceImpl() {
        dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        partidoDao = SqlPartidoDaoFactory.getDao();
        compraEntradaDAO = SqlCompraEntradaDaoFactory.getDao();
    }

    private void validatePartido(Partido partido) throws InputValidationException {

        PropertyValidator.validateMandatoryString("NomEquipoVis",partido.getNomEquipoVis());
        PropertyValidator.validateDouble("Precio",partido.getPrecio(),0,MAX_PRICE);
        PropertyValidator.validateDouble("NumMaxEntradasDisp", partido.getNumMaxEntradasDisp(), 1, MAX_NUM_ENTRADAS);
        PropertyValidator.validateDouble("NumEntradasVendidas", partido.getNumEntradasVendidas(), 0, MAX_NUM_ENTRADAS);
        if(partido.getFechaCelebracion().isBefore(partido.getFechaCreacion())) {
            throw new InputValidationException("La fecha de celebración " + partido.getFechaCelebracion() +
                    " es anterior a la fecha de creación del partido: " + partido.getFechaCreacion());
        };
    }

    private void validateEmail(String email) throws InputValidationException, NullPointerException{
        if(!(email.endsWith("@gmail.com") || email.endsWith("@udc.es"))){
            throw new InputValidationException("El correo " + email + "no es válido");
        }
    }


    @Override
    public Partido addPartido(Partido partido) throws InputValidationException {

        partido.setFechaCreacion(LocalDateTime.now());
        validatePartido(partido);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                Partido partidoCreado = partidoDao.create(connection, partido);

                /* Commit. */
                connection.commit();

                return partidoCreado;

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public Partido getPartido(Long IdPartido) throws InstanceNotFoundException {
        try (Connection connection = dataSource.getConnection()){
            return partidoDao.findId(connection,IdPartido);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Partido> getPartidosPorFecha(LocalDateTime fecha1, LocalDateTime fecha2) throws InputValidationException {
        try(Connection connection= dataSource.getConnection()){
            if(fecha1.compareTo(fecha2)>0){
                throw new InputValidationException("La segunda fecha debe ser posterior a la primera\n");
            }
            return partidoDao.findbetweenDates(connection,fecha1, fecha2);
        }catch (SQLException | InputValidationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long CompraEntradas(Long IdPartido, String Email, String NumTarjetaBancaria
            , int unidades) throws InstanceNotFoundException, InputValidationException,
            NotEnoughUnitsException, TicketsNotOnSaleException {
        LocalDateTime FechaCompra = LocalDateTime.now();
        PropertyValidator.validateCreditCard(NumTarjetaBancaria);
        PropertyValidator.validateDouble("Unidades",unidades,1,1001);
        validateEmail(Email);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Partido partido = partidoDao.findId(connection, IdPartido); /*lanza INFE en caso de no encontrarse*/

                if(partido.getNumMaxEntradasDisp()-partido.getNumEntradasVendidas()<unidades){
                    throw new NotEnoughUnitsException(partido.getIdPartido());
                }

                if(partido.getFechaCelebracion().compareTo(FechaCompra)<=0){
                    throw new TicketsNotOnSaleException(partido.getIdPartido());
                }

                /* Creamos compra */
                CompraEntrada compra = compraEntradaDAO.create(connection,new CompraEntrada(IdPartido, Email,
                        NumTarjetaBancaria, unidades, FechaCompra));

                //Actualizamos información del partido
                partido.setNumEntradasVendidas(partido.getNumEntradasVendidas()+unidades);
                partidoDao.update(connection,partido);

                /* Commit. */
                connection.commit();

                return compra.getIdCompra();

            } catch (InstanceNotFoundException | TicketsNotOnSaleException | NotEnoughUnitsException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CompraEntrada> ComprasUsuario(String Email) throws InputValidationException {
        validateEmail(Email); //Validamos el email para comprobar que no es nulo y que sigue el formato adecuado

        try (Connection connection = dataSource.getConnection()){

            return compraEntradaDAO.ComprasUsuario(connection,Email);

        } catch (SQLException e){
            throw new RuntimeException(e);
        }


    }

    @Override
    public void RecogerEntradas(Long IdCompra, String CreditCardNumber) throws InstanceNotFoundException, NonEqualCreditCardException, TicketsAlreadyTakenException, InputValidationException {
        PropertyValidator.validateCreditCard(CreditCardNumber);
        try (Connection connection=dataSource.getConnection()){
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                CompraEntrada compra = compraEntradaDAO.findId(connection,IdCompra);

                if(CreditCardNumber.compareTo(compra.getNumTarjetaBancaria())==0) {
                    if (!compra.getrecogida()) {
                        compra.setRecogida(true);
                        compraEntradaDAO.update(connection, compra);
                        connection.commit();
                    }
                    else
                        throw new TicketsAlreadyTakenException(IdCompra);
                }
                else
                    throw new NonEqualCreditCardException(IdCompra, CreditCardNumber);

            }
            catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            }catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }catch (RuntimeException |Error e){
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}