package main;

import graphs.*;
import graphs.publish_subscribe.UnitDelegate;
import graphs.publish_subscribe.UnitMessage;
import utility.ProjectUtility;

import java.io.IOException;

public class Main implements UnitDelegate {

    public static final int ALONE_UNIT_COUNT = 10;

    public static void main(String[] args) throws InterruptedException, IOException {
        Main main = new Main();
        NDependencyUnit forest = new NDependencyUnit("Forest");
        forest.setDelegate(main);
        NDependencyUnit glacery = new NDependencyUnit("Glacier");
        glacery.setDelegate(main);
        for( int i = 0 ; i<ALONE_UNIT_COUNT; i++  ){
            AloneUnit aloneUnit = new AloneUnit( String.format("unit%d", i) );
            aloneUnit.setDelegate(main);
            aloneUnit.setState(0.6);
            if( i%2==0 ){
                forest.addInboundConnection(aloneUnit, 0.1);
            }
            else {
                glacery.addInboundConnection(aloneUnit, 0.1);
            }
        }
        forest.addInboundConnection(glacery, 0.6);
        forest.setUnitState(0.7);
        glacery.addInboundConnection(forest, 0.6);
        InterdependencyGraph<Unit> interdependencyGraph = new InterdependencyGraphImpl(forest, "test_graph", 12);
        interdependencyGraph.startUpdates();
        System.out.println(interdependencyGraph.getMeanState());
        glacery.setUnitState(0.7);
    }

    @Override
    public void updateReceived(Unit unit, UnitMessage messageReceived, double currentState) {
//        System.out.println( String.format("%s: state: %s", unit, currentState) );
    }

    @Override
    public void updateApplied(Unit unit, UnitMessage message, double newState) {
        System.out.println( String.format("%s: state: %s", unit, newState) );
    }
}

