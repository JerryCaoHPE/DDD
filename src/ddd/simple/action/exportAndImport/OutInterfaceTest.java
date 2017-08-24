package ddd.simple.action.exportAndImport;

import java.util.ArrayList;

public class OutInterfaceTest {
	public ArrayList<ArrayList<String>> test(ArrayList<ArrayList<String>> arg){
		System.out.println("调用外部接口");
		return arg;
	}
}
