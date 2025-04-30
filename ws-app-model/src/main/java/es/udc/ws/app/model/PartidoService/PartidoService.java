package es.udc.ws.app.model.PartidoService;

import es.udc.ws.app.model.CompraEntrada.CompraEntrada;
import es.udc.ws.app.model.Partido.Partido;
import es.udc.ws.app.model.PartidoService.Exceptions.NonEqualCreditCardException;
import es.udc.ws.app.model.PartidoService.Exceptions.NotEnoughUnitsException;
import es.udc.ws.app.model.PartidoService.Exceptions.TicketsAlreadyTakenException;
import es.udc.ws.app.model.PartidoService.Exceptions.TicketsNotOnSaleException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface PartidoService {

    /*FUNC-1*/
    // Validaciones InputValidationException:
    // - partido.FechaCelebracion < partido.FechaCreacion
    // - partido.NomEquipoVis == null || partido.NomEquipoVis.length() == 0
    // - partido.Precio < 0
    public Partido addPartido(Partido partido) throws InputValidationException;

    /*FUNC-2*/
    // Validaciones InputValidationException:
    // - fecha1 < fecha2
    public List<Partido> getPartidosPorFecha(LocalDateTime fecha1, LocalDateTime fecha2)
            throws  InputValidationException;

    /*FUNC-3*/
    public Partido getPartido(Long IdPartido) throws InstanceNotFoundException;

    /*FUNC-4*/
    // Validaciones InputValidationException:
    // - email == null || email no sigue el patrón user@domain
    // - creditCardNumber: no formado por 16 dígitos numéricos
    // - units <= 0
    // Validaciones TicketNotOnSaleException
    // - fechaCompra < dbPartido.FechaCelebracion
    // Validaciones NotEnoughUnitsException
    // - unidades > dbPartido.NumMaxEntradasDisp
    public Long CompraEntradas(Long IdPartido, String Email, String NumTarjetaBancaria,
                               int unidades)
            throws InstanceNotFoundException, InputValidationException, NotEnoughUnitsException, TicketsNotOnSaleException;

    /*FUNC-5*/
    // Validaciones InputValidationException:
    // - email == null || email no sigue el patrón user@domain
    public List<CompraEntrada> ComprasUsuario(String Email)
            throws InputValidationException;

    /*FUNC-6*/
    // Validaciones NonEqualCreditCardException:
    // - CreditCardNumber != dbCompraEntrada.CreditCardNumber
    // Validaciones TicketsAlreadyTakenException:
    // - dbCompraEntrada.recogidas == true
    public void RecogerEntradas(Long IdCompra, String CreditCardNumber)
            throws InstanceNotFoundException, NonEqualCreditCardException, TicketsAlreadyTakenException, InputValidationException;}