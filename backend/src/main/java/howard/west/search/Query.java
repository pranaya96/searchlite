package howard.west.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Query {

	// Term id -> position in index file
	private static Map<Integer, Long> posDict = new TreeMap<Integer, Long>();
	// Term id -> document frequency
	private static Map<Integer, Integer> freqDict = new TreeMap<Integer, Integer>();
	// Doc id -> doc name dictionary
	private static Map<Integer, String> docDict = new TreeMap<Integer, String>();
	// Term -> term id dictionary
	private static Map<String, Integer> termDict = new TreeMap<String, Integer>();
	// Index
	private static BaseIndex index = null;

	
	/* 
	 * Write a posting list with a given termID from the file 
	 * You should seek to the file position of this specific
	 * posting list and read it back.
	 * */
	private static PostingList readPosting(FileChannel fc, int termId)
			throws IOException {
		fc.position(posDict.get(termId));
		return index.readPosting(fc);
	}

	//method to get the intersection of list inside two postingLists
	public static List<Integer> getIntersection(List<Integer> aList1, List<Integer>aList2){
		List<Integer> newList = new ArrayList<Integer>();
		int i = 0; 
		int j = 0;
		while(i < aList1.size() && j < aList2.size()){
			if(aList1.get(i) < aList2.get(j)){
				i++;
			}
			else if(aList2.get(j) < aList1.get(i)){
				j++ ;
			}
			else{
				newList.add(aList1.get(i));
				i++ ;

			}
		}
		return newList;

	}

	public static void main(String[] args) throws IOException {
		/* Parse command line */
		if (args.length != 2) {
			System.err.println("Usage: java Query [Basic|VB|Gamma] index_dir");
			return;
		}

		/* Get index */
		String className = "cs276.assignments." + args[0] + "Index";
		try {
			Class<?> indexClass = Class.forName(className);
			index = (BaseIndex) indexClass.newInstance();
		} catch (Exception e) {
			System.err
					.println("Index method must be \"Basic\", \"VB\", or \"Gamma\"");
			throw new RuntimeException(e);
		}

		/* Get index directory */
		String input = args[1];
		File inputdir = new File(input);
		if (!inputdir.exists() || !inputdir.isDirectory()) {
			System.err.println("Invalid index directory: " + input);
			return;
		}

		/* Index file */
		RandomAccessFile indexFile = new RandomAccessFile(new File(input,
				"corpus.index"), "r");

		String line = null;
		/* Term dictionary */
		BufferedReader termReader = new BufferedReader(new FileReader(new File(
				input, "term.dict")));
		while ((line = termReader.readLine()) != null) {
			String[] tokens = line.split("\t");
			termDict.put(tokens[0], Integer.parseInt(tokens[1]));
		}
		termReader.close();

		/* Doc dictionary */
		BufferedReader docReader = new BufferedReader(new FileReader(new File(
				input, "doc.dict")));
		while ((line = docReader.readLine()) != null) {
			String[] tokens = line.split("\t");
			docDict.put(Integer.parseInt(tokens[1]), tokens[0]);
		}
		docReader.close();

		/* Posting dictionary */
		BufferedReader postReader = new BufferedReader(new FileReader(new File(
				input, "posting.dict")));
		while ((line = postReader.readLine()) != null) {
			String[] tokens = line.split("\t");
			posDict.put(Integer.parseInt(tokens[0]), Long.parseLong(tokens[1]));
			freqDict.put(Integer.parseInt(tokens[0]),
					Integer.parseInt(tokens[2]));
		}
		postReader.close();

		/* Processing queries */
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		/* For each query */
		while ((line = br.readLine()) != null) {
		    FileChannel indexChannel = indexFile.getChannel();
		    // Split the query into individual tokens.
		    String[] queryTokens = line.split(" ");
		    if (queryTokens.length <= 0) continue;

		    // Fetch all the posting lists from the index.
		    List<PostingList> postingLists = new ArrayList<PostingList>();
		    boolean noResults = false;
		    for (String queryToken : queryTokens) {
			// Get the term id for this token using the termDict map.
			Integer termId = termDict.get(queryToken);
			if (termId == null) {
			    noResults = true;
			    continue;
			}
			else postingLists.add(readPosting(indexChannel, termId));
		    }

		    if (noResults) {
			System.out.println("no results found");
			continue;
		    }

		    // You will be adding your code to actually intersect all the posting
		    // lists to find the results to the query below. For now, this prints out
		    // The content of each of the posting lists to help you verify the
		    // index you have built.
		 //    for (PostingList pl : postingLists) {
			// System.out.println("TermId: " + pl.getTermId());
			// System.out.print("  ");
			// for (Integer docId : pl.getList()) {
			//     System.out.print(" " + docId);
			// }
			// System.out.println();
		 //    }
		   
		    
		    //intersecting the posting lists to find the results to the query with one or more tokens
		    List<Integer> polist1 =  new ArrayList<Integer>();
		    List<Integer> polist2 =  new ArrayList<Integer>();
	        polist1 = postingLists.get(0).getList();
	        int i=0;
	        while (i < postingLists.size()-1){
	     	 	polist2 = postingLists.get(i+1).getList();
	     	 	polist1 = getIntersection(polist1, polist2);
	     	 	i++;
	     	}

	     	//to print in "directory/file" format
		    for (Integer docs : polist1) {
			    System.out.print(" " + docDict.get(docs));
			    System.out.println(docs);
			    System.out.println();
			}


		    // System.out.println(postingLists.get(0).getList());

		    
		   

	     	
		    /*
		     * TODO: Your code here
		     *       Perform query processing with the inverted index.
		     *       Make sure to print to stdout the list of documents
		     *       containing the query terms, one document file on each
		     *       line, sorted in lexicographical order.
		     */
		}
		br.close();
		indexFile.close();
	}
}
