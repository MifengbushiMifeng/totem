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

import java.util.Iterator;

import be.ac.ucl.ingi.totem.scenario.model.CBGPInfo;
import be.ac.ucl.ingi.totem.scenario.model.CBGPPeerDown;
import be.ac.ucl.ingi.totem.scenario.model.CBGPPeerRecv;
import be.ac.ucl.ingi.totem.scenario.model.CBGPRun;
import be.ac.ulg.montefiore.run.totem.scenario.model.LoadDomain;
import be.ac.ulg.montefiore.run.totem.scenario.model.StartAlgo;
import be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.Scenario;
import be.ac.ulg.montefiore.run.totem.scenario.persistence.ScenarioFactory;
import junit.framework.Assert;
import org.junit.Test;

/*
 * Changes:
 * --------
 *
 */

/**
 * Tests if the
 * <code>be.ac.ulg.montefiore.run.totem.scenario.model.Scenario</code> class
 * is up-to-date against the current JAXB version.
 *
 * <p>Creation date: 04-mars-2005
 *
 * @author Jean Lepropre (lepropre@run.montefiore.ulg.ac.be)
 */
public class DistributedScenarioTest {

    /**
     * This method checks whether the toolbox can load correctly the
     * <code>test-distributed-scenario1.xml</code> file.
     */
    @Test
    public void testLoadAbileneBGPScenario() {
        Scenario scenario = ScenarioFactory.loadScenario("src/resources/junit-test/test-distributed-scenario1.xml");
        
        // 17 events in the scenario
        Assert.assertEquals(17, scenario.getEvent().size());
        
        Iterator it = scenario.getEvent().iterator();
        
        // Test whether the scenario has been unmarshalled correctly
        Assert.assertEquals(it.next().getClass(), LoadDomain.class);
        Assert.assertEquals(it.next().getClass(), StartAlgo.class);
        Assert.assertEquals(it.next().getClass(), CBGPPeerRecv.class);
        Assert.assertEquals(it.next().getClass(), CBGPPeerRecv.class);
        Assert.assertEquals(it.next().getClass(), CBGPPeerRecv.class);
        Assert.assertEquals(it.next().getClass(), CBGPRun.class);
        Assert.assertEquals(it.next().getClass(), CBGPInfo.class);
        Assert.assertEquals(it.next().getClass(), CBGPInfo.class);
        Assert.assertEquals(it.next().getClass(), CBGPInfo.class);
        Assert.assertEquals(it.next().getClass(), CBGPInfo.class);
        Assert.assertEquals(it.next().getClass(), CBGPInfo.class);
        Assert.assertEquals(it.next().getClass(), CBGPInfo.class);
        Assert.assertEquals(it.next().getClass(), CBGPPeerDown.class);
        Assert.assertEquals(it.next().getClass(), CBGPRun.class);
        Assert.assertEquals(it.next().getClass(), CBGPInfo.class);
        Assert.assertEquals(it.next().getClass(), CBGPInfo.class);
        Assert.assertEquals(it.next().getClass(), CBGPInfo.class);
    }

}
