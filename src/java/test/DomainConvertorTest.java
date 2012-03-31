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

import be.ac.ulg.montefiore.run.totem.domain.exception.InvalidDomainException;
import be.ac.ulg.montefiore.run.totem.domain.exception.LinkNotFoundException;
import be.ac.ulg.montefiore.run.totem.domain.exception.NodeNotFoundException;
import be.ac.ulg.montefiore.run.totem.domain.exception.LspNotFoundException;
import be.ac.ulg.montefiore.run.totem.domain.exception.NodeAlreadyExistException;
import be.ac.ulg.montefiore.run.totem.domain.model.Domain;
import be.ac.ulg.montefiore.run.totem.domain.persistence.DomainFactory;
import junit.framework.Assert;

import java.io.FileNotFoundException;

import org.junit.Test;

/*
 * Changes:
 * --------
 *    12-Oct-2005: fix AddConversion() method (GMO)
 */

/**
 * Test the DomainConvertor functionalities
 *
 * <p>Creation date: 21-Jan-2005 13:52:43
 *
 * @author  Fabian Skivee (skivee@run.montefiore.ulg.ac.be)
 */
public class DomainConvertorTest {


    /**
     * Check initial conversion table create from a Domain
     */
    @Test
    public void testInitialConversionTable() throws InvalidDomainException, FileNotFoundException {
        String fileName = "src/resources/junit-test/test-domain1.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        try {
            Assert.assertEquals(domain.getConvertor().getLinkId("0_0 -> 2_0"),0);
            Assert.assertEquals(domain.getConvertor().getLinkId("2_0 -> 0_0"),1);
            Assert.assertEquals(domain.getConvertor().getLinkId("1_1 -> 2_1"),2);
            Assert.assertEquals(domain.getConvertor().getLinkId("2_1 -> 1_1"),3);
            Assert.assertEquals(domain.getConvertor().getLinkId("4_2 -> 2_2"),4);
            Assert.assertEquals(domain.getConvertor().getLinkId("2_2 -> 4_2"),5);
            Assert.assertEquals(domain.getConvertor().getLinkId("3_3 -> 2_3"),6);
            Assert.assertEquals(domain.getConvertor().getLinkId("2_3 -> 3_3"),7);
            Assert.assertEquals(domain.getConvertor().getLinkId("3_4 -> 4_4"),8);
            Assert.assertEquals(domain.getConvertor().getLinkId("4_4 -> 3_4"),9);

            Assert.assertEquals(domain.getConvertor().getLinkId(0),"0_0 -> 2_0");
            Assert.assertEquals(domain.getConvertor().getLinkId(1),"2_0 -> 0_0");
            Assert.assertEquals(domain.getConvertor().getLinkId(2),"1_1 -> 2_1");
            Assert.assertEquals(domain.getConvertor().getLinkId(3),"2_1 -> 1_1");
            Assert.assertEquals(domain.getConvertor().getLinkId(4),"4_2 -> 2_2");
            Assert.assertEquals(domain.getConvertor().getLinkId(5),"2_2 -> 4_2");
            Assert.assertEquals(domain.getConvertor().getLinkId(6),"3_3 -> 2_3");
            Assert.assertEquals(domain.getConvertor().getLinkId(7),"2_3 -> 3_3");
            Assert.assertEquals(domain.getConvertor().getLinkId(8),"3_4 -> 4_4");
            Assert.assertEquals(domain.getConvertor().getLinkId(9),"4_4 -> 3_4");
        } catch (LinkNotFoundException e) {
            Assert.fail();
            e.printStackTrace();
        }

        try {
            Assert.assertEquals(domain.getConvertor().getNodeId("0"),0);
            Assert.assertEquals(domain.getConvertor().getNodeId("1"),1);
            Assert.assertEquals(domain.getConvertor().getNodeId("2"),2);
            Assert.assertEquals(domain.getConvertor().getNodeId("3"),3);
            Assert.assertEquals(domain.getConvertor().getNodeId("4"),4);
            Assert.assertEquals(domain.getConvertor().getNodeId(0),"0");
            Assert.assertEquals(domain.getConvertor().getNodeId(1),"1");
            Assert.assertEquals(domain.getConvertor().getNodeId(2),"2");
            Assert.assertEquals(domain.getConvertor().getNodeId(3),"3");
            Assert.assertEquals(domain.getConvertor().getNodeId(4),"4");
        } catch (NodeNotFoundException e) {
            Assert.fail();
            e.printStackTrace();
        }

        try {
            Assert.assertEquals(domain.getConvertor().getLspId("LSP 0-1"), 0);
            Assert.assertEquals(domain.getConvertor().getLspId("LSP 0-4"), 1);
            Assert.assertEquals(domain.getConvertor().getLspId(0),"LSP 0-1");
            Assert.assertEquals(domain.getConvertor().getLspId(1),"LSP 0-4");
        } catch (LspNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test adding a node in the conversion table
     */
    @Test
    public void testAddConversion() throws InvalidDomainException, FileNotFoundException {
        String fileName = "src/resources/junit-test/test-domain1.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        try {
            domain.getConvertor().addNodeId("NODE5");
            Assert.assertEquals(domain.getConvertor().getNodeId("NODE5"), 5);
            Assert.assertEquals(domain.getConvertor().getNodeId(5),"NODE5");
        } catch (NodeAlreadyExistException e) {
            Assert.fail();
            e.printStackTrace();
        } catch (NodeNotFoundException e) {
            Assert.fail();
            e.printStackTrace();
        }

        try {
            domain.getConvertor().addNodeId("4");
        } catch (NodeAlreadyExistException e) {
            try {
                Assert.assertEquals(domain.getConvertor().getNodeId(4),"4");
            } catch (NodeNotFoundException e1) {
                Assert.fail();
            }
            return;
        }
        /* fail if node "4" doesn't exist */
        Assert.fail();
    }

    /**
     * Test remove a node from the conversion table and then add it again.
     * The adding node has an other id.
     */
    @Test
    public void testRemoveConversion() throws InvalidDomainException, FileNotFoundException {
        String fileName = "src/resources/junit-test/test-domain1.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        try {
            domain.getConvertor().removeNodeId("4");
        } catch (NodeNotFoundException e) {
            Assert.fail();
            e.printStackTrace();
        }
        try {
            domain.getConvertor().getNodeId("4");
        } catch (NodeNotFoundException e) {
            Assert.assertTrue(true);
        }
        try {
            domain.getConvertor().getNodeId(4);
        } catch (NodeNotFoundException e) {
            Assert.assertTrue(true);
        }
        try {
            domain.getConvertor().addNodeId("4");
        } catch (NodeAlreadyExistException e) {
            Assert.fail();
        }
        try {
            Assert.assertEquals(domain.getConvertor().getNodeId("4"),5);
            Assert.assertEquals(domain.getConvertor().getNodeId(5),"4");
        } catch (NodeNotFoundException e) {
            Assert.fail();
        }

    }
}
