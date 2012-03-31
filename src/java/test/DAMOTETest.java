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

import be.ac.ulg.montefiore.run.totem.domain.exception.DomainAlreadyExistException;
import be.ac.ulg.montefiore.run.totem.domain.exception.InvalidDomainException;
import be.ac.ulg.montefiore.run.totem.domain.exception.LinkNotFoundException;
import be.ac.ulg.montefiore.run.totem.domain.exception.NodeNotFoundException;
import be.ac.ulg.montefiore.run.totem.domain.facade.InterDomainManager;
import be.ac.ulg.montefiore.run.totem.domain.model.Domain;
import be.ac.ulg.montefiore.run.totem.domain.model.Path;
import be.ac.ulg.montefiore.run.totem.domain.persistence.DomainFactory;
import be.ac.ulg.montefiore.run.totem.repository.DAMOTE.DAMOTE;
import be.ac.ulg.montefiore.run.totem.repository.model.*;
import be.ac.ulg.montefiore.run.totem.repository.model.exception.*;
import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.HashMap;

/*
 * Changes:
 * --------
 *  - 18-Aug-2006: optimize imports (GMO)
 */

/* Notes: (GMO)
*  -----
* Some tests gives warnings (junit [WARN] and [CRITICAL]), it's normal.
*
* Routing a lsp with DAMOTE returns a sequence of actions, which execution updates the TOTEM database.
* now DAMOTE listens to changes in the database,
* therefore, if addLsp parameter is set to true, actions execute twice on DAMOTE DB, displaying errors messages
* (AddDBException because lsp already exists). Setting addLsp to false avoid this behavior.
*
* Even more error messages are displayed when using preemption because preempted LSPs are directly suppressed from
* DAMOTE database. There is no choice about that. Executing the preemption actions returned by DAMOTE will always result
* in an AddDBException (lspNotFound) on the DAMOTE DB.
*
* Error messages are also displayed by DAMOTE where there is no route to host whith sufficient.
*/

/**
 * Extensive tests to test DAMOTE
 *
 * <p>Creation date: 07-Jun.-2004
 *
 * @author  Olivier Delcourt (delcourt@run.montefiore.ulg.ac.be)
 * @author Gael Monfort (monfort@run.montefiore.ulg.ac.be)
 */
public class DAMOTETest {
    private static Logger logger = Logger.getLogger(DAMOTETest.class.getName());

