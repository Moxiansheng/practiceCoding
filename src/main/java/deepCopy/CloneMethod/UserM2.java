package deepCopy.CloneMethod;

public class UserM2 implements Cloneable {
    private String name;
    private AddressM2 address;

    public UserM2(String name, AddressM2 address) {
        this.setName(name);
        this.address = address;
    }

    @Override
    public UserM2 clone() throws CloneNotSupportedException {
        // 要对此对象进行clone深拷贝，那么其中所有可以变对象，都需要进行clone
        UserM2 user = (UserM2) super.clone();
        user.setAddress(this.address.clone());
        return user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(AddressM2 addressM2) {
        this.address = addressM2;
    }

    public String getName() {
        return name;
    }

    public AddressM2 getAddress() {
        return address;
    }
}
