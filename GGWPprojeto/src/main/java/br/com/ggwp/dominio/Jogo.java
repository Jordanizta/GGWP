package br.com.ggwp.dominio;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.mysql.jdbc.Driver;

@ManagedBean(name = "jogo")
@RequestScoped
public class Jogo {
	private String nomejogo;
	private int idjogo;
	private String estilojogo;
	private String console;
	private float valor;
	private Connection connection;
	private ArrayList jogos;
	private Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

	public String getNomejogo() {
		return nomejogo;
	}

	public void setNomejogo(String nomejogo) {
		this.nomejogo = nomejogo;
	}

	public int getIdjogo() {
		return idjogo;
	}

	public void setIdjogo(int idjogo) {
		this.idjogo = idjogo;
	}

	public String getEstilojogo() {
		return estilojogo;
	}

	public void setEstilojogo(String estilojogo) {
		this.estilojogo = estilojogo;
	}

	public String getConsole() {
		return console;
	}

	public void setConsole(String console) {
		this.console = console;
	}

	public float getValor() {
		return valor;
	}

	public void setValor(float valor) {
		this.valor = valor;
	}

	public Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ggwp", "root", "");
		} catch (Exception e) {
			System.out.println(e);
		}

		return connection;
	}

	public String salvarJogo() {
		boolean result = false;

		try {
			connection = getConnection();

			PreparedStatement prepared = connection
					.prepareStatement("INSERT INTO Jogo(nomejogo,estilojogo,console,valor) VALUES (?,?,?,?)");

			prepared.setString(1, nomejogo);
			prepared.setString(2, estilojogo);
			prepared.setString(3, console);
			prepared.setFloat(4, valor);

			result = prepared.execute();

			connection.close();
		} catch (Exception e) {
			System.out.println(e);
		}

		return "jogo.xhtml";
	}

	public ArrayList mostrarJogo() {
		try {
			jogos = new ArrayList<>();

			connection = getConnection();

			PreparedStatement prepared = connection.prepareStatement("SELECT * from Jogo");

			ResultSet result = prepared.executeQuery();

			while (result.next()) {
				Jogo jogo = new Jogo();

				jogo.setIdjogo(result.getInt("idjogo"));
				jogo.setNomejogo(result.getString("nomejogo"));
				jogo.setEstilojogo(result.getString("estilojogo"));
				jogo.setConsole(result.getString("console"));
				jogo.setValor(result.getFloat("valor"));

				jogos.add(jogo);
			}

			connection.close();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}

		return jogos;
	}

	public String alterarJogo(int idjogo) {
		try {
			connection = getConnection();

			PreparedStatement prepared = connection.prepareStatement("SELECT * from Jogo where idjogo = " + idjogo);

			ResultSet result = prepared.executeQuery();

			result.next();

			Jogo jogo = new Jogo();

			jogo.setIdjogo(result.getInt("idjogo"));
			jogo.setNomejogo(result.getString("nomejogo"));
			jogo.setEstilojogo(result.getString("estilojogo"));
			jogo.setConsole(result.getString("console"));
			jogo.setValor(result.getFloat("valor"));

			sessionMap.put("jogoEdit", jogo);

			connection.close();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}

		return "atualizarjogo.xhtml";
	}

	public String excluirJogo(int idjogo) {
		try {
			connection = getConnection();

			PreparedStatement prepared = connection.prepareStatement("DELETE from Jogo where idjogo = " + idjogo);

			prepared.executeUpdate();

			connection.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
		return "jogo.xhtml";
	}

	public String updateJogo(Jogo jogo) {
		try {
			connection = getConnection();

			PreparedStatement prepared = connection.prepareStatement(
					"UPDATE Jogo SET nomejogo = ? ,estilojogo = ?,console = ?,valor = ? WHERE idjogo = ?");

			prepared.setString(1, jogo.getNomejogo());
			prepared.setString(2, jogo.getEstilojogo());
			prepared.setString(3, jogo.getConsole());
			prepared.setFloat(4, jogo.getValor());
			prepared.setInt(5, jogo.getIdjogo());

			prepared.executeUpdate();

			connection.close();
		} catch (Exception e) {
			System.out.println(e);
		}

		return "jogo.xhtml";
	}

}
