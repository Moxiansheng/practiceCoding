package deepCopy.SerializableMethod;

import java.io.Serializable;

public class UserM3 implements Serializable {
    private String name;
    protected AddressM3 address;

    public UserM3(String name, AddressM3 address){
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public AddressM3 getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(AddressM3 address) {
        this.address = address;
    }
}
