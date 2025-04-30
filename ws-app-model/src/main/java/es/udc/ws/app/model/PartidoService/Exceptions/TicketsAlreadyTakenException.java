package es.udc.ws.app.model.PartidoService.Exceptions;

public class TicketsAlreadyTakenException extends Exception{
    private Long IdCompra;

    public TicketsAlreadyTakenException(Long IdCompra){
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
