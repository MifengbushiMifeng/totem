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
import be.ac.ulg.montefiore.run.totem.domain.exception.*;
import be.ac.ulg.montefiore.run.totem.domain.model.*;
import be.ac.ulg.montefiore.run.totem.domain.model.impl.PathImpl;
import be.ac.ulg.montefiore.run.totem.domain.model.impl.LspImpl;
import be.ac.ulg.montefiore.run.totem.domain.persistence.DomainFactory;
import be.ac.ulg.montefiore.run.totem.repository.CSPF.CSPF;
import be.ac.ulg.montefiore.run.totem.repository.model.*;
import be.ac.ulg.montefiore.run.totem.repository.model.exception.RoutingException;
import be.ac.ulg.montefiore.run.totem.repository.model.exception.NoRouteToHostException;
import be.ac.ulg.montefiore.run.totem.repository.model.exception.TotemActionExecutionException;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

/*
 * Changes:
 * --------
 *  - 20-Oct-2005 : bugfix (GMO)
 *  - 18-Apr-2007 : add testRbwBCMethods, testAddPriorityLevel and testRemovePriorityLevel (GMO)
 */

/**
 * Extensive test to test DiffServ models implementation
 * No preemption used.
 * @see test.PreemptTest
 *   for tests with preemptions.
 *
 * <p>Creation date: 24-Jan.-2005
 *
 * @author  Olivier Delcourt (delcourt@run.montefiore.ulg.ac.be)
 * @author  Gaël Monfort (monfort@run.montefiore.ulg.ac.be)
 */
public class DiffServTest {
    @Test
    public void testRbwBCMethodsDF() throws LspAlreadyExistException, InvalidDomainException, FileNotFoundException, DiffServConfigurationException, LinkCapacityExceededException, LspNotFoundException, LinkNotFoundException, NodeNotFoundException, InvalidPathException {
        String fileName = "src/resources/junit-test/test-domain-preempt.xml";
        Domain domain = DomainFactory.loadDomain(fileName, false, false);
        testRbwBCMethods(domain);
    }

    private void testRbwBCMethods(Domain domain) throws NodeNotFoundException, InvalidPathException, DiffServConfigurationException, LspAlreadyExistException, LspNotFoundException, LinkCapacityExceededException, LinkNotFoundException {

        /* establish lsps */
        Path p = new PathImpl(domain);
        List<Node> nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("0"));
        nodeList.add(domain.getNode("2"));
        nodeList.add(domain.getNode("2bis"));
        nodeList.add(domain.getNode("3"));
        p.createPathFromNode(nodeList);

        Lsp lsp = new LspImpl(domain, "lsp1", 69000, p, 0, 1, 1);
        domain.addLsp(lsp);
        lsp = new LspImpl(domain, "lsp2", 12000, p, 1, 0, 0);
        domain.addLsp(lsp);
        lsp = new LspImpl(domain, "lsp3", 12000, p, 1, 0, 0);
        domain.addLsp(lsp);
        lsp = new LspImpl(domain, "lsp4", 9000, p, 1, 2, 2);
        domain.addLsp(lsp);

        /* test getRbw and setRbw*/
        Link link = domain.getLink("2_2 -> 2b_2");

        float[] rbw = link.getRbw();
        /*
        for (int i = 0; i < rbw.length; i++) {
            System.out.println("rbw[" + i + "]= " + rbw[i]);
        }
        System.out.println();
        */

        float[] zeroRbw = new float[8];
        Arrays.fill(zeroRbw, 0.0f);
        link.setRbw(zeroRbw);

        zeroRbw = link.getRbw();
        for (int i = 0; i < zeroRbw.length; i++) {
            //System.out.println("zeroRbw[" + i + "]= " + zeroRbw[i]);
            if (i == 2 || i==3) //priority with id 2 & 3 don't exists
                Assert.assertEquals(zeroRbw[i], -1f);
            else Assert.assertEquals(zeroRbw[i], 0.0f);
        }

        link.setRbw(rbw);
        float[] rbw2 = link.getRbw();
        /*
        for (int i = 0; i < rbw2.length; i++) {
            System.out.println("rbw2[" + i + "]= " + rbw2[i]);
        }
        System.out.println();
        */

