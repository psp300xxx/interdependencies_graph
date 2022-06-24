package graphs;

import java.util.Collection;
import java.util.List;

public interface WeightingFunction {

    List<Double> getNewWeights(Collection<Double> input);

}
