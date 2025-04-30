package es.udc.ws.app.restservice.dto;

public class RestPartidoDto {
    private Long idPartido;
    private String NomEquipoVis;
    private Float Precio;
    private int NumMaxEntradasDisp;
    private int NumEntradasVendidas;
    private String FechaCelebracion;


    public RestPartidoDto(Long idPartido, String nomEquipoVis, Float precio, int numMaxEntradasDisp, int numEntradasVendidas, String fechaCelebracion) {
        this.idPartido = idPartido;
        this.NomEquipoVis = nomEquipoVis;
        this.Precio = precio;
        this.NumMaxEntradasDisp = numMaxEntradasDisp;
        this.NumEntradasVendidas = numEntradasVendidas;
        this.FechaCelebracion = fechaCelebracion;
    }

    public Long getIdPartido() {
        return idPartido;
    }

    public String getNomEquipoVis() {
        return NomEquipoVis;
    }

    public Float getPrecio() {
        return Precio;
    }

    public int getNumMaxEntradasDisp() {
        return NumMaxEntradasDisp;
    }

    public int getNumEntradasVendidas() {
        return NumEntradasVendidas;
    }

    public String getFechaCelebracion() {
        return FechaCelebracion;
    }

    public void setIdPartido(Long idPartido) {
        this.idPartido = idPartido;
    }

    public void setNomEquipoVis(String nomEquipoVis) {
        NomEquipoVis = nomEquipoVis;
    }

    public void setPrecio(Float precio) {
        Precio = precio;
    }

    public void setNumMaxEntradasDisp(int numMaxEntradasDisp) {
        NumMaxEntradasDisp = numMaxEntradasDisp;
    }

    public void setNumEntradasVendidas(int numEntradasVendidas) {
        NumEntradasVendidas = numEntradasVendidas;
    }

    public void setFechaCelebracion(String fechaCelebracion) {
        FechaCelebracion = fechaCelebracion;
    }

    @Override
    public String toString() {
        return "RestPartidoDto [" +
                "idPartido=" + idPartido +
                ", NomEquipoVis='" + NomEquipoVis +
                ", Precio=" + Precio +
                ", NumMaxEntradasDisp=" + NumMaxEntradasDisp +
                ", NumEntradasVendidas=" + NumEntradasVendidas +
                ", FechaCelebracion='" + FechaCelebracion +
                ']';
    }
}
