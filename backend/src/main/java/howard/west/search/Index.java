package howard.west.search;

import howard.west.util.Pair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

public class Index {

	// Term id -> (position in index file, doc frequency) dictionary
	private static Map<Integer, Pair<Long, Integer>> postingDict 
		= new TreeMap<Integer, Pair<Long, Integer>>();
	// Doc name -> doc id dictionary
	private static Map<String, Integer> docDict
		= new TreeMap<String, Integer>();
	// Term -> term id dictionary
	private static Map<String, Integer> termDict
		= new TreeMap<String, Integer>();
	// Block queue
	private static LinkedList<File> blockQueue
		= new LinkedList<File>();
	



	// Total file counter
	private static int totalFileCount = 0;
	// Document counter
	private static int docIdCounter = 0;
	// Term counter
	private static int wordIdCounter = 0;
	// Index
	private static BaseIndex index = null;

	
	/* 
	 * Write a posting list to the given file 
	 * You should record the file position of this posting list
	 * so that you can read it back during retrieval
	 * 
	 * */
	private static void writePosting(FileChannel fc, PostingList posting)
			throws IOException {
		
		postingDict.put(posting.getTermId(), new Pair<Long, Integer>(fc.position(), posting.getList().size()));
		index.writePosting(fc, posting);

	}

	public static void main(String[] args) throws IOException {
		/* Parse command line */
		if (args.length != 3) {
			System.err
					.println("Usage: java Index [Basic|VB|Gamma] data_dir output_dir");
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

		/* Get root directory */
		String root = args[1];
		File rootdir = new File(root);
		if (!rootdir.exists() || !rootdir.isDirectory()) {
			System.err.println("Invalid data directory: " + root);
			return;
		}

		/* Get output directory */
		String output = args[2];
		File outdir = new File(output);
		if (outdir.exists() && !outdir.isDirectory()) {
			System.err.println("Invalid output directory: " + output);
			return;
		}

		if (!outdir.exists()) {
			if (!outdir.mkdirs()) {
				System.err.println("Create output directory failure");
				return;
			}
		}

		/* A filter to get rid of all files starting with .*/
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				String name = pathname.getName();
				return !name.startsWith(".");
			}
		};

		/* BSBI indexing algorithm */
		File[] dirlist = rootdir.listFiles(filter);

		/* For each block */
		for (File block : dirlist) {
			File blockFile = new File(output, block.getName());
			blockQueue.add(blockFile);

			// docID -> PostingList
			Map<Integer, PostingList> postingListMap = new TreeMap<Integer, PostingList>();
			File blockDir = new File(root, block.getName());
			File[] filelist = blockDir.listFiles(filter);
			
			/* For each file */
			for (File file : filelist) {
				++totalFileCount;
				String fileName = block.getName() + "/" + file.getName();
				docDict.put(fileName, docIdCounter++);
				
				
				
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line;
				while ((line = reader.readLine()) != null) {
					String[] tokens = line.trim().split("\\s+");
					for (String token : tokens) {
						/*
						 * TODO: Your code here
						 *       For each term, build up a list of
						 *       documents in which the term occurs
						 */

                 

                    
						if (!(termDict.containsKey(token))) {
							termDict.put(token, wordIdCounter++);
						}
						
						if(!(postingListMap.containsKey(termDict.get(token)))) {
							PostingList myPostingList = new PostingList(termDict.get(token));
							List<Integer> list = myPostingList.getList();
							list.add(docDict.get(fileName));
							postingListMap.put(termDict.get(token), myPostingList);
						}
						else {
							PostingList myPostingList =postingListMap.get(termDict.get(token));
							List<Integer> list = myPostingList.getList();
							if(!(list.contains(docDict.get(fileName)))){
								list.add(docDict.get(fileName));
							}

						}
						// System.out.println("Processing token " + token + " for file " + fileName);  
					}
				}
				// //printing to check the TreeMap for printing it with key and values
				// for(Map.Entry<Integer, PostingList> each : postingListMap.entrySet()) {
				// 	Integer key = each.getKey();
				// 	PostingList value = each.getValue();

				// 	System.out.println(key + " ==> " + value.getTermId() + value.getList().toString());	
					
					
				

				
				

				reader.close();
			}

			
			/* Sort and output */
			if (!blockFile.createNewFile()) {
				System.err.println("Create new block failure.");
				return;
			}
			
			RandomAccessFile bfc = new RandomAccessFile(blockFile, "rw");
			FileChannel newFc = bfc.getChannel();

			
			/*
			 * TODO: Your code here
			 *       Write all posting lists for all terms to file (bfc) 
			 */
			for(PostingList postings: postingListMap.values()){
				writePosting(newFc, postings);
				index.writePosting(newFc, postings);
			}
			postingListMap.clear();
			bfc.close();
		}

