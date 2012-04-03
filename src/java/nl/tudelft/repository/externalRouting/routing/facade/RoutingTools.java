/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tudelft.repository.externalRouting.routing.facade;

import be.ac.ulg.montefiore.run.totem.domain.exception.*;
import be.ac.ulg.montefiore.run.totem.domain.facade.InterDomainManager;
import be.ac.ulg.montefiore.run.totem.domain.model.Domain;
import be.ac.ulg.montefiore.run.totem.domain.model.Link;
import be.ac.ulg.montefiore.run.totem.domain.model.Node;
import be.ac.ulg.montefiore.run.totem.domain.model.Path;
import be.ac.ulg.montefiore.run.totem.domain.model.impl.LspImpl;
import be.ac.ulg.montefiore.run.totem.domain.model.impl.PathImpl;
import be.ac.ulg.montefiore.run.totem.domain.persistence.DomainFactory;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.exception.InvalidTrafficMatrixException;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.exception.LinkLoadComputerAlreadyExistsException;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.exception.LinkLoadComputerIdException;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.facade.LinkLoadComputerManager;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.facade.TrafficMatrixManager;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.model.LinkLoadComputer;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.model.LoadData;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.model.TrafficMatrix;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.model.impl.AbstractLinkLoadComputer;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.model.impl.SettableIPLoadData;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.persistence.TrafficMatrixFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import nl.tudelft.repository.externalRouting.routing.model.Lsp;
import nl.tudelft.repository.externalRouting.routing.model.FlowValue;
import nl.tudelft.repository.externalRouting.routing.model.jaxb.ExternalRouting;
import nl.tudelft.repository.externalRouting.routing.persistence.RoutingFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author hendrik
 */
public class RoutingTools {

    private static Logger logger = Logger.getLogger(RoutingTools.class);
    public static final String PARAM_DOMAIN = "\\{domain\\}";
    public static final String PARAM_TRAFFICMATRIX = "\\{trafficMatrix\\}";
    public static final String PARAM_ROUTINGOUTPUT = "\\{routingOutput\\}";

    public static void applyExternalRouting(String llcId, int asId, int tmId, final ExternalRouting er) {
        try {
            Domain domain = InterDomainManager.getInstance().getDomain(asId);
            final TrafficMatrix tm = TrafficMatrixManager.getInstance().getTrafficMatrix(asId, tmId);
            final String name = (llcId != null && !llcId.isEmpty()) ?
                    llcId : LinkLoadComputerManager.getInstance().generateId(domain, "EXT");

            LinkLoadComputer llc = new AbstractLinkLoadComputer(domain) {
                SettableIPLoadData data = new SettableIPLoadData(domain);
                
                @Override
                public void recompute() {
                    data.clear();
                    if (er.isSetRouting()) {
                        try {
                            for (FlowValue flowValue : (List<FlowValue>) er.getRouting().getFlowValue()) {
                                double traffic = tm.get(flowValue.getFlow().getSrc(), flowValue.getFlow().getDst());
                                List<Link> links = domain.getLinksBetweenNodes(flowValue.getLink().getSrc(), flowValue.getLink().getDst());
                                if (links.isEmpty()) {
                                    logger.warn("No links exist between " + flowValue.getLink().getSrc() + " & " + flowValue.getLink().getDst());
                                    return;
                                } else if (links.size() > 2) {
                                    logger.warn("Multiple links between " + flowValue.getLink().getSrc() + " & " + flowValue.getLink().getDst() + ", used only the first.");
                                }
                                Link link = links.iterator().next();
                                data.addTraffic(link, traffic * flowValue.getUtil());
                            }
                        } catch (NodeNotFoundException ex) {
                            logger.error(ex);
                        }
                    }
                    dataChanged();
                }

                @Override
                public List<TrafficMatrix> getTrafficMatrices() {
                    return new ArrayList();
                }

                @Override
                public String getShortName() {
                    return name;
                }

                @Override
                public LoadData getData() {
                    return data;
                }

                @Override
                public LoadData detachData() {
                    SettableIPLoadData oldData = data;
                    data = null;
                    return oldData;
                }
            };
            try {
                LinkLoadComputerManager.getInstance().addLinkLoadComputer(llc,true,name);
            } catch (LinkLoadComputerIdException ex) {
                logger.warn(ex);
            }
            llc.recompute();
            if (er.isSetLsps()) {
                for (Lsp lsp : (List<Lsp>) er.getLsps().getLsp()) {
                    Path path = new PathImpl(domain);
                    List<Node> nodes = new ArrayList();
                    for (String node : (List<String>) lsp.getPath().getNode()) {
                        nodes.add(domain.getNode(node));
                    }
                    try {
                        path.createPathFromNode(nodes);
                        domain.addLsp(new LspImpl(domain, lsp.isSetId() ? lsp.getId() : domain.generateLspId(), lsp.getReservation(), path));
                    } catch (LspAlreadyExistException ex) {
                        logger.error(ex);
                    } catch (LspNotFoundException ex) {
                        logger.error(ex);
                    } catch (DiffServConfigurationException ex) {
                        logger.error(ex);
                    } catch (InvalidPathException ex) {
                        logger.error(ex);
                    } catch (LinkCapacityExceededException ex) {
                        logger.error(ex);
                    }
                }
            }
        } catch (InvalidTrafficMatrixException ex) {
            java.util.logging.Logger.getLogger(RoutingTools.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LinkLoadComputerAlreadyExistsException ex) {
            java.util.logging.Logger.getLogger(RoutingTools.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidDomainException ex) {
            logger.warn(ex);
        } catch (NodeNotFoundException ex) {
            logger.warn(ex);
        }
    }

    public static ExternalRouting execExternalCommand(String cmd, int asId, int tmId) throws ExternalRoutingException {
        File routingFile = null;
        
        if (cmd.matches(".*" + PARAM_DOMAIN + ".*")) {
            File domFile = getTmpFile("domain");
            domFile.deleteOnExit();
            cmd = cmd.replaceAll(PARAM_DOMAIN, domFile.getPath());
            try {
                Domain domain = InterDomainManager.getInstance().getDomain(asId);
                DomainFactory.saveDomain(domFile.getPath(), domain);
            } catch (InvalidDomainException ex) {
                logger.warn(ex);
            }
        }
        if (cmd.matches(".*" + PARAM_TRAFFICMATRIX + ".*")) {
            File tmFile = getTmpFile("trafficMatrix");
            tmFile.deleteOnExit();
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
        if (cmd.matches(".*" + PARAM_ROUTINGOUTPUT + ".*")) {
            routingFile = getTmpFile("routingOutput");
            routingFile.deleteOnExit();
            cmd = cmd.replaceAll(PARAM_ROUTINGOUTPUT, routingFile.getPath());
        }
        try {
            logger.warn("executing: " + cmd);
            Process exec = Runtime.getRuntime().exec(cmd);
            exec.waitFor();
        } catch (InterruptedException ex) {
            throw new ExternalRoutingException(ex);
        } catch (IOException ex) {
            throw new ExternalRoutingException(ex);
        }
        if (routingFile != null) {
            return RoutingFactory.loadExternalRouting(routingFile);
        }
        return null;
    }

    private static File getTmpFile(String prefix) throws ExternalRoutingException {
        try {
            return File.createTempFile(prefix, ".xml");
        } catch (IOException ex) {
            throw new ExternalRoutingException(ex);
        }
    }

    private RoutingTools() {
    }

}
