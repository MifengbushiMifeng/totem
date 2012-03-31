
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

import be.ac.ulg.montefiore.run.totem.domain.model.Domain;
import be.ac.ulg.montefiore.run.totem.domain.model.Path;
import be.ac.ulg.montefiore.run.totem.domain.model.Node;
import be.ac.ulg.montefiore.run.totem.domain.model.impl.*;
import be.ac.ulg.montefiore.run.totem.domain.facade.InterDomainManager;
import be.ac.ulg.montefiore.run.totem.domain.exception.*;
import be.ac.ulg.montefiore.run.totem.repository.model.LSPPrimaryRoutingParameter;
import be.ac.ulg.montefiore.run.totem.repository.model.TotemActionList;
import be.ac.ulg.montefiore.run.totem.repository.model.TotemAction;
import be.ac.ulg.montefiore.run.totem.repository.model.exception.*;

import it.unina.repository.MIRA.MIRA;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import junit.framework.Assert;
import org.junit.Test;

/*
* Changes:
* --------
* 
*
*/

/**
* Test MIRA algorithm with the sample topology described in
 * (Wang, B., Su, X., Chen, C.: A New Bandwidth Guaranteed Routing Algorithm for MPLS Traffic Engineering.
 * In Proceedings of IEEE International Conference on Communications, ICC 2002) fig2
 *
 * Note: for newMIRA, use duplex links  
 *
* <p>Creation date: 31-Oct-2005
*
* @author Ga�l Monfort (monfort@run.montefiore.ulg.ac.be)
*/

public class MIRATest {
    @Test
    public void testSMIRA() throws NodeAlreadyExistException, NodeNotFoundException, DomainAlreadyExistException, LinkAlreadyExistException, NoRouteToHostException, RoutingException, LspNotFoundException, LinkNotFoundException, LocalDatabaseException, InvalidPathException, AlgorithmInitialisationException, NodeTypeException, LinkCapacityExceededException, TotemActionExecutionException, DiffServConfigurationException {
        Domain domain = buildDomain();

        MIRA mira = new MIRA();

        LSPPrimaryRoutingParameter param = new LSPPrimaryRoutingParameter("1", "5", "lsp1");
        param.setBandwidth(1);
        param.putRoutingAlgorithmParameter("addLSP", "false");

        HashMap miraParams = new HashMap();
        miraParams.put("ASID", new Integer(6666).toString());
        miraParams.put("version", "SMIRA");

        mira.start(miraParams);

        TotemActionList lst = mira.routeLSP(domain, param);

        //System.out.println("Executing " + lst.size() + " action(s).");
        for (int i = 0; i < lst.size(); i++) {
            ((TotemAction)lst.get(i)).execute();
        }

        Path expectedPath = new PathImpl(domain);
        List<Node> nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("1"));
        nodeList.add(domain.getNode("7"));
        nodeList.add(domain.getNode("8"));
        nodeList.add(domain.getNode("5"));
        expectedPath.createPathFromNode(nodeList);


        //System.out.println(domain.getLsp("lsp1").getLspPath());
        Assert.assertTrue(domain.getLsp("lsp1").getLspPath().equals(expectedPath));

        mira.stop();

        domain.removeLsp(domain.getLsp("lsp1"));

        domain.getLink("7-8").setBandwidth(2);
        domain.getLink("8-7").setBandwidth(2);

        mira.start(miraParams);
        param = new LSPPrimaryRoutingParameter("1", "5", "lsp2");
        param.setBandwidth(1);
        param.putRoutingAlgorithmParameter("addLSP", "false");
        lst = mira.routeLSP(domain, param);

        //System.out.println("Executing " + lst.size() + " action(s).");
        for (int i = 0; i < lst.size(); i++) {
            ((TotemAction)lst.get(i)).execute();
        }

        //System.out.println(domain.getLsp("lsp2").getLspPath());
        Assert.assertTrue(domain.getLsp("lsp2").getLspPath().equals(expectedPath));

