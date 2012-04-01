package nl.tudelft.repository.externalRouting.scenario.model;

import be.ac.ulg.montefiore.run.totem.domain.exception.InvalidDomainException;
import be.ac.ulg.montefiore.run.totem.domain.exception.NodeNotFoundException;
import be.ac.ulg.montefiore.run.totem.domain.facade.InterDomainManager;
import be.ac.ulg.montefiore.run.totem.domain.model.Domain;
import be.ac.ulg.montefiore.run.totem.domain.persistence.DomainFactory;
import be.ac.ulg.montefiore.run.totem.scenario.exception.EventExecutionException;
import be.ac.ulg.montefiore.run.totem.scenario.model.Event;
import be.ac.ulg.montefiore.run.totem.scenario.model.EventResult;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.exception.InvalidTrafficMatrixException;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.facade.TrafficMatrixManager;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.model.TrafficMatrix;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.persistence.TrafficMatrixFactory;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import nl.tudelft.repository.externalRouting.routing.facade.ExternalRoutingException;
import nl.tudelft.repository.externalRouting.routing.facade.RoutingTools;
import nl.tudelft.repository.externalRouting.routing.model.jaxb.ExternalRouting;
import nl.tudelft.repository.externalRouting.routing.persistence.RoutingFactory;
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
        
        int asId = isSetASID() ? getASID() : InterDomainManager.getInstance().getDefaultDomain().getASID();
        int tmId;
        try {
            tmId = isSetTMID() ? getTMID() : TrafficMatrixManager.getInstance().getDefaultTrafficMatrixID(asId);
        } catch (InvalidTrafficMatrixException ex) {
            throw new EventExecutionException(ex);
        }
        
        ExternalRouting output;
        try {
            output = RoutingTools.execExternalCommand(getCmd(),asId,tmId);
            RoutingTools.applyExternalRouting(getLlcId(), asId, tmId, output);
        } catch (ExternalRoutingException ex) {
            logger.warn(ex);
        }
        
        return new EventResult();
    }
    
}