    @Test
    public void testDamoteRoute() throws Exception {

        HashMap startAlgoParameters = new HashMap();
        startAlgoParameters.put("colorClause","false");
        startAlgoParameters.put("colorClause","false");
        startAlgoParameters.put("allowReroute","true");
        startAlgoParameters.put("capacityClause","true");
        startAlgoParameters.put("loadbal","0");
        startAlgoParameters.put("tMin","0");
        startAlgoParameters.put("delay","0");
        startAlgoParameters.put("load","1");

        TotemActionList  actionList;
        AddLspAction action;

        InterDomainManager.getInstance().removeAllDomains();

        String fileName = "src/resources/junit-test/test-domain-withoutDS.xml";

        Domain domain = DomainFactory.loadDomain(fileName, false, true);
        //DomainFactory.saveDomain("src/resources/junit-test/test-domain-withoutDSmodfirst.xml",domain);

        try{
            InterDomainManager.getInstance().addDomain(domain);
        }catch(DomainAlreadyExistException e){
           e.printStackTrace();
        }
        int ASID = domain.getASID();

        startAlgoParameters.put("ASID",(new Integer(ASID)).toString());

        DAMOTE damote = new DAMOTE();
        damote.start(startAlgoParameters);

        //damote.printDamoteDB();

        // first creating a primary LSP from node 2 to 4
        LSPPrimaryRoutingParameter primaryParam = new LSPPrimaryRoutingParameter("2","4","Primary_0");
        primaryParam.setBandwidth(10);
        primaryParam.putRoutingAlgorithmParameter("preempt", Boolean.toString(false));
        primaryParam.putRoutingAlgorithmParameter("addLSP", Boolean.toString(false));
        primaryParam.setClassType(0);
        primaryParam.setSetup(0);
        primaryParam.setHolding(0);

        actionList = damote.routeLSP(domain,primaryParam);
        boolean found = false;
        for (int i=0; i<actionList.size(); i++){
            if (actionList.get(i) instanceof AddLspAction){
                found = true;
                action = (AddLspAction) actionList.get(i);
                Path path = action.getLsp().getLspPath();
                Assert.assertEquals(path.getLinkPath().size(), 1);
                Assert.assertEquals(path.getLinkPath().get(0).getId(),"2_2 -> 4_2");
                //System.out.println("Valeur du lspid " + action.getLsp().getId());
                action.execute();
            }
        }
        Assert.assertTrue(found);

        Assert.assertEquals(1, domain.getNbLsps());

        // now creating a global backup for this lsp

        LSPDetourRoutingParameter detourParam = new LSPDetourRoutingParameter(domain.generateLspId());
        detourParam.setProtectedLSP("Primary_0");
        detourParam.setMethodType(LSPDetourRoutingParameter.GLOBAL);
        detourParam.putRoutingAlgorithmParameter("addLSP","false");

        //damote.printDamoteDB();

        actionList = damote.routeDetour(domain,detourParam);

        found = false;
        for (int i=0; i<actionList.size(); i++){
            if (actionList.get(i) instanceof AddLspAction){
                found = true;
                action = (AddLspAction) actionList.get(i);
                Path path = action.getLsp().getLspPath();
                Assert.assertEquals(path.getLinkPath().size(), 2);
                Assert.assertEquals(path.getLinkPath().get(0).getId(),"2_3 -> 3_3");
                Assert.assertEquals(path.getLinkPath().get(1).getId(),"3_4 -> 4_4");
                //System.out.println("Valeur du lspid " + action.getLsp().getId());

                action.execute();     //TODO: this method does not call setdetourlspinfo
            }
        }
        Assert.assertTrue(found);

        // now computing a primary lsp from node 1 to node 4
        primaryParam = new LSPPrimaryRoutingParameter("1","4","Primary_1");
        primaryParam.setBandwidth(10);
        primaryParam.putRoutingAlgorithmParameter("preempt", Boolean.toString(false));
        primaryParam.putRoutingAlgorithmParameter("addLSP", Boolean.toString(false));
        primaryParam.setClassType(0);
        primaryParam.setSetup(0);
        primaryParam.setHolding(0);

        actionList = damote.routeLSP(domain,primaryParam);
        for (int i=0; i<actionList.size(); i++){
            if (actionList.get(i) instanceof AddLspAction){
                action = (AddLspAction) actionList.get(i);
                Path path = action.getLsp().getLspPath();
                Assert.assertEquals(path.getLinkPath().get(0).getId(),"1_1 -> 2_1");
                Assert.assertEquals(path.getLinkPath().get(1).getId(),"2_2 -> 4_2");
                //System.out.println("Valeur du lspid " + action.getLsp().getId());
                action.execute();
            }
        }


        // now creating local backups for this lsp
        detourParam = new LSPDetourRoutingParameter("LocalP1");
        detourParam.setProtectedLSP("Primary_1");
        detourParam.setMethodType(LSPDetourRoutingParameter.LOCAL);
        detourParam.putRoutingAlgorithmParameter("addLSP","false");

        actionList = damote.routeDetour(domain,detourParam);

        boolean found2 = false;
        found = false;
        for (int i=0; i<actionList.size(); i++){
            if (actionList.get(i) instanceof AddLspAction){
                action = (AddLspAction) actionList.get(i);
                Path path = action.getLsp().getLspPath();
                if (action.getLsp().getId().equals("LocalP1-0")){
                    found = true;
                    Assert.assertEquals(path.getLinkPath().get(0).getId(),"1_1 -> 0_1");
                    Assert.assertEquals(path.getLinkPath().get(1).getId(),"0_0 -> 2_0");
                }
                if (action.getLsp().getId().equals("LocalP1-1")){
                    found2 = true;
                    Assert.assertEquals(path.getLinkPath().get(0).getId(),"2_3 -> 3_3");
                    Assert.assertEquals(path.getLinkPath().get(1).getId(),"3_4 -> 4_4");
                }

                //System.out.println("Valeur du lspid " + action.getLsp().getId() + " Path: " + path);

                action.execute();     //TODO: this method does not call setdetourlspinfo
            }
        }
        Assert.assertTrue(found);
        Assert.assertTrue(found2);

        //damote.printDamoteDB();
        damote.stop();

    }

