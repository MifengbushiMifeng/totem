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
import be.ac.ulg.montefiore.run.totem.domain.model.jaxb.LinkIgp;
import be.ac.ulg.montefiore.run.totem.domain.model.impl.*;
import be.ac.ulg.montefiore.run.totem.domain.persistence.DomainFactory;
import be.ac.ulg.montefiore.run.totem.domain.exception.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;

import org.junit.Test;

/*
 * Changes:
 * --------
 * 12-Oct-2005: fix testGetLink() (GMO)
 */

/**
 * Test the Domain methods
 *
 * <p>Creation date: 9-Jan-2005 18:07:44
 *
 * @author  Fabian Skivee (skivee@run.montefiore.ulg.ac.be)
 */
public class DomaintTest {

    /**
     * Test Access to the Link in a Domain
     *
     * @throws LinkNotFoundException
     */
    @Test
    public void testGetLink() throws LinkNotFoundException, InvalidDomainException, FileNotFoundException {
        String fileName = "src/resources/junit-test/test-domain1.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        //Iterator<Link> linkIt = domain.getAllLinks().iterator();
        //while (linkIt.hasNext()) {
            //System.out.println("Link : " + linkIt.next().getId() );
        //}
        Assert.assertEquals(domain.getAllLinks().size(), 10);
        Link link = domain.getLink("0_0 -> 2_0");
        Assert.assertEquals(link.getBandwidth(),60d,0.1);
        try {
            link = domain.getLink("errorId");
        } catch (LinkNotFoundException e) {
            Assert.assertTrue(true);
            return;
        }
        Assert.fail();
    }

    /**
     * Test access to the LinkIgp in a DomainImpl
     */
    @Test
    public void testGetIgpLink() throws LinkNotFoundException, InvalidDomainException, FileNotFoundException {
        String fileName = "src/resources/junit-test/test-domain1.xml";
        Domain domainIF = DomainFactory.loadDomain(fileName);
        DomainImpl domain = (DomainImpl) domainIF;

        Assert.assertEquals(domainIF.getLink("0_0 -> 2_0").getBandwidth(),60d,0.1);

        LinkIgp link = domain.getLinkIgp("errorId");
        Assert.assertEquals(link,null);
    }

    /**
     * Add a LSP in a domain with existings LSPs
     *
     * @throws LinkNotFoundException
     * @throws LspNotFoundException
     */
    @Test
    public void testAddLsp() throws LinkNotFoundException, LspNotFoundException, NodeNotFoundException, LinkCapacityExceededException, LspAlreadyExistException, InvalidDomainException, FileNotFoundException, DiffServConfigurationException {
        String fileName = "src/resources/junit-test/test-domain1.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        List<Link> linkList = new ArrayList<Link>();
        linkList.add(0,domain.getLink("0_0 -> 2_0"));
        linkList.add(1,domain.getLink("2_1 -> 1_1"));
        Path path = new PathImpl(domain);
        try {
            path.createPathFromLink(linkList);
        } catch (InvalidPathException e) {
            e.printStackTrace();
        }

        Lsp lsp = new LspImpl(domain,"LSP2 0-1",10f,path);


        domain.addLsp(lsp);

        Assert.assertEquals(domain.getLsp("LSP2 0-1").getId(),lsp.getId());
        Assert.assertEquals(domain.getLsp("LSP2 0-1").getLspPath().getLinkPath().get(0).getId(),"0_0 -> 2_0");
        Assert.assertEquals(domain.getLsp("LSP2 0-1").getLspPath().getLinkPath().get(1).getId(),"2_1 -> 1_1");
        Assert.assertEquals(domain.getLsp("LSP2 0-1").getReservation(),10f,0.0001);
    }

