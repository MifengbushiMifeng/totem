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
import be.ac.ulg.montefiore.run.totem.domain.model.Lsp;
import be.ac.ulg.montefiore.run.totem.domain.model.impl.*;
import be.ac.ulg.montefiore.run.totem.domain.facade.InterDomainManager;
import be.ac.ulg.montefiore.run.totem.domain.exception.*;
import be.ac.ulg.montefiore.run.totem.repository.DAMOTE.DAMOTE;
import be.ac.ulg.montefiore.run.totem.repository.model.LSPPrimaryRoutingParameter;
import be.ac.ulg.montefiore.run.totem.repository.model.TotemActionList;
import be.ac.ulg.montefiore.run.totem.repository.model.AddLspAction;
import be.ac.ulg.montefiore.run.totem.repository.model.exception.AlgorithmInitialisationException;
import be.ac.ulg.montefiore.run.totem.repository.model.exception.NoRouteToHostException;
import be.ac.ulg.montefiore.run.totem.repository.model.exception.RoutingException;
import be.ac.ulg.montefiore.run.totem.repository.model.exception.LocalDatabaseException;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import it.unina.repository.MIRA.MIRA;
import org.junit.Test;


/*
 * Changes:
 * --------
 *
 */

/**
 * Test the event Listeners
 *
 * <p>Creation date: 04-Nov-2005
 *
 *
 * @author  Ga�l Monfort (monfort@run.montefiore.ulg.ac.be)
 */

public class ListenersTest {
    /**
     *
     * check the number of listeners in the domain,
     * start algorithms, add a explicit route lsp in the domain,
     * route a second lsp and check that the right route is taken
     * (that the first lsp is added in the algorithm specific database)
     *
     */
    @Test
    public void testaddremoveLSP() throws NodeAlreadyExistException, NodeNotFoundException, LspAlreadyExistException, LinkCapacityExceededException, NoRouteToHostException, RoutingException, LinkNotFoundException, LinkAlreadyExistException, LocalDatabaseException, InvalidPathException, DiffServConfigurationException, LspNotFoundException, AlgorithmInitialisationException {

        Domain domain = new DomainImpl(515);

        for (int i = 1; i < 6; i++) {
            domain.addNode(new NodeImpl(domain, new Integer(i).toString()));
        }

        domain.addLink(new LinkImpl(domain, "1-2", "1", "2", 10000));
        domain.addLink(new LinkImpl(domain, "2-3", "2", "3", 10000));
        domain.addLink(new LinkImpl(domain, "3-4", "3", "4", 10000));
        domain.addLink(new LinkImpl(domain, "1-4", "1", "4", 10000));

        InterDomainManager.getInstance().removeAllDomains();
        try {
            InterDomainManager.getInstance().addDomain(domain);
        } catch (DomainAlreadyExistException e) {
            e.printStackTrace();
            Assert.fail();
            return;
        }

        DAMOTE damote = new DAMOTE();
        MIRA mira = new MIRA();

        HashMap startAlgoParameters = new HashMap();
        startAlgoParameters.remove("ASID");
        startAlgoParameters.put("ASID", new Integer(domain.getASID()).toString());

        int oldNbLst = domain.getObserver().getNbListeners();

        /* assert that listeners don't stay in the domain listeners list after the algorithm stopped */
        for (int i = 0; i < 5; i++) {
            damote.start(startAlgoParameters);
            mira.start(startAlgoParameters);
            damote.stop();
            mira.stop();
        }
        Assert.assertEquals(oldNbLst, domain.getObserver().getNbListeners()); // still one listener: SPFCache

        // start the algo
        damote.start(startAlgoParameters);

        mira.start(startAlgoParameters);


        Path p1 = new PathImpl(domain);
        List<Node> nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("1"));
        nodeList.add(domain.getNode("4"));

        p1.createPathFromNode(nodeList);

        Lsp lsp1 = new LspImpl(domain, "LSP 1-4", 10000, p1);

        //add lsp
        domain.addLsp(lsp1);

        TotemActionList actionList;


        LSPPrimaryRoutingParameter primaryParam = new LSPPrimaryRoutingParameter("1","4","Primary_0");
        primaryParam.setBandwidth(10000);
        primaryParam.putRoutingAlgorithmParameter("addLSP", Boolean.toString(false));
        primaryParam.setClassType(0);
        primaryParam.setSetup(0);
        primaryParam.setHolding(0);

        //LSP should be routed following 1-2-3-4 since 1-4 is reserved
        actionList = damote.routeLSP(domain, primaryParam);
        //damote.printDamoteDB();

        // add lsp to the DB
        boolean found = false;
        AddLspAction action;
        for (int i=0; i<actionList.size(); i++){
            if (actionList.get(i) instanceof AddLspAction){
                found = true;
                action = (AddLspAction) actionList.get(i);
                Path path = action.getLsp().getLspPath();
                Assert.assertEquals(path.getNodePath().size(), 4);
                Assert.assertEquals(path.getNodePath().get(0).getId(),"1");
                Assert.assertEquals(path.getNodePath().get(1).getId(),"2");
                Assert.assertEquals(path.getNodePath().get(2).getId(),"3");
                Assert.assertEquals(path.getNodePath().get(3).getId(),"4");
                //action.execute();
            }
        }
        Assert.assertTrue(found);

        LSPPrimaryRoutingParameter primary1Param = new LSPPrimaryRoutingParameter("1","4","Primary_1");
        primary1Param.setBandwidth(10000);
        primary1Param.putRoutingAlgorithmParameter("addLSP", Boolean.toString(false));
        primary1Param.setClassType(0);
        primary1Param.setSetup(0);
        primary1Param.setHolding(0);

        actionList = mira.routeLSP(domain, primary1Param);

        //boolean found = false;
        //AddLspAction action;
        // add lsp to the DB
        found = false;
        for (int i=0; i<actionList.size(); i++){
            if (actionList.get(i) instanceof AddLspAction){
                found = true;
                action = (AddLspAction) actionList.get(i);
                Path path = action.getLsp().getLspPath();
                Assert.assertEquals(path.getNodePath().size(), 4);
                Assert.assertEquals(path.getNodePath().get(0).getId(),"1");
                Assert.assertEquals(path.getNodePath().get(1).getId(),"2");
                Assert.assertEquals(path.getNodePath().get(2).getId(),"3");
                Assert.assertEquals(path.getNodePath().get(3).getId(),"4");
                //action.execute();
            }
        }
        Assert.assertTrue(found);


        mira.stop();
        damote.stop();

    }
}