        for (int i = 0; i < rbw.length; i++) {
            Assert.assertEquals(rbw[i], rbw2[i]);
        }

        /* test setBC */
        // try a bigger BC
        /*
        System.out.println("BCs:");
        System.out.println(link.getBCs()[0]);
        System.out.println(link.getBCs()[1]);
        System.out.println();

        System.out.println("Reserved:");
        System.out.println(link.getReservedBandwidth(0));
        System.out.println(link.getReservedBandwidth(1));
        System.out.println(link.getReservedBandwidth(4));
        System.out.println(link.getReservedBandwidth(5));
        System.out.println(link.getReservedBandwidth(6));
        System.out.println(link.getReservedBandwidth(7));
        System.out.println();

        System.out.println("Reservable:");
        System.out.println(link.getReservableBandwidth(0));
        System.out.println(link.getReservableBandwidth(1));
        System.out.println(link.getReservableBandwidth(4));
        System.out.println(link.getReservableBandwidth(5));
        System.out.println(link.getReservableBandwidth(6));
        System.out.println(link.getReservableBandwidth(7));
        System.out.println();
        */

        //System.out.println("stage1");

        float[] rsvebw = new float[8];
        for (int i = 0; i < 8 ;i++) {
            rsvebw[i] = link.getReservedBandwidth(i);
        }

        float[] rsvabw = new float[8];
        for (int i = 0; i < 8 ;i++) {
            rsvabw[i] = link.getReservableBandwidth(i);
        }

        link.setBC(0, 160000);
        link.setBC(1, 70000);

        for (int i = 0; i <8; i++) {
            if (i==2 || i==3)continue;
            Assert.assertEquals(rsvebw[i], link.getReservedBandwidth(i));
        }

        /*
        System.out.println("BCs:");
        System.out.println(link.getBCs()[0]);
        System.out.println(link.getBCs()[1]);
        System.out.println();

        System.out.println("Reserved:");
        System.out.println(link.getReservedBandwidth(0));
        System.out.println(link.getReservedBandwidth(1));
        System.out.println(link.getReservedBandwidth(4));
        System.out.println(link.getReservedBandwidth(5));
        System.out.println(link.getReservedBandwidth(6));
        System.out.println(link.getReservedBandwidth(7));
        System.out.println();

        System.out.println("Reservable:");
        System.out.println(link.getReservableBandwidth(0));
        System.out.println(link.getReservableBandwidth(1));
        System.out.println(link.getReservableBandwidth(4));
        System.out.println(link.getReservableBandwidth(5));
        System.out.println(link.getReservableBandwidth(6));
        System.out.println(link.getReservableBandwidth(7));
        System.out.println();
        */

        //System.out.println("stage2");
        // set as before

        link.setBC(0, 150000);
        link.setBC(1, 50000);

        for (int i = 0; i <8; i++) {
             if (i==2 || i==3)continue;
            Assert.assertEquals(rsvebw[i], link.getReservedBandwidth(i));
        }
        for (int i = 0; i <8; i++) {
             if (i==2 || i==3)continue;
            Assert.assertEquals(rsvabw[i], link.getReservableBandwidth(i));
        }

        /*
        System.out.println("BCs:");
        System.out.println(link.getBCs()[0]);
        System.out.println(link.getBCs()[1]);
        System.out.println();

        System.out.println("Reserved:");
        System.out.println(link.getReservedBandwidth(0));
        System.out.println(link.getReservedBandwidth(1));
        System.out.println(link.getReservedBandwidth(4));
        System.out.println(link.getReservedBandwidth(5));
        System.out.println(link.getReservedBandwidth(6));
        System.out.println(link.getReservedBandwidth(7));
        System.out.println();

        System.out.println("Reservable:");
        System.out.println(link.getReservableBandwidth(0));
        System.out.println(link.getReservableBandwidth(1));
        System.out.println(link.getReservableBandwidth(4));
        System.out.println(link.getReservableBandwidth(5));
        System.out.println(link.getReservableBandwidth(6));
        System.out.println(link.getReservableBandwidth(7));
        System.out.println();
        */

        //System.out.println("stage3");
        // try a lower BC, but possible

