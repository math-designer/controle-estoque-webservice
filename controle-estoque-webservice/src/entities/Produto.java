package entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Produto {
	private String codigo;
	private int quantidade;
	private String descricao;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return "[" + this.codigo + "]" + " Descricao: " + this.descricao + "; " + " Quantidade: " + this.quantidade;
	}

	@Override
	public boolean equals(Object obj) {
		Produto otherProduct = (Produto) obj;
		return this.getCodigo() != otherProduct.getCodigo();
	}
}
