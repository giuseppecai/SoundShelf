package prodotti;

public class Genre {

    private String name;

    public Genre(String name) {
        this.name = name;
    }

    public Genre() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Genre [name=" + name + "]";
    }
}
