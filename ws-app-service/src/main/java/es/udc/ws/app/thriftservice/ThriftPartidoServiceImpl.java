package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.CompraEntrada.CompraEntrada;
import es.udc.ws.app.model.Partido.Partido;
import es.udc.ws.app.model.PartidoService.Exceptions.NotEnoughUnitsException;
import es.udc.ws.app.model.PartidoService.Exceptions.TicketsNotOnSaleException;
import es.udc.ws.app.model.PartidoService.PartidoServiceFactory;
import es.udc.ws.app.thrift.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.TException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ThriftPartidoServiceImpl implements ThriftPartidoService.Iface {

    @Override
    public ThriftPartidoDto addPartido(ThriftPartidoDto partidoDto) throws ThriftInputValidationException, TException {
        Partido partido = PartidoToThriftPartidoDto.toPartido(partidoDto);

        try {
            Partido addedPartido = PartidoServiceFactory.getService().addPartido(partido);
            return PartidoToThriftPartidoDto.toThriftPartidoDto(addedPartido);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }
    }

    @Override
    public List<ThriftPartidoDto> getPartidosPorFecha(String fecha2) throws ThriftInputValidationException, TException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        try{
            List<Partido> partidos = PartidoServiceFactory.getService().getPartidosPorFecha(LocalDateTime.now(),
                    LocalDateTime.parse(fecha2,formatter));

            return PartidoToThriftPartidoDto.toThriftPartidoDtos(partidos);
        }
        catch (InputValidationException e){
            throw new ThriftInputValidationException(e.getMessage());
        }
    }

    @Override
    public ThriftPartidoDto getPartido(long IdPartido) throws ThriftInstanceNotFoundException, TException {
        return null;
    }

    @Override
    public long CompraEntradas(long IdPartido, String Email, String NumTarjetaBancaria, int unidades) throws ThriftInstanceNotFoundException, ThriftInputValidationException, ThriftNotEnoughUnitsException, ThriftTicketsNotOnSaleException, TException {
        return 0;
    }

    @Override
    public List<ThriftCompraEntradaDto> ComprasUsuario(String Email) throws ThriftInputValidationException, TException {
        return null;
    }

    @Override
    public void RecogerEntradas(long IdCompra, String CreditCardNumber) throws ThriftInstanceNotFoundException, ThriftTicketsAlreadyTakenException, ThriftNonEqualCreditCardException, TException {

    }
}
