package graphs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SoftMaxWeightingFunction implements WeightingFunction{
    @Override
    public List<Double> getNewWeights(Collection<Double> input) {
        double total = 0.0;
        for( Double curr : input ){
            total += Math.exp(curr);
        }
        List<Double> result = new ArrayList<>();
        for( Double curr : input ){
            result.add( Math.exp(curr)/total );
        }
        return result;
    }
}
