package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestPartidoDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.util.List;

public class JsonToRestPartidoDtoConversor {
    public static ObjectNode toObjectNode(RestPartidoDto partido) {

        ObjectNode partidoObject = JsonNodeFactory.instance.objectNode();

        partidoObject.put("IdPartido", partido.getIdPartido()).
                put("NomEquipoVis", partido.getNomEquipoVis()).
                put("Precio", partido.getPrecio()).
                put("NumMaxEntradasDisp", partido.getNumMaxEntradasDisp()).
                put("NumEntradasVendidas", partido.getNumEntradasVendidas()).
                put("FechaCelebracion", partido.getFechaCelebracion());

        return partidoObject;
    }
    public static ArrayNode toArrayNode(List<RestPartidoDto> partidos) {

        ArrayNode partidosNode = JsonNodeFactory.instance.arrayNode();
        for (int i = 0; i < partidos.size(); i++) {
            RestPartidoDto movieDto = partidos.get(i);
            ObjectNode partidoObject = toObjectNode(movieDto);
            partidosNode.add(partidoObject);
        }

        return partidosNode;
    }
    public static RestPartidoDto toRestPartidoDto(InputStream jsonPartido) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonPartido);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode partidoObject = (ObjectNode) rootNode;

                JsonNode partidoIdNode = partidoObject.get("IdPartido");
                Long IdPartido = (partidoIdNode != null) ? partidoIdNode.longValue() : null;

                String NomEquipoVis = partidoObject.get("NomEquipoVis").textValue().trim();
                float Precio = partidoObject.get("Precio").floatValue();
                int NumMaxEntradasDisp = partidoObject.get("NumMaxEntradasDisp").intValue();
                int NumEntradasVendidas = partidoObject.get("NumEntradasVendidas").intValue();
                String FechaCelebracion = partidoObject.get("FechaCelebracion").textValue().trim();

                return new RestPartidoDto(IdPartido, NomEquipoVis, Precio, NumMaxEntradasDisp,
                        NumEntradasVendidas, FechaCelebracion);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}
