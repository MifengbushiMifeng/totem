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
import be.ac.ulg.montefiore.run.totem.scenario.facade.ScenarioManager;
import be.ac.ulg.montefiore.run.totem.scenario.model.Scenario;
import be.ac.ulg.montefiore.run.totem.scenario.persistence.ScenarioFactory;
import be.ac.ulg.montefiore.run.totem.domain.facade.InterDomainManager;
import be.ac.ulg.montefiore.run.totem.domain.model.Domain;
import be.ac.ulg.montefiore.run.totem.domain.model.Lsp;
import be.ac.ulg.montefiore.run.totem.domain.exception.LspNotFoundException;
import be.ac.ulg.montefiore.run.totem.domain.exception.LinkNotFoundException;
import be.ac.ulg.montefiore.run.totem.domain.exception.NodeNotFoundException;

import java.io.File;

import org.junit.Test;

/*
* Changes:
* --------
*
*
*/

/**
* Scenario functionalities test.
*
*
* @author Ga�l Monfort (monfort@run.montefiore.ulg.ac.be)
*/

public class ScenarioTest {
    @Test
    public void testLoadSaveScenario() throws LspNotFoundException, LinkNotFoundException, NodeNotFoundException {
        String fileName = "src/resources/junit-test/test-scenario1.xml";

        Scenario sc = ScenarioFactory.loadScenario(fileName);

        ScenarioManager scm = ScenarioManager.getInstance();
        InterDomainManager.getInstance().removeAllDomains();
        scm.loadScenario(sc);

        executeAndTestScenario();

        InterDomainManager.getInstance().removeAllDomains();

        String outFileName = "src/resources/junit-test/test-scenario-out.xml";
        ScenarioFactory.saveScenario(outFileName, sc);

        scm.loadScenario(outFileName);

        executeAndTestScenario();

        File file = new File(outFileName);
        file.delete();
    }

    /**
     *  Execute the scenario loaded in ScenarioManager and check its behaviour
     */
    private void executeAndTestScenario() throws LspNotFoundException, LinkNotFoundException, NodeNotFoundException {

        ScenarioManager scm = ScenarioManager.getInstance();

        /* first event is load default domain */
        scm.executeNextEvent();

        Domain domain = InterDomainManager.getInstance().getDefaultDomain();

        Assert.assertNotNull(domain);
        Assert.assertEquals(domain.getASID(), 11537);

        /* second event is startAlgo (CSPF) */
        scm.executeNextEvent();

        /* next event is establish a LSP */
        scm.executeNextEvent();

        Assert.assertEquals(domain.getNbLsps(), 1);

        Lsp lsp1 = domain.getLsp("lsp1");

        Assert.assertEquals(lsp1.getLspPath().getNodePath().get(0).getId(), "STTL");
        Assert.assertEquals(lsp1.getLspPath().getNodePath().get(1).getId(), "DNVR");
        Assert.assertEquals(lsp1.getLspPath().getNodePath().get(2).getId(), "KSCY");
        Assert.assertEquals(lsp1.getLspPath().getNodePath().get(3).getId(), "IPLS");
        Assert.assertEquals(lsp1.getLspPath().getNodePath().get(4).getId(), "CHIN");
        Assert.assertEquals(lsp1.getLspPath().getNodePath().get(5).getId(), "NYCM");

    }

}