        link.setBC(0, 69000);

        Assert.assertEquals(link.getReservableBandwidthCT(0), 0f);

        // try a lower BC but impossible with current traffic

        // First reset BC's
        link.setBC(0, 150000);
        link.setBC(1, 50000);

        try {
            link.setBC(1, 24000);
            Assert.fail("An exception should have been thrown.");
        } catch (LinkCapacityExceededException e) {

        }

        //are changes correcltly rollbacked ?
        /*
        System.out.println("BCs:");
        System.out.println(link.getBCs()[0]);
        System.out.println(link.getBCs()[1]);
        System.out.println();

        System.out.println("Reserved:");
        System.out.println(link.getReservedBandwidth(0));
        System.out.println(link.getReservedBandwidth(1));
        System.out.println(link.getReservedBandwidth(4));
        System.out.println(link.getReservedBandwidth(5));
        System.out.println(link.getReservedBandwidth(6));
        System.out.println(link.getReservedBandwidth(7));
        System.out.println();

        System.out.println("Reservable:");
        System.out.println(link.getReservableBandwidth(0));
        System.out.println(link.getReservableBandwidth(1));
        System.out.println(link.getReservableBandwidth(4));
        System.out.println(link.getReservableBandwidth(5));
        System.out.println(link.getReservableBandwidth(6));
        System.out.println(link.getReservableBandwidth(7));
        System.out.println();
        
        System.out.println("stage4");
        */
        for (int i = 0; i <8; i++) {
             if (i==2 || i==3)continue;
            Assert.assertEquals(rsvebw[i], link.getReservedBandwidth(i));
        }
        for (int i = 0; i <8; i++) {
             if (i==2 || i==3)continue;
            Assert.assertEquals(rsvabw[i], link.getReservableBandwidth(i));
        }
    }

    @Test
    public void testAddPriorityLevelDF() throws LspAlreadyExistException, InvalidDomainException, FileNotFoundException, DiffServConfigurationException, LinkCapacityExceededException, LspNotFoundException, LinkNotFoundException, NodeNotFoundException, InvalidPathException {
        String fileName = "src/resources/junit-test/test-domain-preempt.xml";
        Domain domain = DomainFactory.loadDomain(fileName, false, false);
        testAddPriorityLevel(domain);
    }

    @Test
    public void testRemovePriorityLevelDF() throws LspAlreadyExistException, InvalidDomainException, FileNotFoundException, DiffServConfigurationException, LinkCapacityExceededException, LspNotFoundException, LinkNotFoundException, NodeNotFoundException, InvalidPathException {
        String fileName = "src/resources/junit-test/test-domain-preempt.xml";
        Domain domain = DomainFactory.loadDomain(fileName, false, false);
        testRemovePriorityLevel(domain);
    }

    private void testAddPriorityLevel(Domain domain) throws NodeNotFoundException, DiffServConfigurationException, InvalidPathException, LspAlreadyExistException, LspNotFoundException, LinkCapacityExceededException, LinkNotFoundException {

        /* establish lsps */
        Path p = new PathImpl(domain);
        List<Node> nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("0"));
        nodeList.add(domain.getNode("2"));
        nodeList.add(domain.getNode("2bis"));
        nodeList.add(domain.getNode("3"));
        p.createPathFromNode(nodeList);

        Lsp lsp = new LspImpl(domain, "lsp1", 69000, p, 0, 1, 1);
        domain.addLsp(lsp);
        lsp = new LspImpl(domain, "lsp2", 12000, p, 1, 0, 0);
        domain.addLsp(lsp);
        lsp = new LspImpl(domain, "lsp3", 12000, p, 1, 0, 0);
        domain.addLsp(lsp);
        lsp = new LspImpl(domain, "lsp4", 9000, p, 1, 2, 2);
        domain.addLsp(lsp);

        Link link = domain.getLink("2_2 -> 2b_2");
        float[] rbw = link.getRbw();
        /*for (int i = 0; i < rbw.length; i++) {
            System.out.println("rbw[" + i + "]= " + rbw[i]);
        }
        System.out.println();*/

        try {
            // the id exists
            domain.addPriorityLevel(5, 0, 2);
            Assert.fail("Id already exists");
        } catch (DiffServConfigurationException e) {

        }

        try {
            // the (ct, pl) exists
            domain.addPriorityLevel(2, 0, 1);
            Assert.fail("(ct, pl) already exists");
        } catch (DiffServConfigurationException e) {

        }

        // good
        domain.addPriorityLevel(2, 0, 2);

        // class type doesn't exist
        domain.addPriorityLevel(3, 2, 0);

        // print priorities
        /*
        for (int i = 0; i < domain.getPriorities().size(); i++) {
            int prio = domain.getPriorities().get(i);
            System.out.println("prio:" + prio);
            System.out.println("ct:" + domain.getClassType(prio));
            System.out.println("pl:" + domain.getPreemptionLevel(prio));
            System.out.println();
        }
        */

        // check that they were added correctly
        Assert.assertTrue(domain.isExistingPriority(2));
        Assert.assertTrue(domain.isExistingPriority(3));
        Assert.assertTrue(domain.isExistingPriority(0, 2));
        Assert.assertEquals(3, domain.getPriority(0, 2));
        Assert.assertTrue(domain.isExistingPriority(2, 0));
        Assert.assertEquals(2, domain.getPriority(2, 0));

        // check reservable bw on link
        Assert.assertEquals(0f, link.getReservedBandwidthCT(2));
        Assert.assertEquals(0f, link.getReservableBandwidthCT(2));

        for (Link l : p.getLinkPath()) {
            l.setBC(2, 3000);
        }

        lsp = new LspImpl(domain, "lsp5", 1250, p, 2, 0, 0);
        domain.addLsp(lsp);
        Assert.assertEquals(1750.0f, link.getReservableBandwidthCT(2));

        lsp = new LspImpl(domain, "lsp6", 1150, p, 0, 2, 2);
        domain.addLsp(lsp);
        Assert.assertEquals(1150.0f, link.getReservedBandwidth(2));

        /*
        rbw = link.getRbw();
        for (int i = 0; i < rbw.length; i++) {
            System.out.println("rbw[" + i + "]= " + rbw[i]);
        }
        System.out.println();
        */

        //DomainFactory.saveDomain("dts.xml", domain);
    }

    private void testRemovePriorityLevel(Domain domain) throws NodeNotFoundException, DiffServConfigurationException, InvalidPathException, LinkNotFoundException, LspAlreadyExistException, LspNotFoundException, LinkCapacityExceededException {

        /* establish lsps */
        Path p = new PathImpl(domain);
        List<Node> nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("0"));
        nodeList.add(domain.getNode("2"));
        nodeList.add(domain.getNode("2bis"));
        nodeList.add(domain.getNode("3"));
        p.createPathFromNode(nodeList);

        Lsp lsp = new LspImpl(domain, "lsp1", 69000, p, 0, 1, 1);
        domain.addLsp(lsp);
        lsp = new LspImpl(domain, "lsp2", 12000, p, 1, 0, 0);
        domain.addLsp(lsp);
        lsp = new LspImpl(domain, "lsp3", 12000, p, 1, 0, 0);
        domain.addLsp(lsp);
        lsp = new LspImpl(domain, "lsp4", 9000, p, 1, 1, 1);
        domain.addLsp(lsp);

        Link link = domain.getLink("2_2 -> 2b_2");
        float[] rbw = link.getRbw();
        /*for (int i = 0; i < rbw.length; i++) {
            System.out.println("rbw[" + i + "]= " + rbw[i]);
        }
        System.out.println();*/

        try {
            // remove a used priority level
            domain.removePriorityLevel(1);
            Assert.fail();
        } catch (DiffServConfigurationException e) {}

        //remove a non existing priority level
        Assert.assertFalse(domain.removePriorityLevel(2));

        float[] rbw2 = link.getRbw();
        for (int i = 0; i < rbw.length; i++) {
            Assert.assertEquals(rbw[i], rbw2[i]);
        }

        // ok to remove
        Assert.assertTrue(domain.removePriorityLevel(0));
        Assert.assertTrue(domain.removePriorityLevel(6));

        rbw = link.getRbw();
        for (int i = 0; i < rbw.length; i++) {
            Assert.assertEquals(rbw[i], (i==2||i==3||i==0||i==6) ? -1 : rbw2[i]);
            //System.out.println("rbw[" + i + "]= " + rbw[i]);
        }
        //System.out.println();


    }

    @Test
    public void testDiffServ() throws LspNotFoundException, LinkNotFoundException, LinkCapacityExceededException, InvalidDomainException, FileNotFoundException {

        String fileName = "src/resources/junit-test/test-domain1.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        Lsp lsp = domain.getLsp("LSP 0-4");
        Assert.assertEquals(lsp.getCT(),2);
        int[] ctArray = domain.getAllCTId();
        for (int i=0; i<ctArray.length; i++){
            if (ctArray[i]!=0 && ctArray[i]!=1 && ctArray[i] != 2){
                Assert.fail();
            }
        }

        Assert.assertEquals(lsp.getHoldingPreemption(),2);
        Assert.assertEquals(lsp.getSetupPreemption(),2);


        // We use the following configuration
        /*<priority id="0" ct="0" preemption="0"></priority>
        <priority id="1" ct="0" preemption="1"></priority>
        <priority id="4" ct="1" preemption="0"></priority>
        <priority id="5" ct="1" preemption="1"></priority>
        <priority id="6" ct="1" preemption="2"></priority>
        <priority id="7" ct="1" preemption="3"></priority>
        MRB = 200000
        BC0 = 150000
        BC1 = 50000
        */

        fileName = "src/resources/junit-test/test-domain2.xml";
        domain = DomainFactory.loadDomain(fileName);
        //DomainFactory.saveDomain("src/resources/junit-test/test-domain-essai.xml", domain);

        Assert.assertEquals(domain.getClassType(0),0);
        Assert.assertEquals(domain.getClassType(4),1);
        //Obtain the minimum priority to which unspecified reservations will be added
        Assert.assertEquals(domain.getMinPriority(),7);

        Assert.assertEquals(domain.getNbCT(),2);

        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservableBandwidth(0),150000,0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(0),0,0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservableBandwidth(4),50000,0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(4),0,0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservableBandwidth(5),50000,0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(5),0,0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservableBandwidth(6),50000,0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(6),0,0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservableBandwidth(7),50000,0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(7),0,0.0001);

        // check bw at minimum priority
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservableBandwidth(), 50000, 0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(), 0, 0.0001);


        // Adding an unspecified reservation (added to minPriority!)

        domain.getLink("0_0 -> 2_0").addReservation(50000);

        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservableBandwidth(0),150000,0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(0),0,0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservableBandwidth(4),50000,0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(4),0,0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservableBandwidth(5),50000,0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(5),0,0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservableBandwidth(6),50000,0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(6),0,0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservableBandwidth(7),0,0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(7),50000,0.0001);

        // Adding a second unspecified reservation --> error (not enough space) TODO: manage this
        boolean error = false;
        try {
            domain.getLink("0_0 -> 2_0").addReservation(50000);
        }
        catch (LinkCapacityExceededException e) {
            error = true;
        }
        Assert.assertTrue(error);

        // Adding a reservation to priority 0
        domain.getLink("0_0 -> 2_0").addReservation(50000,0);

        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservableBandwidth(0),100000,0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(0),50000,0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservableBandwidth(4),50000,0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(4),0,0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservableBandwidth(5),50000,0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(5),0,0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservableBandwidth(6),50000,0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(6),0,0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservableBandwidth(7),0,0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(7),50000,0.0001);

    }

    @Test
    public void testNoDiffServ() throws LinkNotFoundException, InvalidDomainException, FileNotFoundException {
        // Now testing with a topology which has no Diff-Serv elements if default Diff-Serv elements are added
        // correctly

        String fileName = "src/resources/junit-test/test-domain-withoutDS.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        //DomainFactory.saveDomain("src/resources/junit-test/test-domain-withDSadded.xml", domain);

        Assert.assertEquals(domain.getNbCT(), 1);
        Assert.assertEquals(domain.getClassType(0), 0);
        Assert.assertEquals(domain.getPreemptionLevel(0), 0);

        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getBandwidth(), domain.getLink("0_0 -> 2_0").getReservableBandwidth(0), 0.0001);

    }

    @Test
    public void testPreemptionLevels() throws InvalidDomainException, FileNotFoundException, LspAlreadyExistException, TotemActionExecutionException, DiffServConfigurationException, LinkCapacityExceededException, LspNotFoundException, NoRouteToHostException, NodeNotFoundException, RoutingException, InvalidPathException {
        String fileName = "src/resources/junit-test/test-domain-preempt.xml";
        Domain domain = DomainFactory.loadDomain(fileName, false, false);
        testPLevelMethods(domain);
    }

    private void testPLevelMethods(Domain domain) throws NodeNotFoundException, InvalidPathException, DiffServConfigurationException, LspAlreadyExistException, LspNotFoundException, LinkCapacityExceededException, RoutingException, NoRouteToHostException, TotemActionExecutionException {
        /* establish lsps */
        Path p = new PathImpl(domain);
        List<Node> nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("0"));
        nodeList.add(domain.getNode("2"));
        nodeList.add(domain.getNode("2bis"));
        nodeList.add(domain.getNode("3"));
        p.createPathFromNode(nodeList);

        Lsp lsp = new LspImpl(domain, "lsp1", 30000, p, 1, 0, 3);
        domain.addLsp(lsp);
        lsp = new LspImpl(domain, "lsp2", 15000, p, 1, 1, 3);
        domain.addLsp(lsp);
        lsp = new LspImpl(domain, "lsp3", 5000, p, 1, 2, 3);
        domain.addLsp(lsp);

        Path p2 = new PathImpl(domain);
        nodeList.set(0, domain.getNode("1"));
        nodeList.set(3, domain.getNode("4"));
        p2.createPathFromNode(nodeList);

        lsp = new LspImpl(domain, "newlsp1", 1000, p2, 1, 2, 2);
        try {
            domain.addLsp(lsp, true);
            Assert.fail("newlsp1 should not establish.");
        } catch (LinkCapacityExceededException e) {
        } catch (DiffServConfigurationException e) {
        }

        LSPPrimaryRoutingParameter params = new LSPPrimaryRoutingParameter("1", "4", "newlsp1");
        params.setBandwidth(1000);
        params.setClassType(1);
        params.setSetup(2);
        params.setHolding(2);
        CSPF cspf = new CSPF();
        try {
            cspf.routeLSP(domain, params);
            Assert.fail("The lsps should not be routable");
        } catch (NoRouteToHostException e) {
            //e.printStackTrace();
        }

        params.setSetup(1);
        params.setHolding(1);

        TotemActionList actions = cspf.routeLSP(domain, params);
        boolean found = false;
        Lsp toEstablish = null;
        for (Object o : actions) {
            // preempted lsp should be lsp number 3.
            if (o instanceof AddLspAction) {
                toEstablish = ((AddLspAction)o).getLsp();
            } else if (o instanceof PreemptLspsAction) {
                found = true;
                List<String> list = ((PreemptLspsAction)o).getLsps();
                Assert.assertEquals(list.size(), 1);
                Assert.assertEquals(list.get(0), "lsp3");
            }
        }
        if (!found) {
            Assert.fail("No preempt list");
        }

        domain.addLsp(toEstablish, true);

        domain.getLsp("newlsp1");
        domain.getLsp("lsp1");
        domain.getLsp("lsp2");

        try {
            domain.getLsp("lsp3");
            Assert.fail("lsp3 still in the domain.");
        } catch (LspNotFoundException e) {
        }

        params = new LSPPrimaryRoutingParameter("0", "4", "newlsp2");
        params.setBandwidth(21000);
        params.setClassType(1);
        params.setHolding(3);
        params.setSetup(0);


        try {
            actions = cspf.routeLSP(domain, params);
            Assert.fail("No route should be found for LSP newlsp2");
        } catch (NoRouteToHostException e) {
        }

        params.setBandwidth(20000);

        actions = cspf.routeLSP(domain, params);
        for (Object o : actions) {
            ((TotemAction)o).execute();
        }

        Assert.assertEquals(domain.getNbLsps(), 2);
        domain.getLsp("lsp1");
        domain.getLsp("newlsp2");

    }

}
