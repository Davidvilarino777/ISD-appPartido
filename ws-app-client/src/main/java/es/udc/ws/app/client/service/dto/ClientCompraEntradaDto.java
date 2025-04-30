package es.udc.ws.app.client.service.dto;

import java.time.LocalDateTime;

public class ClientCompraEntradaDto {
    private Long IdCompra;

    private Long IdPartido;

    private String Email;

    private String NumTarjetaBancaria;

    private int Unidades;

    private LocalDateTime FechaCompra;

    private boolean recogida;

    public ClientCompraEntradaDto(Long idPartido, String email, String numTarjetaBancaria, int unidades, LocalDateTime fechaCompra) {
        this.IdPartido = idPartido;
        this.Email = email;
        this.NumTarjetaBancaria = numTarjetaBancaria;
        this.Unidades = unidades;
        this.FechaCompra = (fechaCompra != null) ? fechaCompra.withNano(0) : null;
        recogida = false;
    }

    public ClientCompraEntradaDto(Long idCompra, Long idPartido, String email, String numTarjetaBancaria, int unidades, LocalDateTime fechaCompra, boolean recogida) {
        this(idPartido, email, numTarjetaBancaria, unidades, fechaCompra);
        this.IdCompra = idCompra;
        this.recogida = recogida;
    }

    public Long getIdCompra() {
        return IdCompra;
    }

    public void setIdCompra(Long idCompra) {
        this.IdCompra = idCompra;
    }

    public Long getIdPartido() {
        return IdPartido;
    }

    public void setIdPartido(Long idPartido) {
        this.IdPartido = idPartido;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getNumTarjetaBancaria() {
        return NumTarjetaBancaria;
    }

    public void setNumTarjetaBancaria(String numTarjetaBancaria) {
        this.NumTarjetaBancaria = numTarjetaBancaria;
    }

    public int getUnidades() {
        return Unidades;
    }

    public void setUnidades(int unidades) {
        this.Unidades = unidades;
    }

    public LocalDateTime getFechaCompra() {
        return FechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {

        this.FechaCompra = (fechaCompra != null) ? fechaCompra.withNano(0) : null;
    }

    public boolean getrecogida() {
        return recogida;
    }

    public void setRecogida(boolean recogida) {
        this.recogida = recogida;
    }
}

