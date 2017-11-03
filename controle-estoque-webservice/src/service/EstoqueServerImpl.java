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