		/* Required: output total number of files. */
		System.out.println(totalFileCount);

		/* Merge blocks */
		while (true) {
			if (blockQueue.size() <= 1)
				break;

			File b1 = blockQueue.removeFirst();
			File b2 = blockQueue.removeFirst();
			
			File combfile = new File(output, b1.getName() + "+" + b2.getName());
			if (!combfile.createNewFile()) {
				System.err.println("Create new block failure.");
				return;
			}
			// System.out.println(b1.toString());
			RandomAccessFile bf1 = new RandomAccessFile(b1, "r");
			RandomAccessFile bf2 = new RandomAccessFile(b2, "r");
			RandomAccessFile mf = new RandomAccessFile(combfile, "rw");
			 
			/*
			 * TODO: Your code here
			 *       Combine blocks bf1 and bf2 into our combined file, mf
			 *       You will want to consider in what order to merge
			 *       the two blocks (based on term ID, perhaps?).
			 *       
			 */
			FileChannel fc1 = bf1.getChannel();
			FileChannel fc2 = bf2.getChannel();
			FileChannel fcmf = mf.getChannel();

			PostingList p1 = index.readPosting(fc1);
			PostingList p2 = index.readPosting(fc2);

			while(p1!=null && p2 != null ){
				if (p1.getTermId() < p2.getTermId()){
					writePosting(fcmf, p1);
					// index.writePosting(fcmf, p1);
					p1 = index.readPosting(fc1);
					
				}
				else if(p2.getTermId() < p1.getTermId()){
					writePosting(fcmf, p2);
					// index.writePosting(fcmf,p2);
					p2 = index.readPosting(fc2);
					
				}
					

				//case when posting lists are equal
				else {
					PostingList newPlist = new PostingList(p1.getTermId());
					Iterator<Integer> it1 = p1.getList().iterator();
					Integer doc1 = getNext(it1);
					Iterator<Integer> it2 =  p2.getList().iterator();
					Integer doc2 = getNext(it2);

					while (doc1!=null && doc2 != null){
						if(doc1 < doc2){
							newPlist.getList().add(doc1);
							doc1 = getNext(it1);
						}
						else if (doc2 < doc1){
							newPlist.getList().add(doc2);
							doc2 = getNext(it2);
						}
						else{
							// newPlist.getList().add(doc2);
							doc1 = getNext(it1);
							// doc2 = getNext(it2);
						}
					}
					while(doc2!=null){
						newPlist.getList().add(doc2);
						doc2 = getNext(it2);
					}
					while (doc1!=null){
						newPlist.getList().add(doc1);
						doc1 = getNext(it1);
					}
					
					writePosting(fcmf,newPlist);
					// index.writePosting(fcmf,newPlist);
					p1 = index.readPosting(fc1);
					p2 = index.readPosting(fc2);

				}






			}
			while(p1!=null){
				writePosting(fcmf, p1);
				// index.writePosting(fcmf, p1);
				p1 = index.readPosting(fc1);
				
			}
			while(p2!=null){
				writePosting(fcmf, p2);
				// index.writePosting(fcmf, p2);
				p2 = index.readPosting(fc2);
				
			}

			bf1.close();
			bf2.close();
			mf.close();
			b1.delete();		
			b2.delete();
			blockQueue.add(combfile);
			
		}

		/* Dump constructed index back into file system */
		File indexFile = blockQueue.removeFirst();
		indexFile.renameTo(new File(output, "corpus.index"));

		BufferedWriter termWriter = new BufferedWriter(new FileWriter(new File(
				output, "term.dict")));
		for (String term : termDict.keySet()) {
			termWriter.write(term + "\t" + termDict.get(term) + "\n");
		}
		termWriter.close();

		BufferedWriter docWriter = new BufferedWriter(new FileWriter(new File(
				output, "doc.dict")));
		for (String doc : docDict.keySet()) {
			docWriter.write(doc + "\t" + docDict.get(doc) + "\n");
		}
		docWriter.close();

		BufferedWriter postWriter = new BufferedWriter(new FileWriter(new File(
				output, "posting.dict")));
		for (Integer termId : postingDict.keySet()) {
			postWriter.write(termId + "\t" + postingDict.get(termId).getFirst()
					+ "\t" + postingDict.get(termId).getSecond() + "\n");
		}
		postWriter.close();
		return;
	}
	static<I> I getNext(Iterator<I> item){
		if (item.hasNext()){
			return item.next();
		}
		return null;
	}

}
