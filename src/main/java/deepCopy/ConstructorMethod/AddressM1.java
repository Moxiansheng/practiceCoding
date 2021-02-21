package deepCopy.ConstructorMethod;

public class AddressM1 {
    private String city;
    private String country;

    public AddressM1() {

    }

    public AddressM1(String city, String country) {
        this.city = city;
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
