package es.udc.ws.app.model.Partido;

import java.time.LocalDateTime;
import java.util.Objects;

public class Partido {
    private Long IdPartido;

    private String NomEquipoVis;

    private float Precio;

    private int NumMaxEntradasDisp;

    private int NumEntradasVendidas;//necesario para función 2)

    private LocalDateTime FechaCelebracion;

    private LocalDateTime FechaCreacion;

    public Partido(String nomEquipoVis, float precio, int numMaxEntradasDisp, int numEntradasVendidas, LocalDateTime fechaCelebracion){
        this.NomEquipoVis= nomEquipoVis;
        this.Precio = precio;
        this.NumMaxEntradasDisp = numMaxEntradasDisp;
        this.NumEntradasVendidas = numEntradasVendidas;
        this.FechaCelebracion = (fechaCelebracion != null) ? fechaCelebracion.withNano(0) : null;
    }

    public Partido(Long idPartido, String nomEquipoVis, float precio, int numMaxEntradasDisp, int numEntradasVendidas, LocalDateTime fechaCelebracion){
        this(nomEquipoVis,precio,numMaxEntradasDisp,numEntradasVendidas,fechaCelebracion);
        this.IdPartido = idPartido;
    }

    public Partido(Long idPartido, String nomEquipoVis, float precio, int numMaxEntradasDisp, int numEntradasVendidas, LocalDateTime fechaCelebracion, LocalDateTime fechaCreacion){
        this(idPartido,nomEquipoVis,precio,numMaxEntradasDisp,numEntradasVendidas,fechaCelebracion);
        this.FechaCreacion = (fechaCreacion != null) ? fechaCreacion.withNano(0) : null;
    }


    public Long getIdPartido(){return IdPartido;}

    public void setIdPartido(Long idPartido){this.IdPartido = idPartido;}

    public String getNomEquipoVis(){return NomEquipoVis;}

    public void setNomEquipoVis(String nomEquipoVis){this.NomEquipoVis = nomEquipoVis;}

    public float getPrecio(){return Precio;}

    public void setPrecio(float precio){this.Precio = precio;}

    public int getNumMaxEntradasDisp(){return NumMaxEntradasDisp;}

    public void setNumMaxEntradasDisp(int numMaxEntradasDisp){this.NumMaxEntradasDisp = numMaxEntradasDisp;}

    public int getNumEntradasVendidas(){return NumEntradasVendidas;}

    public void setNumEntradasVendidas(int numEntradasVendidas){this.NumEntradasVendidas = numEntradasVendidas;}

    public LocalDateTime getFechaCreacion() {return FechaCreacion;}

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.FechaCreacion = (fechaCreacion != null) ? fechaCreacion.withNano(0) : null;
    }

    public LocalDateTime getFechaCelebracion() {return FechaCelebracion;}

    public void setFechaCelebracion(LocalDateTime fechaCelebracion) {
        this.FechaCelebracion = (fechaCelebracion != null) ? fechaCelebracion.withNano(0) : null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Partido partido = (Partido) o;
        return Double.compare(partido.Precio, Precio) == 0 && NumMaxEntradasDisp == partido.NumMaxEntradasDisp && NumEntradasVendidas == partido.NumEntradasVendidas && Objects.equals(IdPartido, partido.IdPartido) && Objects.equals(NomEquipoVis, partido.NomEquipoVis) && Objects.equals(FechaCelebracion, partido.FechaCelebracion) && Objects.equals(FechaCreacion, partido.FechaCreacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(IdPartido, NomEquipoVis, Precio, NumMaxEntradasDisp, NumEntradasVendidas, FechaCelebracion, FechaCreacion);
    }
}
