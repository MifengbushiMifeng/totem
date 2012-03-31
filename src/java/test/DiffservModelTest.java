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

import org.junit.Test;
import be.ac.ulg.montefiore.run.totem.domain.model.*;
import be.ac.ulg.montefiore.run.totem.domain.model.impl.PathImpl;
import be.ac.ulg.montefiore.run.totem.domain.model.impl.LspImpl;
import be.ac.ulg.montefiore.run.totem.domain.persistence.DomainFactory;
import be.ac.ulg.montefiore.run.totem.domain.exception.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;

import junit.framework.Assert;

/*
* Changes:
* --------
*
*/

/**
* <Replace this by a description of the class>
*
* <p>Creation date: 9/01/2008
*
* @author Gaël Monfort (monfort@run.montefiore.ulg.ac.be)
*/

public class DiffservModelTest {
    @Test
    public void testRDM() throws InvalidDomainException, FileNotFoundException, NodeNotFoundException, InvalidPathException, DiffServConfigurationException, LspAlreadyExistException, LspNotFoundException, LinkCapacityExceededException, LinkNotFoundException {
        String fileName = "src/resources/junit-test/domain-rdm.xml";
        Domain domain = DomainFactory.loadDomain(fileName, false, false);

        Path p = new PathImpl(domain);
        List<Node> nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("1"));
        nodeList.add(domain.getNode("2"));
        nodeList.add(domain.getNode("3"));
        p.createPathFromNode(nodeList);

        Link link = domain.getLink("2-3");

        //establish LSP in CT3
        Lsp lsp = new LspImpl(domain, "lsp1", 24, p, 3, 0, 0);
        domain.addLsp(lsp);

        Assert.assertEquals(link.getReservableBandwidthCT(3), 1, 0.0001);
        Assert.assertEquals(link.getReservableBandwidthCT(2), 21, 0.0001);
        Assert.assertEquals(link.getReservableBandwidthCT(1), 56, 0.0001);
        Assert.assertEquals(link.getReservableBandwidthCT(0), 76, 0.0001);

        //same test
        for (int c : domain.getAllCTId()) {
            Assert.assertEquals(link.getReservableBandwidthCT(c), link.getBCs()[c]-24);
        }

        for  (int c : domain.getAllCTId()) {
            if (c != 3)
                Assert.assertEquals(link.getReservedBandwidthCT(c), 0, 0.0001);
            else Assert.assertEquals(link.getReservedBandwidthCT(c), 24, 0.0001);
        }

        //establish lsp in CT2
        lsp = new LspImpl(domain, "lsp2", 22, p, 2, 0, 0);
        try {
            domain.addLsp(lsp);
            Assert.fail("An exception shoud have been thrown.");
        } catch (LinkCapacityExceededException e) {
        }

        lsp.setReservation(19);
        domain.addLsp(lsp);

        Assert.assertEquals(link.getReservedBandwidthCT(0), 0, 0.0001);
        Assert.assertEquals(link.getReservedBandwidthCT(1), 0, 0.0001);
        Assert.assertEquals(link.getReservedBandwidthCT(2), 19, 0.0001);
        Assert.assertEquals(link.getReservedBandwidthCT(3), 24, 0.0001);

        Assert.assertEquals(link.getReservableBandwidthCT(3), 1, 0.0001);
        Assert.assertEquals(link.getReservableBandwidthCT(2), 2, 0.0001);
        Assert.assertEquals(link.getReservableBandwidthCT(1), 37, 0.0001);
        Assert.assertEquals(link.getReservableBandwidthCT(0), 57, 0.0001);

        //establish lsp in CT0

        lsp = new LspImpl(domain, "lsp3", 56, p, 0, 0, 0);
        domain.addLsp(lsp);

        Assert.assertEquals(link.getReservedBandwidthCT(0), 56, 0.0001);
        Assert.assertEquals(link.getReservedBandwidthCT(1), 0, 0.0001);
        Assert.assertEquals(link.getReservedBandwidthCT(2), 19, 0.0001);
        Assert.assertEquals(link.getReservedBandwidthCT(3), 24, 0.0001);

        Assert.assertEquals(link.getReservableBandwidthCT(3), 1, 0.0001);
        Assert.assertEquals(link.getReservableBandwidthCT(2), 1, 0.0001);
        Assert.assertEquals(link.getReservableBandwidthCT(1), 1, 0.0001);
        Assert.assertEquals(link.getReservableBandwidthCT(0), 1, 0.0001);

        domain.removeLsp(domain.getLsp("lsp2"));
        domain.removeLsp(domain.getLsp("lsp1"));
        domain.removeLsp(domain.getLsp("lsp3"));

        Assert.assertEquals(link.getReservedBandwidthCT(0), 0, 0.0001);
        Assert.assertEquals(link.getReservedBandwidthCT(1), 0, 0.0001);
        Assert.assertEquals(link.getReservedBandwidthCT(2), 0, 0.0001);
        Assert.assertEquals(link.getReservedBandwidthCT(3), 0, 0.0001);

        Assert.assertEquals(link.getReservableBandwidthCT(3), 25, 0.0001);
        Assert.assertEquals(link.getReservableBandwidthCT(2), 45, 0.0001);
        Assert.assertEquals(link.getReservableBandwidthCT(1), 80, 0.0001);
        Assert.assertEquals(link.getReservableBandwidthCT(0), 100, 0.0001);

    }


    //TODO: test with preemptions


}
