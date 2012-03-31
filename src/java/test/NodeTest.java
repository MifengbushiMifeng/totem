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
import be.ac.ulg.montefiore.run.totem.domain.model.Node;
import be.ac.ulg.montefiore.run.totem.domain.model.Link;
import be.ac.ulg.montefiore.run.totem.domain.persistence.DomainFactory;
import be.ac.ulg.montefiore.run.totem.domain.exception.InvalidDomainException;
import be.ac.ulg.montefiore.run.totem.domain.exception.NodeNotFoundException;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

/*
 * Changes:
 * --------
 *
 */

/**
 * Test the Node's method
 *
 * <p>Creation date: 20-Jan-2005 17:57:44
 *
 * @author  Fabian Skivee (skivee@run.montefiore.ulg.ac.be)
 */
public class NodeTest {

    /**
     * Test inLink and outLink methods
     *
     * @throws NodeNotFoundException
     */
    @Test
    public void testInAndOutLinks() throws NodeNotFoundException, InvalidDomainException, FileNotFoundException {
        String fileName = "src/resources/junit-test/test-domain1.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        Node node = domain.getNode("2");
        List<Link> inLinklist = node.getInLink();
        Assert.assertTrue(inLinklist.get(0).getId().equals("0_0 -> 2_0"));
        Assert.assertTrue(inLinklist.get(1).getId().equals("1_1 -> 2_1"));
        Assert.assertTrue(inLinklist.get(2).getId().equals("4_2 -> 2_2"));
        Assert.assertTrue(inLinklist.get(3).getId().equals("3_3 -> 2_3"));

        List<Link> outLinkList = node.getOutLink();
        Assert.assertTrue(outLinkList.get(0).getId().equals("2_0 -> 0_0"));
        Assert.assertTrue(outLinkList.get(1).getId().equals("2_1 -> 1_1"));
        Assert.assertTrue(outLinkList.get(2).getId().equals("2_2 -> 4_2"));
        Assert.assertTrue(outLinkList.get(3).getId().equals("2_3 -> 3_3"));
    }

}
