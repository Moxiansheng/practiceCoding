package hashCode;

public class HashCodeEquals {
    int age;
    String name;
    public HashCodeEquals(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String toString() {
        return name + " - " +age;
    }

    /**
     * @desc重写hashCode
     */
    @Override
    public int hashCode(){
        int nameHash =  name.toUpperCase().hashCode();
        return nameHash ^ age;
    }

    /**
     * @desc 覆盖equals方法
     */
    public boolean equals(Object obj){
        if(obj == null){
            return false;
        }

        //如果是同一个对象返回true，反之返回false
        if(this == obj){
            return true;
        }

        //判断是否类型相同
        if(this.getClass() != obj.getClass()){
            return false;
        }

        HashCodeEquals person = (HashCodeEquals)obj;
        return name.equals(person.name) && age==person.age;
    }
}
