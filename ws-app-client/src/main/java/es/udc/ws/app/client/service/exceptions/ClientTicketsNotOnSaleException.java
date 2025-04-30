package es.udc.ws.app.client.service.exceptions;

public class ClientTicketsNotOnSaleException extends Exception{

    private Long IdPartido;

    public ClientTicketsNotOnSaleException(Long IdPartido) {
        super("Las entradas para el partido " + IdPartido + " ya no están a la venta\n");
        this.IdPartido=IdPartido;
    }

    public Long getIdPartido() {
        return IdPartido;
    }

    public void setIdPartido(long idPartido) {
        IdPartido = idPartido;
    }
}
