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

import be.ac.ulg.montefiore.run.totem.repository.CSPF.CSPF;
import be.ac.ulg.montefiore.run.totem.repository.model.LSPPrimaryRoutingParameter;
import be.ac.ulg.montefiore.run.totem.repository.model.AddLspAction;
import be.ac.ulg.montefiore.run.totem.repository.model.exception.NoRouteToHostException;
import be.ac.ulg.montefiore.run.totem.repository.model.exception.RoutingException;
import be.ac.ulg.montefiore.run.totem.domain.model.*;
import be.ac.ulg.montefiore.run.totem.domain.model.impl.PathImpl;
import be.ac.ulg.montefiore.run.totem.domain.persistence.DomainFactory;
import be.ac.ulg.montefiore.run.totem.domain.exception.InvalidDomainException;
import be.ac.ulg.montefiore.run.totem.domain.exception.NodeNotFoundException;
import be.ac.ulg.montefiore.run.totem.domain.exception.InvalidPathException;

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
 * Test CSPF algorithm
 *
 * <p>Creation date: 05-Jan.-2004
 *
 * @author  Fabian Skivee (skivee@run.montefiore.ulg.ac.be)
 */
public class CSPFTest {

    @Test
    public void testSimpleSPF() throws Exception {
        String fileName = "src/resources/junit-test/test-domain1.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        domain.getValidator().forceNoMultiGraph();
        CSPF cspf = new CSPF();
        Path path = cspf.computeSPF(domain,"0","1");
        List<Node> nodeList = path.getNodePath();
        Assert.assertEquals(nodeList.size(),3);
        Assert.assertEquals(nodeList.get(0).getId(),"0");
        Assert.assertEquals(nodeList.get(1).getId(),"2");
        Assert.assertEquals(nodeList.get(2).getId(),"1");
    }

    @Test
    public void testSameSrcDst() throws Exception {
        String fileName = "src/resources/junit-test/test-domain1.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        domain.getValidator().forceNoMultiGraph();
        CSPF cspf = new CSPF();
        LSPPrimaryRoutingParameter param = new LSPPrimaryRoutingParameter("2", "2","0");
        param.setBandwidth(1);
        Lsp lsp = ((AddLspAction) cspf.routeLSP(domain,param).get(0)).getLsp();
        Assert.assertEquals(lsp.getLspPath().getNodePath().size(),1);
        Assert.assertEquals(lsp.getLspPath().getNodePath().get(0).getId(),"2");
    }

    @Test
    public void testBandwidthConstraint() throws Exception {
        String fileName = "src/resources/junit-test/test-domain1.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        domain.getValidator().forceNoMultiGraph();
        CSPF cspf = new CSPF();
        LSPPrimaryRoutingParameter param = new LSPPrimaryRoutingParameter("4","1","0");
        param.setBandwidth(15);
        Lsp lsp = ((AddLspAction) cspf.routeLSP(domain,param).get(0)).getLsp();
        Assert.assertEquals(lsp.getLspPath().getNodePath().size(),4);
        Assert.assertEquals(lsp.getLspPath().getNodePath().get(0).getId(),"4");
        Assert.assertEquals(lsp.getLspPath().getNodePath().get(1).getId(),"3");
        Assert.assertEquals(lsp.getLspPath().getNodePath().get(2).getId(),"2");
        Assert.assertEquals(lsp.getLspPath().getNodePath().get(3).getId(),"1");
    }

    @Test
    public void testMultiPath() throws Exception {
        String fileName = "src/resources/junit-test/test-domain4.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        domain.getValidator().forceNoMultiGraph();
        CSPF cspf = new CSPF();
        List<Path> paths = cspf.computeSPF(domain,"0","4",true);

        Assert.assertEquals(paths.size(), 2);

        List<Node> nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("0"));
        nodeList.add(domain.getNode("2"));
        nodeList.add(domain.getNode("4"));
        Path p0 = new PathImpl(domain);
        p0.createPathFromNode(nodeList);
        Assert.assertTrue(paths.contains(p0));

        nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("0"));
        nodeList.add(domain.getNode("2"));
        nodeList.add(domain.getNode("3"));
        nodeList.add(domain.getNode("4"));
        Path p1 = new PathImpl(domain);
        p1.createPathFromNode(nodeList);
        Assert.assertTrue(paths.contains(p1));


        paths = cspf.computeSPF(domain,"2","4",true);
        nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("2"));
        nodeList.add(domain.getNode("4"));
        Path p2 = new PathImpl(domain);
        p2.createPathFromNode(nodeList);
        Assert.assertTrue(paths.contains(p2));

        nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("2"));
        nodeList.add(domain.getNode("3"));
        nodeList.add(domain.getNode("4"));
        Path p3 = new PathImpl(domain);
        p3.createPathFromNode(nodeList);
        Assert.assertTrue(paths.contains(p3));
    }

    @Test
    public void testComputeSPFSource() throws NoRouteToHostException, RoutingException, NodeNotFoundException, InvalidPathException, InvalidDomainException, FileNotFoundException {
        String fileName = "src/resources/junit-test/test-domain4.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        domain.getValidator().forceNoMultiGraph();
        CSPF cspf = new CSPF();
        List<Path> paths = cspf.computeSPF(domain,"0");

        Assert.assertEquals(paths.size(), 4);
        List<Node> nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("0"));
        nodeList.add(domain.getNode("2"));
        nodeList.add(domain.getNode("1"));
        Path p4 = new PathImpl(domain);
        p4.createPathFromNode(nodeList);
        Assert.assertTrue(paths.contains(p4));

        nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("0"));
        nodeList.add(domain.getNode("2"));
        Path p5 = new PathImpl(domain);
        p5.createPathFromNode(nodeList);
        Assert.assertTrue(paths.contains(p5));

        nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("0"));
        nodeList.add(domain.getNode("2"));
        nodeList.add(domain.getNode("3"));
        Path p6 = new PathImpl(domain);
        p6.createPathFromNode(nodeList);
        Assert.assertTrue(paths.contains(p6));

        nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("0"));
        nodeList.add(domain.getNode("2"));
        nodeList.add(domain.getNode("4"));
        Path p7 = new PathImpl(domain);
        p7.createPathFromNode(nodeList);
        Assert.assertTrue(paths.contains(p7));

        /* //no multipath with computeSPf(domain, source)
        nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("0"));
        nodeList.add(domain.getNode("2"));
        nodeList.add(domain.getNode("3"));
        nodeList.add(domain.getNode("4"));
        Path p8 = new PathImpl(domain);
        p8.createPathFromNode(nodeList);
        Assert.assertTrue(paths.contains(p8));
        */
    }
}
