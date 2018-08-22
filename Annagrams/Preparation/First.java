//HashMap for country codes
import java.util.*;  

public class First {
    //void main
        public static void main (String[] args)
        {
            String name = "Nandu";
            char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();


            for(char letter:alphabet){
                String newWord = name + letter;
                System.out.println(newWord);
            }
            //declare float
            float a,b,area = 0;
            char choice;
            HashMap<String,String> hm=new HashMap<String,String>();  



            hm.put("GBR","United Kingdom of Great Britain and Northern Ireland");  
  			hm.put(" IDN ","Indonesia");  
  			hm.put("IND","India");  

            //Declare input as scanner
            Scanner input = new Scanner(System.in);
 			System.out.println("Enter code:");
            String s = input.next();

 			if(s.length()==3){
            //Take inputs
 			 String get = hm.get(s);
             System.out.println(get);
             // System.out.println(hm.getKey(get));
        	}
        	else{
        		Boolean flag = false;
        		String key = null;
        		for(Map.Entry entry: hm.entrySet()){
            		if(s.equals(entry.getValue())){
                		System.out.println("CODE:"+entry.getKey());
                		flag=true;
                		break; //breaking because its one to one map
            		}
        		}

        		if(flag == false){
        			System.out.println("ENTHutt INPUT aDO!");
        		}


        	}
    	}
}