package DeepCopy.CloneMethod;

import DeepCopy.ConstructorMethod.AddressM1;

public class AddressM2 extends AddressM1 implements Cloneable {
    public AddressM2(String city, String country) {
        super(city, country);
    }

    @Override
    public AddressM2 clone() throws CloneNotSupportedException {
        return (AddressM2) super.clone();
    }
}
