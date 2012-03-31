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
import be.ac.ulg.montefiore.run.totem.domain.model.Lsp;
import be.ac.ulg.montefiore.run.totem.domain.model.impl.DomainImpl;
import be.ac.ulg.montefiore.run.totem.domain.model.impl.NodeImpl;
import be.ac.ulg.montefiore.run.totem.domain.model.impl.LinkImpl;
import be.ac.ulg.montefiore.run.totem.domain.persistence.DomainFactory;
import be.ac.ulg.montefiore.run.totem.domain.exception.*;

import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

/*
 * Changes:
 * --------
 *
 */

/**
 * Factory for loading and saving domain
 *
 * <p>Creation date: 19-Jan-2005 15:57:14
 *
 * @author  Fabian Skivee (skivee@run.montefiore.ulg.ac.be)
 */
public class DomainFactoryTest {
    @Test
    public void testLoad() throws InvalidDomainException, FileNotFoundException {
        String fileName = "src/resources/junit-test/test-domain1.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        Assert.assertEquals(5,domain.getNbNodes());
        Assert.assertEquals(10000,domain.getASID());

        List<Node> nodes = domain.getAllNodes();
        Assert.assertEquals(nodes.get(0).getId(),"0");
        Assert.assertEquals(nodes.get(1).getId(),"1");
        Assert.assertEquals(nodes.get(2).getId(),"2");
        Assert.assertEquals(nodes.get(3).getId(),"3");
        Assert.assertEquals(nodes.get(4).getId(),"4");

        try {
            Lsp lsp1 = domain.getLsp("LSP 0-1");
            Assert.assertEquals(lsp1.getLspPath().getLinkPath().get(0).getId(),"0_0 -> 2_0");
            Assert.assertEquals(lsp1.getLspPath().getLinkPath().get(1).getId(),"2_1 -> 1_1");
            Lsp lsp2 = domain.getLsp("LSP 0-4");
            Assert.assertEquals(lsp2.getLspPath().getLinkPath().get(0).getId(),"0_0 -> 2_0");
            Assert.assertEquals(lsp2.getLspPath().getLinkPath().get(1).getId(),"2_3 -> 3_3");
            Assert.assertEquals(lsp2.getLspPath().getLinkPath().get(2).getId(),"3_4 -> 4_4");
        } catch (LspNotFoundException e) {
            Assert.fail();
            e.printStackTrace();
        }
    }

    @Test
    public void testSave() throws InvalidDomainException, FileNotFoundException {
        String fileName = "src/resources/junit-test/test-domain1.xml";
        String outFile = "saving-domain1.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        DomainFactory.saveDomain(outFile,domain);
        Domain domain1 = DomainFactory.loadDomain(outFile);
        Assert.assertEquals(domain1.getASID(),domain.getASID());
        File file = new File(outFile);
        file.delete();
    }

    @Test
    public void testSave2() throws NodeAlreadyExistException, NodeNotFoundException, LinkAlreadyExistException, InvalidDomainException, FileNotFoundException {
        Domain domain = new DomainImpl(3456);

        domain.addNode(new NodeImpl(domain, "1"));
        domain.addNode(new NodeImpl(domain, "2"));
        domain.addNode(new NodeImpl(domain, "3"));

        domain.addLink(new LinkImpl(domain, "link1", "1", "2", 250));
        domain.addLink(new LinkImpl(domain, "link2", "3", "2", 250));

        String fileName = "saving-domain1.xml";
        DomainFactory.saveDomain(fileName, domain);

        domain = DomainFactory.loadDomain(fileName);
        Assert.assertNotNull(domain.getBandwidthUnit());

        File file = new File(fileName);

        file.delete();

    }
}
