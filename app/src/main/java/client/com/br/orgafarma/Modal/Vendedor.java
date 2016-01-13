package client.com.br.orgafarma.Modal;

/**
 * Created by Felipe on 20/11/2015.
 */
public class Vendedor {

    private String  Nome;

    private  String email;

    public Vendedor(){

    }

    public Vendedor(String nome) {
        Nome = nome;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
