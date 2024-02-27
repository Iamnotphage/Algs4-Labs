import java.io.File;
import java.io.FileInputStream;
import edu.princeton.cs.algs4.StdOut;
import java.io.BufferedReader;
import java.io.InputStreamReader;


// https://corpus.canterbury.ac.nz/descriptions/
// Test Data: bible.txt; bible_lowercase.txt; bible_query.txt; corpus.txt; query.txt
// All the data: lowercase and spaces

public class SearchIndex{
    // the corpus text
    public static String readTxt(String filename) {

        File file = new File("D:\\Learning\\ComputerScience\\Algorithms4\\TextIndexes\\src\\" + filename);
        FileInputStream is = null;
        StringBuilder stringBuilder = null;

        try{
            if(file.length() != 0){

                is = new FileInputStream(file);
                InputStreamReader streamReader = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(streamReader);
                String line;
                stringBuilder = new StringBuilder();

                while((line = reader.readLine()) != null){

                    stringBuilder.append(line);

                    stringBuilder.append(' ');

                }
                reader.close();
                is.close();
            }else{
                StdOut.println("error");
            }

        }catch(Exception e) {
            e.printStackTrace();
        }

        return String.valueOf(stringBuilder);

    }

    public static void main(String[] args) {

        String corpus = readTxt(args[0]);
        // the query text : pattern string
        File file = new File("D:\\Learning\\ComputerScience\\Algorithms4\\TextIndexes\\src\\" + args[1]);
        FileInputStream is = null;

        try {
            if(file.length() != 0) {
                is = new FileInputStream(file);
                InputStreamReader streamReader = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(streamReader);
                String line;
                while((line = reader.readLine()) != null) {

                    BoyerMoore boyermoore = new BoyerMoore(line);
                    int offset = boyermoore.search(corpus);

                    if(offset == -1){
                        StdOut.println("-- " + line); // Not Found
                    }else{
                        int pos = boyermoore.searchPos(corpus,offset);
                        StdOut.print(pos + 1);
                        StdOut.println(' ' + line);
                    }

                }
                reader.close();
                is.close();
            }else{
                StdOut.println("error");
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

}