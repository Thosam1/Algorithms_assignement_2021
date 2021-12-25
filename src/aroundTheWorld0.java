import java.io.*;
import java.lang.reflect.Array;
import java.net.Inet4Address;
import java.util.*;

// since we don't care about the path, we can directly use Dijkstra's shortest path algorithm

public class aroundTheWorld0{
    public static void main(String[] args) {
        MyScanner sc = new MyScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));

        // Start writing your solution here. -------------------------------------
        int n = sc.nextInt(); // cities
        int m = sc.nextInt(); // train routes
        int k = sc.nextInt(); // cities with airports
        int s = sc.nextInt(); // starting city
        int t = sc.nextInt(); // end city

        Graph graph = new Graph(n);
        int[][] weights = new int[n+1][n+1];
//        for(int i = 0; i<=n; ++i){
//            for(int j = 0; j<=n; ++j){
//                weights[i][j] = -1;
//            }
//        }

        ArrayList<Integer> airCities = new ArrayList<Integer>();
        for(int i = 0; i < k; ++i){
            int citiesWithAirport = sc.nextInt();
            airCities.add(citiesWithAirport);
        }
//        System.out.println(airCities);

        for(int i = 0; i < airCities.size(); ++i){
            for(int j = 0; j < i; ++j){
                graph.addEdge(airCities.get(i), airCities.get(j));
//                    graph.addEdge(airCities.get(j), airCities.get(i));
                weights[airCities.get(i)][airCities.get(j)] = 2;
                weights[airCities.get(j)][airCities.get(i)] = 2;
//                    System.out.println("airport weights "+ i + " " + j + " " + weights[i][j]); // stupid mistake
            }
        } // airport flight routes

        for(int i = 0; i < m; ++i){
            int bi = sc.nextInt();
            int ci = sc.nextInt();
            if(!graph.list[bi].contains(ci)){
                graph.addEdge(bi, ci);
            }
//            graph.addEdge(ci, bi); // since undirected
            weights[bi][ci] = 1;
            weights[ci][bi] = 1;
        } // all train routes

//        graph.printGraph(); // -- graph debugged, works well

        // normally here we get the whole graph with all vertices and edges in an adjacency list

        int[] dist = new int[n+1];
        int[] previous = new int[n+1];

        final int INF = 2147483647; // 2 147 483 647
        final int UNDEF = -1;
        List<Integer> unvisited = new ArrayList<>(); // vertices 1 to n

        // running Dijktra's algorithm
        // init
        for(int i = 1; i <= n; ++i){
            dist[i] = INF;
            previous[i] = UNDEF;
            unvisited.add(i);
        }
        dist[s] = 0;
        previous[s] = s;

//        try {
        while (!unvisited.isEmpty()) {
            int vertex;
            if (unvisited.contains(s)) {
                vertex = s; // so we start from source
            } else {
                // vertex with smallest dist from source in unvisited
                int smallestdist = INF;
                int smallestV = -1;
                for (int v : unvisited) {
                    if (dist[v] <= smallestdist) {
                        smallestdist = dist[v];
                        smallestV = v;
                    }
                }
                vertex = unvisited.get(unvisited.indexOf(smallestV));
            }
//                System.out.println("weights btw 1 - 4 " + weights[1][4]);

            for (int v_adjToVertex : graph.list[vertex]) {
                if(unvisited.contains(v_adjToVertex)){
                    if(dist[vertex] + weights[vertex][v_adjToVertex] >= 0){ // eg: 1-2 et 3-4 -> 3-4 aeroport -> on trouverait infini + 2 -> overflow si route overflow de 1
                        if (dist[vertex] + weights[vertex][v_adjToVertex] < dist[v_adjToVertex]) {
                            dist[v_adjToVertex] = weights[vertex][v_adjToVertex] + dist[vertex] ;
//                                    System.out.println("current vertex : " + vertex+ " connected to : " + v_adjToVertex + " current weight : " + weights[vertex][v_adjToVertex]);
                            previous[v_adjToVertex] = vertex;
                        }
                    }

                }
            }
//                System.out.println(" distance table : " + Arrays.toString(dist));

            unvisited.remove(unvisited.indexOf(vertex));
        }

//            System.out.println(dist[t]);
//        } catch (Exception e){
//            System.out.println("Impossible");
//        }

        if(dist[t] == INF || dist[t] == -2147483646){
            System.out.println("Impossible"); // disconnected end
        } else {
            System.out.println(dist[t]);
        }

        // Stop writing your solution here. -------------------------------------
        out.close();
    }

    public static class Graph {
        int vertex;
        LinkedList<Integer> list[];

        public Graph(int vertex) {
            this.vertex = vertex;
            list = new LinkedList[vertex+1];
            for (int i = 1; i <=vertex ; i++) {
                list[i] = new LinkedList<>();
            }
        }

        public void addEdge(int source, int destination){
            //add edge
            list[source].addFirst(destination);

            //add back edge ((for undirected)
            list[destination].addFirst(source);
        }

        public void printGraph(){
            for (int i = 1; i <=vertex ; i++) {
                if(list[i].size()>0) {
                    System.out.print("Vertex " + i + " is connected to: ");
                    for (int j = 0; j < list[i].size(); j++) {
                        System.out.print(list[i].get(j) + " ");
                    }
                    System.out.println();
                }
            }
        }

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
 