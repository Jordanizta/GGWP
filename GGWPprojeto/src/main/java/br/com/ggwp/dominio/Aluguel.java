package br.com.ggwp.dominio;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "aluguel")
@RequestScoped
public class Aluguel {
	private int idaluguel;
	private int idcliente;
	private String nomejogo;
	private String nomecliente;
	private float valor;
	private Connection conexao;
	private Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	private String diaaluguel;
	private String devolucao;
	private ArrayList alugueis;
	// private Cliente cliente;
	// private List<ItemVenda> aluguel;

	public int getIdaluguel() {
		return idaluguel;
	}

	public void setIdaluguel(int idaluguel) {
		this.idaluguel = idaluguel;
	}

	public int getIdcliente() {
		return idcliente;
	}

	public void setIdcliente(int idcliente) {
		this.idcliente = idcliente;
	}

	public String getNomejogo() {
		return nomejogo;
	}

	public void setNomejogo(String nomejogo) {
		this.nomejogo = nomejogo;
	}

	public String getNomecliente() {
		return nomecliente;
	}

	public void setNomecliente(String nomecliente) {
		this.nomecliente = nomecliente;
	}

	public float getValor() {
		return valor;
	}

	public void setValor(float valor) {
		this.valor = valor;
	}

	public String getDiaaluguel() {
		return diaaluguel;
	}

	public void setDiaaluguel(String diaaluguel) {
		this.diaaluguel = diaaluguel;
	}

	public String getDevolucao() {
		return devolucao;
	}

	public void setDevolucao(String devolucao) {
		this.devolucao = devolucao;
	}

	public Connection getConexao() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conexao = DriverManager.getConnection("jdbc:mysql://localhost:3306/ggwp", "root", "");
		} catch (Exception e) {
			System.out.println(e);
		}

		return conexao;
	}

	public String salvarAluguel() {
		boolean result = false;

		try {
			conexao = getConexao();

			PreparedStatement prepara = conexao.prepareStatement(
					"INSERT INTO Aluguel (nomejogo, nomecliente, diaaluguel, devolucao) VALUES (?,?,?,?)");

			prepara.setString(1, nomejogo);
			prepara.setString(2, nomecliente);
			prepara.setString(3, diaaluguel);
			prepara.setString(4, devolucao);

			result = prepara.execute();

			conexao.close();
		} catch (Exception e) {
			System.out.println(e);
		}

		return "aluguel.xhtml";
	}

	public ArrayList obterAlugueis() {
		try {
			alugueis = new ArrayList<>();

			conexao = getConexao();

			PreparedStatement prepara = conexao.prepareStatement("SELECT * from Aluguel");

			ResultSet resulta = prepara.executeQuery();

			while (resulta.next()) {
				Aluguel aluguel = new Aluguel();

				aluguel.setNomejogo(resulta.getString("nomejogo"));
				aluguel.setNomecliente(resulta.getString("nomecliente"));
				aluguel.setDiaaluguel(resulta.getString("diaaluguel"));
				aluguel.setDevolucao(resulta.getString("devolucao"));

				alugueis.add(aluguel);
			}

			conexao.close();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}

		return alugueis;
	}

	public String atualizarDados(int idaluguel) {
		try {
			conexao = getConexao();

			PreparedStatement prepara = conexao
					.prepareStatement("SELECT * from Aluguel where idaluguel = " + idaluguel);

			ResultSet resulta = prepara.executeQuery();

			resulta.next();

			Aluguel aluguel = new Aluguel();

			aluguel.setIdaluguel(resulta.getInt("idaluguel"));
			aluguel.setNomejogo(resulta.getString("nomejogo"));
			aluguel.setNomecliente(resulta.getString("nomecliente"));
			aluguel.setDiaaluguel(resulta.getString("diaaluguel"));
			aluguel.setDevolucao(resulta.getString("devolucao"));

			sessionMap.put("aluguelEdit", aluguel);

			conexao.close();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}

		return "atualizaraluguel.xhtml";
	}
	
	public String updateAluguel(Aluguel aluguel) {
		try {
			conexao = getConexao();

			PreparedStatement prepara = conexao.prepareStatement(
					"UPDATE Aluguel SET nomejogo = ? ,nomecliente = ?,diaaluguel = ?, devolucao = ?, where idaluguel = ?");

			prepara.setString(1, aluguel.getNomejogo());
			prepara.setString(2, aluguel.getNomecliente());
			prepara.setString(3, aluguel.getDiaaluguel());
			prepara.setString(4, aluguel.getDevolucao());

			prepara.executeUpdate();

			conexao.close();
		} catch (Exception e) {
			System.out.println(e);
		}

		return "aluguel.xhtml";
	}
	
	public String excluirAluguel(int idaluguel) {
		try {
			conexao = getConexao();

			PreparedStatement prepared = conexao.prepareStatement("DELETE from Aluguel where idaluguel = " + idaluguel);

			prepared.executeUpdate();

			conexao.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
		return "aluguel.xhtml";
	}
}
