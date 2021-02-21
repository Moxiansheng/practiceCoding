package deepCopy.ConstructorMethod;

public class UserM1 {
    private String name;
    protected AddressM1 address;

    public UserM1() {

    }

    public UserM1(String name, AddressM1 address){
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public AddressM1 getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(AddressM1 address) {
        this.address = address;
    }
}
