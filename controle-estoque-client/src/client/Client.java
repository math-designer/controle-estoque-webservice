package client;

import service.EstoqueServer;
import service.EstoqueServerImplService;

public class Client {
	public static EstoqueServer accessService() {
		EstoqueServerImplService end = new EstoqueServerImplService();
		
		return end.getEstoqueServerImplPort();
	}
}
