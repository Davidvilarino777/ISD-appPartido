package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.CompraEntrada.CompraEntrada;

import java.util.ArrayList;
import java.util.List;

public class CompraEntradatoRestCompraEntradaDtoConversor {
    public static RestCompraEntradaDto toRestCompraEntradaDto(CompraEntrada compraEntrada){
        return new RestCompraEntradaDto(
                compraEntrada.getIdCompra(),
                compraEntrada.getIdPartido(),
                compraEntrada.getEmail(),
                compraEntrada.getNumTarjetaBancaria().substring(Math.max(0, compraEntrada.getNumTarjetaBancaria().length() - 4)),
                compraEntrada.getUnidades(),
                compraEntrada.getFechaCompra().toString(),
                compraEntrada.getrecogida());
    }

    public static List<RestCompraEntradaDto> toRestCompraEntradaDtos(List<CompraEntrada> compras){
        List<RestCompraEntradaDto> comprasDtos = new ArrayList<>(compras.size());

        for (int i = 0; i < compras.size(); i++){
            CompraEntrada compra = compras.get(i);
            comprasDtos.add(toRestCompraEntradaDto(compra));

        }
        return comprasDtos;
    }
}
