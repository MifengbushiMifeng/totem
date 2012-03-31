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
import be.ac.ulg.montefiore.run.totem.domain.model.impl.*;
import be.ac.ulg.montefiore.run.totem.domain.exception.*;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.model.impl.IGPShortcutStrategy;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.model.impl.BasicIGPShortcutStrategy;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.model.impl.TrafficMatrixImpl;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.model.TrafficMatrix;
import be.ac.ulg.montefiore.run.totem.repository.model.exception.NoRouteToHostException;
import be.ac.ulg.montefiore.run.totem.repository.model.exception.RoutingException;

import java.util.List;
import java.util.ArrayList;

import org.junit.Test;

/*
* Changes:
* --------
*
*/

/**
*
* The topology used in these tests is the following.
*<pre>
*     0-----1-----------2-----3-----4
*           |          /|     |
*           |         / |     |
*           |        /  |     |
*           5---6---7---8-----9
* </pre>
*
* We establish some lsps and then look at the path taken by traffic from node 0 to node 4.
*
*
* <p>Creation date: 23 juin 2006
*
* @author Ga�l Monfort (monfort@run.montefiore.ulg.ac.be)
*/

public class HybridStrategiesTest {
    private Domain buildDomain() throws NodeAlreadyExistException, NodeNotFoundException, LinkAlreadyExistException {
        System.out.println("Building Domain...");

        Domain domain = new DomainImpl(11);

        domain.addNode(new NodeImpl(domain, "0"));
        domain.addNode(new NodeImpl(domain, "1"));
        domain.addNode(new NodeImpl(domain, "2"));
        domain.addNode(new NodeImpl(domain, "3"));
        domain.addNode(new NodeImpl(domain, "4"));
        domain.addNode(new NodeImpl(domain, "5"));
        domain.addNode(new NodeImpl(domain, "6"));
        domain.addNode(new NodeImpl(domain, "7"));
        domain.addNode(new NodeImpl(domain, "8"));
        domain.addNode(new NodeImpl(domain, "9"));

        domain.addLink(new LinkImpl(domain, "0-1", "0", "1", 1000));
        domain.addLink(new LinkImpl(domain, "1-2", "1", "2", 1000));
        domain.addLink(new LinkImpl(domain, "2-3", "2", "3", 1000));
        domain.addLink(new LinkImpl(domain, "3-4", "3", "4", 1000));
        domain.addLink(new LinkImpl(domain, "0-5", "0", "5", 1000));
        domain.addLink(new LinkImpl(domain, "1-5", "1", "5", 1000));
        domain.addLink(new LinkImpl(domain, "5-6", "5", "6", 1000));
        domain.addLink(new LinkImpl(domain, "6-7", "6", "7", 1000));
        domain.addLink(new LinkImpl(domain, "7-8", "7", "8", 1000));
        domain.addLink(new LinkImpl(domain, "2-7", "2", "7", 1000));
        domain.addLink(new LinkImpl(domain, "2-8", "2", "8", 1000));
        domain.addLink(new LinkImpl(domain, "8-9", "8", "9", 1000));
        domain.addLink(new LinkImpl(domain, "3-9", "3", "9", 1000));

        domain.addLink(new LinkImpl(domain, "1-0", "1", "0", 1000));
        domain.addLink(new LinkImpl(domain, "2-1", "2", "1", 1000));
        domain.addLink(new LinkImpl(domain, "3-2", "3", "2", 1000));
        domain.addLink(new LinkImpl(domain, "4-3", "4", "3", 1000));
        domain.addLink(new LinkImpl(domain, "5-0", "5", "0", 1000));
        domain.addLink(new LinkImpl(domain, "5-1", "5", "1", 1000));
        domain.addLink(new LinkImpl(domain, "6-5", "6", "5", 1000));
        domain.addLink(new LinkImpl(domain, "7-6", "7", "6", 1000));
        domain.addLink(new LinkImpl(domain, "8-7", "8", "7", 1000));
        domain.addLink(new LinkImpl(domain, "7-2", "7", "2", 1000));
        domain.addLink(new LinkImpl(domain, "8-2", "8", "2", 1000));
        domain.addLink(new LinkImpl(domain, "9-8", "9", "8", 1000));
        domain.addLink(new LinkImpl(domain, "9-3", "9", "3", 1000));

        return domain;
    }

