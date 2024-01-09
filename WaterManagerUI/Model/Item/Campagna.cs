namespace WaterManagerUI.Model.Item;

public class Campagna
{
    public int id { get; set; }
    public String nome { get; set; }
    public HashSet<Campo> campi { get; set; }
    public int idAzienda { get; set; }


    public Campagna(String nome, HashSet<Campo> campi, int idAzienda)
    {
        this.nome = nome;
        this.campi = campi;
        this.idAzienda = idAzienda;
        this.campi = new HashSet<Campo>();
    }


    public Campagna(int id, String nome, int idAzienda)
    {
        this.id = id;
        this.nome = nome;
        this.idAzienda = idAzienda;
        this.campi = new HashSet<Campo>();
    }
}