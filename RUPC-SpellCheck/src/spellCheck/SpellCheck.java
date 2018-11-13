package spellCheck;
import java.io.*;
import java.util.*;

public class SpellCheck {
	
	public static boolean isCorrect(String word, List<String> correct) { 
		if (correct==null) {
			return false;
		}
		//Word = word with first letter capital
		String Word=Character.toLowerCase(word.charAt(0))+word.substring(1);
		if (correct.contains(word)||correct.contains(Word)) {
			System.out.println("			Correct");
			return true;
		}
		for (String s:correct) {
			if ((word.equals(word.toUpperCase())&&word.equalsIgnoreCase(s))) {
				System.out.println("			Correct");
				return true;
			}
		}
		return false;
	}
	
	public static boolean isSpecial(String word) {

		if (word.equals(word.toUpperCase())) {
			System.out.println("			Possible Acronym");
			return true;
		}
		for (int i=0;i<word.length();i++) {
			//if word is not a character or is entirely uppercase
			if (word.charAt(i)<65||(word.charAt(i)>90&&word.charAt(i)<97)||word.charAt(i)>122) {
				System.out.println("			Special Case");
				return true;
			}
		}
		return false;
	}
	
	public static boolean isOmitted(String word,List<String> correct) { 
		if (correct==null) {
			return false;
		}
		List<String> corrections=new ArrayList<String>();
		int mismatchedLetters=0;
		boolean omitted=false;
		for (String s:correct) {
			mismatchedLetters=0;
			if (word.equals(s.substring(1))||word.equals(s.substring(0,s.length()-1))) {
				omitted=true;
				corrections.add(s);
				continue;
			}
			for (int i=0;i<word.length();i++) {
				if (word.charAt(i)!=s.charAt(i+mismatchedLetters)&&word.charAt(i)!=s.charAt(i+1)) {
					mismatchedLetters+=2;
				}
				else if (word.charAt(i)!=s.charAt(i+mismatchedLetters)) {
					mismatchedLetters+=1;
				}
				if (mismatchedLetters>1) {
					break;
				}
			}
			if (mismatchedLetters==1) {
				omitted=true;
				corrections.add(s);
			}
		}
		printList("Omissions",corrections);
		return omitted;
	}
	
	public static boolean isInserted(String word,List<String> correct) {
		if (correct==null) {
			return false;
		}
		List<String> corrections=new ArrayList<String>();
		int mismatchedLetters=0;
		boolean inserted=false;
		for (String s:correct) {
			mismatchedLetters=0;
			if (s.equals(word.substring(1))||s.equals(word.substring(0,word.length()-1))) {
				inserted=true;
				corrections.add(s);
				continue;
			}
			for (int i=0;i<s.length();i++) {
				if (s.charAt(i)!=word.charAt(i+mismatchedLetters)&&s.charAt(i)!=word.charAt(i+1)) {
					mismatchedLetters+=2;
				}
				else if (s.charAt(i)!=word.charAt(i+mismatchedLetters)) {
					mismatchedLetters+=1;
				}
				if (mismatchedLetters>1) {
					break;
				}
			}
			if (mismatchedLetters==1) {
				corrections.add(s);
				inserted=true;
			}
		}
		printList("Insertion",corrections);
		return inserted;
	}
	
	public static boolean isTransposed(String word,List<String> correct) {
		if (correct==null) {
			return false;
		}
		List<String> corrections=new ArrayList<String>();
		int mismatchedLetters=0;
		boolean transposed=false;
		for (String s:correct) {
			mismatchedLetters=0;
			for (int i=0;i<word.length()-1;i++) {
				if (word.charAt(i)!=s.charAt(i)      &&
					(word.charAt(i+1)==s.charAt(i)   &&
					word.charAt(i)==s.charAt(i+1)))
				{
					mismatchedLetters+=2;
				}
				else if (word.charAt(i)!=s.charAt(i)) {
					break;
				}
			}
			if (mismatchedLetters==2) {
				transposed=true;
				corrections.add(s);
			}
		}
		printList("Transposed",corrections);
		return transposed;
	}
	
