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
	public Response reporProduto(@WebParam(name = "codigo") String codigo,
			@WebParam(name = "quantidade") int quantidade);

	@WebMethod
	public Produto consultarProduto(@WebParam(name = "codigo") String codigo);

	@WebMethod
	public Response adicionarProduto(@WebParam(name = "codigo") String codigo,
			@WebParam(name = "quantidade") int quantidade, @WebParam(name = "descricao") String descricao);

	@WebMethod
	public Response realizarVenda(@WebParam(name = "codigo") String codigo,
			@WebParam(name = "quantidade") int quantidade);
}
