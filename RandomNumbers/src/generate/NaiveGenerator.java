package generate;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class NaiveGenerator implements RandGenerator {
	
	
	protected Double[] probabilities; 
	protected Double[] cdf;
	protected Integer[] numbers;
	private   Random rand;
	private   Integer nrOfValues;
	//use this class to sort the array and the indices containing the probabilities 
	
	private class SortComparator implements Comparator<Integer> {
			private final Double[] p;
			public SortComparator(Double[] p) {
				this.p=p;
			}
			@Override
			public int compare(Integer arg0, Integer arg1) {
				// TODO Auto-generated method stub
				return Double.compare(p[arg0],p[arg1]);
			}
	}
	
	
	public  NaiveGenerator( Double[] probabilities ) {
		// TODO Auto-generated constructor stub
		if( probabilities == null )
			throw new NullPointerException();
		if(probabilities.length == 0 )
			throw new IllegalArgumentException();
		double sum = 0;
		for( Double d : probabilities){
			sum+=d;
		}
		if ( sum != 1 ){
			System.out.println("Probabilities do not sum to 1!");
			throw new IllegalArgumentException();
		}
		
		this.probabilities = probabilities;
		initialize();
	}
	
	private void initialize() {
		
		//mark the indices and sort them in descending order of the associated probabilities
		
		nrOfValues = probabilities.length;
		numbers = new Integer[nrOfValues];
		for(int i=0; i < nrOfValues; i++ )
			numbers[i]=i;
		Arrays.sort(numbers,new SortComparator(probabilities));
		
		//calculate the cumulative distribution function 
		
		cdf = new Double[nrOfValues];
		cdf[0]=probabilities[numbers[0]]; 				//initialize the first element
		for(int i=1;i< nrOfValues; i++){
			cdf[i]=cdf[i-1]+probabilities[numbers[i]];  //cumulate each following element
		}
		
		
		rand = new Random();							//create the uniform random generator object;
	}
	
	@Override
	public int nextInt() {
		
		double nr = rand.nextDouble(); 					//generate a uniform random number;
		return numbers[binarySearch(nr)];				//check which interval the generated number is in in the cdf
		
		}
	
		protected int binarySearch(double nr ){
			int lo = 0;
			int hi = nrOfValues -1 ;
			int mid=0;
			while ( lo <= hi){
				mid = lo + ( hi - lo ) /2;
				if ( nr > cdf[mid])
					lo=mid+1;
				else if ( mid > 0 && nr <= cdf[mid-1] )
					hi = mid-1;
				else break;
					
				}
			
			return mid;
		}
}	
