package tests;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import generate.NaiveGenerator;
import generate.RandGenerator;

public class TestRandGenerator {
	RandGenerator generator;
	int nrOfValues;
	Double[] probabilities;
	
	@Before
	public void initialSetup(){
		 probabilities = new Double[]{0.01,0.99};
		 generator =  new NaiveGenerator(probabilities);
		 nrOfValues = probabilities.length;
	}
	
	@Test
	public void testRange(){
		int sampleSize=10000;
		for ( int i=0; i<sampleSize; i++){
			int number = generator.nextInt();
			assertTrue("generated number is not in range", number < nrOfValues && number >=0 );
		}
	}
	@Test
	public void testDistribution(){
		
		int sampleSize=100000;  								//the size of the sample 
		double tolerance=1e-2;
		int frequencies[] = new int[nrOfValues];			//array where we count the frequencies 
		for (int i=0;i<nrOfValues;i++)
			frequencies[i] = 0;						
		
		for(int i=0;i<sampleSize;i++){			
			frequencies[generator.nextInt()]++;						//generate sampleSize numbers and increase the frequency counts
		}
		for(int i=0;i<nrOfValues;i++){
			double fraction = (double)frequencies[i]/sampleSize;
			assertTrue("Tolerance not met", (fraction < probabilities[i]+tolerance) && ( fraction > probabilities[i]-tolerance ) );
		}
	
			
	}
	@Test 
	public void testMean(){
		int sampleSize=100000;  								//the size of the sample 
		double tolerance=1e-2;
		double theoreticalMean = 0;
		for ( int i=0; i<nrOfValues; i++ ){
			theoreticalMean+=probabilities[i]*(i);
		}
		
		double sampleMean=0;
		for(int i=0;i<sampleSize;i++){			
			sampleMean += generator.nextInt();						//generate sampleSize numbers and increase the frequency counts
		}
		
		assertEquals("sample and theoretical mean do not match",theoreticalMean,sampleMean/sampleSize,tolerance);
	}
	

}
