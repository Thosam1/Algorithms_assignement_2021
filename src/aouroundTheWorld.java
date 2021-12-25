import java.io.*;
import java.lang.reflect.Array;
import java.net.Inet4Address;
import java.util.*;

// since we don't care about the path, we can directly use Dijkstra's shortest path algorithm

public class aouroundTheWorld{
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


        for(int i = 0; i < m; ++i){
            int bi = sc.nextInt();
            int ci = sc.nextInt();
            if(!graph.list[bi].contains(new Node(ci, 1))){
                graph.addEdge(bi, ci, 1);
            }
        } // all train routes

        for(int i = 0; i < airCities.size(); ++i){
            for(int j = 0; j < i; ++j){
                int airI = airCities.get(i);
                int airJ = airCities.get(j);
                if(!graph.list[airI].contains(new Node(airJ, 1))){
                    graph.addEdge(airI, airJ, 2);
                }
                graph.addEdge(airCities.get(i), airCities.get(j), 2);
//                    graph.addEdge(airCities.get(j), airCities.get(i));
//                    System.out.println("airport weights "+ i + " " + j + " " + weights[i][j]); // stupid mistake
            }
        } // airport flight routes


//        graph.printGraph(); // -- graph debugged, works well

        // normally here we get the whole graph with all vertices and edges in an adjacency list


        int[] dist = new int[n+1];
//        int[] previous = new int[n+1];

        final int INF = 2147483647; // 2 147 483 647
//        final int UNDEF = -1;
        List<Integer> unvisited = new ArrayList<>(); // vertices 1 to n

        // running Dijktra's algorithm
        // init
        for(int i = 1; i <= n; ++i){
            dist[i] = INF;
//            previous[i] = UNDEF;
            unvisited.add(i);
        }
        dist[s] = 0;


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

                    for (Node v_adjToVertex : graph.list[vertex]) {
                        if(unvisited.contains(v_adjToVertex.vertex)){
                            if(dist[vertex] + v_adjToVertex.weight >= 0){ // eg: 1-2 et 3-4 -> 3-4 aeroport -> on trouverait infini + 2 -> overflow si route overflow de 1
                                if (dist[vertex] + v_adjToVertex.weight < dist[v_adjToVertex.vertex]) {
                                    dist[v_adjToVertex.vertex] = v_adjToVertex.weight + dist[vertex] ;
//                                    System.out.println("current vertex : " + vertex + " connected to : " + v_adjToVertex.vertex + " current weight : " + v_adjToVertex.weight);
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
        ArrayList<Node> list[];

        public Graph(int vertex) {
            this.vertex = vertex;
            list = new ArrayList[vertex+1];
            for (int i = 1; i <=vertex ; i++) {
                list[i] = new ArrayList<>();
            }
        }

        public void addEdge(int source, int destination, int weight){
            //add edge
            list[source].add(new Node(destination, weight));

            //add back edge ((for undirected)
            list[destination].add(new Node(source, weight));
        }

        public void printGraph(){
            for (int i = 1; i <=vertex ; i++) {
                if(list[i].size()>0) {
                    System.out.print("Vertex " + i + " is connected to: ");
                    for (int j = 0; j < list[i].size(); j++) {
                        System.out.print("vertex : " + list[i].get(j).vertex + " weight : " + list[i].get(j).weight + " || ");
                    }
                    System.out.println();
                }
            }
        }

    }
    public static class Node {
        int vertex;
        int weight;
        Node(int vertex, int weight){
            this.vertex = vertex;
            this.weight = weight;
        }

        @Override
        public boolean equals (Object object){
            boolean sameSame = false;
            if (object != null && object instanceof Node)
            {
                sameSame = this.vertex == ((Node)object).vertex && this.weight == ((Node)object).weight;
            }
            return sameSame;
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

