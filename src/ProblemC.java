import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class ProblemC {

    public static void main(String[] args) {
        MyScanner sc = new MyScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));

        // Start writing your solution here. -------------------------------------
        int n = sc.nextInt(); // islands
        int m = sc.nextInt(); // proposals

//        int[] aaa = new int[m];
//        int[] bbb = new int[m];
//        int[] ccc = new int[m];
//        int[] team = new int[m]; // 0 if RED, 1 if BLUE

        Graph graph = new Graph(n, m); // with given nb vertices and edges


        for(int i = 0; i < m; ++i){
            graph.edges[i].source = sc.nextInt() - 1;
            graph.edges[i].destin = sc.nextInt() - 1;
            int w = sc.nextInt();
            graph.edges[i].weight = w;

            String red =  sc.next();        // not important for now
            if(red.equals("red")){
                graph.edges[i].red = 1;
            }else{
                graph.edges[i].red = 0;
            }
        }

        // we can start Kruskal algorithm to find the MST
//        int totalWeight = 0;
        int redWeight = 0;
        int blueWeight = 0;

        // the graph should be okay - we have all edges at this point

        // Step 1 : sort edges
        Arrays.sort(graph.edges); // sorting in increasing order by weightW
        Graph.sSet[] subSets = new Graph.sSet[graph.nbVert]; // since e start with each vertices being one single subset
        for(int j = 0; j < graph.nbVert; ++j){
            subSets[j] = new Graph.sSet(j, 0); // init parent and rank of single vertices
        }

        // Step 2
        int index = 0;
        int edgesValid = 0;
//        graph.printGraph();
        while(edgesValid < graph.nbVert - 1) { // Step 3 : Keep adding edges until all the vertices are connected and a Minimum Spanning Tree (MST) is obtained.
            try{
//                System.out.println(" - - - - - - - - - - - - -  - - -- ");
//                System.out.println("current index : " + index);
//                System.out.println("current valid edges : " + edgesValid);
                // Take the edge with the lowest weight and use it to connect the vertices of graph.
//                System.out.println("current subsets[graph.edges[index].source] " + subSets[graph.edges[index].source] + " source is : " + graph.edges[index].source + " size of subsets is : " + subSets.length);
                if(graph.FindSet(subSets, graph.edges[index].source) != graph.FindSet(subSets, graph.edges[index].destin)) { // cycle condition

                    if(graph.edges[index].red == 1){
                        redWeight += graph.edges[index].weight;
                    }else{
                        blueWeight += graph.edges[index].weight;
                    }

                    graph.Union(subSets, graph.edges[index].source, graph.edges[index].destin);
                    edgesValid++;

//                    System.out.println("added edge to MST : " + graph.edges[index].source + " - " + graph.edges[index].destin);
                }

                // If adding an edge creates a cycle, then reject that edge and go for the next least weight edge.


                index++; // so we take next edge with lowest weight
            } catch(Exception e){
                break;
            }

        }

        // printing out the answer
        System.out.println(redWeight + " " + blueWeight);




    }

    public static class Graph {

        int nbVert, nbEdges;
        Edge edges[]; // array containing all edges, later, we will sort all of them

        public Graph(int nbVert, int nbEdges) {
            this.nbVert = nbVert;
            this.nbEdges = nbEdges;

            edges = new Edge[nbEdges];
            for (int i = 0; i < nbEdges; ++i) {
                edges[i] = new Edge(); // init those edges
            }
        }

        // now that the graph is initialized, we need to create : Find-Set and Make-Set and Union()

        // Union method :
        void Union(sSet[] subsets, int x, int y) {
            Link(subsets, FindSet(subsets, x), FindSet(subsets, y));
        }

        void Link(sSet[] subsets, int x, int y) { // slide 35 of L19
            if (subsets[x].rank > subsets[y].rank) {
                subsets[y].parent = x;
            } else {
                subsets[x].parent = y;
                if (subsets[x].rank == subsets[y].rank) {
                    subsets[y].rank = subsets[y].rank++;
                }
            }
        }

        // Find-Set method :
        int FindSet(sSet[] subSet, int index) {
            if (subSet[index].parent != index) {
                subSet[index].parent = FindSet(subSet, subSet[index].parent); // we then go further // slide 34 L19
            }
            return subSet[index].parent;
        }

        public void printGraph() {
//            Arrays.sort(edges); // sorting before printing
            for (int i = 0; i < nbEdges; i++) {
                System.out.println("here are all edges in increasing order : ");
                System.out.println(" source : " + edges[i].source + " destination : " + edges[i].destin + " weight : " + edges[i].weight);
            }
            System.out.println("");
        }

        public class Edge implements Comparable<Edge> { // we want to be able to compare the edges in order to sort them
            int source, destin, weight, red;


            public int compareTo(Edge compareEdge) {
                if (this.red == 1 && compareEdge.red == 0) {
                    return -1; // bigger priority if red
                } else if (this.red == 0 && compareEdge.red == 1) {
                    return 1; // lower priority if current is blue and compared is red
                } else {
                    return compareEdge.weight - this.weight; // if both blue or both red, then compared weight
                }
            }
        }


        // this is the "Make-Set(v)" method
        public static class sSet {
            //            int x;
            int parent, rank;
            sSet(int parent, int rank) {
//                this.x = x;
                this.parent = parent;
                this.rank = rank;
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