package main;

import graphs.*;

import java.util.LinkedList;
import java.util.List;

public class Main {

    public static final int ALONE_UNIT_COUNT = 10;

    public static void main(String[] args) {
        NDependencyUnit forest = new NDependencyUnit("Forest");
        forest.setUnitWeightOverConnections(0.6);
        NDependencyUnit glacery = new NDependencyUnit("Glacery");
        glacery.setUnitWeightOverConnections(0.5);
        for( int i = 0 ; i<ALONE_UNIT_COUNT; i++  ){
            AloneUnit aloneUnit = new AloneUnit( String.format("unit%d", i) );
            aloneUnit.setState(0.6);
            if( i%2==0 ){
                forest.addConnection(aloneUnit, 0.1);
            }
            else {
                glacery.addConnection(aloneUnit, 0.1);
            }
        }
        forest.addConnection(glacery, 0.6);
        forest.setUnitState(0.7);
        glacery.addConnection(forest, 0.6);
        InterdependencyGraph<Unit> interdependencyGraph = new InterdependencyGraphImpl(forest);
        System.out.println(interdependencyGraph.getMeanState());
        interdependencyGraph.visitGraph( (x)->{
            if( !(x instanceof AloneUnit)){
                System.out.println(x.getName() + " "+ x.getState());
            }
        } );
    }
}

