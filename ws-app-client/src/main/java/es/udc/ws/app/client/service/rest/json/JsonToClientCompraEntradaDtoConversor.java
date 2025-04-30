package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientCompraEntradaDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientCompraEntradaDtoConversor {

    public static ClientCompraEntradaDto toClientCompraEntradaDto(JsonNode jsonCompraEntrada) throws ParsingException{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        try{
            if(jsonCompraEntrada.getNodeType() != JsonNodeType.OBJECT){
                throw new ParsingException("Unrecognized JSON (object expected");
            } else {
                ObjectNode partidoObject = (ObjectNode) jsonCompraEntrada;

                JsonNode compraIdNode = partidoObject.get("IdCompra");
                Long idcompra = (compraIdNode != null) ? compraIdNode.longValue() : null;

                Long idpartido = partidoObject.get("IdPartido").longValue();
                String email = partidoObject.get("Email").textValue().trim();
                String numtarjetabancaria = partidoObject.get("NumTarjetaBancaria").textValue().trim();
                int unidades = partidoObject.get("Unidades").intValue();
                String fechacompra = partidoObject.get("FechaCompra").textValue().trim();
                boolean recog = partidoObject.get("recogida").booleanValue();
                return new ClientCompraEntradaDto(idcompra,idpartido,email,numtarjetabancaria,unidades, LocalDateTime.parse(fechacompra,formatter),recog);
            }
        } catch (ParsingException ex){
            throw ex;
        } catch (Exception e){
            throw new ParsingException(e);
        }
    }

    public static List<ClientCompraEntradaDto> toClientCompraEntradaDtos(InputStream jsonCompras) throws ParsingException{
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonCompras);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY){
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else{
                ArrayNode comprasArray = (ArrayNode) rootNode;
                List<ClientCompraEntradaDto> comprasDtos = new ArrayList<>(comprasArray.size());
                for (JsonNode comprasNode : comprasArray){
                    comprasDtos.add(toClientCompraEntradaDto(comprasNode));
                }
                return comprasDtos;
            }
        } catch (ParsingException ex){
            throw ex;
        } catch (Exception e){
            throw new ParsingException(e);
        }
    }
    public static Long toIdCompra(InputStream jsonIdCompra) throws IOException {
        ObjectMapper objectMapper = ObjectMapperFactory.instance();
        JsonNode rootNode = objectMapper.readTree(jsonIdCompra);

        if(rootNode.getNodeType() != JsonNodeType.OBJECT){
            throw new ParsingException("Unrecognized JSON (object expected");
        }

        else {
            ObjectNode CompraObject = (ObjectNode) rootNode;

            JsonNode compraIdNode = CompraObject.get("IdCompra");
            Long idcompra = (compraIdNode != null) ? compraIdNode.longValue() : null;

            return idcompra;
        }
    }
}