	public static boolean isSubstituted(String word,List<String> correct) {
		if (correct==null) {
			return false;
		}
		List<String> corrections=new ArrayList<String>();
		int mismatchedLetters=0;
		boolean substituted=false;
		for (String s:correct) {
			mismatchedLetters=0;
			for (int i=0;i<word.length();i++) {
				if (Character.toLowerCase(word.charAt(i))!=Character.toLowerCase(s.charAt(i))) {
					mismatchedLetters++;
				}
				if (mismatchedLetters>1) {
					break;
				}
			}
			if (mismatchedLetters==1) {
				substituted=true;
				corrections.add(s);
			}
		}
		printList("Substitutions",corrections);
		return substituted;
	}
	
	public static boolean isCapital(String word,List<String> correct) {
		if (correct==null) {
			return false;
		}
		List<String> corrections=new ArrayList<>();
		boolean capitalized=false;
		for (String s:correct) {
			if (word.toLowerCase().equals(s.toLowerCase())) {
				capitalized=true;
				corrections.add(s);
			}
		}
		printList("Capitalization",corrections);
		return capitalized;
	}
	
	public static void printList(String correction, List<String> list){
		if (list.size()==0) {
			return;
		}
		System.out.print("			"+correction+": ");
		for (int i=0;i<list.size();i++) {
			System.out.print(list.get(i));
			if (i<list.size()-1) {
				System.out.print(", ");
			}
		}
		System.out.println();
	}
	
	public static SCNode createLists(Scanner in,File file,int length) {
		String[] wordArr=new String[length];
		for (int i=0;i<length;i++) {
			wordArr[i]=in.nextLine();
		}
		SCNode node=new SCNode(wordArr,null,in) ;
		return node;
	}
	
	public static void spellCheck(String[] correctWords, String[] checkWords) {
		//Stores correct words in hashmap for O(1) access
		HashMap<Integer,List<String>> map=new HashMap<>();
		String word;
		List<String> list=new ArrayList<>();
		boolean noSuggestion;
		for (int i=0;i<correctWords.length;i++) {
			if (map.get(correctWords[i].length())==null) {
				map.put(correctWords[i].length(),new ArrayList<>());
			}
			map.get(correctWords[i].length()).add(correctWords[i]);
		}
		for (int i=0;i<checkWords.length;i++) {
			noSuggestion=false;
			word=checkWords[i];
			System.out.println("		"+word);
			if (!isCorrect(word,map.get(word.length()))&&!isSpecial(word)) {
				if (
					!isOmitted(word,map.get(word.length()+1))     &
					!isInserted(word,map.get(word.length()-1))    &
					!isTransposed(word,map.get(word.length()))    &
					!isSubstituted(word,map.get(word.length()))   &
					!isCapital(word,map.get(word.length())))
				{
					noSuggestion=true;
				}
			}
			if (noSuggestion) {
				System.out.println("			No suggestions");
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException{

		File file=new File("test3.txt");
		Scanner in=new Scanner(file);
		int dataSets=Integer.parseInt(in.nextLine());
		int words;
		//The Linked List will never have more than 2 non-null nodes
		SCNode head;//Spell Check Node that stores linked List of data
		String[] correctWords;
		String[] checkWords;
		for (int i=0;i<dataSets;i++) {
			System.out.println("Dataset "+(i+1)+": ");
			//number of correct words in set
			words=Integer.parseInt(in.nextLine());
			System.out.println("	Correct Words: "+words);
			head=createLists(in,file,words);
			in=head.getScanner();
			correctWords=head.getData();

			//number of words to be checked
			words=Integer.parseInt(in.nextLine());
			System.out.println("	Test Words: "+words);
			head.next=createLists(in,file,words);
			in=head.next.getScanner();
			checkWords=head.next.getData();
			
			//Up Until this post, accesses and records data correctly
			spellCheck(correctWords,checkWords);
		}
	}
}
