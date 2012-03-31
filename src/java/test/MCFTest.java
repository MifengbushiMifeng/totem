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

import be.ac.ulg.montefiore.run.totem.domain.exception.InvalidDomainException;
import be.ac.ulg.montefiore.run.totem.domain.exception.NodeNotFoundException;
import be.ac.ulg.montefiore.run.totem.domain.exception.LinkNotFoundException;
import be.ac.ulg.montefiore.run.totem.domain.exception.DomainAlreadyExistException;
import be.ac.ulg.montefiore.run.totem.domain.facade.InterDomainManager;
import be.ac.ulg.montefiore.run.totem.domain.model.Link;
import be.ac.ulg.montefiore.run.totem.domain.model.Domain;
import be.ac.ulg.montefiore.run.totem.domain.model.Node;
import be.ac.ulg.montefiore.run.totem.repository.MultiCommodityFlow.MultiCommodityFlow;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.facade.TrafficMatrixManager;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.exception.TrafficMatrixAlreadyExistException;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.exception.InvalidTrafficMatrixException;
import be.ac.ulg.montefiore.run.totem.trafficMatrix.model.TrafficMatrix;

import java.io.IOException;

import org.junit.Test;

/*
 * Changes:
 * --------
 *
 */

/**
 * Test the Multi Commodity Flow
 *
 * <p>Creation date: 01-Feb-2005 10:42:51
 *
 * @author  Fabian Skivee (skivee@run.montefiore.ulg.ac.be)
 */
public class MCFTest {
    @Test
    public void testMCF() throws TrafficMatrixAlreadyExistException, NodeNotFoundException, IOException, LinkNotFoundException, DomainAlreadyExistException, InvalidTrafficMatrixException, InvalidDomainException {
        String topoFile = "src/resources/junit-test/test-domain1b.xml";
        String TMFile = "src/resources/junit-test/TM1-test-domain1.xml";

        Domain domain;
        try {
            domain = InterDomainManager.getInstance().getDomain(10000);
            int asid = domain.getASID();
            InterDomainManager.getInstance().setDefaultDomain(asid);
        } catch (InvalidDomainException e) {
            domain =InterDomainManager.getInstance().loadDomain(topoFile,true,true);
        }
        TrafficMatrix tm = TrafficMatrixManager.getInstance().loadTrafficMatrix(TMFile,0,true);
        MultiCommodityFlow mcf = new MultiCommodityFlow(InterDomainManager.getInstance().getDefaultDomain(), TrafficMatrixManager.getInstance().getDefaultTrafficMatrix());
        mcf.recompute();
        double[] linkLoad = mcf.getData().getLoad();
        //for (int i = 0; i < linkLoad.length; i++) {
        //    System.out.println("Link " + i + " : " + linkLoad[i]);
        //}


        Link l = domain.getLinksBetweenNodes("2", "0").get(0);

        //There is only one linkn to node 0, so all traffic to this node should be routed on that link
        double total = 0;
        for (Node n : domain.getAllNodes()) {
            total += tm.get(n.getId(), "0");
        }
        Assert.assertEquals(linkLoad[domain.getConvertor().getLinkId(l.getId())], total);

        // idem for node 1
        l = domain.getLinksBetweenNodes("2", "1").get(0);
        total = 0;
        for (Node n : domain.getAllNodes()) {
            total += tm.get(n.getId(), "1");
        }
        Assert.assertEquals(linkLoad[domain.getConvertor().getLinkId(l.getId())], total);


        // by observation
        l = domain.getLinksBetweenNodes("4", "2").get(0);
        Assert.assertEquals(linkLoad[domain.getConvertor().getLinkId(l.getId())], 5.5);

        l = domain.getLinksBetweenNodes("2", "1").get(0);
        Assert.assertEquals(linkLoad[domain.getConvertor().getLinkId(l.getId())], 11.0);

    }

}
