/* TOTEM-v3.2 June 18 2008*/

/*
 * ===========================================================
 * TOTEM : A TOolbox for Traffic Engineering Methods
 * ===========================================================
 *
 * (C) Copyright 2004-2006, by Research Unit in Networking RUN, University of Liege. All Rights Reserved.
 *
 * Project Info:  http://totem.run.montefiore.ulg.ac.be
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU General Public License version 2.0 as published by the Free Software Foundation;
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
*/
package test;

import junit.framework.Assert;
import be.ac.ulg.montefiore.run.totem.domain.model.Domain;
import be.ac.ulg.montefiore.run.totem.domain.model.Link;
import be.ac.ulg.montefiore.run.totem.domain.model.Path;
import be.ac.ulg.montefiore.run.totem.domain.model.Node;
import be.ac.ulg.montefiore.run.totem.domain.model.impl.DomainImpl;
import be.ac.ulg.montefiore.run.totem.domain.model.impl.NodeImpl;
import be.ac.ulg.montefiore.run.totem.domain.model.impl.LinkImpl;
import be.ac.ulg.montefiore.run.totem.domain.model.impl.PathImpl;
import be.ac.ulg.montefiore.run.totem.domain.exception.*;
import be.ac.ulg.montefiore.run.totem.domain.facade.InterDomainManager;
import be.ac.ulg.montefiore.run.totem.repository.model.*;
import be.ac.ulg.montefiore.run.totem.repository.model.exception.*;
import nl.tudelft.repository.XAMCRA.XAMCRA;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.junit.Test;

/*
* Changes:
* --------
*
*
*/

/**
* Test SAMCRA
*
* <p>Creation date: 28 nov. 2005
*
* @author Ga�l Monfort (monfort@run.montefiore.ulg.ac.be)
*/

public class XAMCRATest {
    private Domain buildDomain() throws NodeAlreadyExistException, NodeNotFoundException, LinkAlreadyExistException, DomainAlreadyExistException {
        Domain domain = new DomainImpl(4456);

        domain.addNode(new NodeImpl(domain, "0"));
        domain.addNode(new NodeImpl(domain, "1"));
        domain.addNode(new NodeImpl(domain, "2"));
        domain.addNode(new NodeImpl(domain, "3"));
        domain.addNode(new NodeImpl(domain, "4"));
        domain.addNode(new NodeImpl(domain, "5"));

        Link lnk = new LinkImpl(domain, "link0-1", "0", "1", 2000);
        lnk.setDelay(4);
        domain.addLink(lnk);

        lnk = new LinkImpl(domain, "link1-2", "1", "2", 2000);
        lnk.setDelay(2);
        domain.addLink(lnk);

        lnk = new LinkImpl(domain, "link0-3", "0", "3", 4000);
        lnk.setDelay(4);
        domain.addLink(lnk);

        lnk = new LinkImpl(domain, "link3-2", "3", "2", 4000);
        lnk.setDelay(4);
        domain.addLink(lnk);

        lnk = new LinkImpl(domain, "link0-4", "0", "4", 4000);
        lnk.setDelay(2);
        domain.addLink(lnk);

        lnk = new LinkImpl(domain, "link4-5", "4", "5", 4000);
        lnk.setDelay(2);
        domain.addLink(lnk);

        lnk = new LinkImpl(domain, "link5-2", "5", "2", 4000);
        lnk.setDelay(2);
        domain.addLink(lnk);

        InterDomainManager.getInstance().removeAllDomains();
        InterDomainManager.getInstance().addDomain(domain);

        return domain;
    }

    /**
     * Test with only bw constraint
     */
    @Test
    public void testRouteBw() throws NodeAlreadyExistException, NodeNotFoundException, LinkAlreadyExistException, DomainAlreadyExistException, NoRouteToHostException, RoutingException, LinkNotFoundException, LspNotFoundException, LocalDatabaseException, InvalidPathException, AlgorithmInitialisationException, TotemActionExecutionException {
        Domain domain = buildDomain();

        XAMCRA xamcra = new XAMCRA();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("ASID", new Integer(domain.getASID()).toString());
        params.put("version", "SAMCRA");

        xamcra.start(params);

        LSPPrimaryRoutingParameter routingParams = new LSPPrimaryRoutingParameter("0", "2", "lsp1");
        routingParams.setBandwidth(3000);

        TotemActionList acList = xamcra.routeLSP(domain, routingParams);

        for (int i = 0; i < acList.size(); i++) {
            AddLspAction action;
            if (acList.get(i) instanceof AddLspAction) {
                action = (AddLspAction) acList.get(i);
                Path path = action.getLsp().getLspPath();
                System.out.println("Path: " + path.toString());
                action.execute();
            }
        }
        xamcra.stop();

        Path p = new PathImpl(domain);
        List<Node> lst = new ArrayList<Node>();
        lst.add(domain.getNode("0"));
        lst.add(domain.getNode("3"));
        lst.add(domain.getNode("2"));
        p.createPathFromNode(lst);
        Assert.assertEquals(domain.getLsp("lsp1").getLspPath(), p);

    }

