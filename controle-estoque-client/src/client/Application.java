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
