package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.ClientPartidoService;
import es.udc.ws.app.client.service.ClientPartidoServiceFactory;
import es.udc.ws.app.client.service.dto.ClientCompraEntradaDto;
import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.servlet.ServletUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class AppServiceClient {
    public static void main(String[] args) {
        // TODO

        if(args.length == 0){
            printUsageAndExit();
        }

        ClientPartidoService clientPartidoService = ClientPartidoServiceFactory.getService();

        if("-addMatch".equalsIgnoreCase(args[0])) {
            validateArgs(args, 5, new int[] {2,3});
            // [add] AppServiceClient sh-addMatch <NomEquipoVis> <FechaCelebracion>
            // <Precio> <NumMaxEntradasDisp>

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

            try {
                ClientPartidoDto partidoDto = clientPartidoService.addPartido(new ClientPartidoDto(null,
                        args[1], Float.valueOf(args[2]), Short.valueOf(args[3]),
                        0,LocalDateTime.parse(args[4],formatter)));

                System.out.println("• Datos del partido con id " + partidoDto.getIdPartido()+ ":\n" );
                System.out.println("Fecha celebracion: " + partidoDto.getFechaCelebracion() +
                        ", Entradas totales: " + partidoDto.getNumMaxEntradasDisp() +
                        ", Entradas restantes: " +
                        (partidoDto.getNumMaxEntradasDisp() -  partidoDto.getNumEntradasVendidas())+
                        ", Nombre del Equipo visitante: " + partidoDto.getNomEquipoVis() +
                        ", Precio: " + partidoDto.getPrecio());

            } catch (NumberFormatException | InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        }
        else if ("-buy".equalsIgnoreCase(args[0])) {
            validateArgs(args, 5, new int[]{1,4});

            // [buy] AppServiceClient -buy <IdPartido> <Email> <NumTarjetaBancaria> <Unidades>

            Long IdCompra;
            try {
                IdCompra = clientPartidoService.CompraEntradas(Long.parseLong(args[1]),
                        args[2],args[3],Short.valueOf(args[4]));

                System.out.println("Entradas compradas con IdCompra: " + IdCompra);

            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }
        else if ("-collect".equalsIgnoreCase(args[0])){
            validateArgs(args,3,new int[] {1});
            try{
                clientPartidoService.RecogerEntradas(Long.parseLong(args[1]),args[2]);
                System.out.println("• Recogidas entradas de la compra con id: " + args[1] + "\n" );
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }
        else if ("-findMatch".equalsIgnoreCase(args[0])){
            //[findmatch] findmatch <id>
            validateArgs(args,2,new int[] {});
            try{
                ClientPartidoDto partido = clientPartidoService.getPartido(Long.parseLong(args[1]));
                System.out.println("• Datos del partido con id " + partido.getIdPartido()+ ":\n" );
                System.out.println("Fecha celebracion: " + partido.getFechaCelebracion() +
                                    ", Entradas disponibles: " + (partido.getNumMaxEntradasDisp() - partido.getNumEntradasVendidas()) +
                                    ", Entradas totales: " + partido.getNumMaxEntradasDisp() +
                                    ", Nombre del equipo visitante: " + partido.getNomEquipoVis() +
                                    ", Precio: " + partido.getPrecio());
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }
        else if ("-findMatches".equalsIgnoreCase(args[0])){
            validateArgs(args,2,new int[] {});
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime fecha = LocalDateTime.parse(args[1], formatter);
            try{
                List<ClientPartidoDto> partidos = clientPartidoService.getPartidosPorFecha(fecha);
                for (int i = 0; i < partidos.size(); i++) {
                    ClientPartidoDto partidoDto= partidos.get(i);
                    System.out.println("• Datos del partido con id " + partidoDto.getIdPartido()+ ":\n" );
                    System.out.println("Fecha celebracion: " + partidoDto.getFechaCelebracion() +
                            ", Entradas disponibles: " + (partidoDto.getNumMaxEntradasDisp()-partidoDto.getNumEntradasVendidas()) +
                            ", Entradas totales: " + partidoDto.getNumMaxEntradasDisp() +
                            ", Nombre del Equipo visitante: " + partidoDto.getNomEquipoVis() +
                            ", Precio: " + partidoDto.getPrecio());
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else if("-findPurchases".equalsIgnoreCase(args[0])) {
            //[findPurchases] findPurchases <userEmail>
            validateArgs(args, 2, new int[]{});

            try {
                List<ClientCompraEntradaDto> compras = clientPartidoService.ComprasUsuario(args[1]);
                for (int i = 0; i < compras.size(); i++) {
                    ClientCompraEntradaDto compraDto = compras.get(i);
                    System.out.println("Datos de la compra con id: " + compraDto.getIdCompra() + ":\n");
                    System.out.println("IdPartido: " + compraDto.getIdPartido() +
                            ", Entradas: " + compraDto.getUnidades() +
                            ", Tarjeta acabada en: " + compraDto.getNumTarjetaBancaria() +
                            ", Recogidas: " + compraDto.getrecogida());
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }
    }

    public static void validateArgs(String[] args, int expectedArgs,
                                    int[] numericArguments) {
        if(expectedArgs != args.length) {
            printUsageAndExit();
        }
        for(int i = 0 ; i< numericArguments.length ; i++) {
            int position = numericArguments[i];
            try {
                Double.parseDouble(args[position]);
            } catch(NumberFormatException n) {
                printUsageAndExit();
            }
        }
    }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println("Usage:\n" +
                "    [addMatch]    AppServiceClient -addMatch <visitor> <price> <maxTickets> <celebrationdate>\n" +
                "    [buy] AppServiceClient -buy <matchId> <userEmail> <numTickets> <cardNumber> \n" +
                "    [collect] AppServiceClient -collect <purchaseId> <cardNumber> \n" +
                "    [findMatches]   AppServiceClient -findMatches <untilDay>\n" +
                "    [findMatch]   AppServiceClient -findMatch <matchId>\n" +
                "    [findPurchases]   AppServiceClient -findPurchases <userEmail>\n");

    }
}