    @Test
    public void test2() throws NoRouteToHostException, RoutingException, LinkNotFoundException, NodeNotFoundException, LocalDatabaseException, InvalidDomainException, FileNotFoundException, AlgorithmInitialisationException, TotemActionExecutionException {
        logger.warn("Some tests may produce error messages identified as [CRITICAL]. What's important is that there are no Failures or Errors in junit summary result.");

        HashMap startAlgoParameters = new HashMap();
        startAlgoParameters.put("colorClause","false");
        startAlgoParameters.put("colorClause","false");
        startAlgoParameters.put("allowReroute","true");
        startAlgoParameters.put("capacityClause","true");
        startAlgoParameters.put("loadbal","0");
        startAlgoParameters.put("tMin","0");
        startAlgoParameters.put("delay","0");
        startAlgoParameters.put("load","1");

        String fileName = "src/resources/junit-test/test-domain2.xml";

        InterDomainManager.getInstance().removeAllDomains();
        Domain domain = DomainFactory.loadDomain(fileName);
        //DomainFactory.saveDomain("src/resources/junit-test/test-domain-withoutDSmod.xml",domain);

        try{
            InterDomainManager.getInstance().addDomain(domain);
        }catch(DomainAlreadyExistException e){
           e.printStackTrace();
        }

        int ASID = domain.getASID();


        startAlgoParameters.remove("ASID");
        startAlgoParameters.put("ASID",(new Integer(ASID)).toString());
        startAlgoParameters.remove("allowReroute");
        startAlgoParameters.put("allowReroute","false");

        DAMOTE damote = new DAMOTE();
        damote.start(startAlgoParameters);
        //damote.printDamoteDB();


        // first creating a primary LSP from 2 to 0 using all the available capacity for priority 1
        LSPPrimaryRoutingParameter primaryParam = new LSPPrimaryRoutingParameter("2","0","Primary_0");
        primaryParam.setBandwidth(150000);
        primaryParam.putRoutingAlgorithmParameter("preempt", Boolean.toString(true));
        primaryParam.putRoutingAlgorithmParameter("addLSP", Boolean.toString(false));
        primaryParam.setClassType(0);
        primaryParam.setSetup(1);
        primaryParam.setHolding(1);

        TotemActionList actionList = damote.routeLSP(domain,primaryParam);
        //damote.printDamoteDB();
        AddLspAction action;
        //Assert.assertEquals(actionList.size(), 1);
        for (int i=0; i<actionList.size(); i++){
            if (actionList.get(i) instanceof AddLspAction){
                action = (AddLspAction) actionList.get(i);
                Path path = action.getLsp().getLspPath();
                Assert.assertEquals(path.getLinkPath().size(), 1);
                Assert.assertEquals(path.getLinkPath().get(0).getId(),"2_0 -> 0_0");
                //System.out.println("Valeur du lspid " + action.getLsp().getId());
                action.execute();
            }
            if (actionList.get(i) instanceof PreemptLspsAction){
                Assert.assertEquals(((PreemptLspsAction) actionList.get(i)).getLsps().size(), 0);
            }
        }

        // creating a second primary LSP from 2 to 0 which requires a bandwidth of 10000 for priority 0
        // as there is not enough free bandwidth, lsp from priority 1 is being preempted
        primaryParam = new LSPPrimaryRoutingParameter("2","0","Primary_1");
        primaryParam.setBandwidth(10000);
        primaryParam.putRoutingAlgorithmParameter("preempt", Boolean.toString(false));
        primaryParam.putRoutingAlgorithmParameter("addLSP", Boolean.toString(false));
        primaryParam.setClassType(0);
        primaryParam.setSetup(0);
        primaryParam.setHolding(0);

        // No route can't be found without preemption
        boolean error = false;
        try {
            actionList = damote.routeLSP(domain,primaryParam);
        }
        catch (NoRouteToHostException ex) {
             error = true;
        }
        Assert.assertTrue(error);

        primaryParam.putRoutingAlgorithmParameter("preempt", Boolean.toString(true));
        actionList = damote.routeLSP(domain,primaryParam);

        Assert.assertTrue(actionList.size() >= 2);
        boolean foundAdd = false;
        boolean foundPreempt = false;
        for (int i=0; i<actionList.size(); i++){

            if (actionList.get(i) instanceof AddLspAction){
                foundAdd = true;
                action = (AddLspAction) actionList.get(i);
                Path path = action.getLsp().getLspPath();
                Assert.assertEquals(path.getLinkPath().size(), 1);
                Assert.assertEquals(path.getLinkPath().get(0).getId(),"2_0 -> 0_0");
                //System.out.println("Valeur du lspid " + action.getLsp().getId());
                action.execute();
            }


            if (actionList.get(i) instanceof PreemptLspsAction){
                foundPreempt = true;
                PreemptLspsAction preemptAction = (PreemptLspsAction) actionList.get(i);
                Assert.assertEquals(1, preemptAction.getLsps().size());

                preemptAction.execute();
            }

        }
        Assert.assertTrue(foundAdd && foundPreempt);

        Assert.assertEquals(domain.getNbLsps(), 1);
        Assert.assertTrue(domain.getAllLsps().get(0).getId().equals("Primary_1"));
        Assert.assertEquals(domain.getLink("2_0 -> 0_0").getReservedBandwidth(0), 10000, 0.0001);
        Assert.assertEquals(domain.getLink("2_0 -> 0_0").getReservableBandwidth(1), 140000, 0.0001);

        damote.stop();

    }