        mira.stop();

    }

    /* same test as for SMIRA */
    @Test
    public void testNEWMIRA() throws NodeNotFoundException, LspNotFoundException, NoRouteToHostException, RoutingException, LinkNotFoundException, NodeAlreadyExistException, DomainAlreadyExistException, LinkAlreadyExistException, LocalDatabaseException, InvalidPathException, AlgorithmInitialisationException, NodeTypeException, LinkCapacityExceededException, TotemActionExecutionException, DiffServConfigurationException {
        Domain domain = buildDomain();

        MIRA mira = new MIRA();

        LSPPrimaryRoutingParameter param = new LSPPrimaryRoutingParameter("1", "5", "lsp1");
        param.setBandwidth(1);
        param.putRoutingAlgorithmParameter("addLSP", "false");

        HashMap miraParams = new HashMap();
        miraParams.put("ASID", new Integer(6666).toString());
        miraParams.put("version", "NEWMIRA");

        mira.start(miraParams);

        TotemActionList lst = mira.routeLSP(domain, param);

        //System.out.println("Executing " + lst.size() + " action(s).");
        for (int i = 0; i < lst.size(); i++) {
            ((TotemAction)lst.get(i)).execute();
        }

        Path expectedPath = new PathImpl(domain);
        List<Node> nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("1"));
        nodeList.add(domain.getNode("2"));
        nodeList.add(domain.getNode("3"));
        nodeList.add(domain.getNode("4"));
        nodeList.add(domain.getNode("5"));

        expectedPath.createPathFromNode(nodeList);

        //System.out.println(domain.getLsp("lsp1").getLspPath());
        Assert.assertTrue(domain.getLsp("lsp1").getLspPath().equals(expectedPath));

        mira.stop();

        domain.removeLsp(domain.getLsp("lsp1"));

        domain.getLink("7-8").setBandwidth(2);
        domain.getLink("8-7").setBandwidth(2);

        mira.start(miraParams);
        param = new LSPPrimaryRoutingParameter("1", "5", "lsp2");
        param.setBandwidth(1);
        param.putRoutingAlgorithmParameter("addLSP", "false");
        lst = mira.routeLSP(domain, param);

        //System.out.println("Executing " + lst.size() + " action(s).");
        for (int i = 0; i < lst.size(); i++) {
            ((TotemAction)lst.get(i)).execute();
        }

        //System.out.println(domain.getLsp("lsp2").getLspPath());
        Assert.assertTrue(domain.getLsp("lsp2").getLspPath().equals(expectedPath));

        mira.stop();

    }

    private Domain buildDomain() throws NodeNotFoundException, LinkAlreadyExistException, NodeAlreadyExistException, DomainAlreadyExistException, NodeTypeException {
        InterDomainManager.getInstance().removeAllDomains();
        Domain domain = new DomainImpl(6666);

        for (int i = 1; i < 12; i++) {
            Node n = new NodeImpl(domain, new Integer(i).toString());
            if (i==1 || i==5 || i==6 | i==9 || i==10 || i==11)
                n.setNodeType(Node.Type.EDGE);
            else n.setNodeType(Node.Type.CORE);
            domain.addNode(n);
        }

        domain.addLink(new LinkImpl(domain, "1-2", "1", "2", 1));
        domain.addLink(new LinkImpl(domain, "2-3", "2", "3", 1));
        domain.addLink(new LinkImpl(domain, "3-4", "3", "4", 1));
        domain.addLink(new LinkImpl(domain, "4-5", "4", "5", 1));

        domain.addLink(new LinkImpl(domain, "1-7", "1", "7", 1));
        domain.addLink(new LinkImpl(domain, "6-7", "6", "7", 1));
        domain.addLink(new LinkImpl(domain, "10-7", "10", "7", 1));

        domain.addLink(new LinkImpl(domain, "7-8", "7", "8", 1));

        domain.addLink(new LinkImpl(domain, "8-5", "8", "5", 1));
        domain.addLink(new LinkImpl(domain, "8-9", "8", "9", 1));
        domain.addLink(new LinkImpl(domain, "8-11", "8", "11", 1));


        /* also add reverse links (without that NEWMIRA won't work */
        domain.addLink(new LinkImpl(domain, "2-1", "2", "1", 1));
        domain.addLink(new LinkImpl(domain, "3-2", "3", "2", 1));
        domain.addLink(new LinkImpl(domain, "4-3", "4", "3", 1));
        domain.addLink(new LinkImpl(domain, "5-4", "5", "4", 1));

        domain.addLink(new LinkImpl(domain, "7-1", "7", "1", 1));
        domain.addLink(new LinkImpl(domain, "7-6", "7", "6", 1));
        domain.addLink(new LinkImpl(domain, "7-10", "7", "10", 1));

        domain.addLink(new LinkImpl(domain, "8-7", "8", "7", 1));

        domain.addLink(new LinkImpl(domain, "5-8", "5", "8", 1));
        domain.addLink(new LinkImpl(domain, "9-8", "9", "8", 1));
        domain.addLink(new LinkImpl(domain, "11-8", "11", "8", 1));

        InterDomainManager.getInstance().addDomain(domain);

        return domain;
    }
}
