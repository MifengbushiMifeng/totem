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
import be.ac.ulg.montefiore.run.totem.domain.persistence.DomainFactory;
import be.ac.ulg.montefiore.run.totem.domain.model.Domain;
import be.ac.ulg.montefiore.run.totem.domain.model.Lsp;
import be.ac.ulg.montefiore.run.totem.domain.model.Link;
import be.ac.ulg.montefiore.run.totem.domain.exception.*;
import be.ac.ulg.montefiore.run.totem.repository.CSPF.CSPF;
import be.ac.ulg.montefiore.run.totem.repository.model.LSPPrimaryRoutingParameter;
import be.ac.ulg.montefiore.run.totem.repository.model.AddLspAction;
import be.ac.ulg.montefiore.run.totem.repository.model.LSPDetourRoutingParameter;
import be.ac.ulg.montefiore.run.totem.repository.model.exception.NoRouteToHostException;
import be.ac.ulg.montefiore.run.totem.repository.model.exception.RoutingException;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

/*
* Changes:
* --------
*
*/

/**
* This test the bandwidth sharing functionality.
* It test backup-backup bandwidth sharing as well primary-backup on simple topologies.
*
* The topologies are taken from the examples (fig 1 & fig 2) shown in "A scalable and decentralized fast-rerouting
* scheme with efficient bandwidth sharing" (Simon Balon, Laurent Mélon, Guy Leduc) 
*
* <p>Creation date: 16/11/2006
*
* @author Gaël Monfort (monfort@run.montefiore.ulg.ac.be)
*/

public class BWSharingTest {
    @Test
    public void testBackupBackup() throws InvalidDomainException, FileNotFoundException, NoRouteToHostException, RoutingException, LspAlreadyExistException, DiffServConfigurationException, LspNotFoundException, LinkCapacityExceededException, LinkNotFoundException {
        String fileName = "src/resources/junit-test/test-domain-bwsharing1.xml";
        Domain domain = DomainFactory.loadDomain(fileName, false, true);

        CSPF cspf = new CSPF();

        // create the primaries

        LSPPrimaryRoutingParameter param = new LSPPrimaryRoutingParameter("N1", "N4", "LSP1");
        param.setBandwidth(100);
        Lsp lsp = ((AddLspAction) cspf.routeLSP(domain,param).get(0)).getLsp();

        domain.addLsp(lsp);

        List<Link> path = lsp.getLspPath().getLinkPath();
        Assert.assertEquals(path.get(0).getId(), "N1-N2");
        Assert.assertEquals(path.get(1).getId(), "N2-N4");

        param = new LSPPrimaryRoutingParameter("N1", "N6", "LSP2");

        param.setBandwidth(100);
        lsp = ((AddLspAction) cspf.routeLSP(domain,param).get(0)).getLsp();

        domain.addLsp(lsp);

        path = lsp.getLspPath().getLinkPath();
        Assert.assertEquals(path.get(0).getId(), "N1-N3");
        Assert.assertEquals(path.get(1).getId(), "N3-N6");

        // create the backups

        LSPDetourRoutingParameter dParam = new LSPDetourRoutingParameter("Backup1");
        dParam.setProtectedLSP("LSP1");
        dParam.setMethodType(LSPDetourRoutingParameter.GLOBAL);
        dParam.setProtectionType(LSPDetourRoutingParameter.LINK_DISJOINT);

        lsp = ((AddLspAction) cspf.routeDetour(domain, dParam).get(0)).getLsp();

        path = lsp.getLspPath().getLinkPath();
        Assert.assertEquals(path.get(0).getId(), "N1-N5");
        Assert.assertEquals(path.get(1).getId(), "N5-N4");

        domain.addLsp(lsp);

        Assert.assertEquals(domain.getLink("N1-N5").getReservedBandwidth(), 100, 0.001);
        Assert.assertEquals(domain.getLink("N1-N5").getReservableBandwidth(), 0, 0.001);

        dParam = new LSPDetourRoutingParameter("Backup2");
        dParam.setProtectedLSP("LSP2");
        dParam.setMethodType(LSPDetourRoutingParameter.GLOBAL);
        dParam.setProtectionType(LSPDetourRoutingParameter.LINK_DISJOINT);

        lsp = ((AddLspAction) cspf.routeDetour(domain, dParam).get(0)).getLsp();

        path = lsp.getLspPath().getLinkPath();
        Assert.assertEquals(path.get(0).getId(), "N1-N5");
        Assert.assertEquals(path.get(1).getId(), "N5-N6");

        domain.addLsp(lsp);

        // bandwidth is shared on N1-N5
        Assert.assertEquals(domain.getLink("N1-N5").getReservedBandwidth(), 100, 0.001);
        Assert.assertEquals(domain.getLink("N1-N5").getReservableBandwidth(), 0, 0.001);

    }
    /*
    @Test
    public void testPrimaryBackup() throws InvalidDomainException, FileNotFoundException, NoRouteToHostException, RoutingException, LspAlreadyExistException, DiffServConfigurationException, LspNotFoundException, LinkCapacityExceededException, LinkNotFoundException, NodeNotFoundException, InvalidPathException {
        String fileName = "src/resources/junit-test/test-domain-bwsharing2.xml";
        Domain domain = DomainFactory.loadDomain(fileName, false, true);

        CSPF cspf = new CSPF();

        // create the primaries

        Path path = new PathImpl(domain);
        List<Link> lPath = new ArrayList<Link>(3);
        lPath.add(domain.getLink("N1-N2"));
        lPath.add(domain.getLink("N2-N4"));
        lPath.add(domain.getLink("N4-N5"));

        path.createPathFromLink(lPath);

        Lsp lsp = new LspImpl(domain, "LSP1", 100, path);

        domain.addLsp(lsp);


        path = new PathImpl(domain);
        lPath = new ArrayList<Link>(2);
        lPath.add(domain.getLink("N3-N2"));
        lPath.add(domain.getLink("N2-N5"));
        path.createPathFromLink(lPath);

        lsp = new LspImpl(domain, "LSP2", 100, path);

        domain.addLsp(lsp);

        // create the backups

        LSPDetourRoutingParameter dParam = new LSPDetourRoutingParameter("Backup2");
        dParam.setProtectedLSP("LSP2");
        dParam.setMethodType(LSPDetourRoutingParameter.GLOBAL);
        dParam.setProtectionType(LSPDetourRoutingParameter.LINK_DISJOINT);

        lsp = ((AddLspAction) cspf.routeDetour(domain, dParam).get(0)).getLsp();

        domain.addLsp(lsp);

        lPath = lsp.getLspPath().getLinkPath();
        Assert.assertEquals(lPath.get(0).getId(), "N3-N4");
        Assert.assertEquals(lPath.get(1).getId(), "N4-N5");

        //sharing must occur on link N4-N5
        Assert.assertEquals(domain.getLink("N4-N5").getReservedBandwidth(), 100, 0.001);
        Assert.assertEquals(domain.getLink("N4-N5").getReservableBandwidth(), 0, 0.001);

    }
    */

}