    /**
     * Add a LSP in a domain without MPLS section
     *
     * @throws LinkNotFoundException
     * @throws LspNotFoundException
     */
    @Test
    public void testAddLsp2() throws LinkNotFoundException, LspNotFoundException, NodeNotFoundException, LinkCapacityExceededException, LspAlreadyExistException, InvalidDomainException, FileNotFoundException, DiffServConfigurationException {
        String fileName = "src/resources/junit-test/test-domain1b.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        List<Link> linkList = new ArrayList<Link>();
        linkList.add(0,domain.getLink("0_0 -> 2_0"));
        linkList.add(1,domain.getLink("2_1 -> 1_1"));
        Path path = new PathImpl(domain);
        try {
            path.createPathFromLink(linkList);
        } catch (InvalidPathException e) {
            e.printStackTrace();
        }

        Lsp lsp = new LspImpl(domain,"LSP2 0-1",10f,path);

        domain.addLsp(lsp);

        Assert.assertEquals(domain.getNbLsps(), 1);
        Assert.assertEquals(domain.getLsp("LSP2 0-1").getId(),lsp.getId());
        Assert.assertEquals(domain.getLsp("LSP2 0-1").getLspPath().getLinkPath().get(0).getId(),"0_0 -> 2_0");
        Assert.assertEquals(domain.getLsp("LSP2 0-1").getLspPath().getLinkPath().get(1).getId(),"2_1 -> 1_1");
        Assert.assertEquals(domain.getLsp("LSP2 0-1").getReservation(),10f,0.0001);
    }

    /**
     * Add and remove a LSP from a domain
     *
     * @throws LspNotFoundException
     * @throws LinkNotFoundException
     * @throws LinkCapacityExceededException
     * @throws NodeNotFoundException
     */
    @Test
    public void testAddRemoveLsp() throws LspNotFoundException, LinkNotFoundException, LinkCapacityExceededException, NodeNotFoundException, LspAlreadyExistException, DiffServConfigurationException, InvalidPathException, InvalidDomainException, FileNotFoundException {
        String fileName = "src/resources/junit-test/test-domain2.xml";
        Domain domain = DomainFactory.loadDomain(fileName);

        Assert.assertEquals(domain.getNbLsps(), 0);

        List<Node> nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("0"));
        nodeList.add(domain.getNode("2"));
        nodeList.add(domain.getNode("3"));
        nodeList.add(domain.getNode("4"));

        Path p1 = new PathImpl(domain);
        p1.createPathFromNode(nodeList);

        Lsp lsp1 = new LspImpl(domain, "lsp1", 23000, p1, 1, 2, 2);  //priority 6
        domain.addLsp(lsp1);

