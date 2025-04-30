package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.app.thrift.ThriftPartidoDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ClientPartidoDtoToThriftPartidoDtoConversor {
    public static ThriftPartidoDto toThriftPartidoDto(
            ClientPartidoDto clientPartidoDto) {

        Long IdPartido = clientPartidoDto.getIdPartido();

        return new ThriftPartidoDto(
                IdPartido == null ? -1 : IdPartido,
                clientPartidoDto.getNomEquipoVis(),
                clientPartidoDto.getPrecio(),
                clientPartidoDto.getNumMaxEntradasDisp(),
                clientPartidoDto.getNumEntradasVendidas(),
                clientPartidoDto.getFechaCelebracion().toString());

    }

    public static List<ClientPartidoDto> toClientPartidoDtos(List<ThriftPartidoDto> partidos) {

        List<ClientPartidoDto> clientPartidoDtos = new ArrayList<>(partidos.size());

        for (ThriftPartidoDto partido : partidos) {
            clientPartidoDtos.add(toClientPartidoDto(partido));
        }
        return clientPartidoDtos;

    }

    public static ClientPartidoDto toClientPartidoDto(ThriftPartidoDto partido) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return new ClientPartidoDto(
                partido.getIdPartido(),
                partido.getNomEquipoVis(),
                (float) partido.getPrecio(),
                partido.getNumMaxEntradasDisp(),
                partido.getNumEntradasVendidas(),
                LocalDateTime.parse(partido.getFechaCelebracion(), formatter));

    }
}
