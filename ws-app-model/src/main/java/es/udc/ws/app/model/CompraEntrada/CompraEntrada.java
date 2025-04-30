package es.udc.ws.app.model.CompraEntrada;

import java.time.LocalDateTime;
import java.util.Objects;

public class CompraEntrada {
    private Long IdCompra;

    private Long IdPartido;

    private String Email;

    private String NumTarjetaBancaria;

    private int Unidades;

    private LocalDateTime FechaCompra;

    private boolean recogida;

    public CompraEntrada(Long idPartido, String email, String numTarjetaBancaria, int unidades, LocalDateTime fechaCompra) {
        this.IdPartido = idPartido;
        this.Email = email;
        this.NumTarjetaBancaria = numTarjetaBancaria;
        this.Unidades = unidades;
        this.FechaCompra = (fechaCompra != null) ? fechaCompra.withNano(0) : null;
        recogida = false;
    }

    public CompraEntrada(Long idCompra, Long idPartido, String email, String numTarjetaBancaria, int unidades, LocalDateTime fechaCompra, boolean recogida) {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompraEntrada that = (CompraEntrada) o;
        return Unidades == that.Unidades && recogida == that.recogida && Objects.equals(IdCompra, that.IdCompra) && Objects.equals(IdPartido, that.IdPartido) && Objects.equals(Email, that.Email) && Objects.equals(NumTarjetaBancaria, that.NumTarjetaBancaria) && Objects.equals(FechaCompra, that.FechaCompra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(IdCompra, IdPartido, Email, NumTarjetaBancaria, Unidades, FechaCompra, recogida);
    }
}