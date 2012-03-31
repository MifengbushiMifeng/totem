package nl.tudelft.repository.externalRouting.scenario.model;

import be.ac.ulg.montefiore.run.totem.scenario.exception.EventExecutionException;
import be.ac.ulg.montefiore.run.totem.scenario.model.Event;
import be.ac.ulg.montefiore.run.totem.scenario.model.EventResult;
import nl.tudelft.repository.externalRouting.scenario.model.jaxb.impl.ComputeExternalRoutingImpl;
import org.apache.log4j.Logger;

/**
 *
 * @author Hendrik van Antwerpen
 */
public class ComputeExternalRouting extends ComputeExternalRoutingImpl implements Event {

    private static final Logger logger = Logger.getLogger(ComputeExternalRouting.class);
    
    public ComputeExternalRouting() {
    }

    @Override
    public EventResult action() throws EventExecutionException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
