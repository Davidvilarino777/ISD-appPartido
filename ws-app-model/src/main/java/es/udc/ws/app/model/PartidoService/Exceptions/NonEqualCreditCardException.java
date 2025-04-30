package es.udc.ws.app.model.PartidoService.Exceptions;

public class NonEqualCreditCardException extends Exception{
    private Long idCompra;
    private String creditCardNumber;

    public NonEqualCreditCardException(Long idCompra, String creditCardNumber){
        super("La tarjeta de credito\" " + creditCardNumber +
                "presentada en la recogida de la compra\"" + idCompra +
                "no coincide con la original\n");
    }

    public Long getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Long idCompra) {
        this.idCompra = idCompra;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

}
