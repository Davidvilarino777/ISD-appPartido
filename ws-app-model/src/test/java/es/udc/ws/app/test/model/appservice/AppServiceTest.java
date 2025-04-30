package es.udc.ws.app.test.model.appservice;

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
import es.udc.ws.app.model.PartidoService.PartidoService;
import es.udc.ws.app.model.PartidoService.PartidoServiceFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static es.udc.ws.app.model.util.ModelConstants.APP_DATA_SOURCE;
import static org.junit.jupiter.api.Assertions.*;

public class AppServiceTest {

    private final long NON_EXISTENT_ID_PARTIDO = -1;
    private final String USER_EMAIL = "isd@gmail.com";

    private final String USER_EMAIL2 = "isd2@gmail.com";
    private final String INVALID_USER_EMAIL = "isdgmail.com";

    private final String VALID_CREDIT_CARD_NUMBER = "1234567890123456";
    private final String INVALID_CREDIT_CARD_NUMBER = "";

    int unidades = 4;


    private static PartidoService partidoService = null;
    private static SqlPartidoDao partidodao = null;
    private static DataSource dataSource;

    private static SqlCompraEntradaDao compraentradadao = null;


    private Partido getValidPartido(LocalDateTime fecha){
        return new Partido("Outes Fc",(float) 9.99,1000,
                0,fecha);
    }

    @BeforeAll
    public static void init() {

        /*
         * Create a simple data source and add it to "DataSourceLocator" (this
         * is needed to test "es.udc.ws.movies.model.movieservice.MovieService"
         */
        dataSource = new SimpleDataSource();

        /* Add "dataSource" to "DataSourceLocator". */
        DataSourceLocator.addDataSource(APP_DATA_SOURCE, dataSource);

        partidoService = PartidoServiceFactory.getService();

        partidodao = SqlPartidoDaoFactory.getDao();

        compraentradadao = SqlCompraEntradaDaoFactory.getDao();

    }

    private Partido createPartido(Partido partido) {

        Partido addedPartido ;
        try {
            addedPartido = partidoService.addPartido(partido);
        } catch (InputValidationException e) {
            throw new RuntimeException(e);
        }
        return addedPartido;
    }

    private void BorrarPartido(Long IdPartido) throws InstanceNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                partidodao.remove(connection,IdPartido);

                connection.commit();
            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (SQLException e){
                connection.rollback();
                throw  new RuntimeException(e);
            } catch (RuntimeException | Error e){
                connection.rollback();
                throw e;
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void borrarCompra(Long IdCompra) throws InstanceNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                compraentradadao.remove(connection,IdCompra);

                connection.commit();
            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (SQLException e){
                connection.rollback();
                throw  new RuntimeException(e);
            } catch (RuntimeException | Error e){
                connection.rollback();
                throw e;
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public CompraEntrada getCompra(Long IdCompra) throws InstanceNotFoundException {
        try (Connection connection = dataSource.getConnection()){
            return compraentradadao.findId(connection,IdCompra);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Test
    public void TestCreatePartido(){

        String[] NomEquipoVis = new String[] {"FC Barcelona", "Sevilla FC",
                "Outes FC"};
        Float[] Precio = new Float[] {(float) 29.99,(float) 29.99,(float) 29.99};
        int[] NumEntradasDisp = new int[] {10000,10000,10000};
        int[] NumEntradasVendidas = new int[] {0,0,0};
        LocalDateTime[] FechaCelebracion = new LocalDateTime[] {
                LocalDateTime.now().withNano(0).plusDays(3),
                LocalDateTime.now().withNano(0).plusDays(5),
                LocalDateTime.now().withNano(0).plusDays(10)
        };

        Partido[] partidos = {null, null, null};

        try{
            int i;
            for(i=0;i<3;i++){
                partidos[i] = new Partido(NomEquipoVis[i], Precio[i], NumEntradasDisp[i],
                        NumEntradasVendidas[i],FechaCelebracion[i] );
                partidos[i]=createPartido(partidos[i]);
            }
        }
        finally{
            int i;
            for(i=0;i<3;i++){
                if(partidos[i]!=null) {
                    try {
                        BorrarPartido(partidos[i].getIdPartido());
                    } catch (InstanceNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }


    }
    @Test
    public void testAddInvalidPartido() {
        // Check Partido NomEquipoVis not null
        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidPartido(LocalDateTime.now().withNano(0));
            partido.setNomEquipoVis(null);
            partidoService.addPartido(partido);
        });

        // Check Partido NomEquipoVis.length>0
        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidPartido(LocalDateTime.now().withNano(0));
            partido.setNomEquipoVis("");
            partidoService.addPartido(partido);
        });

        // Check Partido NumMaxEntradasDisp<NUM_MAX_ENTRADAS
        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidPartido(LocalDateTime.now().withNano(0));
            partido.setNumMaxEntradasDisp(999999);
            partidoService.addPartido(partido);
        });

        // Check Partido NumMaxEntradasDisp>=0
        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidPartido(LocalDateTime.now().withNano(0));
            partido.setNumMaxEntradasDisp(-1);
            partidoService.addPartido(partido);
        });

