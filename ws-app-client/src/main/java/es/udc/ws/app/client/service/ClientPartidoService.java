package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.dto.ClientCompraEntradaDto;
import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.app.client.service.exceptions.ClientNonEqualCreditCardException;
import es.udc.ws.app.client.service.exceptions.ClientNotEnoughUnitsException;
import es.udc.ws.app.client.service.exceptions.ClientTicketsAlreadyTakenException;
import es.udc.ws.app.client.service.exceptions.ClientTicketsNotOnSaleException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface ClientPartidoService {

    /*FUNC-1*/
    public ClientPartidoDto addPartido(ClientPartidoDto partido) throws InputValidationException;

    /*FUNC-2*/
    public List<ClientPartidoDto> getPartidosPorFecha(LocalDateTime fecha2)
            throws InputValidationException;

    /*FUNC-3*/
    public ClientPartidoDto getPartido(Long IdPartido) throws InstanceNotFoundException;

    /*FUNC-4*/
    public Long CompraEntradas(Long IdPartido, String Email, String NumTarjetaBancaria,
                               int unidades)
            throws InstanceNotFoundException, InputValidationException,
            ClientNotEnoughUnitsException, ClientTicketsNotOnSaleException;

    /*FUNC-5*/
    public List<ClientCompraEntradaDto> ComprasUsuario(String Email)
            throws InputValidationException;

    /*FUNC-6*/
    public void RecogerEntradas(Long IdCompra, String CreditCardNumber)
            throws InstanceNotFoundException, ClientTicketsAlreadyTakenException,
            ClientNonEqualCreditCardException;
}
