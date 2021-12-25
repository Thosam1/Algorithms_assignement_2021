import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class main{
    public static void main(String[] args) {
        MyScanner sc = new MyScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));

        // Start writing your solution here. -------------------------------------
        int n = sc.nextInt();
        int c = sc.nextInt();
        int e = sc.nextInt();

        ArrayList<Integer> costVal = new ArrayList<Integer>();
        ArrayList<Integer> caloricVal = new ArrayList<Integer>();

        for(int i = 0; i < n; ++i){
            int cost = sc.nextInt();
            int val = sc.nextInt();
            costVal.add(cost);
            caloricVal.add(val);
        }
//        System.out.println(n);
//        System.out.println(c);
//        System.out.println(e);
//        System.out.println(costVal);
//        System.out.println(caloricVal);

        // recurrence, either we take it or we dont
        int[][][] memory = new int[n+1][c+1][e+1];
        // init the array
        for(int j = 0; j <= c; ++j){
            for(int k = 0; k <= e; ++k){
                memory[0][j][k] = 0; // we cannot take anything more, j-k are not 0
            }
        }
        for (int i = 0; i <= n; ++i){
            memory[i][0][0] = 1; // we can run this even faster
        }
//        memory[0][0][0] = 1;


        for(int i = 1; i <= n; ++i){
            for(int j = 1; j <= c; ++j){
                for(int k = 1; k <= e; ++k){
                    int cost = j-costVal.get(i-1);
                    int cal = k-caloricVal.get(i-1);
                    int val = (cost < 0 || cal < 0) ? 0 : memory[i-1][cost][cal];

                    memory[i][j][k] = Math.max(memory[i - 1][j][k], val);
                }
            }
        }
//        System.out.println(memory[n][c][e]);

        String answer = (memory[n][c][e] == 1) ? "Yes" : "No";
        System.out.println(answer);

        // Stop writing your solution here. -------------------------------------
        out.close();
    }


    //-----------PrintWriter for faster output---------------------------------
    public static PrintWriter out;

    //-----------MyScanner class for faster input----------
    public static class MyScanner {
        BufferedReader br;
        StringTokenizer st;

        public MyScanner() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        long nextLong() {
            return Long.parseLong(next());
        }

        double nextDouble() {
            return Double.parseDouble(next());
        }

        String nextLine(){
            String str = "";
            try {
                str = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return str;
        }

    }
}
