package jobCode;

public class LC765 {
    public static void main(String[] args) {
        // int[] row = {5,6,4,0,2,1,9,3,8,7,11,10};
        // System.out.println(minSwapsCouples(row));

        String s = "733064366", t = "459309139";
        System.out.println(solve(s, t));
    }

    public static String solve (String s, String t) {
        if(s.length() > t.length()){
            return solve(t, s);
        }
        char[] ss = s.toCharArray();
        char[] tt = t.toCharArray();

        StringBuilder sb = new StringBuilder();
        int flag = 0, i = 1;
        for(; i <= ss.length; i++){
            int temp = (ss[ss.length - i] - '0') + (tt[tt.length - i] - '0') + flag;
            sb.append((char) (temp%10+'0'));
            flag = temp/10;
        }

        for(;i <= tt.length; i++){
            int temp = (tt[tt.length - i] - '0') + flag;
            sb.append((char)(temp%10+'0'));
            flag = temp/10;
        }

        return flag == 1 ? sb.append('1').reverse().toString() : sb.reverse().toString();
    }

    public static int minSwapsCouples(int[] row) {
        int res = 0;
        int[] index = new int[60];
        for(int i = 0; i < row.length; i++){
            index[row[i]] = i;
        }
        for(int i = 0; i < row.length; i+=2){
            if(Math.abs(row[i+1]-row[i]) != 1){
                int right = row[i+1];
                int tempI = (row[i] & 1) == 0 ? row[i]+1: row[i]-1;
                int tempR = index[tempI];
                int temp = index[tempI];
                index[tempI] = index[right];
                index[right] = temp;
                temp = row[tempR];
                row[tempR] = row[i+1];
                row[i+1] = temp;
                res++;
            }
        }
        return res;
    }
}
