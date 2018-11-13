package spellCheck;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Main {
	static File file=new File("test1.txt");

	public static int correct(String[] correctWords, String word) {
		String lower=(word.charAt(0)+"").toLowerCase()+word.substring(1);
		for (int i=0;i<correctWords.length;i++) {
			if (correctWords[i].equals(word)||correctWords[i].equals(lower)) {
				System.out.println("		word is correct");
				return 1;
			}
		}
		return 0;
	}
	
	public static int acronym(String word) {
		if (word.toUpperCase().equals(word)) {
			System.out.println("		word is possible acronym");
			return 1;
		}
		return 0;
	}
	
	public static int special(String word) {
		word=word.toLowerCase();
		for (int i=0;i<word.length();i++) {
			if (word.charAt(i)<97||word.charAt(i)>122){
				System.out.print("		non-alphabet characters detected");
				return 1;
			}
		}
		return 0;
	}
	
	public static int omit(String correct, String word) {
		int wp=0;//word pointer
		int cp=0;//correct word pointer
		int incorrectLetters=0;//keeps track of number of mistakes
		List<String> ans=new ArrayList<String>();
		incorrectLetters=0;
		correct=correct.toLowerCase();
		if (correct.charAt(0)!=word.charAt(0)) {//if first letter is omitted, compare from end to beginning
			incorrectLetters++;//we already know first letter does not match, and loop ends before first letter of correct
			for (int j=0;j<word.length();j++) {
				if (word.charAt(word.length()-1-j)!=correct.charAt(correct.length()-1-j)) {
					incorrectLetters++;
				}
			}
		}
		else if (correct.charAt(correct.length()-1)!=word.charAt(word.length()-1)) {//if last letter is omitted, compare from beginning to end
			incorrectLetters++;//we already know last letter does not match, and loop ends before last letter of correct
			for (int j=0;j<word.length();j++) {
				if (word.charAt(j)!=correct.charAt(j)) {
					incorrectLetters++;
				}
			}
		}
		else {//if middle letter is omitted, compare every letter
			wp=0;
			cp=0;
			while (wp<word.length()) {
				if (word.charAt(wp)!=correct.charAt(cp)) {
					incorrectLetters++;
					wp--;//wp is added to in next line, this line is to compare current letter in word with next letter in correct
				}
				wp++;
				cp++;
			}
		}
		if (incorrectLetters==1) {
			ans.add(correct);
		}
		if (ans.size()!=0) {
			System.out.print("		Omissions: ");
			for (int i=0;i<ans.size();i++) {
				System.out.print(ans.get(i)+" ");
			}
			System.out.println();
			return 1;
		}
		return 0;
	}

	public static int insert(String correct, String word) {
		List<String> ans=new ArrayList<String>();
		int cp=0;//correct word place counter
		int wp=0;//word place counter
		int incorrect=0;//number of incorrect letters;
		wp=0;
		cp=0;
		incorrect=0;
		correct=correct.toLowerCase();
			if (word.charAt(0)!=correct.charAt(0)) {
				incorrect++;
				wp++;
			}
			else if (word.charAt(word.length()-1)!=correct.charAt(correct.length()-1)) {
				incorrect++;
			}
			for (int j=0;j<correct.length();j++) {
				if (incorrect>1) {
					break;
				}
				if (correct.charAt(cp)!=word.charAt(wp)) {
					incorrect++;
				}
				cp++;
				wp++;
			}
			if (incorrect==1) {
				System.out.println(correct);
				ans.add(correct);
			}
		if (ans.size()!=0) {
			System.out.print("		Insertions: ");
			for (int i=0;i<ans.size();i++) {
				System.out.print(ans.get(i)+" ");
			}
			System.out.println();
			return 1;
		}
		return 0;
	}
	
	public static int transpose(String correct, String word) {
		int incorrect=0;//number of incorrect letters
		List<String> ans=new ArrayList<String>();
		incorrect=0;
		correct=correct.toLowerCase();
		for (int j=0;j<word.length();j++) {
			if (incorrect>2) {
				break;
			}
			if (word.charAt(j)!=correct.charAt(j)&&j<word.length()-1) {
				if (word.charAt(j+1)==correct.charAt(j)&&word.charAt(j)==correct.charAt(j+1)) {
					incorrect+=2;
					j++;
				}
				else {
					incorrect+=3;//anything greater than 2 to make sure it doesn't pass as transpose
					break;
				}
			}
		}
		if (incorrect==2) {
			ans.add(correct);
		}
		
		if (ans.size()>0) {
			System.out.print("		Transposed: ");
			for (int i=0;i<ans.size();i++) {
				System.out.print(ans.get(i)+" ");
			}
			System.out.println();
			return 1;
		}
		return 0;
	}
	
	public static int substitute(String correct, String word) {
		int incorrect=0;
		List<String> ans=new ArrayList<String>();
		incorrect=0;
		correct=correct.toLowerCase();
		for (int j=0;j<word.length();j++) {
			if (word.charAt(j)!=correct.charAt(j)) {
				incorrect++;
			}
			if (incorrect>2) {
				break;
			}
		}
		if (incorrect==1) {
			ans.add(correct);
		}
		if (ans.size()>0) {
			System.out.print("		Substitutions: ");
			for (int i=0;i<ans.size();i++) {
				System.out.print(ans.get(i)+" ");
			}
			System.out.println();
			return 1;
		}
		return 0;
	}
	
	public static int capital(String correct, String word) {
		List<String> ans=new ArrayList<String>();
		if (word.toUpperCase().equals(correct.toUpperCase())&&!word.equals(correct)) {
			ans.add(correct);
		}
		if (ans.size()>0) {
			System.out.print("Capitals: ");
			for (int i=0;i<ans.size();i++) {
				System.out.print(ans.get(i));
			}
			System.out.println();
			return 1;
		}
		return 0;
	}
	
	public static void spellCheck(String[] correctWords, String[] words) {
		int corrections=0;
		String word;
		String correct;
		for (int i=0;i<words.length;i++) {
			corrections=0;
			word=words[i];
			System.out.println("	Test Word "+(i+1)+": "+word);
			for (int j=0;j<correctWords.length;j++) {
				correct=correctWords[j];
				corrections+=correct(correctWords,word);
				corrections+=acronym(word);
				corrections+=special(word);
				if (corrections==0) {
					if (correct.length()==word.length()) {
						corrections+=transpose(correct,word);
						corrections+=substitute(correct,word);
						corrections+=capital(correct,word);
					}
					else if (correct.length()==word.length()-1) {
						corrections+=insert(correct,word);
						
					}
					else if (correct.length()==word.length()+1) {
						corrections+=omit(correct,word);
						
					}
				}
				else {
					break;
				}
				if (corrections==0) {
					System.out.println("		(no suggestions)");
				}
				else {
					break;
				}
			}
		}
	}
	
	public static void main(String[] args) throws IOException{
		Scanner in=new Scanner(file);
		int dataSets=Integer.parseInt(in.nextLine());//number of datasets in current test file
		int setCounter=1;//current dataset
		String[] correctWords=new String[0];
		String[] words=new String[0];
		int numCorrect=0;
		int numTest;
		while (setCounter<=dataSets) {
			numCorrect=Integer.parseInt(in.nextLine());
			System.out.println("Analyzing "+setCounter+" Data Set(s)");
			System.out.println("Data Set "+setCounter+":");
			System.out.println("	Correct Words: "+numCorrect);
			correctWords=new String[numCorrect];
			for (int i=0;i<numCorrect;i++) {
				correctWords[i]=in.nextLine();
			}
			numTest=Integer.parseInt(in.nextLine());//determines how many test words are in this set
			words=new String[numTest];
			for (int i=0;i<numTest;i++) {
				words[i]=in.nextLine();
			}
			System.out.println("	Test Words: "+numTest);
			spellCheck(correctWords,words);
			setCounter++;
		}
		in.close();
	}
}
