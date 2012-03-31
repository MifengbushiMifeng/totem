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
import be.ac.ulg.montefiore.run.totem.domain.model.Path;
import be.ac.ulg.montefiore.run.totem.domain.model.Node;
import be.ac.ulg.montefiore.run.totem.domain.model.Link;
import be.ac.ulg.montefiore.run.totem.domain.model.impl.PathImpl;
import be.ac.ulg.montefiore.run.totem.domain.persistence.DomainFactory;
import be.ac.ulg.montefiore.run.totem.domain.exception.InvalidDomainException;
import be.ac.ulg.montefiore.run.totem.domain.exception.LinkNotFoundException;
import be.ac.ulg.montefiore.run.totem.domain.exception.NodeNotFoundException;
import be.ac.ulg.montefiore.run.totem.domain.exception.InvalidPathException;
import be.ac.ulg.montefiore.run.totem.domain.simplifiedDomain.SimplifiedDomain;
import be.ac.ulg.montefiore.run.totem.domain.simplifiedDomain.SimplifiedPath;
import be.ac.ulg.montefiore.run.totem.repository.CSPF.Bhandari;
import be.ac.ulg.montefiore.run.totem.repository.CSPF.CSPF;
import be.ac.ulg.montefiore.run.totem.repository.model.exception.NoRouteToHostException;
import be.ac.ulg.montefiore.run.totem.repository.model.exception.RoutingException;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;

import org.junit.Test;

/*
 * Changes:
 * --------
 *
 */

/**
 * Test Bhandari SPF
 *
 * <p>Creation date: 7-Jan-2005 09:49:36
 *
 * @author  Fabian Skivee (skivee@run.montefiore.ulg.ac.be)
 */
public class BhandariTest {
    @Test
    public void testSimpleDomain() throws FileNotFoundException, InvalidDomainException {
        String fileName = "src/resources/junit-test/test-domain1.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        Bhandari bhandari = new Bhandari();
        Path path = null;
        try {
            path = bhandari.computeSPF(domain,"0","1");
        } catch (RoutingException e) {
            Assert.fail("Bhandari path computation failed");
            e.printStackTrace();
        } catch (NoRouteToHostException e) {
            Assert.fail("Bhandari path computation failed");
            e.printStackTrace();
        }
        List<Node> nodeList = path.getNodePath();
        Assert.assertEquals(nodeList.size(),3);
        Assert.assertEquals(nodeList.get(0).getId(),"0");
        Assert.assertEquals(nodeList.get(1).getId(),"2");
        Assert.assertEquals(nodeList.get(2).getId(),"1");
    }

    /**
     * Test Bhandari on a topology used in "Introduction to Algorithms"
     * containing negative weight
     *
     * @throws Exception
     */
    @Test
    public void testNegativeWeight() throws Exception {

        SimplifiedDomain topo = new SimplifiedDomain("Bhandari : testNegativeWeight",5,10);
        topo.addLink(0,0,1,0,6, 0);
        topo.addLink(1,1,2,0,5, 0);
        topo.addLink(2,2,1,0,-2, 0);
        topo.addLink(3,3,2,0,7, 0);
        topo.addLink(4,4,3,0,9, 0);
        topo.addLink(5,1,4,0,8, 0);
        topo.addLink(6,4,2,0,-3, 0);
        topo.addLink(7,1,3,0,-4, 0);
        topo.addLink(8,3,0,0,2, 0);
        topo.addLink(9,0,4,0,7, 0);

        // path from 0 to 1
        int[] linkPath1 = { 9, 6, 2 };
        SimplifiedPath path1 = new SimplifiedPath(topo,linkPath1);

        // path from 0 to 2
        int[] linkPath2 = { 9, 6 };
        SimplifiedPath path2 = new SimplifiedPath(topo,linkPath2);

        // path from 0 to 3
        int[] linkPath3 = { 9, 6, 2, 7};
        SimplifiedPath path3 = new SimplifiedPath(topo,linkPath3);

        // path from 0 to 4
        int[] linkPath4 = { 9 };
        SimplifiedPath path4 = new SimplifiedPath(topo,linkPath4);

        Bhandari bhandari = new Bhandari();
        SimplifiedPath computedPath1 = bhandari.computeSPF(topo,0,1);
        Assert.assertEquals(computedPath1.getLinkIdPath().length,3);
        Assert.assertTrue(computedPath1.equals(path1));

        SimplifiedPath computedPath2 = bhandari.computeSPF(topo,0,2);
        Assert.assertEquals(computedPath2.getLinkIdPath().length,2);
        Assert.assertTrue(computedPath2.equals(path2));

        SimplifiedPath computedPath3 = bhandari.computeSPF(topo,0,3);
        Assert.assertEquals(computedPath3.getLinkIdPath().length,4);
        Assert.assertTrue(computedPath3.equals(path3));

        SimplifiedPath computedPath4 = bhandari.computeSPF(topo,0,4);
        Assert.assertEquals(computedPath4.getLinkIdPath().length,1);
        Assert.assertTrue(computedPath4.equals(path4));

        // Compute all path from source node 0
        List<SimplifiedPath> pathList = bhandari.computeSPF(topo,0);
        Assert.assertTrue(pathList.get(0).equals(path1));
        Assert.assertTrue(pathList.get(1).equals(path2));
        Assert.assertTrue(pathList.get(2).equals(path3));
        Assert.assertTrue(pathList.get(3).equals(path4));

    }