    /* Attention: BD may not keep synchronized if preemptions actions are not executed
        (preempted lsps will be different in DAMOTE and TOTEM) */
    @Test
    public void test3() throws LinkNotFoundException, NoRouteToHostException, RoutingException, NodeNotFoundException, LocalDatabaseException, InvalidDomainException, FileNotFoundException, AlgorithmInitialisationException, TotemActionExecutionException {
        // big test to assess preemption mechanisms, first beginning with preemption mechanisms
        // developed into the toolbox itself

        String fileName = "src/resources/junit-test/test-domain13.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        //DomainFactory.saveDomain("src/resources/junit-test/test-domain12DSadded.xml",domain);

        try{
            InterDomainManager.getInstance().addDomain(domain);
        }catch(DomainAlreadyExistException e){
            e.printStackTrace();
        }

        int ASID = domain.getASID();

        HashMap startAlgoParameters = new HashMap();
        startAlgoParameters.put("colorClause","false");
        startAlgoParameters.put("colorClause","false");
        startAlgoParameters.put("allowReroute","true");
        startAlgoParameters.put("capacityClause","true");
        startAlgoParameters.put("loadbal","0");
        startAlgoParameters.put("tMin","0");
        startAlgoParameters.put("delay","0");
        startAlgoParameters.put("load","1");

        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservableBandwidth(0),200000,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservableBandwidth(1),200000,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservableBandwidth(2),200000,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservedBandwidth(0),0,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservedBandwidth(1),0,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservedBandwidth(2),0,0.0001);

        startAlgoParameters.remove("ASID");
        startAlgoParameters.put("ASID",(new Integer(ASID)).toString());


        DAMOTE damote = new DAMOTE();
        damote.start(startAlgoParameters);

        LSPPrimaryRoutingParameter primaryParam = new LSPPrimaryRoutingParameter("2","4","Primary_0");
        primaryParam.setBandwidth(50000);
        primaryParam.putRoutingAlgorithmParameter("preempt", Boolean.toString(true));
        primaryParam.putRoutingAlgorithmParameter("addLSP", Boolean.toString(false));
        primaryParam.setClassType(0);
        primaryParam.setSetup(2);
        primaryParam.setHolding(2);

