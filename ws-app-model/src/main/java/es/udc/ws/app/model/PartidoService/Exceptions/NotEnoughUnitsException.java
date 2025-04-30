package es.udc.ws.app.model.PartidoService.Exceptions;

public class NotEnoughUnitsException extends Exception{
    private Long IdPartido;

    public NotEnoughUnitsException(Long IdPartido){
        super("El partido de identificador=\"" + IdPartido + "\n no tiene suficientes entradas");
        this.IdPartido = IdPartido;
    }

    public Long getIdPartido() {
        return IdPartido;
    }

    public void setIdPartido(Long IdPartido) {
        this.IdPartido = IdPartido;
    }
}