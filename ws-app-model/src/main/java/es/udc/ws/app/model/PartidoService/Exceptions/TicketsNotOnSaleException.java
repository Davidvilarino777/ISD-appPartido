package es.udc.ws.app.model.PartidoService.Exceptions;

public class TicketsNotOnSaleException extends Exception{
    private Long IdPartido;

    public TicketsNotOnSaleException(Long IdPartido) {
        super("Las entradas para el partido" + IdPartido + "ya no están a la venta\n");
        this.IdPartido=IdPartido;
    }

    public Long getIdPartido() {
        return IdPartido;
    }

    public void setIdPartido(long idPartido) {
        IdPartido = idPartido;
    }
}