    @Test
    public void testNonConsecutiveId() throws NoRouteToHostException, RoutingException, NodeNotFoundException, LinkNotFoundException, InvalidPathException, InvalidDomainException, FileNotFoundException {
        String fileName = "src/resources/junit-test/test-domain1.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        domain.removeNode(domain.getNode("1"));
        Bhandari bhandari = new Bhandari();


        List<Node> nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("0"));
        nodeList.add(domain.getNode("2"));
        Path path0 = new PathImpl(domain);
        path0.createPathFromNode(nodeList);

        nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("0"));
        nodeList.add(domain.getNode("2"));
        nodeList.add(domain.getNode("3"));
        Path path1 = new PathImpl(domain);
        path1.createPathFromNode(nodeList);

        nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("0"));
        nodeList.add(domain.getNode("2"));
        nodeList.add(domain.getNode("4"));
        Path path2 = new PathImpl(domain);
        path2.createPathFromNode(nodeList);

        List<Path> pathList = bhandari.computeSPF(domain,"0");
        Assert.assertTrue(pathList.get(0).equals(path0));
        Assert.assertTrue(pathList.get(1).equals(path1));
        Assert.assertTrue(pathList.get(2).equals(path2));
    }

    @Test
    public void testCompareWithCSPF() throws NoRouteToHostException, RoutingException, LinkNotFoundException, NodeNotFoundException, InvalidDomainException, FileNotFoundException {
        String fileName = "src/resources/junit-test/test-domain1.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        domain.getValidator().forceNoMultiGraph();
        Bhandari bhandari = new Bhandari();
        CSPF cspf = new CSPF();
        List<Node> nodeList = domain.getUpNodes();
        for (int srcId = 0; srcId < nodeList.size(); srcId++) {
            Node srcNode = nodeList.get(srcId);
            for (int dstId = 0; dstId < nodeList.size(); dstId++) {
                if (dstId != srcId) {
                    Node dstNode = nodeList.get(dstId);
                    Path bhandariPath = bhandari.computeSPF(domain,srcNode.getId(),dstNode.getId());
                    Path cspfPath = cspf.computeSPF(domain,srcNode.getId(),dstNode.getId());
                    float bhandariCost = 0;
                    List<Link> linkList = bhandariPath.getLinkPath();
                    for (int i = 0; i < linkList.size(); i++) {
                        bhandariCost += linkList.get(i).getMetric();
                    }
                    float cspfCost = 0;
                    linkList = cspfPath.getLinkPath();
                    for (int i = 0; i < linkList.size(); i++) {
                        cspfCost += linkList.get(i).getMetric();
                    }
                    if (Math.abs(cspfCost - bhandariCost) > 0.00001) {
                        System.out.println("Path differents from " + srcNode.getId() + " to "  + dstNode.getId());
                        StringBuffer sb = new StringBuffer(" [");
                        for (int i = 0; i < bhandariPath.getNodePath().size(); i++) {
                            sb.append(" " + bhandariPath.getNodePath().get(i).getId());
                        }
                        sb.append(" ]");
                        System.out.println("Bhandari path " + sb.toString() + " wiht cost " + bhandariCost);
                        sb = new StringBuffer(" [");
                        for (int i = 0; i < cspfPath.getNodePath().size(); i++) {
                            sb.append(" " + cspfPath.getNodePath().get(i).getId());
                        }
                        sb.append(" ]");
                        System.out.println("CSPF path " + sb.toString() + " wiht cost " + cspfCost);
                    }
                    Assert.assertEquals(cspfCost,bhandariCost,0.00001);
                }
            }
        }
    }

}
