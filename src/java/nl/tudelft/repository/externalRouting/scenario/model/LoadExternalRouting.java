package nl.tudelft.repository.externalRouting.scenario.model;

import be.ac.ulg.montefiore.run.totem.domain.facade.InterDomainManager;
import be.ac.ulg.montefiore.run.totem.scenario.exception.EventExecutionException;
import be.ac.ulg.montefiore.run.totem.scenario.model.Event;
import be.ac.ulg.montefiore.run.totem.scenario.model.EventResult;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.exception.InvalidTrafficMatrixException;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.facade.TrafficMatrixManager;
import nl.tudelft.repository.externalRouting.routing.facade.RoutingTools;
import nl.tudelft.repository.externalRouting.routing.model.jaxb.ExternalRouting;
import nl.tudelft.repository.externalRouting.routing.persistence.RoutingFactory;
import nl.tudelft.repository.externalRouting.scenario.model.jaxb.impl.LoadExternalRoutingImpl;
import org.apache.log4j.Logger;

/**
 *
 * @author Hendrik van Antwerpen
 */
public class LoadExternalRouting extends LoadExternalRoutingImpl implements Event {

    private static final Logger logger = Logger.getLogger(LoadExternalRouting.class);
    
    public LoadExternalRouting() {
    }

    @Override
    public EventResult action() throws EventExecutionException {
        int asId = isSetASID() ? getASID() : InterDomainManager.getInstance().getDefaultDomain().getASID();
        int tmId;
        try {
            tmId = TrafficMatrixManager.getInstance().getDefaultTrafficMatrixID(asId);
        } catch (InvalidTrafficMatrixException ex) {
            throw new EventExecutionException(ex);
        }
        String fileName = getRoutingFile();
        ExternalRouting r = RoutingFactory.loadExternalRouting(fileName);
        RoutingTools.applyExternalRouting(getLlcId(), asId, tmId, r);
        return new EventResult();
    }
    
}
