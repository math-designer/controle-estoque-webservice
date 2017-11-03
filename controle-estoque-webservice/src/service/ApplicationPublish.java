package service;

import javax.xml.ws.Endpoint;

public class ApplicationPublish {

	public static void main(String[] args) {
		Endpoint.publish("http://127.0.0.1:5000/estoque", new EstoqueServerImpl());
		System.out.println("Servico rodando em: http://127.0.0.1:5000/estoque");
	}

}
