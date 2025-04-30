package es.udc.ws.app.client.service.exceptions;

public class ClientNotEnoughUnitsException extends Exception{
    private Long IdPartido;

    public ClientNotEnoughUnitsException(Long IdPartido){
        super("El partido de identificador " + IdPartido + " no tiene suficientes entradas");
        this.IdPartido = IdPartido;
    }

    public Long getIdPartido() {
        return IdPartido;
    }

    public void setIdPartido(Long IdPartido) {
        this.IdPartido = IdPartido;
    }
}
