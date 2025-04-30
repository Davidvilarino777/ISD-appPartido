package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.CompraEntrada.CompraEntrada;
import es.udc.ws.app.model.Partido.Partido;
import es.udc.ws.app.model.PartidoService.Exceptions.NonEqualCreditCardException;
import es.udc.ws.app.model.PartidoService.Exceptions.NotEnoughUnitsException;
import es.udc.ws.app.model.PartidoService.Exceptions.TicketsAlreadyTakenException;
import es.udc.ws.app.model.PartidoService.Exceptions.TicketsNotOnSaleException;
import es.udc.ws.app.model.PartidoService.PartidoServiceFactory;
import es.udc.ws.app.restservice.dto.CompraEntradatoRestCompraEntradaDtoConversor;
import es.udc.ws.app.restservice.dto.PartidotoRestPartidoDtoConversor;
import es.udc.ws.app.restservice.dto.RestCompraEntradaDto;
import es.udc.ws.app.restservice.dto.RestPartidoDto;
import es.udc.ws.app.restservice.json.JsonToRestCompraEntradaDtoConversor;
import es.udc.ws.app.restservice.json.JsonToRestPartidoDtoConversor;
import es.udc.ws.app.restservice.json.PartidoExceptionToJsonConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompraServlet extends RestHttpServletTemplate {
    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {
        ServletUtils.checkEmptyPath(req);
        int NUMPARAMETROS =req.getParameterMap().size();
        if(NUMPARAMETROS==4) {
            Long IdPartido = ServletUtils.getMandatoryParameterAsLong(req, "IdPartido");
            String Email = ServletUtils.getMandatoryParameter(req, "Email");
            String CreditCardNumber = ServletUtils.getMandatoryParameter(req, "CreditCardNumber");
            int unidades = Math.toIntExact(ServletUtils.getMandatoryParameterAsLong(req, "Unidades"));

            Long idCompra = null;
            try {
                idCompra = PartidoServiceFactory.getService().
                        CompraEntradas(IdPartido, Email, CreditCardNumber, unidades);
            } catch (NotEnoughUnitsException e) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                        PartidoExceptionToJsonConversor.toNotEnoughUnitsException(e),
                        null);
                return;
            } catch (TicketsNotOnSaleException e) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_GONE,
                        PartidoExceptionToJsonConversor.toTicketsNotOnSaleException(e),
                        null);
                return;
            }


            String compraURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + idCompra.toString();
            Map<String, String> headers = new HashMap<>(1);
            headers.put("Location", compraURL);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                    JsonToRestCompraEntradaDtoConversor.IdCompratoObjectNode(idCompra), headers);
        }
        else if(NUMPARAMETROS==2){
            Long IdCompra = ServletUtils.getMandatoryParameterAsLong(req, "IdCompra");
            String CreditCardNumber = ServletUtils.getMandatoryParameter(req, "NumTarjetaBancaria");

            try {
                PartidoServiceFactory.getService().RecogerEntradas(IdCompra,CreditCardNumber);
            } catch (TicketsAlreadyTakenException e) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_GONE,
                        PartidoExceptionToJsonConversor.toTicketsAlreadyTakenException(e),
                        null);
                return;
            } catch (NonEqualCreditCardException e) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                        PartidoExceptionToJsonConversor.toNonEqualCreditCardException(e),
                        null);
                return;
            }
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);
        }
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, InstanceNotFoundException, InputValidationException {
        String email = req.getParameter("email");

        List<CompraEntrada> comprasUsuario = PartidoServiceFactory.getService().ComprasUsuario(email);

        List<RestCompraEntradaDto> comprasDtos = CompraEntradatoRestCompraEntradaDtoConversor.toRestCompraEntradaDtos(comprasUsuario);

        ServletUtils.writeServiceResponse(resp,HttpServletResponse.SC_OK,
                JsonToRestCompraEntradaDtoConversor.toArrayNode(comprasDtos),null);
    }
}
