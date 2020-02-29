
public class MyFirstClass 
{
	public static void main(String[] args)
	{
		System.out.println("Hello");
		/*
		 * Hi
		 * hello
		 * whats up
		
		int myInt = 5;
		long myLong = 1l; //lowercase 'l' at the end
		float myFloat = 1.0f; //specify the 'f' at the end
		double myDouble = 2.5d; //Dealing with money is easier with doubles over floats 
		System.out.println("My int is: " + myInt);
		System.out.println("My long is: " + myLong);
		System.out.println("My float is: " + myFloat);
		System.out.println("My double is: " + myDouble);
		
		float a = 0f;
		float b = 1f;
		for(int i = 0; i < 10; i++)
		{
			a += 0.1f;
		}
		System.out.println("A equals B: " + (a == b)); // why false? -> FLOATING POINT PRECISION (same with doubles as well)
		System.out.println("A: " + a);
		System.out.println("B: " + b);
		*/
		String s = new String("Hello ");
		String s2 = "Hello";
		System.out.println(s + s2);
		Integer integerClass = new Integer(5); //instantiating an integer class
		int pointToAdd = 1000;
		int score = 0;
		int looped = 0;
		for (int i = 0; i < 5000000; i++)
		{
			if (score < Integer.MAX_VALUE - pointToAdd) //so we dont exceed the max
			{
				score += pointToAdd;
			}
			if (score < 0)
			{
				looped++;
			}
		}
		System.out.println("My Score: " + score);
		System.out.println("We overflow: " + looped + " times");
	}
}