        // Check Partido NumEntradasVendidas<NUM_MAX_ENTRADAS
        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidPartido(LocalDateTime.now().withNano(0));
            partido.setNumEntradasVendidas(999999);
            partidoService.addPartido(partido);
        });

        // Check Partido NumEntradasVendidas>=0
        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidPartido(LocalDateTime.now().withNano(0));
            partido.setNumEntradasVendidas(-1);
            partidoService.addPartido(partido);
        });

    }

    @Test
    public void testAddPartidoyFindPorFecha() throws InstanceNotFoundException{
        Partido partido1 = createPartido(getValidPartido(LocalDateTime.now().withNano(0).plusDays(2)));
        Partido partido2 = createPartido(getValidPartido(LocalDateTime.now().withNano(0).plusDays(4)));
        Partido partido3 = createPartido(getValidPartido(LocalDateTime.now().withNano(0).plusDays(6)));
        Long Idpartido1= partido1.getIdPartido();
        Long Idpartido2= partido2.getIdPartido();
        Long Idpartido3= partido3.getIdPartido();

        try {
            List<Partido> partidos=partidoService.getPartidosPorFecha(LocalDateTime.now().withNano(0).plusDays(2),LocalDateTime.now().withNano(0).plusDays(4));

            assertEquals(Idpartido1,partidos.get(0).getIdPartido());
            assertEquals(partido1.getNomEquipoVis(),partidos.get(0).getNomEquipoVis());
            assertEquals(partido1.getPrecio(),partidos.get(0).getPrecio());
            assertEquals(partido1.getNumMaxEntradasDisp(),partidos.get(0).getNumMaxEntradasDisp());
            assertEquals(partido1.getNumEntradasVendidas(),partidos.get(0).getNumEntradasVendidas());
            assertEquals(partido1.getFechaCelebracion(),partidos.get(0).getFechaCelebracion());

            assertEquals(Idpartido2,partidos.get(1).getIdPartido());
            assertEquals(partido2.getNomEquipoVis(),partidos.get(1).getNomEquipoVis());
            assertEquals(partido2.getPrecio(),partidos.get(1).getPrecio());
            assertEquals(partido2.getNumMaxEntradasDisp(),partidos.get(1).getNumMaxEntradasDisp());
            assertEquals(partido2.getNumEntradasVendidas(),partidos.get(1).getNumEntradasVendidas());
            assertEquals(partido2.getFechaCelebracion(),partidos.get(1).getFechaCelebracion());


            assertEquals(2,partidos.size());


        } catch (InputValidationException e) {
            throw new RuntimeException(e);
        } finally {
            BorrarPartido(Idpartido1);
            BorrarPartido(Idpartido2);
            BorrarPartido(Idpartido3);
        }
    }
    @Test
    public void testAddPartidoyFindPartido() throws InputValidationException, InstanceNotFoundException {

        Partido partido = getValidPartido(LocalDateTime.now().withNano(0));

        Partido addedPartido = null;

        try {
            LocalDateTime antesCreacion = LocalDateTime.now().withNano(0);

            addedPartido = partidoService.addPartido(partido);

            LocalDateTime despoCreacion = LocalDateTime.now().withNano(0);

            Partido partidoencontrado = partidoService.getPartido(addedPartido.getIdPartido());

            assertEquals(addedPartido, partidoencontrado);
            assertEquals(partidoencontrado.getNomEquipoVis(),partido.getNomEquipoVis());
            assertEquals(partidoencontrado.getPrecio(),partido.getPrecio());
            assertEquals(partidoencontrado.getNumMaxEntradasDisp(),partido.getNumMaxEntradasDisp());
            assertEquals(partidoencontrado.getNumEntradasVendidas(),partido.getNumEntradasVendidas());
            assertTrue((partidoencontrado.getFechaCreacion().compareTo(antesCreacion) >= 0)
                    && (partidoencontrado.getFechaCreacion().compareTo(despoCreacion) <= 0));
            assertTrue(partidoencontrado.getFechaCelebracion().compareTo(antesCreacion) >= 0);
        } finally {
            if (addedPartido != null){
                BorrarPartido(addedPartido.getIdPartido());
            }
        }
    }

    @Test
    public void testFindNonExistentPartido() {
        assertThrows(InstanceNotFoundException.class, () -> partidoService.getPartido(NON_EXISTENT_ID_PARTIDO));
    }



    @Test
    public void TestCompraEntradasAndFindcompra() throws InputValidationException,
            InstanceNotFoundException,NotEnoughUnitsException, TicketsNotOnSaleException {
        Partido partido = createPartido(getValidPartido(LocalDateTime.now().withNano(0).plusDays(3))); //Ponemos .plusDays(3) para que la fecha de celebración sea 3 días después de la que creamos ahora mismo cuando compramos entradas
        Partido partidoUpdated = null;
        Long idCompra = null;

        try {

            // Buy movie
            LocalDateTime antesCompra = LocalDateTime.now().withNano(0);

            idCompra = partidoService.CompraEntradas(partido.getIdPartido(), USER_EMAIL,
                    VALID_CREDIT_CARD_NUMBER, unidades);

            LocalDateTime despuesCompra = LocalDateTime.now().withNano(0);

            partidoUpdated = partidoService.getPartido(partido.getIdPartido());

            // Find sale
            CompraEntrada foundcompra = getCompra(idCompra);

            // Check sale
            assertEquals(idCompra,foundcompra.getIdCompra());
            assertEquals(VALID_CREDIT_CARD_NUMBER, foundcompra.getNumTarjetaBancaria());
            assertEquals(USER_EMAIL, foundcompra.getEmail());
            assertEquals(unidades, foundcompra.getUnidades());
            assertEquals(partidoUpdated.getNumEntradasVendidas() -
                    partido.getNumEntradasVendidas(),unidades);
            assertTrue((foundcompra.getFechaCompra().compareTo(antesCompra) >= 0)
                    && (foundcompra.getFechaCompra().compareTo(despuesCompra) <= 0));
            assertTrue(foundcompra.getFechaCompra().
                    compareTo(partido.getFechaCelebracion()) <= 0);

        } finally {
            // Clear database: remove sale (if created) and movie
            if (idCompra != null) {
                borrarCompra(idCompra);
            }
            BorrarPartido(partido.getIdPartido());
        }
    }

    @Test
    public void TestInvalidCompra() throws InputValidationException, InstanceNotFoundException,
            TicketsNotOnSaleException, NotEnoughUnitsException {

        Partido partido = getValidPartido(LocalDateTime.now().withNano(0));
        Partido addedPartido = partidoService.addPartido(partido);

        try{
            //Checkea si el Numero de Tarjeta Bancaria es válida
            assertThrows(InputValidationException.class, () -> {
                partidoService.CompraEntradas(addedPartido.getIdPartido(),
                        USER_EMAIL,INVALID_CREDIT_CARD_NUMBER,unidades);
            });

            //Checkea si la direccion de email es válida
            assertThrows(InputValidationException.class, () -> {
                partidoService.CompraEntradas(addedPartido.getIdPartido(),
                        INVALID_USER_EMAIL,VALID_CREDIT_CARD_NUMBER,unidades);
            });

            //Checkea si las unidades son mayores que 0
            assertThrows(InputValidationException.class, () -> {
                partidoService.CompraEntradas(addedPartido.getIdPartido(),
                        USER_EMAIL,VALID_CREDIT_CARD_NUMBER,-1);
            });

        }

        finally {
            // Clear database: remove sale (if created) and movie
            if (addedPartido != null) {
                BorrarPartido(addedPartido.getIdPartido());
            }
        }

    }

    @Test
    public void CompraNonExistentPartido() throws InputValidationException, InstanceNotFoundException {

        Partido partido = getValidPartido(LocalDateTime.now().withNano(0));
        Partido addedPartido = partidoService.addPartido(partido);

        try{
            //Checkea si existe el id del partido
            assertThrows(InstanceNotFoundException.class, () -> {
                partidoService.CompraEntradas(NON_EXISTENT_ID_PARTIDO,
                        USER_EMAIL,VALID_CREDIT_CARD_NUMBER,unidades);
            });
        }
        finally {
            // Clear database: remove sale (if created) and movie
            if (addedPartido != null) {
                BorrarPartido(addedPartido.getIdPartido());
            }
        }
    }

    @Test
    public void TestTicketsNotOnSaleException() throws InputValidationException, InstanceNotFoundException {

        Partido partido = getValidPartido(LocalDateTime.now().withNano(0));
        Partido addedPartido = partidoService.addPartido(partido);

        try{
            //Checkea TicketsNotOnSaleException
            assertThrows(TicketsNotOnSaleException.class, () -> {
                partidoService.CompraEntradas(addedPartido.getIdPartido(),
                        USER_EMAIL,VALID_CREDIT_CARD_NUMBER,unidades);
            });
        }
        finally {
            // Clear database: remove sale (if created) and movie
            if (addedPartido != null) {
                BorrarPartido(addedPartido.getIdPartido());
            }
        }
    }

    @Test
    public void TestNotEnoughUnitsException() throws InputValidationException, InstanceNotFoundException {

        Partido partido = getValidPartido(LocalDateTime.now().withNano(0));
        Partido addedPartido = partidoService.addPartido(partido);

        try{
            //Checkea NotEnoughUnitsException
            assertThrows(NotEnoughUnitsException.class, () -> {
                partidoService.CompraEntradas(addedPartido.getIdPartido(),
                        USER_EMAIL,VALID_CREDIT_CARD_NUMBER,1001);
            });
        }
        finally {
            // Clear database: remove sale (if created) and movie
            if (addedPartido != null) {
                BorrarPartido(addedPartido.getIdPartido());
            }
        }
    }


    @Test
    public void testValidComprasUsuario() throws InputValidationException, InstanceNotFoundException,NotEnoughUnitsException,TicketsNotOnSaleException {

        Partido partido1 = createPartido(getValidPartido(LocalDateTime.now().withNano(0).plusDays(3)));
        Partido partido2 = createPartido(getValidPartido(LocalDateTime.now().withNano(0).plusDays(3)));
        Partido partido3 = createPartido(getValidPartido(LocalDateTime.now().withNano(0).plusDays(3)));

        Long idcompra1 = null;
        Long idcompra2 = null;
        Long idcompra3 = null;

        CompraEntrada compraEncontrada1,compraEncontrada2;

        try {
            idcompra1 = partidoService.CompraEntradas(partido1.getIdPartido(),USER_EMAIL,VALID_CREDIT_CARD_NUMBER,2); //Creamos una compra para user_email
            idcompra2 = partidoService.CompraEntradas(partido2.getIdPartido(),USER_EMAIL,VALID_CREDIT_CARD_NUMBER,3); //Creamos una compra para user_email
            idcompra3 = partidoService.CompraEntradas(partido1.getIdPartido(),USER_EMAIL2,VALID_CREDIT_CARD_NUMBER,4); //Creamos una compra para user_email2, esta no podrá ser añadida a la lista de user_email

            List<CompraEntrada> listacomprasbuscadas = partidoService.ComprasUsuario(USER_EMAIL); //Buscamos las compras de user_email

            compraEncontrada1 = getCompra(idcompra1); //Sacamos los datos de las compras
            compraEncontrada2 = getCompra(idcompra2);


            assertEquals(compraEncontrada1.getIdCompra(), listacomprasbuscadas.get(0).getIdCompra()); //Vamos comparando los datos de la primera compra con los que hay en la lista para ver si son correctamente almacenados
            assertEquals(compraEncontrada1.getIdPartido(), listacomprasbuscadas.get(0).getIdPartido());
            assertEquals(compraEncontrada1.getEmail(), listacomprasbuscadas.get(0).getEmail());
            assertEquals(compraEncontrada1.getNumTarjetaBancaria(), listacomprasbuscadas.get(0).getNumTarjetaBancaria());
            assertEquals(compraEncontrada1.getUnidades(), listacomprasbuscadas.get(0).getUnidades());
            assertEquals(compraEncontrada1.getFechaCompra(), listacomprasbuscadas.get(0).getFechaCompra());
            assertEquals(compraEncontrada1.getrecogida(), listacomprasbuscadas.get(0).getrecogida());

            assertEquals(compraEncontrada2.getIdCompra(), listacomprasbuscadas.get(1).getIdCompra()); //Vamos comparando los datos de la segunda compra con los que hay en la lista para ver si son correctos, da igual que sean para dos partidos distintos, tiene que estar en la lista igualmente
            assertEquals(compraEncontrada2.getIdPartido(), listacomprasbuscadas.get(1).getIdPartido());
            assertEquals(compraEncontrada2.getEmail(), listacomprasbuscadas.get(1).getEmail());
            assertEquals(compraEncontrada2.getNumTarjetaBancaria(), listacomprasbuscadas.get(1).getNumTarjetaBancaria());
            assertEquals(compraEncontrada2.getUnidades(), listacomprasbuscadas.get(1).getUnidades());
            assertEquals(compraEncontrada2.getFechaCompra(), listacomprasbuscadas.get(1).getFechaCompra());
            assertEquals(compraEncontrada2.getrecogida(), listacomprasbuscadas.get(1).getrecogida());

            assertEquals(2,listacomprasbuscadas.size()); //Comprobamos que el tamaño de la lista sea 2, uno para la compra1 para el partido1, otra para la compra2 del partido 2
        }
        finally { //Borramos las compras y los partidos de la base de datos
            if(idcompra1 != null){
                borrarCompra(idcompra1);
            }
            if(idcompra2 != null){
                borrarCompra(idcompra2);
            }
            if(idcompra3 != null){
                borrarCompra(idcompra3);
            }
            BorrarPartido(partido1.getIdPartido());
            BorrarPartido(partido2.getIdPartido());
            BorrarPartido(partido3.getIdPartido());
        }

    }

    @Test
    public void testFindComprasUsuarioInvalidUser() {

        assertThrows(InputValidationException.class, () ->
                partidoService.ComprasUsuario(""));

        assertThrows(InputValidationException.class, () ->
                partidoService.ComprasUsuario("hola"));
    }

    @Test
    public void testFindComprasUsuarioNoUser() throws InstanceNotFoundException, InputValidationException, NotEnoughUnitsException,TicketsNotOnSaleException{

        Partido partido = createPartido(getValidPartido(LocalDateTime.now().withNano(0).plusDays(3)));
        Long compraEntrada = null;

        try {
            compraEntrada = partidoService.CompraEntradas(partido.getIdPartido(),USER_EMAIL,VALID_CREDIT_CARD_NUMBER,1);
            List<CompraEntrada> listacomprasusuariobuscada = partidoService.ComprasUsuario("manolito@gmail.com");
            assertEquals(0,listacomprasusuariobuscada.size());
        }
        finally {
            if (compraEntrada != null){
                borrarCompra(compraEntrada);
            }
            BorrarPartido(partido.getIdPartido());
        }
    }
    @Test
    public void testRecogerEntradas() throws InstanceNotFoundException, NonEqualCreditCardException, TicketsNotOnSaleException, InputValidationException {
        Partido partido = createPartido(getValidPartido(LocalDateTime.now().withNano(0).plusDays(3)));
        Long IdCompra = null;
        try {
            IdCompra = partidoService.CompraEntradas(partido.getIdPartido(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER, 3);
            CompraEntrada compra = getCompra(IdCompra);
            assertFalse(compra.getrecogida());
            partidoService.RecogerEntradas(compra.getIdCompra(), compra.getNumTarjetaBancaria());
            compra = getCompra(IdCompra);
            assertTrue(compra.getrecogida());


        } catch (NotEnoughUnitsException | TicketsAlreadyTakenException e) {
            throw new RuntimeException(e);
        } finally {
            if (IdCompra != null) {
                borrarCompra(IdCompra);
            }
            BorrarPartido(partido.getIdPartido());
        }


    }

    @Test
    public void NonEqualCreditCardExceptionRecogerEntrada() throws NotEnoughUnitsException, TicketsNotOnSaleException, InstanceNotFoundException, InputValidationException {
        Partido partido1 = createPartido(getValidPartido(LocalDateTime.now().withNano(0).plusDays(3)));
        Long id = partidoService.CompraEntradas(partido1.getIdPartido(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER, 5);

        try {
            assertThrows(NonEqualCreditCardException.class, () -> {
                partidoService.RecogerEntradas(id, "8888888888888888");
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            borrarCompra(id);
            BorrarPartido(partido1.getIdPartido());
        }
    }

    @Test
    public void InputvalidationExceptionRecogerEntrada() throws NotEnoughUnitsException, TicketsNotOnSaleException, InstanceNotFoundException, InputValidationException {
        Partido partido1 = createPartido(getValidPartido(LocalDateTime.now().withNano(0).plusDays(3)));
        Long id = partidoService.CompraEntradas(partido1.getIdPartido(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER, 5);

        try {
            assertThrows(InputValidationException.class, () -> {
                partidoService.RecogerEntradas(id, "non valid");

            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            borrarCompra(id);
            BorrarPartido(partido1.getIdPartido());
        }
    }

    @Test
    public void testTicketsAlreadyTaken() throws NotEnoughUnitsException, TicketsNotOnSaleException, InstanceNotFoundException, InputValidationException {
        Partido partido1 = createPartido(getValidPartido(LocalDateTime.now().withNano(0).plusDays(3)));
        Long id = partidoService.CompraEntradas(partido1.getIdPartido(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER, 5);

        try {
            assertThrows(TicketsAlreadyTakenException.class, () -> {
                partidoService.RecogerEntradas(id, VALID_CREDIT_CARD_NUMBER);
                partidoService.RecogerEntradas(id, VALID_CREDIT_CARD_NUMBER);

            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally{
            borrarCompra(id);
            BorrarPartido(partido1.getIdPartido());
        }
    }
}


