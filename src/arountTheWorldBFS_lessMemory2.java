
import java.io.*;
import java.util.*;

public class arountTheWorldBFS_lessMemory2 {

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
            if(!graph.list[bi].contains(new Node(ci, 1))){
                graph.addEdge(bi, ci, 1, t);
            }
        } // all train routes

        for(int i = 0; i < airCities.size(); ++i){
            for(int j = 0; j < i; ++j){
                int airI = airCities.get(i);
                int airJ = airCities.get(j);
                if(!graph.list[airI].contains(new Node(airJ, 1))){
                    graph.addEdge(airI, airJ, 2, t);
                }
            }
        } // airport flight routes


//        graph.printGraph(); // -- graph debugged, works well

        // normally here we get the whole graph with all vertices and edges in an adjacency list

        // PART 2


        int answer = graph.shortestPathLength(t, s);

        if(answer != -1){
            System.out.println(answer);
        }else{
            System.out.println("Impossible");
        }

        // Stop writing your solution here. -------------------------------------
        out.close();
    }

    public static class Graph {
        int vertex;
        ArrayList<Node> list[];

        public Graph(int vertex) {
            this.vertex = vertex;
            list = new ArrayList[vertex + 1];
            for (int i = 1; i <= vertex; i++) {
                list[i] = new ArrayList<>();
            }
        }

        public void addEdge(int source, int destination, int weight, int sink) {

            if(destination == sink){
                // add edge
                list[source].add(0, new Node(destination, weight));
                //add back edge ((for undirected)
                if(list[destination].isEmpty()){
                    list[destination].add(new Node(source, weight));
                } else {
                    list[destination].add(1, new Node(source, weight));
                }

            } else if (source == sink) {
                // add edge
                if(list[source].isEmpty()){
                    list[source].add(new Node(destination, weight));
                } else {
                    list[source].add(1, new Node(destination, weight));
                }
                //add back edge ((for undirected)
                list[destination].add(0, new Node(source, weight));
            } else {
                //add edge
                if(list[source].isEmpty()){
                    list[source].add(new Node(destination, weight));
                } else {
                    list[source].add(1, new Node(destination, weight));
                }
                //add back edge ((for undirected)
                if(list[destination].isEmpty()){
                    list[destination].add(new Node(source, weight));
                } else {
                    list[destination].add(1, new Node(source, weight));
                }

            }
        }

        public void printGraph() {
            for (int i = 1; i <= vertex; i++) {
                if (list[i].size() > 0) {
                        System.out.print("Vertex " + i + " is connected to: ");
                    for (int j = 0; j < list[i].size(); j++) {
                            System.out.print("vertex : " + list[i].get(j).vertex + " weight : " + list[i].get(j).weight + " || ");
                    }
                    System.out.println();
                }
            }
        }

        public int shortestPathLength(int s, int t) {
            boolean[] visited = new boolean[vertex + 1];
            int[] minDist = new int[vertex + 1];
            final int INF = 2147483645; // 2 147 483 647 || -2 to prevent overflow

            for (int i = 1; i <= vertex; ++i) {
                visited[i] = false; // init
                minDist[i] = INF;
            }

            Queue<Integer> queue = new ArrayDeque<>();

            visited[s] = true; // since we start at s
            minDist[s] = 0;

            if (s == t) {

            } else if (list[t].isEmpty()) { // trivial case, then it is impossible

            }else {
                queue.add(s);
                while (!queue.isEmpty()) {
//                    System.out.println(" ----------------------------------------- ");
//                    System.out.println(" Current Queue looks like : " + queue);

                    Integer removed = queue.remove();
//                    System.out.println("item removed : " + removed);
//                    System.out.println("nb neighbours : " + list[removed].size());
//                    System.out.println("all items that are next to removed" + list[removed]);

                    for (Node neigh : list[removed]) {

                        if (visited[neigh.vertex] == false || neigh.vertex == t) {
//                            System.out.println("neighbour being considered: " + neigh.vertex);
                            int possible = minDist[removed] + neigh.weight;
                            if (minDist[neigh.vertex] > possible) {
                                minDist[neigh.vertex] = possible;
//                                        System.out.println(" possible is less than previously computed");
                                if (minDist[neigh.vertex] < minDist[t]) {
                                    queue.add(neigh.vertex); // otherwise don't bother searching further down this node
//                                    System.out.println("added to queue : " + neigh.vertex);
                                }
                            }
                        }
                    }
                    visited[removed] = true; // so we won't even look down there anymore
//                    for(int i = 0; i < queue.size(); ++i){
//                        if(minDist[queue.(i)] >= minDist[t]){ // au lieu de ça, on peut se débrouiller pour mettre les "t" en index 0 de la graph à la création, comme ça pas besoin de modifier laglo ici / filter les elem de la queue car le filtrage sera fait auto dans la for loop
//                            queue.remove()
//                        }
//                    }
                }
            }

            if(minDist[t] == INF){
                return -1;
            }else{
                return minDist[t];
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


// maybe the problem comes from the loop in the neighbour for loop, -> maybe we should store into an array to access elts faster