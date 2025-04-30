package es.udc.ws.app.restservice.dto;

import java.time.LocalDateTime;

public class RestCompraEntradaDto {

    private Long IdCompra;

    private Long IdPartido;

    private String Email;

    private String NumTarjetaBancaria;

    private int Unidades;

    private String FechaCompra;

    private boolean recogida;

    public RestCompraEntradaDto(Long idCompra, Long idPartido, String email, String numTarjetaBancaria, int unidades, String fechaCompra, boolean recogida) {
        IdCompra = idCompra;
        IdPartido = idPartido;
        Email = email;
        NumTarjetaBancaria = numTarjetaBancaria;
        Unidades = unidades;
        FechaCompra = fechaCompra;
        this.recogida = recogida;
    }


    public String getNumTarjetaBancaria() {
        return NumTarjetaBancaria;
    }

    public void setNumTarjetaBancaria(String numTarjetaBancaria) {
        NumTarjetaBancaria = numTarjetaBancaria;
    }

    public Long getIdCompra() {
        return IdCompra;
    }

    public void setIdCompra(Long idCompra) {
        IdCompra = idCompra;
    }

    public Long getIdPartido() {
        return IdPartido;
    }

    public void setIdPartido(Long idPartido) {
        IdPartido = idPartido;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getUnidades() {
        return Unidades;
    }

    public void setUnidades(int unidades) {
        Unidades = unidades;
    }

    public String getFechaCompra() {
        return FechaCompra;
    }

    public void setFechaCompra(String fechaCompra) {
        FechaCompra = fechaCompra;
    }

    public boolean isRecogida() {
        return recogida;
    }

    public void setRecogida(boolean recogida) {
        this.recogida = recogida;
    }
}
