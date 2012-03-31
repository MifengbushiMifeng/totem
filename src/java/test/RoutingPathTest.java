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
import be.ac.ulg.montefiore.run.totem.domain.facade.InterDomainManager;
import be.ac.ulg.montefiore.run.totem.domain.exception.*;
import be.ac.ulg.montefiore.run.totem.domain.model.*;
import be.ac.ulg.montefiore.run.totem.domain.model.jaxb.LspBackupType;
import be.ac.ulg.montefiore.run.totem.domain.model.impl.PathImpl;
import be.ac.ulg.montefiore.run.totem.domain.model.impl.LspImpl;
import be.ac.ulg.montefiore.run.totem.domain.model.impl.FastRerouteSwitchingMethod;

import java.util.List;
import java.util.ArrayList;

import org.junit.Test;

/*
* Changes:
* --------
*
*/

/**
* <Replace this by a description of the class>
*
* <p>Creation date: 21/08/2007
*
* @author Gaël Monfort (monfort@run.montefiore.ulg.ac.be)
*/

public class RoutingPathTest {
    @Test
    public void testPrimaryRoutingPath() throws InvalidDomainException, DomainAlreadyExistException, NodeNotFoundException, InvalidPathException, LspAlreadyExistException, DiffServConfigurationException, LspNotFoundException, LinkCapacityExceededException, LinkNotFoundException, StatusTypeException {
        Domain domain = InterDomainManager.getInstance().loadDomain("examples/abilene/abilene.xml", true, false);
        //domain.setSwitchingMethod(new FastRerouteSwitchingMethod(domain));
        domain.setSwitchingMethod(new FastRerouteSwitchingMethod(domain));

        Path primaryPath = new PathImpl(domain);
        List<Node> nodes = new ArrayList<Node>(4);
        nodes.add(domain.getNode("STTL"));
        nodes.add(domain.getNode("DNVR"));
        nodes.add(domain.getNode("KSCY"));
        nodes.add(domain.getNode("IPLS"));
        primaryPath.createPathFromNode(nodes);

        Lsp primaryLsp = new LspImpl(domain, "pri1", 1000, primaryPath);
        domain.addLsp(primaryLsp);

        System.out.println(primaryLsp.getWorkingPath());
        Assert.assertEquals(primaryPath, primaryLsp.getWorkingPath());

        Link dLink = domain.getLink("KSCY-IPLS");
        dLink.setLinkStatus(Link.STATUS_DOWN);

        try {
            primaryLsp.getWorkingPath();
            Assert.fail();
        } catch (InvalidPathException e) {
            System.out.println(e.getMessage());
        }

        dLink.setLinkStatus(Link.STATUS_UP);
        System.out.println(primaryLsp.getWorkingPath());
        Assert.assertEquals(primaryPath, primaryLsp.getWorkingPath());

        InterDomainManager.getInstance().removeAllDomains();
    }


