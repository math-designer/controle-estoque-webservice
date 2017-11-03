
package service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "EstoqueServer", targetNamespace = "http://service/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface EstoqueServer {


    /**
     * 
     * @param codigo
     * @param quantidade
     * @param descricao
     * @return
     *     returns service.Response
     */
    @WebMethod
    @WebResult(partName = "return")
    @Action(input = "http://service/EstoqueServer/adicionarProdutoRequest", output = "http://service/EstoqueServer/adicionarProdutoResponse")
    public Response adicionarProduto(
        @WebParam(name = "codigo", partName = "codigo")
        String codigo,
        @WebParam(name = "quantidade", partName = "quantidade")
        int quantidade,
        @WebParam(name = "descricao", partName = "descricao")
        String descricao);

    /**
     * 
     * @param codigo
     * @param quantidade
     * @return
     *     returns service.Response
     */
    @WebMethod
    @WebResult(partName = "return")
    @Action(input = "http://service/EstoqueServer/reporProdutoRequest", output = "http://service/EstoqueServer/reporProdutoResponse")
    public Response reporProduto(
        @WebParam(name = "codigo", partName = "codigo")
        String codigo,
        @WebParam(name = "quantidade", partName = "quantidade")
        int quantidade);

    /**
     * 
     * @param codigo
     * @return
     *     returns service.Produto
     */
    @WebMethod
    @WebResult(partName = "return")
    @Action(input = "http://service/EstoqueServer/consultarProdutoRequest", output = "http://service/EstoqueServer/consultarProdutoResponse")
    public Produto consultarProduto(
        @WebParam(name = "codigo", partName = "codigo")
        String codigo);

    /**
     * 
     * @param codigo
     * @param quantidade
     * @return
     *     returns service.Response
     */
    @WebMethod
    @WebResult(partName = "return")
    @Action(input = "http://service/EstoqueServer/realizarVendaRequest", output = "http://service/EstoqueServer/realizarVendaResponse")
    public Response realizarVenda(
        @WebParam(name = "codigo", partName = "codigo")
        String codigo,
        @WebParam(name = "quantidade", partName = "quantidade")
        int quantidade);

}
