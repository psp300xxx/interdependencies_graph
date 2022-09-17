package graphs;

import org.junit.Test;
import utility.ProjectUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SoftMaxWeightingFunctionTest {

    private WeightingFunction weightingFunction = new SoftMaxWeightingFunction();
    private Random random = new Random();

    private final int MAX_RANDOM_BOUND = 20;


    @Test
    public void testAllNumbersArePercentage(){
        List<Double> input = new ArrayList<>();

        for( int i =0 ; i< random.nextInt(MAX_RANDOM_BOUND); i++ ){
            input.add( random.nextDouble()*MAX_RANDOM_BOUND );
        }
        List<Double> result = weightingFunction.getNewWeights(input);
        assertEquals(result.size(), input.size());
        assertTrue( result.stream().allMatch( (x) -> { return x<1.0 && x > 0.0; } ) );
    }

    @Test
    public void testWeightsSumCloseTo1(){
        List<Double> input = new ArrayList<>();

        for( int i =0 ; i< 2+random.nextInt(MAX_RANDOM_BOUND); i++ ) {
            input.add(random.nextDouble() * MAX_RANDOM_BOUND);
        }
        List<Double> result = weightingFunction.getNewWeights(input);
        assertEquals(result.size(), input.size());
        Optional<Double> sum = result.stream().reduce( (x, y) -> { return x+y; } );
        double realSum = sum.get();
        assertTrue(ProjectUtility.closeEnough(realSum, 1.0));
    }

}