    //TODO: this test will not work because the Fast reroute traffic switching do not use global backups. (only local or global if the first link/node fails)
    /*
    @Test
    public void testE2ERoutingPath() throws NodeNotFoundException, InvalidPathException, LspAlreadyExistException, DiffServConfigurationException, LspNotFoundException, LinkCapacityExceededException, InvalidDomainException, DomainAlreadyExistException, LinkNotFoundException, StatusTypeException {
        Domain domain = InterDomainManager.getInstance().loadDomain("examples/abilene/abilene.xml", true, false);
        domain.setSwitchingMethod(new FastRerouteSwitchingMethod(domain));

        Path primaryPath = new PathImpl(domain);
        List<Node> nodes = new ArrayList<Node>(4);
        nodes.add(domain.getNode("STTL"));
        nodes.add(domain.getNode("DNVR"));
        nodes.add(domain.getNode("KSCY"));
        nodes.add(domain.getNode("IPLS"));
        primaryPath.createPathFromNode(nodes);

        Lsp primaryLsp = new LspImpl(domain, "pri1", 1000, primaryPath);
        domain.addLsp(primaryLsp);

        Path E2EPath = new PathImpl(domain);
        nodes = new ArrayList<Node>(6);
        nodes.add(domain.getNode("STTL"));
        nodes.add(domain.getNode("SNVA"));
        nodes.add(domain.getNode("LOSA"));
        nodes.add(domain.getNode("HSTN"));
        nodes.add(domain.getNode("ATLA"));
        nodes.add(domain.getNode("IPLS"));
        E2EPath.createPathFromNode(nodes);

        Lsp E2ELsp = new LspImpl(domain, "pri1", "e2e1", E2EPath, LspBackupType.DETOUR_E_2_E, primaryPath.getLinkPath());
        domain.addLsp(E2ELsp);

        Link dLink = domain.getLink("DNVR-KSCY");
        dLink.setLinkStatus(Link.STATUS_DOWN);

        // The lsp routing path should be the path of the e2e backup
        System.out.println(primaryLsp.getWorkingPath());
        Assert.assertEquals(primaryLsp.getWorkingPath(), E2ELsp.getLspPath());

        dLink = domain.getLink("ATLA-IPLS");
        dLink.setLinkStatus(Link.STATUS_DOWN);

        try {
            primaryLsp.getWorkingPath();
            Assert.fail();
        } catch (InvalidPathException e) {
            System.out.println(e.getMessage());
        }

        try {
            E2ELsp.getWorkingPath();
            Assert.fail();
        } catch (InvalidPathException e) {
            System.out.println(e.getMessage());
        }

        InterDomainManager.getInstance().removeAllDomains();
    } */

    @Test
    public void testLocalBackupRoutingPath() throws InvalidDomainException, DomainAlreadyExistException, NodeNotFoundException, InvalidPathException, LspAlreadyExistException, DiffServConfigurationException, LspNotFoundException, LinkCapacityExceededException, LinkNotFoundException, StatusTypeException {
        Domain domain = InterDomainManager.getInstance().loadDomain("examples/abilene/abilene.xml", true, false);
        domain.setSwitchingMethod(new FastRerouteSwitchingMethod(domain));

        Path primaryPath = new PathImpl(domain);
        List<Node> nodes = new ArrayList<Node>(4);
        nodes.add(domain.getNode("STTL"));
        nodes.add(domain.getNode("DNVR"));
        nodes.add(domain.getNode("KSCY"));
        nodes.add(domain.getNode("IPLS"));
        primaryPath.createPathFromNode(nodes);

        Lsp primaryLsp = new LspImpl(domain, "pri1", 1000, primaryPath);
        domain.addLsp(primaryLsp);

        Path localPath = new PathImpl(domain);
        nodes = new ArrayList<Node>();
        nodes.add(domain.getNode("DNVR"));
        nodes.add(domain.getNode("SNVA"));
        nodes.add(domain.getNode("LOSA"));
        nodes.add(domain.getNode("HSTN"));
        nodes.add(domain.getNode("KSCY"));
        localPath.createPathFromNode(nodes);

        List<Link> pLink = new ArrayList<Link>();
        pLink.add(domain.getLink("DNVR-KSCY"));
        Lsp backupLsp = new LspImpl(domain, "pri1", "bLsp", localPath, LspBackupType.DETOUR_LOCAL, pLink);

        domain.addLsp(backupLsp);

        Assert.assertEquals(primaryPath, primaryLsp.getWorkingPath());

        pLink.get(0).setLinkStatus(Link.STATUS_DOWN);

        nodes.add(0, domain.getNode("STTL"));
        nodes.add(nodes.size(), domain.getNode("IPLS"));

        Path p = new PathImpl(domain);
        p.createPathFromNode(nodes);
        Assert.assertEquals(primaryLsp.getWorkingPath(), p);

        domain.getLink("LOSA-HSTN").setLinkStatus(Link.STATUS_DOWN);

        try {
            primaryLsp.getWorkingPath();
            Assert.fail();
        } catch (InvalidPathException e) {
        }

        pLink.get(0).setLinkStatus(Link.STATUS_UP);

        Assert.assertEquals(primaryPath, primaryLsp.getWorkingPath());
    }
}
