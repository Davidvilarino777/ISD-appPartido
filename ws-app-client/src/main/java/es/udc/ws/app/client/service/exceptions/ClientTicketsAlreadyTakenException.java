package es.udc.ws.app.client.service.exceptions;

public class ClientTicketsAlreadyTakenException extends Exception{
    private Long IdCompra;

    public ClientTicketsAlreadyTakenException(Long IdCompra){
        super("Las entradas correspondiente a la compra:\"" + IdCompra + "\n ya fueron recogidas");
        this.IdCompra = IdCompra;
    }

    public Long getIdCompra() {
        return IdCompra;
    }

    public void setIdCompra(Long IdCompra) {
        this.IdCompra = IdCompra;
    }
}
