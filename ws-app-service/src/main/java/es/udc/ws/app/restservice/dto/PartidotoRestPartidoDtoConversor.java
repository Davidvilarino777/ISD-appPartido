package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.Partido.Partido;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PartidotoRestPartidoDtoConversor {

    public static List<RestPartidoDto> toRestPartidoDtos(List<Partido> partidos) {
        List<RestPartidoDto> partidoDtos = new ArrayList<>(partidos.size());
        for (int i = 0; i < partidos.size(); i++) {
            Partido partido = partidos.get(i);
            partidoDtos.add(toRestPartidoDto(partido));
        }
        return partidoDtos;
    }
    public static RestPartidoDto toRestPartidoDto(Partido partido){
        return new RestPartidoDto(partido.getIdPartido(), partido.getNomEquipoVis(),
                partido.getPrecio(), partido.getNumMaxEntradasDisp(), partido.getNumEntradasVendidas(),
                partido.getFechaCelebracion().toString());
    }

    public static Partido toPartido(RestPartidoDto partidoDto){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return new Partido(partidoDto.getIdPartido(), partidoDto.getNomEquipoVis(), partidoDto.getPrecio(),
                partidoDto.getNumMaxEntradasDisp(),partidoDto.getNumEntradasVendidas(),
                LocalDateTime.parse(partidoDto.getFechaCelebracion(), formatter));
    }
}
