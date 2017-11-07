# Implementando um Webservice em JAVA
---

## Criando o serviço
Em java, os serviços web são definidos por classes. As operações disponíveis pelo serviço são definidos por métodos de classe.
Nesse simples exemplo implementaremos um [Webservice](https://pt.wikipedia.org/wiki/Web_service) baseado em [SOAP.](https://pt.wikipedia.org/wiki/SOAP) que faz o controle de estoque de produtos. Este serviço possui as seguintes operações:
- Incluir produto
- Realizar venda
- Repor estoque
- Consultar produto

Para começar, precisamos criar uma interface definindo as operações que o nosso serviço possui, conhecida como Service Endpoint Interface (SEI). Criamos então a interface EstoqueServer.

```java
package service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import entities.Response;
import entities.Produto;

@WebService
@SOAPBinding(style = Style.RPC)
public interface EstoqueServer {
	@WebMethod
	public Response reporProduto(@WebParam(name = "codigo") String codigo, @WebParam(name = "quantidade") int quantidade);

	@WebMethod
	public Produto consultarProduto(@WebParam(name = "codigo") String codigo);

	@WebMethod
	public Response adicionarProduto(@WebParam(name = "codigo") String codigo, @WebParam(name = "quantidade") int quantidade, @WebParam(name = "descricao") String descricao);

	@WebMethod
	public Response realizarVenda(@WebParam(name = "codigo") String codigo, @WebParam(name = "quantidade") int quantidade);
}
```
A anotação @WebService informa ao compilador que este é um serviço web, nossa SEI. @SOAPBinding indica que o serviço é basendo em SOAP. @WebMethod, como pode-se imaginar indica que o método é uma operação disponíbilizada pelo serviço. Por último, mas não menos importante, está @WebParam que diz o nome do parâmetro que a operação recebe.

Com a nossa interface criada, agora precissamos criar sua implementação, conhecida como Service Implementation Bean (SIB). Criaremos então a classe EstoqueServerImpl.
 ```java
 package service;

import entities.Produto;
import entities.Response;

import javax.jws.WebService;

import dao.ProdutoDao;

@WebService(endpointInterface = "service.EstoqueServer")
public class EstoqueServerImpl implements EstoqueServer {

	private ProdutoDao prodDao;

	public EstoqueServerImpl() {
		this.prodDao = new ProdutoDao();
	}

	private Response buildResponse(String mensagem, int codigo) {
		Response response = new Response();
		response.setCodigo(codigo);
		response.setMensagem(mensagem);
		return response;
	}

	public Response reporProduto(String codigo, int quantidade) {
		if (this.prodDao.reporProduto(codigo, quantidade)) {
			return this.buildResponse("Produto reposto.", Response.STATUS_OK);
		}
		return this.buildResponse("Falha ao repor produto.", Response.STATUS_ERROR);
	}

	public Response realizarVenda(String codigo, int quantidade) {
		if (this.prodDao.vendaProduto(codigo, quantidade)) {
			return this.buildResponse("Venda realizada.", Response.STATUS_OK);
		}
		return this.buildResponse("Falha ao realizar venda.", Response.STATUS_ERROR);
	}

	public Produto consultarProduto(String codigo) {
		Produto prod = this.prodDao.informacaoProduto(codigo);

		if (prod == null) {
			prod = new Produto();
			prod.setCodigo("-999");
			prod.setQuantidade(0);
			prod.setDescricao("sem descricao");
		}
		return prod;
	}

	public Response adicionarProduto(String codigo, int quantidade, String descricao) {
		if (this.prodDao.adicionarProduto(codigo, descricao, quantidade)) {
			return this.buildResponse("Produto adicionado.", Response.STATUS_OK);
		}
		return this.buildResponse("Falha ao adicionar produto.", Response.STATUS_ERROR);
	}

}
 ```
A primeira coisa a ser notada aqui é a propriedade endpointInterface da anotação @WebService. Ela diz qual a interface (SEI) que nossa classe implementa.
Todas as operações de inserção, *reporProduto*, *realizarVenda* e *adicionarProduto* retornam um objeto do tipo Response, que contém somente o código do status e a mensagem da operação. A única operação de consulta, *consultarProduto*, retorna um objeto do tipo Product, que contém as informações do produto em si. Se o produto não for encontrado o objeto é criado com as informações 'default'.
O método *buildResponse* serve apenas para evitar a duplicação de código para  a criação do objeto Response.

Utilizamos a classe ProductDao para separarmos a lógica de acesso ao banco de dados do nosso serviço. Como o foco aqui é apenas a criação do serviço, não entraremos nos detalhes dessa classe.

Com o serviço criado, agora precisamos publica-lo. Para isso temos a classe ApplicationPublish.

```java
package service;

import javax.xml.ws.Endpoint;

public class ApplicationPublish {
	public static void main(String[] args) {
		Endpoint.publish("http://127.0.0.1:5000/estoque", new EstoqueServerImpl());
		System.out.println("Servico rodando em: http://127.0.0.1:5000/estoque");
	}
}

```
O código é bem simples. Atraves do método *publish* da classe Endpoint informamos qual o endereço e a porta que o nosso serviço estará disponível. Informamos também a instância do nosso SIB, que é EstoqueServerImpl.

Se rodarmos este código e acessarmos o endereço http://127.0.0.1:5000/estoque?wsdl veremos o [WSDL](https://pt.wikipedia.org/wiki/Web_Services_Description_Language) do nosso serviço.

## Usando o serviço

O java dispõe de uma ferramenta que nos ajuda a criar um cliente para um serviço web. Vamos ao cmd do windows (ou terminal do linux) e digitar `wsimport http://127.0.0.1:5000/estoque?wsdl`. Esse comando gera as classes que precisamos para acessar as operações do serviço. Para o nosso exemplo as classes que nos interessam são:
- EstoqueServer.class
- EstoqueServerImplService.class
- Produto.class
- Response.class

Agora precisamos criar a nossa classe cliente, temos a classe Client

```java
package client;

import service.EstoqueServer;
import service.EstoqueServerImplService;

public class Client {
	public static EstoqueServer accessService() {
		EstoqueServerImplService end = new EstoqueServerImplService();
		return end.getEstoqueServerImplPort();
	}
}
```

Estamos instânciando o nosso serviço. A classe é de mesmo nome do nosso SIB mais a palavra 'Service'. É através do método `getEstoqueServerImplPort` que acessamos os métodos que o serviço possui. Então para interagir com o serviço bastaria:
```java
end.getEstoqueServerImplPort().consultarProduto(codigo);
end.getEstoqueServerImplPort().realizarVenda(codigo, quantidade);
//...
```

Para uma melhor interação com o serviço, temos a classe Application, como um menu interativo para interagir com o serviço que acabamos de criar.

```java
package client;

import java.util.Scanner;

import service.Produto;
import service.Response;

public class Application {
	public static Scanner scan = new Scanner(System.in);

	public static void main(String[] args) {
		int op;

		do {
			System.out.println("\n########### MENU ############");
			System.out.println(
					"[1] - Adicionar produto \n[2] - Consultar produto \n[3] - Realizar venda \n[4] - Repor estoque");
			System.out.print("Opcao: ");

			op = scan.nextInt();
			scan.nextLine();

			switch (op) {
			case 1:
				adicionarProduto();
				break;
			case 2:
				consultarProduto();
				break;
			case 3:
				relizarVenda();
				break;
			case 4:
				reporEstoque();
				break;
			case 0:
				System.out.println("Programa finalizado");
				break;
			default:
				System.out.println("Opcao invalida");
			}
		} while (op != 0);
	}

	private static void adicionarProduto() {
		System.out.print("Informe a descricao do produto: ");
		String descricao = scan.nextLine();

		System.out.print("Informe o código do produto: ");
		String codigo = scan.nextLine();

		System.out.print("Informe a quantidade do produto: ");
		int quantidade = scan.nextInt();
		scan.nextLine();

		Response response = Client.accessService().adicionarProduto(codigo, quantidade, descricao);
		printResponse(response);
	}

	private static void consultarProduto() {
		System.out.print("Informe o código do produto: ");
		String codigo = scan.nextLine();

		Produto produto = Client.accessService().consultarProduto(codigo);
		if(produto.getCodigo().compareTo("-999") != 0) {
			System.out.println("Codigo: " + produto.getCodigo());
			System.out.println("Descricao: " + produto.getDescricao());
			System.out.println("Quantidade: " + produto.getQuantidade());
		} else {
			System.out.println("[ERRO] Produto nao encontrado");
		}
		
	}

	private static void relizarVenda() {
		System.out.print("Informe o código do produto: ");
		String codigo = scan.nextLine();

		System.out.print("Informe a quantidade do produto: ");
		int quantidade = scan.nextInt();
		scan.nextLine();

		Response response = Client.accessService().realizarVenda(codigo, quantidade);
		printResponse(response);
	}

	private static void reporEstoque() {
		System.out.print("Informe o código do produto: ");
		String codigo = scan.nextLine();

		System.out.print("Informe a quantidade do produto: ");
		int quantidade = scan.nextInt();
		scan.nextLine();

		Response response = Client.accessService().reporProduto(codigo, quantidade);
		printResponse(response);
	}
	
	private static void printResponse(Response resp) {
		if(resp.getCodigo() == 100) {
			System.out.println("[OK] " + resp.getMensagem());
		} else {
			System.out.println("[ERRO] " + resp.getMensagem());
		}
	}

}
```

Acho que o código é alto explicativo e dispensa comentários.