    @Test
    public void testIGPShortcut() throws NodeAlreadyExistException, NodeNotFoundException, LinkAlreadyExistException, NoRouteToHostException, RoutingException, InvalidDomainException, InvalidPathException, LspAlreadyExistException, LinkCapacityExceededException, DiffServConfigurationException, LspNotFoundException, LinkNotFoundException {

        Path lspPath;
        List<Node> lst;
        Lsp lsp;

        Domain domain = buildDomain();

        TrafficMatrix tm = new TrafficMatrixImpl(domain);
        tm.set("0", "4", 100);

        IGPShortcutStrategy is = new IGPShortcutStrategy(domain, tm);

        /* Computes path when no lsps exists in the domain */
        is.recompute();

        for (Link l : domain.getAllLinks()) {
            if (l.getId().equals("0-1") ||
                    l.getId().equals("1-2") ||
                    l.getId().equals("2-3") ||
                    l.getId().equals("3-4")) {
                Assert.assertEquals(is.getData().getLoad(l), 100, 0.00001);
            } else {
                Assert.assertEquals(is.getData().getLoad(l), 0, 0.00001);
            }
        }

        /* add a lsp between 1 and 2 */
        lspPath = new PathImpl(domain);
        lst = new ArrayList<Node>();
        lst.add(domain.getNode("1"));
        lst.add(domain.getNode("5"));
        lst.add(domain.getNode("6"));
        lst.add(domain.getNode("7"));
        lst.add(domain.getNode("2"));
        lspPath.createPathFromNode(lst);
        lsp = new LspImpl(domain, "lsp1", 200, lspPath);
        domain.addLsp(lsp);

        /* Add a lsp between 2 and 3 */
        lspPath = new PathImpl(domain);
        lst = new ArrayList<Node>();
        lst.add(domain.getNode("2"));
        lst.add(domain.getNode("8"));
        lst.add(domain.getNode("9"));
        lst.add(domain.getNode("3"));
        lspPath.createPathFromNode(lst);
        lsp = new LspImpl(domain, "lsp2", 200, lspPath);
        domain.addLsp(lsp);

        /* the path computed should use both lsp1 and lsp2 */
        is.recompute();


        for (Link l : domain.getAllLinks()) {
            if (l.getId().equals("0-1") ||
                    l.getId().equals("1-5") ||
                    l.getId().equals("5-6") ||
                    l.getId().equals("6-7") ||
                    l.getId().equals("7-2") ||
                    l.getId().equals("2-8") ||
                    l.getId().equals("8-9") ||
                    l.getId().equals("9-3") ||
                    l.getId().equals("3-4")) {
                Assert.assertEquals(is.getData().getLoad(l), 100, 0.00001);
            } else {
                Assert.assertEquals(is.getData().getLoad(l), 0, 0.00001);
            }
        }

        /* Compute another path starting at node 1 which egress closer to node 4 */

        lspPath = new PathImpl(domain);
        lst = new ArrayList<Node>();
        lst.add(domain.getNode("1"));
        lst.add(domain.getNode("5"));
        lst.add(domain.getNode("6"));
        lst.add(domain.getNode("7"));
        lst.add(domain.getNode("8"));
        lst.add(domain.getNode("9"));
        lst.add(domain.getNode("3"));
        lspPath.createPathFromNode(lst);
        lsp = new LspImpl(domain, "lsp3", 200, lspPath);
        domain.addLsp(lsp);

        /* The path should now use lsp3 */

        is.recompute();

        for (Link l : domain.getAllLinks()) {
            if (l.getId().equals("0-1") ||
                    l.getId().equals("1-5") ||
                    l.getId().equals("5-6") ||
                    l.getId().equals("6-7") ||
                    l.getId().equals("7-8") ||
                    l.getId().equals("8-9") ||
                    l.getId().equals("9-3") ||
                    l.getId().equals("3-4")) {
                Assert.assertEquals(is.getData().getLoad(l), 100, 0.00001);
            } else {
                Assert.assertEquals(is.getData().getLoad(l), 0, 0.00001);
            }
        }

        /* Same but path to node 3 */
        tm.set("0", "4", 0);
        tm.set("0", "3", 100);

        is.recompute();

        for (Link l : domain.getAllLinks()) {
            if (l.getId().equals("0-1") ||
                    l.getId().equals("1-5") ||
                    l.getId().equals("5-6") ||
                    l.getId().equals("6-7") ||
                    l.getId().equals("7-8") ||
                    l.getId().equals("8-9") ||
                    l.getId().equals("9-3")) {
                Assert.assertEquals(is.getData().getLoad(l), 100, 0.00001);
            } else {
                Assert.assertEquals(is.getData().getLoad(l), 0, 0.00001);
            }
        }
        return;
    }


