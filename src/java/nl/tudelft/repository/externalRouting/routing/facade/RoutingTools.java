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
import be.ac.ulg.montefiore.run.totem.trafficMatrix.exception.LinkLoadComputerAlreadyExistsException;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.facade.LinkLoadComputerManager;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.model.LoadData;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.model.TrafficMatrix;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.model.impl.AbstractLinkLoadComputer;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.model.impl.SettableIPLoadData;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import nl.tudelft.repository.externalRouting.routing.model.jaxb.ExternalRouting;
import nl.tudelft.repository.externalRouting.routing.model.Lsp;
import nl.tudelft.repository.externalRouting.routing.model.Route;
import org.apache.log4j.Logger;

/**
 *
 * @author hendrik
 */
public class RoutingTools {

    private static Logger logger = Logger.getLogger(RoutingTools.class);

    public static void applyExternalRouting(String llcId, int asId, final ExternalRouting er) {
        try {
            Domain domain = InterDomainManager.getInstance().getDomain(asId);
            final String name = (llcId != null && !llcId.isEmpty()) ? llcId : LinkLoadComputerManager.getInstance().generateId(domain);

            int idx = 0;
            LinkLoadComputerManager.getInstance().addLinkLoadComputer(new AbstractLinkLoadComputer(domain) {

                SettableIPLoadData data = new SettableIPLoadData(domain);

                @Override
                public void recompute() {
                    data.clear();
                    if (er.isSetRouting()) {
                        try {
                            for (Route route : (List<Route>) er.getRouting().getRoute()) {
                                List<Link> links = domain.getLinksBetweenNodes(route.getSrc(), route.getDst());
                                if (links.isEmpty()) {
                                    logger.warn("No links exist between " + route.getSrc() + " & " + route.getDst());
                                    return;
                                } else if (links.size() > 2) {
                                    logger.warn("Multiple links between " + route.getSrc() + " & " + route.getDst() + ", used only the first.");
                                }
                                Link link = links.iterator().next();
                                double load = Double.POSITIVE_INFINITY;
                                if (route.isSetLoad()) {
                                    load = route.getLoad();
                                } else if (route.isSetUtil()) {
                                    load = link.getBandwidth() * route.getUtil();
                                }
                                data.addTraffic(link, load);
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
            });
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
        } catch (LinkLoadComputerAlreadyExistsException ex) {
            java.util.logging.Logger.getLogger(RoutingTools.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidDomainException ex) {
            logger.warn(ex);
        } catch (NodeNotFoundException ex) {
            logger.warn(ex);
        }
    }

    private RoutingTools() {
    }
}
