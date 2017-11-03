package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entities.Produto;

public class ProdutoDao {

	public boolean adicionarProduto(String codigo, String descricao, int quantidade) {
		try (Connection conn = new ConnectionFactory().getConnection()){
			String sql = "insert into produtos(codigo, descricao, quantidade) values(?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, codigo);
			stmt.setString(2, descricao);
			stmt.setInt(3, quantidade);
			stmt.executeUpdate();
			stmt.close();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean vendaProduto(String codigo, int quantidade) {
		Produto prod = this.informacaoProduto(codigo);
		if(prod == null || prod.getQuantidade() < quantidade) {
			return false;
		}
		
		try (Connection conn = new ConnectionFactory().getConnection()){
			String sql = "update produtos set quantidade = quantidade - ? where codigo = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, quantidade);
			stmt.setString(2, codigo);
			int res = stmt.executeUpdate();
			stmt.close();

			return res > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean reporProduto(String codigo, int quantidade) {
		try (Connection conn = new ConnectionFactory().getConnection()){
			String sql = "update produtos set quantidade = quantidade + ? where codigo = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, quantidade);
			stmt.setString(2, codigo);
			int res = stmt.executeUpdate();
			stmt.close();

			return res > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Produto informacaoProduto(String codigo) {
		try (Connection conn = new ConnectionFactory().getConnection()){
			String sql = "select * from produtos where codigo = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, codigo);

			ResultSet rs = stmt.executeQuery();
			Produto prod = null;
			
			if(rs.next()) {
				prod = new Produto();
				prod.setCodigo(rs.getString("codigo"));
				prod.setDescricao(rs.getString("descricao"));
				prod.setQuantidade(rs.getInt("quantidade"));
			}
			
			rs.close();
			stmt.close();
			
			return prod;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
