public class Test 
{ 
    public static void main(String[] args) 
    { 
        int i = 100;  
          
        // automatic type conversion 
        long l = i;  
          
        // automatic type conversion 
        float f = l;  
        
        // automatic type conversion 
        double d = f; 
        System.out.println("Int value "+i); 
        System.out.println("Long value "+l); 
        System.out.println("Float value "+f); 
        System.out.println("Double value "+d);
        System.out.println("---------------------------------");
        int x = 1;
        while (x < 100) {
        	x*=2;
        }
        System.out.println("x = " + x);
    } 
} 