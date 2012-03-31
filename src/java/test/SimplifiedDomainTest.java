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
import be.ac.ulg.montefiore.run.totem.domain.model.DomainConvertor;
import be.ac.ulg.montefiore.run.totem.domain.model.Node;
import be.ac.ulg.montefiore.run.totem.domain.persistence.DomainFactory;
import be.ac.ulg.montefiore.run.totem.domain.simplifiedDomain.SimplifiedDomainBuilder;
import be.ac.ulg.montefiore.run.totem.domain.simplifiedDomain.SimplifiedDomain;
import be.ac.ulg.montefiore.run.totem.domain.exception.InvalidDomainException;
import be.ac.ulg.montefiore.run.totem.domain.exception.LinkNotFoundException;
import be.ac.ulg.montefiore.run.totem.domain.exception.NodeNotFoundException;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

/*
 * Changes:
 * --------
 *
 */

/**
 * Test the Simplified Domain functionalities
 *
 * <p>Creation date: 26-Jan-2005 11:58:46
 *
 * @author  Fabian Skivee (skivee@run.montefiore.ulg.ac.be)
 */
public class SimplifiedDomainTest {

    /**
     * Test build a SimplifiedDomain from a Domain
     */
    @Test
    public void testBuildSimplifiedTopology() throws InvalidDomainException, FileNotFoundException {
        String fileName = "src/resources/junit-test/test-domain1.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        SimplifiedDomain sDomain = SimplifiedDomainBuilder.build(domain);
        DomainConvertor convertor = domain.getConvertor();
        List<Link> linkList = domain.getUpLinks();
        for (int i = 0; i < linkList.size(); i++) {
            Link link = linkList.get(i);
            int linkId = 0;
            try {
                linkId = convertor.getLinkId(link.getId());
                Assert.assertEquals(sDomain.getLinkSrc(linkId),convertor.getNodeId(link.getSrcNode().getId()));
                Assert.assertEquals(sDomain.getLinkDst(linkId),convertor.getNodeId(link.getDstNode().getId()));
                Assert.assertEquals(sDomain.getLinkCapacity(linkId),link.getBandwidth(),0.00001);
                Assert.assertEquals(sDomain.getLinkWeight(linkId),link.getMetric(),0.00001);
                Assert.assertEquals(sDomain.getLinkDelay(linkId),link.getDelay(),0.00001);
            } catch (LinkNotFoundException e) {
                Assert.fail();
                e.printStackTrace();
            } catch (NodeNotFoundException e) {
                Assert.fail();
                e.printStackTrace();
            }
        }
    }

