import java.io.*;
import java.util.*;

public class aroundTheWorld_lessMemory_Last_Version69_v2 {
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

        HashSet<Integer> airCities = new HashSet<>(); // could sort them so we find the city in logn time eg: binary search

        for(int i = 0; i < k; ++i){
            int citiesWithAirport = sc.nextInt();
            airCities.add(citiesWithAirport);
        }

        for(int i = 0; i < m; ++i) {
            int bi = sc.nextInt();
            int ci = sc.nextInt();
            graph.addEdge(bi, ci, t);
        } // all train routes

        // we do everything with normal routes, unti we arrive at the first airport.
        // at that first airport we can calculate for all possible other edges to the rest of the airports k-1
        // and then after this, we can release all the memory from this -> assign null

        // Initialisation of  the "modified" BFS
        boolean[] visited = new boolean[graph.vertex + 1];
        int[] minDist = new int[graph.vertex + 1];

        final int INF = 2147483645; // 2 147 483 647 || -2 to prevent overflow

        for (int i = 1; i <= graph.vertex; ++i) {
            visited[i] = false; // init
            minDist[i] = INF;

        }

        Queue<Integer> queue = new ArrayDeque<>();

        visited[s] = true; // since we start at s
        minDist[s] = 0;
        Boolean airportDone = false;
        Boolean tFound = false;

//        graph.printGraph();

        if (s == t) {
            // do nothing, minDist[s==t] = 0
        } else if (graph.list[t].isEmpty() && k == 0) { // trivial case, then it is impossible
            // do nothing, minDist[t] = INF
        } else {

            // starting the actual BFS
            queue.add(s);

            while(!queue.isEmpty()){
//                System.out.println(" ----------------------------------------- ");
//                System.out.println(" Current Queue looks like : " + queue);

                Integer removed = queue.remove();

//                System.out.println("item removed : " + removed);
//                System.out.println("nb neighbours (roads) : " + graph.list[removed].size());
//                System.out.println("all items that are next to removed" + graph.list[removed]);



                if (airportDone == false) { // so if already done, we don't add a log factor to the time (the contains method)

//                    System.out.println("airport Done is : " + airportDone);

                    if(airCities.contains(removed)) { // first airport city case //

//                            System.out.println("air cities contains - removed, we turn this bool into true !!!");

                        // then before checking with routes, we check with airports
                        airCities.remove(removed); // now we have all neighbor airport cities to "removed"
                        // at this point we got all airport cities neighbors of "removed"

                        // here we do one BFS
                        airportDone = true; // we don't go over this if statement anymore


                        // we do one for loop to go over all airport neighbours
                        for (Integer neigh : airCities) {
                            if(graph.list[removed].contains(neigh)){ // so we won't have any duplicates in the queue    // ------------------------------------------- !!!!!!!!!!!!!!!!!!!
//                                    System.out.println("we skip this neighbour because there is a road to it : " + neigh);
                                continue;
                            } else {


                                if (visited[neigh] == false || neigh == t) {
//                                    System.out.println("valid neighbour being considered: " + neigh);
                                    int possible = minDist[removed] + 2; // here weight is 2
                                    if (minDist[neigh] > possible) {
                                        minDist[neigh] = possible;
//                                        System.out.println(" possible is less than previously computed");

                                        if(neigh == t){
                                            tFound = true;
                                            break;
                                        }

                                        if (minDist[neigh] < minDist[t]) {
//                                            System.out.println("added to queue : " + neigh);
                                            queue.add(neigh); // otherwise don't bother searching further down this node - here we add only if this neigh is an airport and no roads can reach it from "removed"

                                        }
                                    }
                                }

                            }
                        } // end for loop
                        if(tFound){break;}



                    }

                    for (Integer neigh : graph.list[removed]) {
                        if (visited[neigh] == false || neigh == t) {
//                            System.out.println("valid neighbour route being considered: " + neigh);
                            int possible = minDist[removed] + 1;
                            if (minDist[neigh] > possible) {
                                minDist[neigh] = possible;
//                                        System.out.println(" possible is less than previously computed");
                                if(neigh == t){
                                    tFound = true;
                                    break;
                                }
                                if (minDist[neigh] < minDist[t]) {
                                    queue.add(neigh); // otherwise don't bother searching further down this node
//                                    System.out.println("added to queue : " + neigh);
                                }
                            }
                        }
                    }
                    if(tFound){break;}

                    visited[removed] = true;


                } else {

                    for (Integer neigh : graph.list[removed]) {
                        if (visited[neigh] == false || neigh == t) {
//                            System.out.println("valid neighbour being considered: " + neigh);
                            int possible = minDist[removed] + 1;
                            if (minDist[neigh] > possible) {
                                minDist[neigh] = possible;
//                                        System.out.println(" possible is less than previously computed");
                                if(neigh == t){
                                    tFound = true;
                                    break;
                                }
                                if (minDist[neigh] < minDist[t]) {
                                    queue.add(neigh); // otherwise don't bother searching further down this node
//                                    System.out.println("added to queue : " + neigh);
                                }
                            }
                        }
                    }
                    if(tFound){break;}

                    visited[removed] = true;
                }

            }

        }

        // printing answer :
        if(minDist[t] == INF){
            System.out.println("Impossible");
        } else {
            System.out.println(minDist[t]);
        }


    }

    public static class Graph {
        int vertex;
        HashSet<Integer> list[];

        public Graph(int vertex){
            this.vertex = vertex;
            list = new HashSet[vertex + 1];
            for (int i = 1; i <= vertex; ++i){
                list[i] = new HashSet<>();
            }
        }

        public void addEdge(int source, int destination, int sink){

            list[source].add(destination);
            list[destination].add(source);

//            if(destination == sink){
//                list[source].add(0, destination); // adding at index 0
//
//                if(list[destination].isEmpty()){
//                    list[destination].add(source);
//                } else {
//                    list[destination].add(1, source);
//                }
//            } else if (source == sink) {
//                // add edge
//                if(list[source].isEmpty()){
//                    list[source].add(destination);
//                } else {
//                    list[source].add(1, destination);
//                }
//                //add back edge ((for undirected)
//                list[destination].add(0, source);
//            } else {
//
//                //add edge
//                if(list[source].isEmpty()){
//                    list[source].add(destination);
//                } else {
//                    list[source].add(1, destination);
//                }
//                //add back edge ((for undirected)
//                if(list[destination].isEmpty()){
//                    list[destination].add(source);
//                } else {
//                    list[destination].add(1, source);
//                }
//
//
//            }
        }

        public void printGraph(){
            for (int i = 1; i <=vertex ; i++) {
                if(list[i].size()>0) {
                    System.out.print("Vertex " + i + " is connected to: ");
                    for (int j = 0; j < list[i].size(); j++) {
//                        System.out.print("vertex : " + list[i].get(j) + " ---> ");
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
