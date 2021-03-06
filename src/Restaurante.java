
import java.io.*;
import java.lang.reflect.Array;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import org.json.JSONArray;
import org.json.JSONObject;

import static java.util.stream.Collectors.toList;


public class Restaurante implements JsonFormatter, ModelObject {
    private String nome;
    private String cnpj;
    private Endereco endereco;
    private int numMesas;
    private String telefone;
    private List<Prato> cardapio;
    private List<Pedido> historicoDePedidos;
    private Queue<Pedido> pedidosAbertos;

    public Restaurante(String nome, String cnpj, Endereco endereco, int numMesas, String telefone) throws ExceptionRestaurante {
        setNome(nome);
        setCnpj(cnpj);
        setEndereco(endereco);
        setNumMesas(numMesas);
        setCardapio(new ArrayList<>());
        setPedidosAbertos(new ArrayDeque<Pedido>());
        setHistoricoDePedidos(new ArrayList<>());
        setTelefone(telefone);
    }

    public Restaurante() {
    }

    //Pegar nome restaurante
    public String getNome() {
        return nome;
    }

    //Colocar nome no restaurante
    public void setNome(String nome) throws ExceptionRestaurante {
        if (nome != null && !nome.equals(""))
            this.nome = nome;
        else
            throw new ExceptionRestaurante("Nome invalido");
    }

    //Pegar cnpj do restaurante
    public String getCnpj() {
        return cnpj;
    }

    //Colocar cnpj no restaurante
    public void setCnpj(String cnpj) throws ExceptionRestaurante {
        if (cnpj != null && !cnpj.equals(""))
            this.cnpj = cnpj;
        else
            throw new ExceptionRestaurante("CNPJ invalido");
    }

    //Pegar endereco do restaurante
    public Endereco getEndereco() {
        return endereco;
    }

    //Colocar endereco no restaurante
    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    //Pegar numero de mesas do restaurante
    public int getNumMesas() {
        return numMesas;
    }

    //Colocar numero de mesas do restaurante
    public void setNumMesas(int numMesas) throws ExceptionRestaurante {
        if (numMesas > 0)
            this.numMesas = numMesas;
        else
            throw new ExceptionRestaurante("Numero de mesas invalido");
    }

    //Pegar cardapio
    public List<Prato> getCardapio() {
        return cardapio;
    }

    //Colocar cardapio
    private void setCardapio(List<Prato> cardapio) {
        if (cardapio != null)
            this.cardapio = cardapio;
        else
            this.cardapio = new ArrayList();
    }

    //Adicionar prato ao card�pio
    public void addPratoCardapio(Prato prato) throws ExceptionRestaurante {
        if (prato != null)
            this.cardapio.add(prato);
        else
            throw new ExceptionRestaurante("Prato invalido");
    }

    //Colocar pedidos abertos
    private void setPedidosAbertos(Queue pedidosAbertos) {
        this.pedidosAbertos = pedidosAbertos;
    }

    //Adicionar pedido aos abertos
    public void addPedidoAberto(Pedido pedido) throws ExceptionRestaurante {
        if (pedido != null)
            pedidosAbertos.add(pedido);
        else
            throw new ExceptionRestaurante("Pedido invalido");
    }

    //Pegar pedido em aberto
    public Pedido getPedidoAberto() {
        return pedidosAbertos.peek();
    }

    //Pegar todos os pedidos abertos
    public List<Pedido> getPedidos() {
        return new ArrayList<>(pedidosAbertos);
    }

    //Fechar primeiro pedido e adiciona-lo ao historico de pedido
    public Pedido fechaPrimeiroPedido() {
        historicoDePedidos.add(pedidosAbertos.peek());
        return pedidosAbertos.remove();
    }

    //Pegar historio de pedido
    public List<Pedido> getHistoricoDePedidos() {
        return historicoDePedidos;
    }

    //Colocar historico de pedido
    private void setHistoricoDePedidos(List<Pedido> historicoDePedidos) {
        this.historicoDePedidos = historicoDePedidos;
    }

    //Converte o carpadio em um vetor JSON
    public JSONArray cardapioToJsonArray() {
        JSONArray array = new JSONArray();
        for (int i = 0; i < cardapio.size(); i++) {
            array.put(cardapio.get(i).toJson());
        }
        return array;
    }

    //Conveter o historico de pedidos em um vetor JSON
    public JSONArray historicoToJsonArray() {
        JSONArray array = new JSONArray();
        for (int i = 0; i < historicoDePedidos.size(); i++) {
            array.put(historicoDePedidos.get(i).toJson());
        }
        return array;
    }

    public JSONArray pedidosAbertoToJsonArray() {
        JSONArray array = new JSONArray(pedidosAbertos);
        return array;
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("nome", this.nome);
        obj.put("cnpj", this.cnpj);
        obj.put("endereco", this.endereco.toJson());
        obj.put("numMesas", this.numMesas);
        obj.put("telefone", this.telefone);
        obj.put("cardapio", this.cardapioToJsonArray());
        obj.put("historicoDePedidos", this.historicoToJsonArray());
        obj.put("pedidosAbertos", pedidosAbertoToJsonArray());
        return obj;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public List<Prato> getPratosPrincipais() {
        return cardapio.stream()
                .filter((p) -> p.getTipo() == 1)
                .sorted(Comparator.comparingDouble(Prato::getPreco))
                .collect(toList());
    }

    public Prato getPratoPeloNome(String nome) {
        for (Prato p : cardapio)
            if (p.getNome().equals(nome))
                return p;
        return null;
    }

    public Pedido getPedidoPeloPreco(float preco) {
        for (Pedido p : pedidosAbertos)
            if (p.getPrecoFinal() == preco)
                return p;
        return null;
    }

    public Queue<Pedido> getPedidosAbertos() {
        return pedidosAbertos;
    }

    public void updatePedido(Pedido pedido){
        for (int i = 0; i < pedidosAbertos.size(); i++){
            if (pedido.getHora().equals(pedidosAbertos.peek())){
                pedidosAbertos.remove();
                pedidosAbertos.add(pedido);
                this.pedidosAbertos = pedidosAbertos.stream()
                        .sorted(Comparator.comparing(Pedido::getPrecoFinal))
                        .collect(Collectors.toCollection(ArrayDeque::new));
            }else{
                pedidosAbertos.add(pedidosAbertos.remove());
            }
        }
    }
}