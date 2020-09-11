package br.com.neto.adauto.service;

import br.com.neto.adauto.exception.ApplicationException;
import br.com.neto.adauto.model.Connection;
import br.com.neto.adauto.model.Redirection;
import br.com.neto.adauto.model.Router;
import br.com.neto.adauto.utils.RouterUtils;
import br.com.neto.adauto.utils.ScannerUtils;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RouterService {

    private Map<Integer, Router> routers = new TreeMap<Integer, Router>();

    public void createRouter() {
        Integer routerPort = ScannerUtils.getInteger("Porta do roteador default:");
        String routersForRedirection = ScannerUtils.getString("Enderecos de encaminhamento(separados por espaco):");
        String[] routersForRedirectionInParts = routersForRedirection.split(" ");
        List<Redirection> redirections = RouterUtils.formattingRoutingTableFromCommand(routersForRedirectionInParts);
        createRouter(routerPort, redirections);
    }

    public void createRouterCommand(String command) {
        String[] parts = command.split(" ");
        Integer routerPort = Integer.valueOf(parts[0]);
        List<Redirection> redirections = RouterUtils.formattingRoutingTableFromCommand(parts);
        createRouter(routerPort, redirections);
    }

    private void createRouter(Integer port, List<Redirection> redirections) {
        try {
            if (routers.get(port) != null) {
                ScannerUtils.showMessage("Ja existe um roteador em execucao nessa porta!!!");
                return;
            }
            Router router = new Router();
            router.setPort(port);
            router.getRoutingTable().addAllRedirections(redirections);
            Connection connection = ConnectionService.getConnection(router.getPort());
            router.setConnection(connection);
            router.start();
            routers.put(router.getPort(), router);
            ScannerUtils.showMessage("Roteador criado!!!");
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }

    public void routerList() {
        String leftAlignFormat = "| %-15s | %-14s |%n";
        System.out.format("+-----------------+----------------+%n");
        System.out.format("|Porta do Roteador|Redirecionamento|%n");
        System.out.format("+-----------------+----------------+%n");
       
        if (!routers.isEmpty()) {
            for (Map.Entry<Integer, Router> entry : routers.entrySet()) {
                System.out.format(leftAlignFormat, entry.getValue().getPort(), entry.getValue().getRoutingTable().getRedirections().size());
            }
        } else {
            System.out.format("|Nenhum roteador executando        |%n");
        }
        System.out.format("+-----------------+----------------+%n");
    }

    public void routerDetail(Integer routerPort) {
        if (routerPort == null) {
            routerPort = ScannerUtils.getInteger("Porta do roteador que deseja ver:");
        }
        Router router = routers.get(routerPort);
        if (router == null) {
                ScannerUtils.showMessage("Nenhum roteador na porta!");
            return;
        }
        String leftAlignFormat = "| %-15s | %-14s | %-14s | %-15s |%n";
        System.out.format("+-----------------+----------------+----------------+-----------------+%n");
        System.out.format("| Destino         | Mascara        | Gateway        | IOutput         |%n");
        System.out.format("+-----------------+----------------+----------------+-----------------+%n");
        if (router.getRoutingTable().getRedirections().size() > 0) {
            for (Redirection redirection : router.getRoutingTable().getRedirections()) {
                System.out.format(leftAlignFormat, redirection.getDestiny(), RouterUtils.formatMaskCIDRNotation(redirection.getMask()), redirection.getGateway(), redirection.getInterfaceOutput());
            }
        } else {
            System.out.format("| Nenhum redirecionamento neste roteador                            |%n");
        }
        System.out.format("+-----------------+----------------+----------------+-----------------+%n");
    }
    public void routerListDetail() {
        if (!routers.isEmpty()) {
            for (Map.Entry<Integer, Router> entry : routers.entrySet()) {
                Router router = routers.get(entry.getValue().getPort());
                
                String leftAlignFormat = "| %-15s | %-14s | %-14s | %-15s |%n";
                System.out.format("+---------------------------------------------------------------------+%n");
                System.out.format("|" + entry.getValue().getPort() + "                                                                 |%n");
                
                System.out.format("+-----------------+----------------+----------------+-----------------+%n");
                System.out.format("| Destino         | Mascara        | Gateway        | IOutput         |%n");
                System.out.format("+-----------------+----------------+----------------+-----------------+%n");
                if (router.getRoutingTable().getRedirections().size() > 0) {
                    for (Redirection redirection : router.getRoutingTable().getRedirections()) {
                        System.out.format(leftAlignFormat, redirection.getDestiny(), RouterUtils.formatMaskCIDRNotation(redirection.getMask()), redirection.getGateway(), redirection.getInterfaceOutput());
                    }
                } else {
                    System.out.format("| Nenhum redirecionamento neste roteador                            |%n");
                }
                System.out.format("+-----------------+----------------+----------------+-----------------+%n");
                    }
        } else {
            System.out.format("|Nenhum roteador executando        |%n");
        }
    }
    
    public void stopRouter(Integer routerPort) {
        if (routerPort == null) {
            routerPort = ScannerUtils.getInteger("Porta do roteador a ser parado:");
        }
        Router router = routers.get(routerPort);
        if (router == null) {
            ScannerUtils.showMessage("Roteador não encontrado!");
            return;
        }
        router.stop();
        routers.remove(routerPort);
        ScannerUtils.showMessage("Roteador parado!");
    }

}
