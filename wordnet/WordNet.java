import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 * Your first task is to build the WordNet digraph:
 * each vertex v is an integer that represents a synset,
 * and each directed edge v→w represents that w is a hypernym
 * of v. The WordNet digraph is a rooted DAG: it is acyclic
 * and has one vertex—the root—that is an ancestor of every
 * other vertex. However, it is not necessarily a tree because
 * a synset can have more than one hypernym.
 * */
public class WordNet {

    private final SAP sap;
    private final ArrayList<String> synList; // 同位词集添加到这里 索引就是id

    private final HashMap<String, List<Integer>> synsetToIdMap; // 一个id对应多个名词, 所以反过来一个名词对应部分id

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms){
        // NlogN or better
        if(synsets == null || hypernyms == null){
            throw new IllegalArgumentException();
        }
        synList = new ArrayList<>();
        synsetToIdMap = new HashMap<>();
        int id = 0; // for each line, we got a id.
        In in = new In(synsets);

        // pre-processing synsets
        while(in.hasNextLine()){
            String synLine = in.readLine();
            String[] synFields = synLine.split(","); // synsets field 同位词的字段用 ',' 分割
            id = Integer.parseInt(synFields[0]); // synsets 第一个字段是id

            // synsets第二个字段是同位词集
            this.synList.add(synFields[1]); // 遍历每行同位词集，加入到synList里
            String[] nounSet = synFields[1].split(" "); // 同位词集中，各个同位词 用" "分割

            for(String noun : nounSet) {

                if(!synsetToIdMap.containsKey(noun)){
                    this.synsetToIdMap.put(noun, new ArrayList<>());
                }// 没有这个词才放进map， 有这个词说明这个词对应多个id
                // 对应多个id就要加入到List里面
                synsetToIdMap.get(noun).add(id);

            }
        }

        // create the digraph
        // 结点是数字代表同位词的id; a rooted DAG
        Digraph wordnet = new Digraph(id + 1); // start with 0, so length is (id+1)

        in = new In(hypernyms);
        // pre-processing hypernyms --> add edges to digraph
        while(in.hasNextLine()){
            String hyperLine = in.readLine();
            String[] hyperFields = hyperLine.split(","); // hypernyms field 字段用 ','分割

            int v = Integer.parseInt(hyperFields[0]); // 第一个字段是v结点，后续字段是w结点（可能有多个）

            for(int i = 1; i < hyperFields.length; i++){
                wordnet.addEdge(v, Integer.parseInt(hyperFields[i])); // v->w
            }
        }

        // Throw an IllegalArgumentException: if The input to the constructor does not correspond to a rooted DAG.

        int rootNumber = 0;
        for(int i = 0; i < wordnet.V(); i++){
            if(wordnet.outdegree(i) == 0){
                rootNumber++;
            }
        }
        // check if rooted
        if(rootNumber != 1){
            throw new IllegalArgumentException();
        }

        // Check whether is an acyclic graph: use DirectedCycle
        DirectedCycle dr = new DirectedCycle(wordnet);
        if(dr.hasCycle()){
            throw new IllegalArgumentException();
        }


//        TopologicalSort tls = new TopologicalSort(wordnet);
//        int root = 0;
//        while(!tls.reversePost.isEmpty()){
//            root = tls.reversePost.pop();
//        }

        sap = new SAP(wordnet);

        // StdOut.println(root);

    }

    // Topological sort: DFS implementation
//    private class TopologicalSort{
//        private boolean[] marked;
//        private Stack<Integer> reversePost;
//
//
//        public TopologicalSort(Digraph G){
//            marked = new boolean[G.V()];
//            reversePost = new Stack<>();
//
//            for(int v = 0; v < G.V(); v++){
//                if(!marked[v]){
//                    dfs(G, v);
//                }
//            }
//        }
//
//        private void dfs(Digraph G, int v){
//            marked[v] = true;
//            for(int w : G.adj(v)){
//                if(!marked[w]){
//                    dfs(G, w);
//                }
//            }
//            reversePost.push(v);
//        }
//
//        public Iterable<Integer> reversePost(){
//            return reversePost;
//        }
//    }


    // returns all WordNet nouns
    public Iterable<String> nouns(){
        return synsetToIdMap.keySet(); // ....
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word){
        // requirement: logN or better
        // my solution: O(1)
        if(word == null){
            throw new IllegalArgumentException();
        }

        return synsetToIdMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB){
        // requirement: N
        if(nounA == null || nounB == null){
            throw new IllegalArgumentException();
        }
        if(!this.isNoun(nounA) || !this.isNoun(nounB)){
            throw new IllegalArgumentException();
        }
        return sap.length(synsetToIdMap.get(nounA), synsetToIdMap.get(nounB));

    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB){
        // requirement: N
        if(nounA == null || nounB == null){
            throw new IllegalArgumentException();
        }
        if(!this.isNoun(nounA) || !this.isNoun(nounB)){
            throw new IllegalArgumentException();
        }
        return synList.get(
                sap.ancestor(
                        synsetToIdMap.get(nounA), synsetToIdMap.get(nounB)
                )
        );
    }

    // do unit testing of this class
    public static void main(String[] args){
        WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");
        int a = 0;
    }

}