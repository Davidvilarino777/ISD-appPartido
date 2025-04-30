package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientPartidoDtoConversor {

    public static ObjectNode toObjectNode(ClientPartidoDto partido) throws IOException {

        ObjectNode partidoObject = JsonNodeFactory.instance.objectNode();

        if (partido.getIdPartido() != null) {
            partidoObject.put("IdPartido", partido.getIdPartido());
        }
        partidoObject.put("NomEquipoVis", partido.getNomEquipoVis()).
                put("Precio", partido.getPrecio()).
                put("NumMaxEntradasDisp", partido.getNumMaxEntradasDisp()).
                put("NumEntradasVendidas", partido.getNumEntradasVendidas()).
                put("FechaCelebracion", partido.getFechaCelebracion().toString());

        return partidoObject;
    }

    public static ClientPartidoDto toClientPartidoDto(InputStream jsonPartido) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonPartido);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientPartidoDto(rootNode);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientPartidoDto> toClientPartidoDtos(InputStream jsonPartidos) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonPartidos);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode partidosArray = (ArrayNode) rootNode;
                List<ClientPartidoDto> partidoDtos = new ArrayList<>(partidosArray.size());
                for (JsonNode partidoNode : partidosArray) {
                    partidoDtos.add(toClientPartidoDto(partidoNode));
                }

                return partidoDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientPartidoDto toClientPartidoDto(JsonNode partidoNode) throws ParsingException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        if (partidoNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode partidoObject = (ObjectNode) partidoNode;

            JsonNode partidoIdNode = partidoObject.get("IdPartido");
            Long IdPartido = (partidoIdNode != null) ? partidoIdNode.longValue() : null;

            String NomEquipoVis = partidoObject.get("NomEquipoVis").textValue().trim();
            float Precio = partidoObject.get("Precio").floatValue();
            int NumMaxEntradasDisp = partidoObject.get("NumMaxEntradasDisp").intValue();
            int NumEntradasVendidas = partidoObject.get("NumEntradasVendidas").intValue();
            String FechaCelebracion = partidoObject.get("FechaCelebracion").textValue().trim();

            return new ClientPartidoDto(IdPartido, NomEquipoVis, Precio, NumMaxEntradasDisp,
                    NumEntradasVendidas, LocalDateTime.parse(FechaCelebracion,formatter));
        }
    }
}