    /**
     * Test with bw and delay constraint (path 0-3-2 does not satisfy constraints anymore)
     */
    @Test
    public void testRouteBwDelay() throws NodeAlreadyExistException, NodeNotFoundException, DomainAlreadyExistException, LinkAlreadyExistException, NoRouteToHostException, RoutingException, LinkNotFoundException, LspNotFoundException, LocalDatabaseException, InvalidPathException, AlgorithmInitialisationException, TotemActionExecutionException {
        Domain domain = buildDomain();

        XAMCRA xamcra = new XAMCRA();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("ASID", new Integer(domain.getASID()).toString());
        params.put("version", "SAMCRA");
        params.put("useDelay", "true");

        xamcra.start(params);

        LSPPrimaryRoutingParameter routingParams = new LSPPrimaryRoutingParameter("0", "2", "lsp1");
        routingParams.setBandwidth(3000);
        routingParams.putRoutingAlgorithmParameter("DelayConstraint", "7");

        TotemActionList acList = xamcra.routeLSP(domain, routingParams);

        for (int i = 0; i < acList.size(); i++) {
            AddLspAction action;
            if (acList.get(i) instanceof AddLspAction) {
                action = (AddLspAction) acList.get(i);
                Path path = action.getLsp().getLspPath();
                System.out.println("Path: " + path.toString());
                action.execute();
            }
        }

        xamcra.stop();

        // check computed path
        Path p = new PathImpl(domain);
        List<Node> lst = new ArrayList<Node>();
        lst.add(domain.getNode("0"));
        lst.add(domain.getNode("4"));
        lst.add(domain.getNode("5"));
        lst.add(domain.getNode("2"));
        p.createPathFromNode(lst);
        Assert.assertEquals(domain.getLsp("lsp1").getLspPath(), p);
    }

    ///non consecutive id tests
    @Test
    public void testNonConsecutiveIds() throws NodeAlreadyExistException, NodeNotFoundException, DomainAlreadyExistException, LinkAlreadyExistException, LinkNotFoundException, NoRouteToHostException, RoutingException, LspNotFoundException, LocalDatabaseException, InvalidPathException, AlgorithmInitialisationException, TotemActionExecutionException {
        Domain domain = buildDomain();

        domain.removeNode(domain.getNode("3"));
        domain.removeNode(domain.getNode("1"));

        Link lnk = new LinkImpl(domain, "link0-2", "0", "2", 2000);
        lnk.setDelay(2);
        domain.addLink(lnk);

        //now domain contains non consecutive node ids (0 2 4 5)

        System.out.print("Node ids: ");
        for (Node n : domain.getAllNodes()) {
            String strId = n.getId();
            System.out.print(domain.getConvertor().getNodeId(strId));
            System.out.print(" ");
        }
        System.out.println();


        XAMCRA xamcra = new XAMCRA();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("ASID", new Integer(domain.getASID()).toString());
        params.put("version", "SAMCRA");
        params.put("useDelay", "true");

        xamcra.start(params);

        LSPPrimaryRoutingParameter routingParams = new LSPPrimaryRoutingParameter("0", "5", "lsp1");
        routingParams.setBandwidth(4000);

        routingParams.putRoutingAlgorithmParameter("DelayConstraint", "7");

        TotemActionList acList = xamcra.routeLSP(domain, routingParams);

        for (int i = 0; i < acList.size(); i++) {
            AddLspAction action;
            if (acList.get(i) instanceof AddLspAction) {
                action = (AddLspAction) acList.get(i);
                Path path = action.getLsp().getLspPath();
                System.out.println("Path: " + path.toString());
                action.execute();
            }
        }

        xamcra.stop();


        // check computed path
        // only path 0-4-5 has sufficient bandwidth
        Path p = new PathImpl(domain);
        List<Node> lst = new ArrayList<Node>();
        lst.add(domain.getNode("0"));
        lst.add(domain.getNode("4"));
        lst.add(domain.getNode("5"));
        p.createPathFromNode(lst);
        Assert.assertEquals(domain.getLsp("lsp1").getLspPath(), p);
    }