        Assert.assertEquals(domain.getNbLsps(), 1);

        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(6), 23000, 0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservableBandwidth(6), 27000, 0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(7), 0, 0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservableBandwidth(7), 27000, 0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(), 23000, 0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservableBandwidth(), 27000, 0.0001);

        Assert.assertEquals(domain.getLspsOnLink(domain.getLink("0_0 -> 2_0")).size(), 1);

        domain.removeLsp(domain.getLsp("lsp1"));

        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(6), 0, 0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservableBandwidth(6), 50000, 0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(7), 0, 0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservableBandwidth(7), 50000, 0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservedBandwidth(), 0, 0.0001);
        Assert.assertEquals(domain.getLink("0_0 -> 2_0").getReservableBandwidth(), 50000, 0.0001);

        boolean error = false;
        try {
            domain.getLsp("lsp1");
        }
        catch (LspNotFoundException e) {
            error = true;
        }
        Assert.assertTrue(error);

        Assert.assertEquals(domain.getLspsOnLink(domain.getLink("0_0 -> 2_0")).size(), 0);
    }

    /**
     * Test add link on test-domain1.xml
     *
     * @throws LspNotFoundException
     * @throws LinkAlreadyExistException
     */
    @Test
    public void testAddLink() throws LspNotFoundException, LinkAlreadyExistException, NodeNotFoundException, InvalidDomainException, FileNotFoundException {
        String fileName = "src/resources/junit-test/test-domain1.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        Link link1 = new LinkImpl(domain,"1","0","1",2000);
        Link link2 = new LinkImpl(domain,"2","1","0",2000);
        domain.addLink(link1);
        domain.addLink(link2);
        Assert.assertEquals(domain.getNbLinks(),12);
    }

    /**
     * Create a new domain and add two links.
     *
     * @throws LinkAlreadyExistException
     * @throws NodeNotFoundException
     * @throws NodeAlreadyExistException
     */
    @Test
    public void testAddLink2() throws LinkAlreadyExistException, NodeNotFoundException, NodeAlreadyExistException {
        Domain domain = new DomainImpl(13);
        Node node0 = new NodeImpl(domain,"0");
        Node node1 = new NodeImpl(domain,"1");
        Link link1 = new LinkImpl(domain,"1","0","1",2000f);
        Link link2 = new LinkImpl(domain,"2","1","0",2000f);
        domain.addNode(node0);
        domain.addNode(node1);
        domain.addLink(link1);
        domain.addLink(link2);
        Assert.assertEquals(domain.getASID(),13);
        Assert.assertEquals(domain.getNbLinks(),2);
        try {
            Assert.assertEquals(domain.getLink("1").getBandwidth(),2000f,0.000001);
            Assert.assertEquals(domain.getLink("2").getBandwidth(),2000f,0.000001);
        } catch (LinkNotFoundException e) {
            Assert.fail();
            e.printStackTrace();
        }
    }

    /**
     * Remove a link from test-domain1.xml
     *
     * @throws LinkNotFoundException
     */
    @Test
    public void testRemoveLink() throws LinkNotFoundException, InvalidDomainException, FileNotFoundException {
        String fileName = "src/resources/junit-test/test-domain1.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        Link link1 = domain.getLink("0_0 -> 2_0");
        domain.removeLink(link1);
        Assert.assertEquals(domain.getNbLinks(),9);
    }

    @Test
    public void testInOutLinks() throws NodeAlreadyExistException, NodeNotFoundException, LinkAlreadyExistException, LinkNotFoundException {
        Domain domain = new DomainImpl(1456);
        domain.addNode(new NodeImpl(domain, "1"));
        domain.addNode(new NodeImpl(domain, "2"));
        domain.addNode(new NodeImpl(domain, "3"));
        domain.addNode(new NodeImpl(domain, "4"));
        domain.addNode(new NodeImpl(domain, "5"));

        domain.addLink(new LinkImpl(domain, "1-2", "1", "2", 100));
        domain.addLink(new LinkImpl(domain, "2-3", "2", "3", 100));
        domain.addLink(new LinkImpl(domain, "3-5", "3", "5", 100));
        domain.addLink(new LinkImpl(domain, "1-5", "1", "5", 100));
        domain.addLink(new LinkImpl(domain, "1-4", "1", "4", 100));
        domain.addLink(new LinkImpl(domain, "1-4b", "1", "4", 100));
        domain.addLink(new LinkImpl(domain, "4-5", "4", "5", 100));

        //reverse links
        domain.addLink(new LinkImpl(domain, "5-3", "5", "3", 100));
        domain.addLink(new LinkImpl(domain, "3-2", "3", "2", 100));
        domain.addLink(new LinkImpl(domain, "2-1", "2", "1", 100));

        List<Link> lst = domain.getLinksBetweenNodes(domain.getNode("3"), domain.getNode("5"));

        Assert.assertEquals(lst.size(), 1);

        Assert.assertTrue(lst.contains(domain.getLink("3-5")));

        Assert.assertEquals(domain.getLinksBetweenNodes(domain.getNode("4"), domain.getNode("1")).size(), 0);
        lst = domain.getLinksBetweenNodes(domain.getNode("1"), domain.getNode("4"));

        Assert.assertEquals(lst.size(), 2);
        Assert.assertTrue(lst.contains(domain.getLink("1-4")));
        Assert.assertTrue(lst.contains(domain.getLink("1-4b")));

        lst = domain.getNode("1").getInLink();

        Assert.assertEquals(lst.size(), 1);
        Assert.assertTrue(lst.contains(domain.getLink("2-1")));

        lst = domain.getNode("1").getOutLink();
        Assert.assertEquals(lst.size(), 4);
        Assert.assertTrue(lst.contains(domain.getLink("1-2")));
        Assert.assertTrue(lst.contains(domain.getLink("1-5")));
        Assert.assertTrue(lst.contains(domain.getLink("1-4")));
        Assert.assertTrue(lst.contains(domain.getLink("1-4b")));

        domain.removeLink(domain.getLink("4-5"));
        domain.removeLink(domain.getLink("2-1"));
        domain.removeLink(domain.getLink("1-2"));
        domain.removeLink(domain.getLink("3-5"));
        domain.removeLink(domain.getLink("1-4b"));

        lst = domain.getNode("1").getInLink();

        Assert.assertEquals(lst.size(), 0);

        lst = domain.getNode("1").getOutLink();
        Assert.assertEquals(lst.size(), 2);
        Assert.assertTrue(lst.contains(domain.getLink("1-5")));
        Assert.assertTrue(lst.contains(domain.getLink("1-4")));

        lst = domain.getNode("4").getOutLink();
        Assert.assertEquals(lst.size(), 0);

        Assert.assertEquals(domain.getLinksBetweenNodes("4", "5").size(), 0);
        Assert.assertEquals(domain.getLinksBetweenNodes("5", "4").size(), 0);
        Assert.assertEquals(domain.getLinksBetweenNodes("4", "3").size(), 0);
    }

}
