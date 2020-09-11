package br.com.neto.adauto.service;

import br.com.neto.adauto.commom.SendPackageThread;
import br.com.neto.adauto.exception.ApplicationException;
import br.com.neto.adauto.model.Connection;
import br.com.neto.adauto.model.Package;
import br.com.neto.adauto.utils.ScannerUtils;

public class EmitterService {

    public void emit() throws ApplicationException {
        String routerAddress = ScannerUtils.getString("Endereco do roteador default:");
        Integer routerPort = ScannerUtils.getInteger("Porta do roteador default:");
        String sourceAddress = ScannerUtils.getString("Endereco de origem:");
        String destinationAddress = ScannerUtils.getString("Endereco de destino:");
        String message = ScannerUtils.getString("Insira uma mensagem para envio:");
        

        emitCommand(routerAddress, sourceAddress, destinationAddress, routerPort, message);
    }

    public void emitCommand(String command) throws ApplicationException {
        String[] parts = command.split(" ");
        String routerAddress = parts[0];
        Integer routerPort = Integer.valueOf(parts[1]);
        String sourceAddress = parts[2];
        String destinationAddress = parts[3];
        String message = parts[4];

        emitCommand(routerAddress, sourceAddress, destinationAddress, routerPort, message);
    }

    public void emitPackage(String routerAddress, Integer routerPort, Package packageToSend) throws ApplicationException {
        Connection connection = ConnectionService.getConnection(routerPort);
        new SendPackageThread(routerAddress, routerPort, packageToSend, connection).start();
    }

    private void emitCommand(String routerAddress, String sourceAddress, String destinationAddress, Integer routerPort, String message) throws ApplicationException {
        Package packageToSend = new Package(sourceAddress, destinationAddress, routerPort, message);
        Connection connection = ConnectionService.getConnection(routerPort);
        new SendPackageThread(routerAddress, routerPort, packageToSend, connection).start(); 
    }

}