    /**
     * Test with Metric and TEMetric Constraints. 3 paths exists : (0-1-2) (0-2-3) and (0-4-5-2). None of them meet
     * the constraints: 1st is to be rejected for Metric constraint, 2nd for TEMetric constraint and 3 for BW constraint
    **/
    @Test
    public void testMetricConstraint() throws NodeAlreadyExistException, NodeNotFoundException, DomainAlreadyExistException, LinkAlreadyExistException, RoutingException, LinkNotFoundException, LocalDatabaseException, AlgorithmInitialisationException, LinkCapacityExceededException, TotemActionExecutionException, DiffServConfigurationException {
        Domain domain = buildDomain();

        Link lnk;
        lnk = domain.getLink("link0-1");
        lnk.setMetric(20);
        lnk.setTEMetric(50);

        lnk = domain.getLink("link1-2");
        lnk.setMetric(240);
        lnk.setTEMetric(40);

        lnk = domain.getLink("link0-3");
        lnk.setMetric(20);
        lnk.setTEMetric(50);

        lnk = domain.getLink("link3-2");
        lnk.setMetric(20);
        lnk.setTEMetric(50);

        lnk = domain.getLink("link0-4");
        lnk.setMetric(80);
        lnk.setTEMetric(25);
        lnk.setBandwidth(10000);

        lnk = domain.getLink("link4-5");
        lnk.setMetric(80);
        lnk.setTEMetric(25);
        lnk.setBandwidth(1000);

        lnk = domain.getLink("link5-2");
        lnk.setMetric(80);
        lnk.setTEMetric(25);
        lnk.setBandwidth(10000);

        XAMCRA xamcra = new XAMCRA();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("ASID", new Integer(domain.getASID()).toString());
        params.put("version", "SAMCRA");
        params.put("useTEMetric", "true");
        params.put("useMetric", "true");

        xamcra.start(params);

        LSPPrimaryRoutingParameter routingParams = new LSPPrimaryRoutingParameter("0", "2", "lsp1");
        routingParams.setBandwidth(2000);
        routingParams.putRoutingAlgorithmParameter("TEMetricConstraint", "99");
        routingParams.putRoutingAlgorithmParameter("MetricConstraint", "250");

        boolean exception = false;
        TotemActionList acList = null;
        try {
            acList = xamcra.routeLSP(domain, routingParams);
            for (int i = 0; i < acList.size(); i++) {
                AddLspAction action;
                if (acList.get(i) instanceof AddLspAction) {
                    action = (AddLspAction) acList.get(i);
                    Path path = action.getLsp().getLspPath();
                    System.out.println("Path: " + path.toString());
                    action.execute();
                }
            }
        } catch (NoRouteToHostException e) {
            //e.printStackTrace();
            exception = true;
        }

        Assert.assertTrue(exception);

        xamcra.stop();

    }

    /*  test passed successfully
    @Test
    public void testMultipleRemove() throws NodeAlreadyExistException, NodeNotFoundException, DomainAlreadyExistException, LinkAlreadyExistException, NoRouteToHostException, RoutingException, LinkNotFoundException {
        Domain domain = buildDomain();

        XAMCRA xamcra = new XAMCRA();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("ASID", new Integer(domain.getASID()).toString());
        params.put("version", "SAMCRA");
        params.put("useTEMetric", "true");
        params.put("useMetric", "true");

        xamcra.start(params);

        LSPPrimaryRoutingParameter routingParams = new LSPPrimaryRoutingParameter("0", "2", "lsp1");
        routingParams.setBandwidth(2000);
        routingParams.putRoutingAlgorithmParameter("addLSP", "false");

        xamcra.printDB();

        TotemActionList acList = null;
        acList = xamcra.routeLSP(domain, routingParams);
        for (int i = 0; i < acList.size(); i++) {
            AddLspAction action;
            if (acList.get(i) instanceof AddLspAction) {
                action = (AddLspAction) acList.get(i);
                Path path = action.getLsp().getLspPath();
                System.out.println("final Path: " + path.toString());
                action.execute();
                xamcra.printDB();
                action.execute();
                xamcra.printDB();
            }
        }

        xamcra.stop();
    }
    */
}
