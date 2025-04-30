package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestCompraEntradaDto;

import java.util.List;

public class JsonToRestCompraEntradaDtoConversor {
    public static ObjectNode toObjectNode(RestCompraEntradaDto Compra) {

        ObjectNode CompraNode = JsonNodeFactory.instance.objectNode();

        if (Compra.getIdCompra() != null) {
            CompraNode.put("IdCompra", Compra.getIdCompra());
        }
        CompraNode.put("IdPartido", Compra.getIdPartido()).
                put("Email", Compra.getEmail()).
                put("NumTarjetaBancaria", Compra.getNumTarjetaBancaria()).
                put("Unidades",Compra.getUnidades()).
                put("FechaCompra",Compra.getFechaCompra()).
                put("recogida",Compra.isRecogida());

        return CompraNode;
    }

    public static ObjectNode IdCompratoObjectNode(Long idCompra) {

        ObjectNode CompraNode = JsonNodeFactory.instance.objectNode();

        if (idCompra != null) {
            CompraNode.put("IdCompra", idCompra);
        }

        return CompraNode;
    }

    public static ArrayNode toArrayNode (List<RestCompraEntradaDto> compras){
        ArrayNode comprasNode = JsonNodeFactory.instance.arrayNode();
        for (int i = 0; i < compras.size(); i++) {
            RestCompraEntradaDto compraDto = compras.get(i);
            ObjectNode compraObjetct = toObjectNode(compraDto);
            comprasNode.add(compraObjetct);
        }
        return comprasNode;
    }
}
