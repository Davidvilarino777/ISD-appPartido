namespace java es.udc.ws.app.thrift

struct ThriftPartidoDto{
    1: i64 idPartido
    2: string NomEquipoVis
    3: double Precio
    4: i32 NumMaxEntradasDisp
    5: i32 NumEntradasVendidas
    6: string FechaCelebracion
}

struct ThriftCompraEntradaDto{
    1: i64 IdCompra
    2: string Email
    3: string NumTarjetaBancaria
    4: i32 Unidades
    5: string FechaCompra
    6: bool recogida
}

exception ThriftInputValidationException {
    1: string message
}

exception ThriftInstanceNotFoundException {
    1: string instanceId
    2: string instanceType
}

exception ThriftNonEqualCreditCardException {
    1: i64 IdCompra
    2: string NumTarjetaBancaria
}

exception ThriftNotEnoughUnitsException {
    1: i64 IdPartido
}

exception ThriftTicketsAlreadyTakenException {
    1: i64 IdCompra
}

exception ThriftTicketsNotOnSaleException{
    1: i64 IdCompra
}

service ThriftPartidoService {
     ThriftPartidoDto addPartido(1: ThriftPartidoDto partido) throws (1:ThriftInputValidationException e)

     list<ThriftPartidoDto> getPartidosPorFecha(1:string fecha2) throws (1: ThriftInputValidationException e)

     ThriftPartidoDto getPartido(1: i64 IdPartido) throws (1: ThriftInstanceNotFoundException e)

     i64 CompraEntradas(1: i64 IdPartido, 2: string Email, 3: string NumTarjetaBancaria,
                                    4: i32 unidades)
                 throws (1: ThriftInstanceNotFoundException einf, 2: ThriftInputValidationException eiv,
                 3: ThriftNotEnoughUnitsException eneu, 4: ThriftTicketsNotOnSaleException etnos)

     list<ThriftCompraEntradaDto> ComprasUsuario(1: string Email) throws (1: ThriftInputValidationException e);

     void RecogerEntradas(1: i64 IdCompra, 2: string CreditCardNumber)
                 throws (1: ThriftInstanceNotFoundException einf, 2: ThriftTicketsAlreadyTakenException etat,
                 3: ThriftNonEqualCreditCardException enecc);

}