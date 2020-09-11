package br.com.neto.adauto.model;

import br.com.neto.adauto.commom.ReceivePackegeThread;
import br.com.neto.adauto.exception.ApplicationException;
import br.com.neto.adauto.service.ConnectionService;
import br.com.neto.adauto.service.EmitterService;

public class Router {

    private EmitterService emitterService;

    private Integer port;

    private RoutingTable routingTable;

    private Connection connection;

    private ReceivePackegeThread receiveThread;

    public Router() {
        this.routingTable = new RoutingTable();
        this.receiveThread = new ReceivePackegeThread();
        this.emitterService = new EmitterService();
    }

    public void start() throws ApplicationException {
        if (port == null) {
            throw new ApplicationException("Porta nao especificada");
        }
        this.receiveThread.setConnection(this.connection);
        this.receiveThread.setRouter(this);
        this.receiveThread.start();
    }

    public void stop() {
        this.receiveThread.setRunning(false);
        ConnectionService.closeConnection(getPort());
    }

    public void processReceipt(Package packageReceive) throws ApplicationException {
        packageReceive.decreaseTLL();
        // Caso o pacote exceda o TTL, sera descartado
        if (packageReceive.getTll() == 0) {
            String message = "TTL excedido, descartando pacotes para " + packageReceive.getDestinationAddress();
            printMessage(message);
            return;
        }

        Redirection redirection = routingTable.findRouterToForward(packageReceive.getDestinationAddress());

        // Nao encontrado na tabela de roteamento
        if (redirection == null) {
            String message = "Destino " + packageReceive.getDestinationAddress() + " nao encontrado na tabela de roteamento, descartando pacotes";
            printMessage(message);
            return;
        }

        // Roteamento direto
        if (redirection.isDirect()) {
            String message = "Destino alcançado. A partir de " + packageReceive.getSourceAddress()
                    + " para " + packageReceive.getDestinationAddress()
                    + " : " + packageReceive.getMessage();
            printMessage(message);
            return;
        }

        // Destinado a outro roteador
        if (!redirection.isDirect()) {
            emitterService.emitPackage(redirection.getGateway(), redirection.getInterfaceOutput(), packageReceive);
            String message = "Pacote de encaminhamento para " + packageReceive.getDestinationAddress()
                    + " para o proximo salto " + redirection.getGateway()
                    + " atraves da interface " + redirection.getInterfaceOutput();
            printMessage(message);
        }
    }

    private void printMessage(String message) {
        System.out.println("Roteador " + getPort() + " ---> " + message);
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public RoutingTable getRoutingTable() {
        return routingTable;
    }

    public void setRoutingTable(RoutingTable routingTable) {
        this.routingTable = routingTable;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public ReceivePackegeThread getReceiveThread() {
        return receiveThread;
    }

    public void setReceiveThread(ReceivePackegeThread receiveThread) {
        this.receiveThread = receiveThread;
    }
}
