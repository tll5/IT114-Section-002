import java.util.*;

public class MyClass2 
{
	public static void main(String[] args)
	{
		// java ClassSamples2 "something"
		if (args.length > 0)
		{
			System.out.println(args[0]);
		}
		Object[] objects = new Object[1]; //Format: type[] variable
		/*
		Object myObject = (Object)new MyClass2();
		System.out.println(myObject);
		MyClass2 myClass = (MyClass2)myObject; //casting a new Object once MyClass2 object is created
		System.out.println(myClass);
		*/
		System.out.println(objects.length); //initialization error (null/nothing) in the array
		objects[0] = "Test";
		
		//ADDING TO LISTS
		List<String> myList = new ArrayList<String>();
		myList.add("test");
		System.out.println("My List has a size of " + myList.size());
		
		Collection col = new ArrayList<Integer>();
		col.add(12);
		col.add(11);
		col.add(2);
		
		//THE ITERATOR FUNCTION
		Iterator it = col.iterator();
		while (it.hasNext())
		{
			System.out.println(it.next());
		}
		
		List<Integer> breakIt = new ArrayList<Integer>();
		for(int i = 0; i < 10; i++)
		{
			breakIt.add(i);
		}
		
		//DO-WHILE LOOP
		/*
		do
		{
			System.out.println("Hi");
		}
		while(false);
		*/
		
		//FOR-EACH LOOP - creates a lot of garbage and heavy 
		for(Integer i : breakIt) //more convenient if you don't know a max value 
		{
			System.out.println(i);
		}
		
		for(int i : breakIt)
		{
			if(i == 5)
			{
				//breakIt.remove(i);
			}
		}
		/*
		int size = breakIt.size();
		for(int i = 0; i < size; i++)
		{
			System.out.println(breakIt.get(i));
			if(i == 2)
			{
				int v = breakIt.remove(i);
				System.out.println("Removed: " + v);
			}
			if(i == 3)
			{
				System.out.println("Here's the missing value: " + i);
			}
		}
		*/
		System.out.println("Iterator Remove Sample");
		Iterator bIt = breakIt.iterator();
		int i = 0;
		while(bIt.hasNext())
		{
			bIt.hasNext();
			if(i == 2)
			{
				bIt.remove(); //removes the latest call to next
			}
			System.out.println(bIt.next());
			i++;
		}
		System.out.println("Correct Iteration");
		bIt = breakIt.iterator();
		while(bIt.hasNext())
		{
			System.out.println(bIt.next());
		}
		
		System.out.println("Queue is a Q");
		Queue<String> myQ = new LinkedList<String>();
		myQ.add("Hello");
		String peeked = myQ.peek();
		System.out.println("Peeked: " + peeked);
		String polled = myQ.poll();
		System.out.println("Polled: " + polled);
		System.out.println("Next Peek: " + myQ.peek());
	}
}
