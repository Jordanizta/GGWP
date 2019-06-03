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

@ManagedBean(name = "cliente")
@RequestScoped
public class Cliente {
	private String nome;
	private int idcliente;
	private String telefone;
	private String endereco;
	private ArrayList clientes;
	private Connection connection;
	private Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getIdcliente() {
		return idcliente;
	}

	public void setIdcliente(int idcliente) {
		this.idcliente = idcliente;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
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

	public String salvaCliente() {
		boolean result = false;

		try {
			connection = getConnection();

			PreparedStatement prepared = connection
					.prepareStatement("INSERT INTO Cliente(nomecliente,telefone,endereco) VALUES (?,?,?)");

			prepared.setString(1, nome);
			prepared.setString(2, telefone);
			prepared.setString(3, endereco);

			result = prepared.execute();

			connection.close();
		} catch (Exception e) {
			System.out.println(e);
		}

		return "cliente.xhtml";
	}

	public String atualizarDados(int idcliente) {
		try {
			connection = getConnection();

			PreparedStatement prepared = connection
					.prepareStatement("SELECT * from Cliente where idcliente = " + idcliente);

			ResultSet result = prepared.executeQuery();

			result.next();

			Cliente cliente = new Cliente();

			cliente.setIdcliente(result.getInt("idcliente"));
			cliente.setNome(result.getString("nomecliente"));
			cliente.setTelefone(result.getString("telefone"));
			cliente.setEndereco(result.getString("endereco"));

			sessionMap.put("clienteEdit", cliente);

			connection.close();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}

		return "atualizarcliente.xhtml";
	}

	public String updateCliente(Cliente cliente) {
		try {
			connection = getConnection();

			PreparedStatement prepared = connection.prepareStatement(
					"UPDATE Cliente SET nomecliente = ? ,telefone = ?,endereco = ? where idcliente = ?");

			prepared.setString(1, cliente.getNome());
			prepared.setString(2, cliente.getTelefone());
			prepared.setString(3, cliente.getEndereco());
			prepared.setInt(4, cliente.getIdcliente());

			prepared.executeUpdate();

			connection.close();
		} catch (Exception e) {
			System.out.println(e);
		}

		return "cliente.xhtml";
	}

	public ArrayList obterCliente() {
		try {
			clientes = new ArrayList<>();

			connection = getConnection();

			PreparedStatement prepared = connection.prepareStatement("select * from Cliente");

			ResultSet result = prepared.executeQuery();

			while (result.next()) {
				Cliente cliente = new Cliente();

				cliente.setNome(result.getString("nomecliente"));
				cliente.setIdcliente(result.getInt("idcliente"));
				cliente.setTelefone(result.getString("telefone"));
				cliente.setEndereco(result.getString("endereco"));

				clientes.add(cliente);
			}

			connection.close();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}

		return clientes;
	}

	public String excluirCliente(int idcliente) {
		try {
			connection = getConnection();

			PreparedStatement prepared = connection.prepareStatement("DELETE from Cliente where idcliente = " + idcliente);

			prepared.executeUpdate();

			connection.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
		return "clientes.xhtml";
	}

}
