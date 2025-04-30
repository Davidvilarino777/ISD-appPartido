package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.Partido.Partido;
import es.udc.ws.app.thrift.ThriftPartidoDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PartidoToThriftPartidoDto {

    public static Partido toPartido(ThriftPartidoDto partidoDto){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return new Partido(partidoDto.getIdPartido(),partidoDto.getNomEquipoVis(),
                (float) partidoDto.getPrecio(),partidoDto.getNumMaxEntradasDisp(),partidoDto.getNumEntradasVendidas(),
                LocalDateTime.parse(partidoDto.getFechaCelebracion(), formatter));
    }
    public static ThriftPartidoDto toThriftPartidoDto(Partido partido){
        return new ThriftPartidoDto(partido.getIdPartido(),partido.getNomEquipoVis(),
                partido.getPrecio(), partido.getNumMaxEntradasDisp(), partido.getNumEntradasVendidas(),
                partido.getFechaCelebracion().toString());
    }

    public static List<ThriftPartidoDto> toThriftPartidoDtos(List<Partido> partidos) {

        List<ThriftPartidoDto> dtos = new ArrayList<>(partidos.size());

        for (Partido partido : partidos) {
            dtos.add(toThriftPartidoDto(partido));
        }
        return dtos;

    }
}
