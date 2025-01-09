package prodotti;

import java.util.ArrayList;
import java.util.List;

public class Product {

    private int productCode;
    private String name;
    private List<Artist> artists = new ArrayList<>();
    private String releaseDate;
    private String description;
    private int availability;
    private double salePrice;
    private double originalPrice;
    private String supportedDevice;
    private List<Genre> genres = new ArrayList<>();
    private String image;
    private boolean isDeleted;

    public Product(int productCode, String name, List<Artist> artists, String releaseDate, String description,
    		int availability, double salePrice, double originalPrice, String supportedDevice, 
                   List<Genre> genres, String image, boolean isDeleted) {
        this.productCode = productCode;
        this.name = name;
        this.artists = artists != null ? artists : new ArrayList<>();
        this.releaseDate = releaseDate;
        this.description = description;
        this.availability = availability;
        this.salePrice = salePrice;
        this.originalPrice = originalPrice;
        this.supportedDevice = supportedDevice;
        this.genres = genres != null ? genres : new ArrayList<>();
        this.image = image;
        this.isDeleted = isDeleted;
    }

    public Product() {
    }

    public Product(int productCode, String name, String description, int availability, double salePrice,
                   double originalPrice, String supportedDevice, String image, String releaseDate, boolean isDeleted) {
        this.productCode = productCode;
        this.name = name;
        this.description = description;
        this.availability = availability;
        this.salePrice = salePrice;
        this.originalPrice = originalPrice;
        this.supportedDevice = supportedDevice;
        this.image = image;
        this.releaseDate = releaseDate;
        this.isDeleted = isDeleted;
        this.artists = new ArrayList<>();
        this.genres = new ArrayList<>();
    }

	public int getProductCode() {
        return productCode;
    }

    public void setProductCode(int productCode) {
        this.productCode = productCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists != null ? artists : new ArrayList<>();
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getSupportedDevice() {
        return supportedDevice;
    }

    public void setSupportedDevice(String supportedDevice) {
        this.supportedDevice = supportedDevice;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres != null ? genres : new ArrayList<>();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void addArtist(Artist artist) {
        if (artist != null) {
            this.artists.add(artist);
        }
    }

    public void addGenre(Genre genre) {
        if (genre != null) {
            this.genres.add(genre);
        }
    }
}
