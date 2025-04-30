package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.ClientPartidoService;
import es.udc.ws.app.client.service.dto.ClientCompraEntradaDto;
import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.app.client.service.exceptions.ClientNonEqualCreditCardException;
import es.udc.ws.app.client.service.exceptions.ClientNotEnoughUnitsException;
import es.udc.ws.app.client.service.exceptions.ClientTicketsAlreadyTakenException;
import es.udc.ws.app.client.service.exceptions.ClientTicketsNotOnSaleException;
import es.udc.ws.app.thrift.ThriftInputValidationException;
import es.udc.ws.app.thrift.ThriftPartidoService;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ThriftClientPartidoService implements ClientPartidoService {

    private final static String ENDPOINT_ADDRESS_PARAMETER =
            "ThriftClientPartidoService.endpointAddress";

    private final static String endpointAddress =
            ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);

    @Override
    public ClientPartidoDto addPartido(ClientPartidoDto partido) throws InputValidationException {

        ThriftPartidoService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return ClientPartidoDtoToThriftPartidoDtoConversor.toClientPartidoDto(
                    client.addPartido(ClientPartidoDtoToThriftPartidoDtoConversor.
                            toThriftPartidoDto(partido)));

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientPartidoDto> getPartidosPorFecha(LocalDateTime fecha2) throws InputValidationException {
        ThriftPartidoService.Client client = getClient();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return ClientPartidoDtoToThriftPartidoDtoConversor.
                    toClientPartidoDtos(client.getPartidosPorFecha(fecha2.toString()));

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientPartidoDto getPartido(Long IdPartido) throws InstanceNotFoundException {
        return null;
    }

    @Override
    public Long CompraEntradas(Long IdPartido, String Email, String NumTarjetaBancaria, int unidades) throws InstanceNotFoundException, InputValidationException, ClientNotEnoughUnitsException, ClientTicketsNotOnSaleException {
        return null;
    }

    @Override
    public List<ClientCompraEntradaDto> ComprasUsuario(String Email) throws InputValidationException {
        return null;
    }

    @Override
    public void RecogerEntradas(Long IdCompra, String CreditCardNumber) throws InstanceNotFoundException, ClientTicketsAlreadyTakenException, ClientNonEqualCreditCardException {

    }

    private ThriftPartidoService.Client getClient() {

        try {

            TTransport transport = new THttpClient(endpointAddress);
            TProtocol protocol = new TBinaryProtocol(transport);

            return new ThriftPartidoService.Client(protocol);

        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }

    }
}
