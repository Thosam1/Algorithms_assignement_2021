import java.io.*;
import java.util.*;

public class aroundTheWorldBFS_lessMemory {

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

            public void addEdge(int source, int destination, int weight) {
                //add edge
                list[source].add(new Node(destination, weight));

                //add back edge ((for undirected)
                list[destination].add(new Node(source, weight));
            }

            public void printGraph() {
                for (int i = 1; i <= vertex; i++) {
                    if (list[i].size() > 0) {
//                        System.out.print("Vertex " + i + " is connected to: ");
                        for (int j = 0; j < list[i].size(); j++) {
//                            System.out.print("vertex : " + list[i].get(j).vertex + " weight : " + list[i].get(j).weight + " || ");
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

                Queue<Integer> queue = new PriorityQueue<>();

                visited[s] = true; // since we start at s
                queue.add(s);
                boolean found = false; // break out from while loop
                minDist[s] = 0;

                if (s == t) {
                    found = true;
                } else {
                    while (!queue.isEmpty()) {
                        Queue<Integer> block = new PriorityQueue<>();
                        List<Integer> blockToValidate = new LinkedList<>();
                        Set<Integer> blockToEnqueue = new TreeSet<>();
//                        System.out.println(" -------------------------------------------------------------------------------------- ");

                        for (int i = 0; i < queue.size(); ++i) {
                            int rem = queue.remove();
                            block.add(rem); // adding all integer of main queue to this queue - treated all as a block // dequeuing the main queue
                            blockToValidate.add(rem);
                        }

                        while (!block.isEmpty()) {
                            int removed = block.remove();
//                            System.out.println(" -------------- same block ---------");
//                            System.out.println("removed : " + removed);
//                            System.out.println("all neighbours : " + list[removed].toString());
                            for (Node neigh : list[removed]) {
                                if (visited[neigh.vertex] == false || neigh.vertex == t) {
//                                    System.out.println("neighbour being considered : " + neigh.vertex);
                                    int possible = minDist[removed] + neigh.weight;
                                    if(minDist[neigh.vertex] > possible){
                                        minDist[neigh.vertex] = possible;
//                                        System.out.println(" possible is less than previously computed");
                                        blockToEnqueue.add(neigh.vertex);
                                    }else{
//                                        blockToEnqueue.add(neigh.vertex);
                                    }
//                                    blockToEnqueue.add(neigh.vertex); // take
                                    // optimize -> ignore elts within the queue / visited/validate before the for loop or just before if statement

                                }
                            }
                        }


                        // at the end, we visited all elts in main queue -> validate them
                        for (Integer i : blockToValidate) {
                            visited[i] = true;
//                            System.out.println("visited true is : " + i);
                        }
                        // at the end, we enqueue all ells in block

//                        System.out.println("nb of enqueued blocks : " + blockToEnqueue.size());
                        for (Integer i : blockToEnqueue) {
                            if (minDist[t] != INF) { // then we can filter out
                                if (minDist[i] < minDist[t]) {
                                    queue.add(i);
//                                    System.out.println("added to queue : " + i);
                                } else {
//                                    System.out.println(" is not enqueued : " + i);
                                }
                                // then we can skip this node since the distance will be greater than what we already found
                            } else {
                                queue.add(i);
//                                System.out.println("added to queue : " + i);
                            }
                        }
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
