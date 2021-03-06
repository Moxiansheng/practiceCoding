package reflection;

public class reflectedClass {
    private final static String TAG = "BookTag";

    private String name;
    private String author;

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj instanceof reflectedClass){
            reflectedClass rc1 = (reflectedClass) obj;
            if(name.equals(rc1.name) && author.equals(rc1.author)){
                return true;
            }
        }
        return false;
    }

    public reflectedClass() {
    }

    private reflectedClass(String name, String author) {
        this.name = name;
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    private String declaredMethod(int index) {
        String string = null;
        switch (index) {
            case 0:
                string = "I am declaredMethod 1 !";
                break;
            case 1:
                string = "I am declaredMethod 2 !";
                break;
            default:
                string = "I am declaredMethod 0 !";
        }

        return string;
    }

    public static void main(String[] args) {
        A a = new A();
        System.out.println(A.m);
    }
}

class A{
    static int m = 100;
    static{
        System.out.println("static");
        m = 300;
    }



    public A(){
        System.out.println("no parameter initialization");
    }
}