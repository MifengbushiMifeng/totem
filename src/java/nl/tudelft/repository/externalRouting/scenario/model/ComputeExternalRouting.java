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

    public static final String PARAM_DOMAIN = "\\{domain\\}";
    public static final String PARAM_TRAFFICMATRIX = "\\{trafficMatrix\\}";
    public static final String PARAM_ROUTINGOUTPUT = "\\{routingOutput\\}";
    
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
        
        File routingFile = null;
        String cmd = getCmd();
        if ( cmd.matches(".*"+PARAM_DOMAIN+".*") ) {
            File domFile = getTmpFile("domain");
            //domFile.deleteOnExit();
            cmd = cmd.replaceAll(PARAM_DOMAIN, domFile.getPath());
            try {
                Domain domain = InterDomainManager.getInstance().getDomain(asId);
                DomainFactory.saveDomain(domFile.getPath(), domain);
            } catch (InvalidDomainException ex) {
                logger.warn(ex);
            }
        }
        if ( cmd.matches(".*"+PARAM_TRAFFICMATRIX+".*") ) {
            File tmFile = getTmpFile("trafficMatrix");
            //tmFile.deleteOnExit();
            cmd = cmd.replaceAll(PARAM_TRAFFICMATRIX, tmFile.getPath());
            try {
                TrafficMatrix tm = TrafficMatrixManager.getInstance().getTrafficMatrix(asId, tmId);
                TrafficMatrixFactory.saveTrafficMatrix(tmFile.getPath(), tm);
            } catch (InvalidDomainException ex) {
                logger.warn(ex);
            } catch (NodeNotFoundException ex) {
                logger.warn(ex);
            } catch (InvalidTrafficMatrixException ex) {
                logger.warn(ex);
            }
        }
        if ( cmd.matches(".*"+PARAM_ROUTINGOUTPUT+".*") ) {
            routingFile = getTmpFile("routingOutput");
            //routingFile.deleteOnExit();
            cmd = cmd.replaceAll(PARAM_ROUTINGOUTPUT, routingFile.getPath());
        }
        try {
            logger.warn("executing: "+cmd);
            Process exec = Runtime.getRuntime().exec(cmd);
            exec.waitFor();
        } catch (InterruptedException ex) {
            throw new EventExecutionException(ex);
        } catch (IOException ex) {
            throw new EventExecutionException(ex);
        }
        if ( routingFile != null ) {
            ExternalRouting output = RoutingFactory.loadExternalRouting(routingFile);
            RoutingTools.applyExternalRouting(getLlcId(), asId, output);
        }
        return new EventResult();
    }

    private File getTmpFile(String prefix) throws EventExecutionException {
            try {
                return File.createTempFile(prefix, ".xml");
            } catch (IOException ex) {
                throw new EventExecutionException(ex);
            }
    }
    
}
