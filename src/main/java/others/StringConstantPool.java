package others;

public class StringConstantPool {
    public static void main(String[] args) {
        String m = new String("mn" + new String("nm"));
        String n = new String(m);
        System.out.println(m.intern() == m);
        System.out.println(n.intern() == n);
        String o = new String("op" + "qr");
        String p = new String("o") + new String("p");
        System.out.println(p.intern() == p);
        String q = "qr" + "rs";
        String r = new String("r") + new String("s");
        System.out.println(r.intern() == r);
        String t = "uu" + new String("tt");
        System.out.println(t.intern() == t);
        String s = new String("u") + new String("u");
        System.out.println(s.intern() == s);
        String u = new String("ca") + new String("cb");
        String v = new String("c") + new String("a");
        String w = new String("c") + new String("b");
        String z = new String("ca" + "cb");
        System.out.println(u.intern() == u);
        System.out.println(v.intern() == v);
        System.out.println(w.intern() == w);
        String a = "a" + "b";
        String b = "ab";
        String c = a.intern();
        String d = b.intern();
        System.out.println(a == b);
        System.out.println(a == c);
        System.out.println(a == d);

//        String e = new String("a" + "b");
//
//        String f = new String("c") + new String("d");
//        String g = f.intern();
//        String h = "cd";
//        System.out.println(f == g);
//        System.out.println(g == h);
//
//        String i = "aaa";
//        String j = "bbb";
//        String k = "aaabbb";
//        String l = i + j;
//        System.out.println(k == l.intern());


    }
}
