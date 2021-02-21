package deepCopy.SerializableMethod;

import java.io.Serializable;

public class AddressM3 implements Serializable {
    private String city;
    private String country;

    public AddressM3(String city, String country){
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
