import org.omg.CORBA.UNKNOWN;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class ProblemC_v2 {

    public static void main(String[] args) {
        MyScanner sc = new MyScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));

        // Start writing your solution here. -------------------------------------
        int n = sc.nextInt(); // islands
        int m = sc.nextInt(); // proposals


        Graph graph = new Graph(n, m); // with given nb vertices and edges

        for(int i = 0; i < m; ++i){
            int source = sc.nextInt() - 1;
            int destin = sc.nextInt() - 1;
            int w = sc.nextInt();

            String red =  sc.next();        // not important for now
            if(red.equals("red")){
                graph.redEdges.add(new Graph.Edge(source, destin, w));
            }else{
                graph.blueEdges.add(new Graph.Edge(source, destin, w));
            }
        }



        // we can start Kruskal algorithm to find the MST

        int redTotalWeight = 0;
        int blueTotalWeight = 0;

        // the graph should be okay - we have all edges at this point

        // Step 1 : sort edges
        Collections.sort(graph.redEdges); // sorting in Decreasing order by weightW
        Collections.sort(graph.blueEdges);

        Graph.sSet[] subSets = new Graph.sSet[graph.nbVert]; // since e start with each vertices being one single subset
        for(int j = 0; j < graph.nbVert; ++j){
            subSets[j] = new Graph.sSet(j, 0); // init parent and rank of single vertices
        }

        // Step 2
        int index = 0; // can be removed

//        graph.printGraph();

        // Bruno


        int nbRedEdges = 0;
        int nbBlueEdges = 0;

        while(nbRedEdges + nbBlueEdges < graph.nbVert - 1) { // Step 3 : Keep adding edges until all the vertices are connected and a Minimum Spanning Tree (MST) is obtained.


//            System.out.println(" - - - - - - - - - - - - -  - - -- ");
//            System.out.println("current index : " + index);
//            System.out.println("current valid edges : " + (nbRedEdges + nbBlueEdges));

            // Take the edge with the max weight and use it to connect the vertices of graph.

            Graph.Edge chosen = new Graph.Edge(-1, -1, -1); // init
            Boolean chosenIsRed = false;
            try {
                if(graph.redEdges.isEmpty()){ // then we are force to take blue
                    chosen = graph.blueEdges.remove(0);
                } else if(graph.blueEdges.isEmpty()){  // take red then
                    chosen = graph.redEdges.remove(0);
                    chosenIsRed = true;

                } else { // we choose depending on the numbers - both have at least one element

                    if(nbBlueEdges >= nbRedEdges){ // then we must take the biggest possible red
                        chosen = graph.redEdges.remove(0);
                        chosenIsRed = true;

                    } else { // take the biggest of both
                        if(graph.redEdges.get(0).weight > graph.blueEdges.get(0).weight){
                            chosen = graph.redEdges.remove(0);
                            chosenIsRed = true;
                        } else {
                            chosen = graph.blueEdges.remove(0);
                        }
                    }
                }
            } catch(Exception e){
                break;
            }



//            System.out.println("current subsets[graph.edges[index].source] : " + subSets[chosen.source] + " source is : " + chosen.source + " size of subsets is : " + subSets.length);
            try{


                        if(graph.FindSet(subSets, chosen.source) != graph.FindSet(subSets, chosen.destin)) { // cycle condition

                            if(chosenIsRed == true){
                                redTotalWeight += chosen.weight;
                                nbRedEdges++;
                            }else{
                                blueTotalWeight += chosen.weight;
                                nbBlueEdges++;
                            }
                            graph.Union(subSets, chosen.source, chosen.destin);

//                            System.out.println("added edge to MST : " + chosen.source + " - " + chosen.destin);
                        }

//                 If adding an edge creates a cycle, then reject that edge and go for the next least weight edge.


                        index++; // can be removed

            }catch (Exception e){break;}




        }

        // printing out the answer
        System.out.println(redTotalWeight + " " + blueTotalWeight);
        System.out.println("nb of vertices : " + graph.nbVert);
        System.out.println("nb of red edges : " + nbRedEdges);
        System.out.println("nb of blue edges : " + nbBlueEdges);
        System.out.println("nb of total valid edges : " + (nbRedEdges + nbBlueEdges));

    }

    public static class Graph {

        int nbVert, nbEdges;
        List<Edge> redEdges = new ArrayList<Edge>(); // array containing all edges, later, we will sort all of them
        List<Edge> blueEdges =  new ArrayList<Edge>();

        public Graph(int nbVert, int nbEdges) {
            this.nbVert = nbVert;
            this.nbEdges = nbEdges;
        }

        public void initRedBlue(ArrayList<Edge> redEdges, ArrayList<Edge> blueEdges){
            this.redEdges = redEdges;
            this.blueEdges = blueEdges;
        }

        // now that the graph is initialized, we need to create : Find-Set and Make-Set and Union()

        // Union method :
        void Union(sSet[] subsets, int x, int y){
            Link(subsets, FindSet(subsets, x), FindSet(subsets, y));
        }

        void Link(sSet[] subsets, int x, int y){ // slide 35 of L19
            if(subsets[x].rank > subsets[y].rank){
                subsets[y].parent = x;
            } else {
                subsets[x].parent = y;
                if(subsets[x].rank == subsets[y].rank){
                    subsets[y].rank = subsets[y].rank++;
                }
            }
        }

        // Find-Set method :
        int FindSet(sSet[] subSet, int index){
            if(subSet[index].parent != index){
                subSet[index].parent = FindSet(subSet, subSet[index].parent); // we then go further // slide 34 L19
            }
            return subSet[index].parent;
        }

        public void printGraph() {
//            Arrays.sort(edges); // sorting before printing
            for (int i = 0; i < redEdges.size(); i++) {
                System.out.println("here are all RED edges in decreasing order : ");
                System.out.println(" source : " + redEdges.get(i).source + " destination : " + redEdges.get(i).destin + " weight : " + redEdges.get(i).weight);
            }
            System.out.println("");

            for (int i = 0; i < blueEdges.size(); i++) {
                System.out.println("here are all BLUE edges in decreasing order : ");
                System.out.println(" source : " + blueEdges.get(i).source + " destination : " + blueEdges.get(i).destin + " weight : " + blueEdges.get(i).weight);
            }
            System.out.println("");
        }

        public static class Edge implements Comparable<Edge> { // we want to be able to compare the edges in order to sort them
            int source, destin, weight;

            Edge(int source, int destin, int weight){
                this.source = source;
                this.destin = destin;
                this.weight = weight;
            }

            public int compareTo(Edge compareEdge) {
                return compareEdge.weight -  this.weight;
            }
        }

        // this is the "Make-Set(v)" method
        public static class sSet {
            int parent, rank;
            sSet(int parent, int rank) {
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