        TotemActionList actionList = damote.routeLSP(domain,primaryParam);
        AddLspAction action;
        for (int i=0; i<actionList.size(); i++){
            if (actionList.get(i) instanceof AddLspAction){
                action = (AddLspAction) actionList.get(i);
                Path path = action.getLsp().getLspPath();
                Assert.assertEquals(path.getLinkPath().get(0).getId(),"2_2 -> 4_2");
                //System.out.println("Valeur du lspid " + action.getLsp().getId());
                action.execute();
            }
        }

        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservableBandwidth(0),200000,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservableBandwidth(1),200000,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservableBandwidth(2),150000,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservedBandwidth(0),0,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservedBandwidth(1),0,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservedBandwidth(2),50000,0.0001);

        primaryParam = new LSPPrimaryRoutingParameter("2","4","Primary_1");
        primaryParam.setBandwidth(50000);
        primaryParam.putRoutingAlgorithmParameter("preempt", Boolean.toString(true));
        primaryParam.putRoutingAlgorithmParameter("addLSP", Boolean.toString(false));
        primaryParam.setClassType(0);
        primaryParam.setSetup(2);
        primaryParam.setHolding(2);

        actionList = damote.routeLSP(domain,primaryParam);
        for (int i=0; i<actionList.size(); i++){
            if (actionList.get(i) instanceof AddLspAction){
                action = (AddLspAction) actionList.get(i);
                Path path = action.getLsp().getLspPath();
                Assert.assertEquals(path.getLinkPath().get(0).getId(),"2_2 -> 4_2");
                //System.out.println("Valeur du lspid " + action.getLsp().getId());
                action.execute();
            }
        }

        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservableBandwidth(0),200000,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservableBandwidth(1),200000,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservableBandwidth(2),100000,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservedBandwidth(0),0,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservedBandwidth(1),0,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservedBandwidth(2),100000,0.0001);

        primaryParam = new LSPPrimaryRoutingParameter("2","4","Primary_2");
        primaryParam.setBandwidth(50000);
        primaryParam.putRoutingAlgorithmParameter("preempt", Boolean.toString(true));
        primaryParam.putRoutingAlgorithmParameter("addLSP", Boolean.toString(false));
        primaryParam.setClassType(0);
        primaryParam.setSetup(1);
        primaryParam.setHolding(1);

        actionList = damote.routeLSP(domain,primaryParam);
        for (int i=0; i<actionList.size(); i++){
            if (actionList.get(i) instanceof AddLspAction){
                action = (AddLspAction) actionList.get(i);
                Path path = action.getLsp().getLspPath();
                Assert.assertEquals(path.getLinkPath().get(0).getId(),"2_2 -> 4_2");
                //System.out.println("Valeur du lspid " + action.getLsp().getId());
                action.execute();
            }
        }

        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservableBandwidth(0),200000,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservableBandwidth(1),150000,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservableBandwidth(2),50000,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservedBandwidth(0),0,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservedBandwidth(1),50000,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservedBandwidth(2),100000,0.0001);

        primaryParam = new LSPPrimaryRoutingParameter("2","4","Primary_3");
        primaryParam.setBandwidth(50000);
        primaryParam.putRoutingAlgorithmParameter("preempt", Boolean.toString(true));
        primaryParam.putRoutingAlgorithmParameter("addLSP", Boolean.toString(false));
        primaryParam.setClassType(0);
        primaryParam.setSetup(1);
        primaryParam.setHolding(1);

        actionList = damote.routeLSP(domain,primaryParam);
        for (int i=0; i<actionList.size(); i++){
            if (actionList.get(i) instanceof AddLspAction) {
                action = (AddLspAction) actionList.get(i);
                Path path = action.getLsp().getLspPath();
                Assert.assertEquals(path.getLinkPath().get(0).getId(),"2_2 -> 4_2");
                //System.out.println("Valeur du lspid " + action.getLsp().getId());
                action.execute();
            }
        }

        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservableBandwidth(0),200000,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservableBandwidth(1),100000,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservableBandwidth(2),0,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservedBandwidth(0),0,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservedBandwidth(1),100000,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservedBandwidth(2),100000,0.0001);

        //damote.printDamoteDB();


        // creating a second primary LSP from 2 to 4 which requires the total bandwidth at priority0
        // thus needing to preempt all four previous lsps
        primaryParam = new LSPPrimaryRoutingParameter("2","4","Primary_4");
        primaryParam.setBandwidth(200000);
        primaryParam.putRoutingAlgorithmParameter("preempt", Boolean.toString(true));
        primaryParam.putRoutingAlgorithmParameter("addLSP", Boolean.toString(false));
        primaryParam.setClassType(0);
        primaryParam.setSetup(0);
        primaryParam.setHolding(0);

        actionList = damote.routeLSP(domain,primaryParam);
        //damote.printDamoteDB();
        for (int i=0; i<actionList.size(); i++){
            if (actionList.get(i) instanceof AddLspAction){
                action = (AddLspAction) actionList.get(i);
                Path path = action.getLsp().getLspPath();
                Assert.assertEquals(path.getLinkPath().get(0).getId(),"2_2 -> 4_2");
                //System.out.println("Valeur du lspid " + action.getLsp().getId());
                action.execute();
            }
            if (actionList.get(i) instanceof PreemptLspsAction){
                PreemptLspsAction preemptAction = (PreemptLspsAction) actionList.get(i);
                preemptAction.execute();
            }
        }
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservableBandwidth(0),0,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservableBandwidth(1),0,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservableBandwidth(2),0,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservedBandwidth(0),200000,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservedBandwidth(1),0,0.0001);
        Assert.assertEquals(domain.getLink("2_2 -> 4_2").getReservedBandwidth(2),0,0.0001);

        //damote.printDamoteDB();

        damote.stop();

    }
}
