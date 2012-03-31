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
import be.ac.ulg.montefiore.run.totem.domain.model.*;
import be.ac.ulg.montefiore.run.totem.domain.model.impl.PathImpl;
import be.ac.ulg.montefiore.run.totem.domain.model.impl.LspImpl;
import be.ac.ulg.montefiore.run.totem.domain.persistence.DomainFactory;
import be.ac.ulg.montefiore.run.totem.domain.exception.*;
import be.ac.ulg.montefiore.run.totem.repository.CSPF.CSPF;
import be.ac.ulg.montefiore.run.totem.repository.model.LSPPrimaryRoutingParameter;
import be.ac.ulg.montefiore.run.totem.repository.model.TotemActionList;
import be.ac.ulg.montefiore.run.totem.repository.model.TotemAction;
import be.ac.ulg.montefiore.run.totem.repository.model.exception.RoutingException;
import be.ac.ulg.montefiore.run.totem.repository.model.exception.NoRouteToHostException;
import be.ac.ulg.montefiore.run.totem.repository.model.exception.TotemActionExecutionException;

import java.io.FileNotFoundException;
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
* Test preemption in DiffServ environnement
*
* <p>Creation date: 20-Oct-2005
*
* @author Ga�l Monfort (monfort@run.montefiore.ulg.ac.be)
*/

// TODO: test setup and holding preemption levels

public class PreemptTest {

    /**
     * Try to add a lsp on a path with insufficent free bandwidth and check reservations on lsp links.
     */
    @Test
    public void testPreemptImpos() throws NodeNotFoundException, LspAlreadyExistException, LspNotFoundException, LinkNotFoundException, DiffServConfigurationException, InvalidPathException, InvalidDomainException, FileNotFoundException {
        String fileName = "src/resources/junit-test/test-domain-preempt.xml";
        Domain domain = DomainFactory.loadDomain(fileName);

        Path p1 = new PathImpl(domain);
        Path p2 = new PathImpl(domain);

        List<Node> nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("0"));
        nodeList.add(domain.getNode("2"));
        nodeList.add(domain.getNode("2bis"));
        nodeList.add(domain.getNode("3"));

        p1.createPathFromNode(nodeList);

        nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("1"));
        nodeList.add(domain.getNode("2"));
        nodeList.add(domain.getNode("2bis"));
        nodeList.add(domain.getNode("4"));

        p2.createPathFromNode(nodeList);

        Lsp lspa = new LspImpl(domain, "lspa", 140000, p1, 0, 0, 0);    // id priority: 0
        Lsp lspb = new LspImpl(domain, "lspb", 40000, p2, 0, 0, 0);     // id priority: 0

        boolean error = false;
        try {
            domain.addLsp(lspa);
            domain.addLsp(lspb);
        } catch (LinkCapacityExceededException e) {
            error = true;
        }
        Assert.assertTrue(error);

        Assert.assertEquals(domain.getLsp("lspa").getId(), "lspa");

        error  = false;
        try {
            domain.getLsp("lspb");
        }
        catch (LspNotFoundException e) {
            error = true;
        }
        Assert.assertTrue(error);

        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(0), 140000, 0.0001);
        Assert.assertEquals(domain.getLink("1_1 -> 2_1").getReservedBandwidth(0), 0, 0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 2b_2").getReservedBandwidth(0), 140000, 0.0001);
        Assert.assertEquals(domain.getLink("2b_3 -> 3_3").getReservedBandwidth(0), 140000, 0.0001);
        Assert.assertEquals(domain.getLink("2b_4 -> 4_2").getReservedBandwidth(0), 0, 0.0001);

   }

    @Test
    public void test2PreemptNoBwSharing() throws InvalidDomainException, FileNotFoundException, LspAlreadyExistException, DiffServConfigurationException, LspNotFoundException, LinkCapacityExceededException, LinkNotFoundException, NodeNotFoundException, InvalidPathException, TotemActionExecutionException {
        String fileName = "src/resources/junit-test/test-domain-preempt.xml";
        Domain domain = DomainFactory.loadDomain(fileName, true, false);

        Assert.assertTrue(domain.usePreemption());

        try {
            establishtest2(domain);
        } catch (NoRouteToHostException e) {
            Assert.fail("No route to host");
        } catch (RoutingException e) {
            Assert.fail("routing exception");
        }

        Assert.assertEquals(domain.getNbLsps(), 1);

        // lsp 2 should have preempted lsp1
        Assert.assertNotNull(domain.getLsp("lsp2"));

        Link link = domain.getLink("2_2 -> 2b_2");

//        System.out.println(link.getReservableBandwidth());
//        System.out.println(link.getReservableBandwidth(0));
//        System.out.println(link.getReservableBandwidth(1));
//        System.out.println(link.getReservableBandwidth(4));
//        System.out.println(link.getReservableBandwidth(5));
//        System.out.println(link.getReservableBandwidth(6));
//        System.out.println(link.getReservableBandwidth(7));

        //at lowest classType (1) and lowest preemption level (3)
        Assert.assertEquals(link.getReservableBandwidth(), 4000f);

        //prempt 0
        Assert.assertEquals(link.getReservableBandwidth(4), 50000f);
        //preemt 1
        Assert.assertEquals(link.getReservableBandwidth(5), 50000f);
        //preempt 2
        Assert.assertEquals(link.getReservableBandwidth(6), 4000f);
        //preempt 3
        Assert.assertEquals(link.getReservableBandwidth(7), 4000f);

    }

    /**
     *
     * Preenptions cannot be used with bandwidth sharing.
     * Check that CSPF do not find a route, assuming preeemmtion
     *
     */
    /* Bandwidth sharing can no longer be started on a domain with multiple CTs
    @Test
    public void test2PreemptBwSharing() throws InvalidDomainException, FileNotFoundException, LspAlreadyExistException, DiffServConfigurationException, LspNotFoundException, LinkCapacityExceededException, LinkNotFoundException, NodeNotFoundException, InvalidPathException {
        try {
        System.out.println("Test2preemptBWSharing.");
        String fileName = "src/resources/junit-test/test-domain-preempt.xml";
        Domain domain = DomainFactory.loadDomain(fileName, true, true);

        Assert.assertFalse(domain.getBandwidthManagement().usePreemption());

        try {
            establishtest2(domain);
            Assert.fail("LSP could be routed");
        } catch (NoRouteToHostException e) {
            System.out.println("No route to host : OK");
        } catch (RoutingException e) {
            Assert.fail("routing exception");
        }

        Assert.assertEquals(domain.getNbLsps(), 1);

        Assert.assertNotNull(domain.getLsp("lsp1"));

        Link link = domain.getLink("2_2 -> 2b_2");


        //System.out.println(link.getReservableBandwidth());
        //System.out.println(link.getReservableBandwidth(0));
        //System.out.println(link.getReservableBandwidth(1));
        //System.out.println(link.getReservableBandwidth(4));
        //System.out.println(link.getReservableBandwidth(5));
        //System.out.println(link.getReservableBandwidth(6));
        //System.out.println(link.getReservableBandwidth(7));

        //at lowest classType (1) and lowest preemption level (3)
        Assert.assertEquals(link.getReservableBandwidth(), 2000f);

        //prempt 0
        Assert.assertEquals(link.getReservableBandwidth(4), 2000f);
        //preemt 1
        Assert.assertEquals(link.getReservableBandwidth(5), 2000f);
        //preempt 2
        Assert.assertEquals(link.getReservableBandwidth(6), 2000f);
        //preempt 3
        Assert.assertEquals(link.getReservableBandwidth(7), 2000f);


        } finally {System.out.println("End of Test2preemptBWSharing.");}

    }
*/
    private void establishtest2(Domain domain) throws NoRouteToHostException, RoutingException, TotemActionExecutionException {
        CSPF cspf = new CSPF();
        cspf.start();

        /* add first lsp */
        LSPPrimaryRoutingParameter params = new LSPPrimaryRoutingParameter("0", "3", "lsp1");
        params.setBandwidth(48000);
        params.setHolding(3);
        params.setSetup(3);
        params.setClassType(1);

        try {
            TotemActionList acl = cspf.routeLSP(domain, params);

            for (Object o : acl) {
                TotemAction a = (TotemAction) o;
                a.execute();
            }

        } catch (RoutingException e) {
            Assert.fail("Routing exception");
        } catch (NoRouteToHostException e) {
            Assert.fail("No route to host exception");
        }

        /* add second lsp, more prioritary, should not preempt lsp1 */
        params = new LSPPrimaryRoutingParameter("1", "4", "lsp2");
        params.setBandwidth(46000);
        params.setHolding(2);
        params.setSetup(2);
        params.setClassType(1);

        TotemActionList acl = null;
        acl = cspf.routeLSP(domain, params);

        for (Object o : acl) {
            TotemAction a = (TotemAction) o;
            a.execute();
        }

        cspf.stop();
    }



    @Test
    public void testPreemptNoBwSharing() throws InvalidDomainException, FileNotFoundException, LspAlreadyExistException, DiffServConfigurationException, LspNotFoundException, LinkCapacityExceededException, LinkNotFoundException, NodeNotFoundException, InvalidPathException {
        String fileName = "src/resources/junit-test/test-domain-preempt.xml";
        Domain domain = DomainFactory.loadDomain(fileName, true, false);

        Assert.assertTrue(domain.usePreemption());
        testPreempt(domain);
    }

    private void testPreempt(Domain domain) throws LspAlreadyExistException, LinkCapacityExceededException, NodeNotFoundException, LinkNotFoundException, LspNotFoundException, DiffServConfigurationException, InvalidPathException, InvalidDomainException, FileNotFoundException {
        Path p1 = new PathImpl(domain);
        Path p2 = new PathImpl(domain);

        List<Node> nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("0"));
        nodeList.add(domain.getNode("2"));
        nodeList.add(domain.getNode("2bis"));
        nodeList.add(domain.getNode("3"));

        p1.createPathFromNode(nodeList);

        nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("1"));
        nodeList.add(domain.getNode("2"));
        nodeList.add(domain.getNode("2bis"));
        nodeList.add(domain.getNode("4"));

        p2.createPathFromNode(nodeList);

        //lsps for CT 0
        Lsp lspa = new LspImpl(domain, "lspa", 140000, p1, 0, 1, 1);    // id priority: 1
        Lsp lspb = new LspImpl(domain, "lspb", 40000, p2, 0, 0, 0);     // id priority: 0
        Lsp lspc = new LspImpl(domain, "lspc", 120000, p1, 0, 0, 0);    // id priority: 0

        //lsps for CT 1
        Lsp lsp1 = new LspImpl(domain, "lsp1", 10000, p1, 1, 2, 2);     // id priority: 6
        Lsp lsp2 = new LspImpl(domain, "lsp2", 9000, p1, 1, 2, 2);      // id priority: 6
        Lsp lsp3 = new LspImpl(domain, "lsp3", 8000, p1, 1, 3, 3);      // id priority: 7
        Lsp lsp4 = new LspImpl(domain, "lsp4", 11000, p1, 1, 3, 3);     // id priority: 7
        Lsp lsp5 = new LspImpl(domain, "lsp5", 50000, p2, 1, 1, 1);     // id priority: 5

        domain.addLsp(lspa);

        Assert.assertEquals(domain.getNbLsps(), 1);
        domain.addLsp(lsp1);
        Assert.assertEquals(domain.getNbLsps(), 2);
        domain.addLsp(lsp2);
        Assert.assertEquals(domain.getNbLsps(), 3);
        domain.addLsp(lsp3);
        Assert.assertEquals(domain.getNbLsps(), 4);
        domain.addLsp(lsp4);
        Assert.assertEquals(domain.getNbLsps(), 5);

        // lspb should preempt lspa
        domain.addLsp(lspb, true);
        System.out.println(domain.getNbLsps());
        Assert.assertEquals(domain.getNbLsps(), 5);

        boolean error = false;
        try {
           domain.getLsp("lspa");
        }
        catch (LspNotFoundException e) {
            error = true;
        }
        Assert.assertTrue(error);

        // only lspa for CT 1 (priority 0 & 1)
        Assert.assertEquals(domain.getLink("2_2 -> 2b_2").getReservableBandwidth(0), 110000, 0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 2b_2").getReservableBandwidth(1), 110000, 0.0001);

        // lspc cannot preempt lspb
        error = false;
        try {
            domain.addLsp(lspc);
        }
        catch (LinkCapacityExceededException e) {
            error = true;
        }
        Assert.assertTrue(error);

        Assert.assertEquals(domain.getNbLsps(), 5);

        // lsp5 should preempt lsp1, lsp2, lsp3 and lsp4

        domain.addLsp(lsp5, true);

        Assert.assertEquals(domain.getNbLsps(), 2);
        for (Lsp lspit : domain.getAllLsps()) {
            Assert.assertTrue(lspit.getId().equals("lsp5") || lspit.getId().equals("lspb"));
        }

        // check reservations

        Assert.assertEquals(domain.getLink("2_2 -> 2b_2").getReservedBandwidth(0), 40000, 0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 2b_2").getReservedBandwidth(1), 0, 0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 2b_2").getReservedBandwidth(4), 0, 0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 2b_2").getReservedBandwidth(5), 50000, 0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 2b_2").getReservedBandwidth(6), 0, 0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 2b_2").getReservedBandwidth(7), 0, 0.0001);

        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(0), 0, 0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(1), 0, 0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(4), 0, 0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(5), 0, 0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(6), 0, 0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(7), 0, 0.0001);

        Assert.assertEquals(domain.getLink("1_1 -> 2_1").getReservedBandwidth(0), 40000, 0.0001);
        Assert.assertEquals(domain.getLink("1_1 -> 2_1").getReservedBandwidth(1), 0, 0.0001);
        Assert.assertEquals(domain.getLink("1_1 -> 2_1").getReservedBandwidth(4), 0, 0.0001);
        Assert.assertEquals(domain.getLink("1_1 -> 2_1").getReservedBandwidth(5), 50000, 0.0001);
        Assert.assertEquals(domain.getLink("1_1 -> 2_1").getReservedBandwidth(6), 0, 0.0001);
        Assert.assertEquals(domain.getLink("1_1 -> 2_1").getReservedBandwidth(7), 0, 0.0001);

        //for (Lsp lspit : domain.getAllLsps()) {
        //    System.out.println("id:" + lspit.getId() + " setupP:" + lspit.getSetupPreemption() + " HoldingP:" + lspit.getHoldingPreemption() + " path:" + lspit.getLspPath());
        //}
    }

    /* preemption may occur even when a lsp can't establish */
    // that's not the case anymore
    @Test
    public void testUselessPreemption() throws NodeNotFoundException, LspAlreadyExistException, LinkCapacityExceededException, DiffServConfigurationException, InvalidPathException, InvalidDomainException, FileNotFoundException, LspNotFoundException {
        String fileName = "src/resources/junit-test/test-domain-preempt.xml";
        Domain domain = DomainFactory.loadDomain(fileName);

        Path p1 = new PathImpl(domain);
        Path p2 = new PathImpl(domain);

        List<Node> nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("0"));
        nodeList.add(domain.getNode("2"));
        nodeList.add(domain.getNode("2bis"));
        nodeList.add(domain.getNode("3"));

        p1.createPathFromNode(nodeList);

        nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("1"));
        nodeList.add(domain.getNode("2"));
        nodeList.add(domain.getNode("2bis"));
        nodeList.add(domain.getNode("4"));

        p2.createPathFromNode(nodeList);

        //lsps for CT 1
        Lsp lsp1 = new LspImpl(domain, "lsp1", 20000, p2, 1, 0, 0);     // id priority: 4
        Lsp lsp2 = new LspImpl(domain, "lsp2", 10000, p1, 1, 3, 3);      // id priority: 7
        Lsp lsp3 = new LspImpl(domain, "lsp3", 45000, p1, 1, 2, 2);      // id priority: 6

        domain.addLsp(lsp1);

        domain.addLsp(lsp2);

        boolean error = false;
        try {
            //lsp3 will preempt lsp2 but fail to establish
            domain.addLsp(lsp3);
        }
        catch (LinkCapacityExceededException e) {
            //System.out.println("Fail to establish lsp3");
            error = true;
        }
        Assert.assertTrue(error);

        Assert.assertEquals(domain.getNbLsps(), 2);

        // only lsp1 left
        //for (Lsp lspit : domain.getAllLsps()) {
        //    System.out.println(lspit.getId());
        //}

    }

    @Test
    public void testNotDefinedPL() throws LspAlreadyExistException, LinkCapacityExceededException, NodeNotFoundException, DiffServConfigurationException, InvalidPathException, InvalidDomainException, FileNotFoundException, LspNotFoundException {
        String fileName = "src/resources/junit-test/test-domain-preempt.xml";
        Domain domain = DomainFactory.loadDomain(fileName);

        Path p1 = new PathImpl(domain);

        List<Node> nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("0"));
        nodeList.add(domain.getNode("2"));
        nodeList.add(domain.getNode("2bis"));
        nodeList.add(domain.getNode("3"));

        p1.createPathFromNode(nodeList);

        Lsp lsp2 = new LspImpl(domain, "lsp2", 22, p1, 1, 3, 3);

        domain.addLsp(lsp2);

        boolean error = false;
        Lsp lsp1 = null;
        try {
            lsp1 = new LspImpl(domain, "lsp1", 20000, p1, 0, 1, 2);
        } catch (DiffServConfigurationException e) {
            error = true;
        }
        Assert.assertTrue(error);
    }

}