    @Test
    public void testBasicIGPShortcut() throws InvalidDomainException, NodeAlreadyExistException, NodeNotFoundException, LinkAlreadyExistException, InvalidPathException, LspAlreadyExistException, LinkCapacityExceededException, NoRouteToHostException, RoutingException, LspNotFoundException, LinkNotFoundException, DiffServConfigurationException {
        Path lspPath;
        Path refPath;
        Path p;

        List<Node> lst;
        Lsp lsp;

        Domain domain = buildDomain();

        TrafficMatrix tm = new TrafficMatrixImpl(domain);
        tm.set("0", "4", 200);

        BasicIGPShortcutStrategy is = new BasicIGPShortcutStrategy(domain, tm);

        /* Compute a LSP starting at node 1 with egress as node 3 */

        lspPath = new PathImpl(domain);
        lst = new ArrayList<Node>();
        lst.add(domain.getNode("1"));
        lst.add(domain.getNode("5"));
        lst.add(domain.getNode("6"));
        lst.add(domain.getNode("7"));
        lst.add(domain.getNode("8"));
        lst.add(domain.getNode("9"));
        lst.add(domain.getNode("3"));
        lspPath.createPathFromNode(lst);
        lsp = new LspImpl(domain, "lsp1", 200, lspPath);
        domain.addLsp(lsp);


        /* Compute a lsp from 0 to 4, the shortest path should be used */
        //test computePath
        p = is.computePath(domain.getNode("0"), domain.getNode("4"));

        refPath = new PathImpl(domain);
        lst = new ArrayList<Node>();
        lst.add(domain.getNode("0"));
        lst.add(domain.getNode("1"));
        lst.add(domain.getNode("2"));
        lst.add(domain.getNode("3"));
        lst.add(domain.getNode("4"));
        refPath.createPathFromNode(lst);

        Assert.assertEquals(p, refPath);

        //test normal computation
        is.recompute();
        for (Link l : domain.getAllLinks()) {
            //System.out.println(l.getId() + ": " + is.getData().getLoad(l));
            if (l.getId().equals("0-1") ||
                    l.getId().equals("1-2") ||
                    l.getId().equals("2-3") ||
                    l.getId().equals("3-4")) {
                Assert.assertEquals(is.getData().getLoad(l), 200, 0.00001);
            } else {
                Assert.assertEquals(is.getData().getLoad(l), 0, 0.00001);
            }
        }

        /* Compute a lsp from 1 to 4, the shortest path should be used */

        p = is.computePath(domain.getNode("1"), domain.getNode("4"));

        refPath = new PathImpl(domain);
        lst = new ArrayList<Node>();
        lst.add(domain.getNode("1"));
        lst.add(domain.getNode("2"));
        lst.add(domain.getNode("3"));
        lst.add(domain.getNode("4"));
        refPath.createPathFromNode(lst);

        Assert.assertEquals(p, refPath);

        tm.set("0", "4", 0);
        tm.set("1", "4", 201);

        //test normal computation
        is.recompute();
        for (Link l : domain.getAllLinks()) {
            if (
                    l.getId().equals("1-2") ||
                    l.getId().equals("2-3") ||
                    l.getId().equals("3-4")) {
                Assert.assertEquals(is.getData().getLoad(l), 201, 0.00001);
            } else {
                Assert.assertEquals(is.getData().getLoad(l), 0, 0.00001);
            }
        }

        /* Compute a lsp from 0 to 3, the lsp path should be used */

        p = is.computePath(domain.getNode("0"), domain.getNode("3"));

        refPath = new PathImpl(domain);
        lst = new ArrayList<Node>();
        lst.add(domain.getNode("0"));
        lst.add(domain.getNode("1"));
        lst.add(domain.getNode("5"));
        lst.add(domain.getNode("6"));
        lst.add(domain.getNode("7"));
        lst.add(domain.getNode("8"));
        lst.add(domain.getNode("9"));
        lst.add(domain.getNode("3"));
        refPath.createPathFromNode(lst);

        Assert.assertEquals(p, refPath);

        System.out.println(p);

        tm.set("1", "4", 0);
        tm.set("0", "3", 202);

        //test normal computation
        is.recompute();
        for (Link l : domain.getAllLinks()) {
            if (l.getId().equals("0-1") ||
                    l.getId().equals("1-5") ||
                    l.getId().equals("5-6") ||
                    l.getId().equals("6-7") ||
                    l.getId().equals("7-8") ||
                    l.getId().equals("8-9") ||
                    l.getId().equals("9-3")
                    ) {
                Assert.assertEquals(is.getData().getLoad(l), 202, 0.00001);
            } else {
                Assert.assertEquals(is.getData().getLoad(l), 0, 0.00001);
            }
        }

        /* Compute a lsp from 1 to 3, the lsp should be used */


        p = is.computePath(domain.getNode("1"), domain.getNode("3"));

        refPath = new PathImpl(domain);
        lst = new ArrayList<Node>();
        lst.add(domain.getNode("1"));
        lst.add(domain.getNode("5"));
        lst.add(domain.getNode("6"));
        lst.add(domain.getNode("7"));
        lst.add(domain.getNode("8"));
        lst.add(domain.getNode("9"));
        lst.add(domain.getNode("3"));
        refPath.createPathFromNode(lst);

        Assert.assertEquals(p, refPath);

        System.out.println(p);

        tm.set("0", "3", 0);
        tm.set("1", "3", 203);

        //test normal computation
        is.recompute();
        for (Link l : domain.getAllLinks()) {
            if (
                    l.getId().equals("1-5") ||
                    l.getId().equals("5-6") ||
                    l.getId().equals("6-7") ||
                    l.getId().equals("7-8") ||
                    l.getId().equals("8-9") ||
                    l.getId().equals("9-3")
                    ) {
                Assert.assertEquals(is.getData().getLoad(l), 203, 0.00001);
            } else {
                Assert.assertEquals(is.getData().getLoad(l), 0, 0.00001);
            }
        }

    }

}
