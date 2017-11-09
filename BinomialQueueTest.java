import static org.junit.Assert.*;

import javax.naming.SizeLimitExceededException;

import org.junit.Test;

public class BinomialQueueTest {

	@Test
	public void testGetMin() throws SizeLimitExceededException {
		BinomialQueue<Integer, String> bin = new BinomialQueue<>();
		
		int[] balancer = new int[255], divisor = new int[balancer.length];
		balancer[0] = balancer.length/2;
		divisor[0] = 1;
		bin.add(new Integer(balancer[0]), Integer.toHexString(balancer[0]));
		for (int i = 1; i < balancer.length; i++) {
			
			divisor[i] = divisor[(i-1)/2]*2;
			int cielDiv = (balancer[0] + divisor[i] - 1)/divisor[i];
			
			balancer[i] = i % 2 == 1
			            ? balancer[i/2] + cielDiv
			            : balancer[i/2-1] - cielDiv;
			
			bin.add(new Integer(balancer[i]), Integer.toHexString(balancer[i]));
		}
		
		for (int i = 0; i < balancer.length; i++) {
			assertEquals(Integer.toHexString(i), bin.removeMin());
		}
	}
}
