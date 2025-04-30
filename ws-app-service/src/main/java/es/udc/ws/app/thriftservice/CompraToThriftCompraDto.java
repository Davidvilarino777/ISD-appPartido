package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.CompraEntrada.CompraEntrada;
import es.udc.ws.app.model.Partido.Partido;
import es.udc.ws.app.thrift.ThriftCompraEntradaDto;
import es.udc.ws.app.thrift.ThriftPartidoDto;

import java.util.ArrayList;
import java.util.List;


public class CompraToThriftCompraDto {

    public static ThriftCompraEntradaDto toThriftCompraDto(CompraEntrada compra){
        return new ThriftCompraEntradaDto(compra.getIdCompra(),compra.getEmail(),
                compra.getNumTarjetaBancaria(), compra.getUnidades(),
                compra.getFechaCompra().toString(),compra.getrecogida());
    }

    public static List<ThriftCompraEntradaDto> toThriftCompraDtos(List<CompraEntrada> compras) {

        List<ThriftCompraEntradaDto> dtos = new ArrayList<>(compras.size());

        for (CompraEntrada compra : compras) {
            dtos.add(toThriftCompraDto(compra));
        }
        return dtos;
    }

}