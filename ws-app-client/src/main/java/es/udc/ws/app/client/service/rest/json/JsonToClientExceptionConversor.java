package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import es.udc.ws.app.client.service.exceptions.ClientNonEqualCreditCardException;
import es.udc.ws.app.client.service.exceptions.ClientNotEnoughUnitsException;
import es.udc.ws.app.client.service.exceptions.ClientTicketsAlreadyTakenException;
import es.udc.ws.app.client.service.exceptions.ClientTicketsNotOnSaleException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;

public class JsonToClientExceptionConversor {

    public static Exception fromBadRequestErrorCode(InputStream ex) throws ParsingException{
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT){
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String tipoerror = rootNode.get("errorType").textValue();
                if(tipoerror.equals("InputValidation")){
                    return toInputValidationException(rootNode);
                } else {
                    throw  new ParsingException("Unrecognized error type: " + tipoerror);
                }
            }
        } catch (ParsingException e){
            throw e;
        } catch (Exception e){
            throw new ParsingException(e);
        }
    }


    private static InputValidationException toInputValidationException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new InputValidationException(message);
    }

    public static Exception fromNotFoundErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String tipoerror = rootNode.get("errorType").textValue();
                if (tipoerror.equals("InstanceNotFound")) {
                    return toInstanceNotFoundException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + tipoerror);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InstanceNotFoundException toInstanceNotFoundException(JsonNode rootNode) {
        String instanceId = rootNode.get("instanceId").textValue();
        String instanceType = rootNode.get("instanceType").textValue();
        return new InstanceNotFoundException(instanceId, instanceType);
    }

    public static Exception fromForbiddenErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String tipoerror = rootNode.get("errorType").textValue();
                if (tipoerror.equals("NotEnoughUnitsException")) {
                    return toNotEnoughUnitsException(rootNode);
                } else if (tipoerror.equals("NonEqualCreditCardException")) {
                    return toNonEqualCreditCardException(rootNode);
                }else {
                    throw new ParsingException("Unrecognized error type: " + tipoerror);
                    }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
    private static ClientNotEnoughUnitsException toNotEnoughUnitsException(JsonNode rootNode) {
        Long idpartido = rootNode.get("IdPartido").longValue();
        return new ClientNotEnoughUnitsException(idpartido);
    }
    private static ClientNonEqualCreditCardException toNonEqualCreditCardException(JsonNode rootNode) {
        Long idcompra = rootNode.get("IdCompra").longValue();
        String creditcardnumber = rootNode.get("CreditCardNumber").textValue().trim();
        return new ClientNonEqualCreditCardException(idcompra,creditcardnumber);
    }

    public static Exception fromGoneErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String tipoerror = rootNode.get("errorType").textValue();
                if (tipoerror.equals("TicketsNotOnSaleException")) {
                    return toTicketsNotOnSaleException(rootNode);
                }
                else if (tipoerror.equals("TicketsAlreadyTakenException")){
                    return toTicketsAlreadyTakenException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + tipoerror);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientTicketsNotOnSaleException toTicketsNotOnSaleException(JsonNode rootNode) {
        Long idpartido = rootNode.get("IdPartido").longValue();
        return new ClientTicketsNotOnSaleException(idpartido);
    }

    private static ClientTicketsAlreadyTakenException toTicketsAlreadyTakenException(JsonNode rootNode) {
        Long idcompra = rootNode.get("IdCompra").longValue();
        return new ClientTicketsAlreadyTakenException(idcompra);
    }

}