    /**
     * Test creation of SimplifiedDomain on a Domain with non consecutive id.
     * The Domain with non consecutive id is obtain by removing a node
     */
    @Test
    public void testNonConsecutiveId() throws InvalidDomainException, FileNotFoundException {
        String fileName = "src/resources/junit-test/test-domain1.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        try {
            domain.removeNode(domain.getNode("0"));
        } catch (NodeNotFoundException e) {
            Assert.fail();
        } catch (LinkNotFoundException e) {
            Assert.fail();
        }
        SimplifiedDomain sDomain = SimplifiedDomainBuilder.build(domain);
        DomainConvertor convertor = domain.getConvertor();
        List<Link> linkList = domain.getUpLinks();
        for (int i = 0; i < linkList.size(); i++) {
            Link link = linkList.get(i);
            int linkId = 0;
            try {
                linkId = convertor.getLinkId(link.getId());
                Assert.assertEquals(sDomain.getLinkSrc(linkId),convertor.getNodeId(link.getSrcNode().getId()));
                Assert.assertEquals(sDomain.getLinkDst(linkId),convertor.getNodeId(link.getDstNode().getId()));
                Assert.assertEquals(sDomain.getLinkCapacity(linkId),link.getBandwidth(),0.00001);
                Assert.assertEquals(sDomain.getLinkWeight(linkId),link.getMetric(),0.00001);
                Assert.assertEquals(sDomain.getLinkDelay(linkId),link.getDelay(),0.00001);
            } catch (LinkNotFoundException e) {
                Assert.fail();
                e.printStackTrace();
            } catch (NodeNotFoundException e) {
                Assert.fail();
                e.printStackTrace();
            }
        }
    }

    /**
     * Check that getOutLinks from a node is the same for the SimplifiedDomain and the Domain
     */
    @Test
    public void testOutLinks() throws InvalidDomainException, FileNotFoundException {
        String fileName = "src/resources/junit-test/test-domain1.xml";
        String nodeId = "2";
        Domain domain = DomainFactory.loadDomain(fileName);
        DomainConvertor convertor = domain.getConvertor();
        SimplifiedDomain sDomain = SimplifiedDomainBuilder.build(domain);
        int[] linkIdList = new int[0];
        try {
            linkIdList = sDomain.getOutLinks(convertor.getNodeId(nodeId));
        } catch (NodeNotFoundException e) {
            Assert.fail();
            e.printStackTrace();
        }
        try {
            List<Link> linkList = domain.getNode(nodeId).getOutLink();
            Assert.assertEquals(linkList.size(),linkIdList.length);
            for (int i = 0; i < linkList.size(); i++) {
                Link link = linkList.get(i);
                int linkIdx = 0;
                for (linkIdx = 0; linkIdx < linkIdList.length; linkIdx++) {
                    int linkId = linkIdList[linkIdx];
                    if (linkId == convertor.getLinkId(link.getId())) {
                        Assert.assertEquals(linkId,convertor.getLinkId(link.getId()));
                        break;
                    }
                }
                if (linkIdx == linkIdList.length) {
                    Assert.fail();
                }
            }
        } catch (NodeNotFoundException e) {
            Assert.fail();
            e.printStackTrace();
        } catch (LinkNotFoundException e) {
            Assert.fail();
            e.printStackTrace();
        }
    }

    /**
     * Check that getInLinks from a node is the same for the SimplifiedDomain and the Domain
     */
    @Test
    public void testInLinks() throws InvalidDomainException, FileNotFoundException {
        String fileName = "src/resources/junit-test/test-domain1.xml";
        String nodeId = "2";
        Domain domain = DomainFactory.loadDomain(fileName);
        DomainConvertor convertor = domain.getConvertor();
        SimplifiedDomain sDomain = SimplifiedDomainBuilder.build(domain);
        int[] linkIdList = new int[0];
        try {
            linkIdList = sDomain.getInLinks(convertor.getNodeId(nodeId));
        } catch (NodeNotFoundException e) {
            Assert.fail();
            e.printStackTrace();
        }
        try {
            List<Link> linkList = domain.getNode(nodeId).getInLink();
            Assert.assertEquals(linkList.size(),linkIdList.length);
            for (int i = 0; i < linkList.size(); i++) {
                Link link = linkList.get(i);
                int linkIdx = 0;
                for (linkIdx = 0; linkIdx < linkIdList.length; linkIdx++) {
                    int linkId = linkIdList[linkIdx];
                    if (linkId == convertor.getLinkId(link.getId())) {
                        Assert.assertEquals(linkId,convertor.getLinkId(link.getId()));
                        break;
                    }
                }
                if (linkIdx == linkIdList.length) {
                    Assert.fail();
                }
            }
        } catch (NodeNotFoundException e) {
            Assert.fail();
            e.printStackTrace();
        } catch (LinkNotFoundException e) {
            Assert.fail();
            e.printStackTrace();
        }
    }

    /**
     * Check clone function of a SimplifiedDomain
     */
    @Test
    public void testSimplifiedClone() throws InvalidDomainException, FileNotFoundException {
        String fileName = "src/resources/junit-test/test-domain1.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        DomainConvertor convertor = domain.getConvertor();
        SimplifiedDomain sd1 = SimplifiedDomainBuilder.build(domain);
        SimplifiedDomain sd2 = null;
        try {
            sd2 = (SimplifiedDomain) sd1.clone();
        } catch (CloneNotSupportedException e) {
            Assert.fail();
            e.printStackTrace();
        }
        Assert.assertEquals(sd1.getName(),sd2.getName());
        Assert.assertEquals(sd1.getNbLinks(),sd2.getNbLinks());
        Assert.assertEquals(sd1.getNbNodes(),sd2.getNbNodes());

        // Check all links
        SimplifiedDomain.Link[] linkSd1 = sd1.getLinks();
        SimplifiedDomain.Link[] linkSd2 = sd2.getLinks();
        Assert.assertEquals(linkSd1.length,linkSd2.length);
        for (int i = 0; i < linkSd2.length; i++) {
            Assert.assertNotSame(linkSd2[i],linkSd1);
            Assert.assertEquals(linkSd2[i].getId(),linkSd1[i].getId());
        }
        // Check connectivity
        List<Node> nodeList = domain.getUpNodes();
        for (int srcNode = 0; srcNode < nodeList.size(); srcNode++) {
            try {
                int srcNodeId = convertor.getNodeId(nodeList.get(srcNode).getId());
                for (int dstNode = 0; dstNode < nodeList.size(); dstNode++) {
                    int dstNodeId = convertor.getNodeId(nodeList.get(dstNode).getId());
                    if (srcNodeId != dstNodeId) {
                        List<SimplifiedDomain.Link> sd1Links = sd1.getConnectivity(srcNode,dstNode);
                        List<SimplifiedDomain.Link> sd2Links = sd2.getConnectivity(srcNode,dstNode);
                        if ((sd1Links != null) && (sd2Links != null)) {
                            Assert.assertEquals(sd1Links.size(),sd2Links.size());
                            for (int i = 0; i < sd2Links.size(); i++) {
                                Assert.assertNotSame(sd1Links.get(i),sd2Links.get(i));
                                Assert.assertEquals(sd1Links.get(i).getId(),sd2Links.get(i).getId());
                            }
                        }
                    }
                }
            } catch (NodeNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


}
