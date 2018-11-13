package spellCheck;
import java.util.*;

public class SCNode {
	private String[] data;
	SCNode next;
	Scanner in;
	
	public SCNode(String[] data, SCNode next, Scanner in) {
		this.data=data;
		this.next=next;
		this.in=in;
	}
	
	public Scanner getScanner() {
		return in;
	}
	
	public String[] getData() {
		return data;
	}
}
