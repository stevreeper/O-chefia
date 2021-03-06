import org.json.JSONObject;

public class UsuarioRestaurante extends Usuario implements JsonFormatter, ModelObject{
    private Restaurante restaurante;

    public UsuarioRestaurante(Restaurante restaurante, Usuario usuario) throws ExceptionUsuario{
        super(usuario.getNome(), usuario.getSenha(), usuario.getEmail(), usuario.getCpf(), usuario.getTelefone());
        setRestaurante(restaurante);
    }

    public UsuarioRestaurante(){
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public Usuario getUsuario() throws Exception{
        return new Usuario(super.getNome(), super.getSenha(), super.getEmail());
    }

    @Override
    public JSONObject toJson(){
        JSONObject obj = new JSONObject();
        obj.put("restaurante", restaurante.toJson());
        obj.put("usuario", super.toJson());
        return obj;
    }
}
