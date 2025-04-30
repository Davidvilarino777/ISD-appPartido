package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.Partido.Partido;
import es.udc.ws.app.model.PartidoService.PartidoServiceFactory;
import es.udc.ws.app.restservice.dto.PartidotoRestPartidoDtoConversor;
import es.udc.ws.app.restservice.dto.RestPartidoDto;
import es.udc.ws.app.restservice.json.JsonToRestPartidoDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class PartidoServlet extends RestHttpServletTemplate{

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException {
        ServletUtils.checkEmptyPath(req);

        RestPartidoDto partidoDto = JsonToRestPartidoDtoConversor.toRestPartidoDto(req.getInputStream());
        Partido partido = PartidotoRestPartidoDtoConversor.toPartido(partidoDto);

        partido = PartidoServiceFactory.getService().addPartido(partido);

        partidoDto = PartidotoRestPartidoDtoConversor.toRestPartidoDto(partido);
        String partidoURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + partido.getIdPartido();
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Location", partidoURL);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                JsonToRestPartidoDtoConversor.toObjectNode(partidoDto), headers);
    }
    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {

        if (req.getPathInfo() == null || req.getPathInfo().equals("/")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            ServletUtils.checkEmptyPath(req);
            LocalDateTime fecha = LocalDateTime.parse(req.getParameter("fecha"), formatter);

            List<Partido> partidos = PartidoServiceFactory.getService().getPartidosPorFecha
                    (LocalDateTime.now().withNano(0), fecha);

            List<RestPartidoDto> partidoDto = PartidotoRestPartidoDtoConversor.toRestPartidoDtos(partidos);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    JsonToRestPartidoDtoConversor.toArrayNode(partidoDto), null);
        } else {
            Long partidoId = ServletUtils.getIdFromPath(req, "IdPartido");
            Partido partido;
            partido = PartidoServiceFactory.getService().getPartido(partidoId);
            RestPartidoDto partidoDto = PartidotoRestPartidoDtoConversor.toRestPartidoDto(partido);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK, JsonToRestPartidoDtoConversor.toObjectNode(partidoDto), null);
        }
    }
}
