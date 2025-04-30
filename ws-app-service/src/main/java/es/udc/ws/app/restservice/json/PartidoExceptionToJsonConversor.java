package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.model.PartidoService.Exceptions.NonEqualCreditCardException;
import es.udc.ws.app.model.PartidoService.Exceptions.NotEnoughUnitsException;
import es.udc.ws.app.model.PartidoService.Exceptions.TicketsAlreadyTakenException;
import es.udc.ws.app.model.PartidoService.Exceptions.TicketsNotOnSaleException;

public class PartidoExceptionToJsonConversor {

    public static ObjectNode toNonEqualCreditCardException (NonEqualCreditCardException ex){
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
        exceptionObject.put("errorType","NonEqualCreditCard");
        exceptionObject.put("idCompra",(ex.getIdCompra() != null) ? ex.getIdCompra() : null);
        if(ex.getCreditCardNumber() != null){
            exceptionObject.put("creditcardNumber",ex.getCreditCardNumber());
        } else {
            exceptionObject.set("credicardNumber",null);
        }
        return exceptionObject;
    }


    public static ObjectNode toNotEnoughUnitsException (NotEnoughUnitsException ex){
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType","NotEnoughUnitsException");
        exceptionObject.put("IdPartido",(ex.getIdPartido() != null) ? ex.getIdPartido() : null);


        return exceptionObject;
    }

    public static ObjectNode toTicketsAlreadyTakenException (TicketsAlreadyTakenException ex){
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType","TicketsAlreadyTakenException");
        exceptionObject.put("IdCompra",(ex.getIdCompra() != null) ? ex.getIdCompra() : null);

        return exceptionObject;
    }

    public static ObjectNode toTicketsNotOnSaleException (TicketsNotOnSaleException ex){
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType","TicketsNotOnSaleException");
        exceptionObject.put("IdPartido",(ex.getIdPartido() != null) ? ex.getIdPartido() : null);

        return exceptionObject;
    }
}

