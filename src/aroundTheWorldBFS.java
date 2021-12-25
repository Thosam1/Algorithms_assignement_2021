import java.io.*;

import java.util.*;
public class aroundTheWorldBFS {

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

        ArrayList<Integer> airCities = new ArrayList<Integer>();
        for(int i = 0; i < k; ++i){
            int citiesWithAirport = sc.nextInt();
            airCities.add(citiesWithAirport);
        }
    //        System.out.println(airCities);


        for(int i = 0; i < m; ++i){
            int bi = sc.nextInt();
            int ci = sc.nextInt();
            if(!graph.list[bi].contains(ci)){
                graph.addEdge(bi, ci, 1);
            }
        } // all train routes

        for(int i = 0; i < airCities.size(); ++i){
            for(int j = 0; j < i; ++j){
//                System.out.println("i : " + i + " j : " + j);
                int airI = airCities.get(i);
                int airJ = airCities.get(j);
                if(!graph.list[airI].contains(airJ)){
                    graph.addEdge(airI, airJ, 2); // only if really necessary (not already a road inbetween)
                }
    //               System.out.println("airport weights "+ i + " " + j + " " + weights[i][j]); // stupid mistake
            }
        } // airport flight routes


    //        graph.printGraph(); // -- graph debugged, works well

        // normally here we get the whole graph with all vertices and edges in an adjacency list

        // PART 2


        int answer = graph.shortestPathLength(s, t);
        if(answer != -1){
            System.out.println(answer);
        }else{
            System.out.println("Impossible");
        }

    // Stop writing your solution here. -------------------------------------
        out.close();
    }

    public static class Graph { // graph that transform weight 2 node into a separation of 2 node of weight 1
        int vertex;
        ArrayList<Integer> list[];

        public Graph(int vertex) {
            this.vertex = vertex;
            list = new ArrayList[(vertex+1) * 2];
            for (int i = 1; i <=vertex*2 ; i++) {
                list[i] = new ArrayList<>();
            }
        }

        public void addEdge(int source, int destination, int weight){
            int least = (source <= destination) ? source : destination;
            int biggest = (source <= destination) ? destination : source;
            if(weight == 2) {
                int interm = least + vertex;
                list[least].add(interm);
                list[interm].add(least);
                list[interm].add(biggest);
                list[biggest].add(interm);
            } else {
                //add edge
                list[source].add(biggest);
                //add back edge ((for undirected)
                list[biggest].add(least);
            }
        }

        public void printGraph(){
            for (int i = 1; i <=vertex*2 ; i++) {
                if(list[i].size()>0) {
                    System.out.print("Vertex " + i + " is connected to: ");
                    for (int j = 0; j < list[i].size(); j++) {
                        System.out.print("vertex : " + list[i].get(j) + " || ");
                    }
                    System.out.println();
                }
            }
        }

        public int shortestPathLength(int s, int t) {
            boolean[] visited = new boolean[2 * vertex + 1];
            printGraph();

            for (int i = 1; i <= 2 * vertex; ++i){
                visited[i] = false;
            }
            Queue<Integer> queue = new PriorityQueue<>();

            visited[s] = true; // since we start at s
            queue.add(s);
            boolean found = false; // break out from while loop
            int sum = 0;

            if(s == t){
                found = true;
            } else {
                while(!queue.isEmpty()){
                    int removed = queue.remove();
                    for(int neigh : list[removed]){ // all neighbours
                        if (visited[neigh] == false) {
                            queue.add(neigh);
                            visited[neigh] = true;
                        }
                        if(neigh == t){
                            found = true;
                            break;
                        }
                    }
                    sum++;
                    if(found){
                        break;
                    }
                }
            }
            if(!found){
                return -1;
            }else{
                return sum;
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
