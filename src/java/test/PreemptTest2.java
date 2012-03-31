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

import java.util.List;
import java.util.ArrayList;
import java.io.FileNotFoundException;

import junit.framework.Assert;

/*
* Changes:
* --------
*
*/

/**
* Test preemption. Some special cases.
*
* <p>Creation date: 10/01/2008
*
* @author Gaël Monfort (monfort@run.montefiore.ulg.ac.be)
*/
public class PreemptTest2 {

    /**
     * A lsp with a lower preemption level will not be preempted as it frees no bandwidth in the considered classtype.
     */
    @Test
    public void testPreempt() throws NodeNotFoundException, InvalidDomainException, FileNotFoundException, InvalidPathException, DiffServConfigurationException, LspAlreadyExistException, LspNotFoundException, LinkCapacityExceededException {
        String fileName = "src/resources/junit-test/test-domain-preempt.xml";
        Domain domain = DomainFactory.loadDomain(fileName);

        Path p1 = new PathImpl(domain);

        List<Node> nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("0"));
        nodeList.add(domain.getNode("2"));
        nodeList.add(domain.getNode("2bis"));
        nodeList.add(domain.getNode("3"));

        p1.createPathFromNode(nodeList);

        //First establish an lsp in the ct1 with plevel 3

        Lsp lsp = new LspImpl(domain, "lsp1", 50000, p1, 1, 3, 3);
        domain.addLsp(lsp);

        //Second, establish an lsp in the ct0 with a plevel 1

        lsp = new LspImpl(domain, "lsp2", 49000, p1, 0, 1, 1);
        domain.addLsp(lsp);

        //Third, create an lsp in the ct0 with pLevel 0
        //it would preempt lsp 2 but not lsp1
        lsp = new LspImpl(domain, "lsp3", 150000, p1, 0, 0, 0);
        domain.addLsp(lsp, true);

        domain.getLsp("lsp1");
        domain.getLsp("lsp3");

        try {
            domain.getLsp("lsp2");
            Assert.fail("lsp2 should have been preempted");
        } catch (LspNotFoundException e) {
        }
    }

    /**
     *
     * mrbw will be changed so that preemption in another classtype will b e useful.
     *
     * @throws NodeNotFoundException
     * @throws InvalidDomainException
     * @throws FileNotFoundException
     * @throws InvalidPathException
     * @throws DiffServConfigurationException
     * @throws LspAlreadyExistException
     * @throws LspNotFoundException
     * @throws LinkCapacityExceededException
     */
    @Test
    public void testPreempt2() throws NodeNotFoundException, InvalidDomainException, FileNotFoundException, InvalidPathException, DiffServConfigurationException, LspAlreadyExistException, LspNotFoundException, LinkCapacityExceededException {
        String fileName = "src/resources/junit-test/test-domain-preempt.xml";
        Domain domain = DomainFactory.loadDomain(fileName);

        Path p1 = new PathImpl(domain);

        List<Node> nodeList = new ArrayList<Node>();
        nodeList.add(domain.getNode("0"));
        nodeList.add(domain.getNode("2"));
        nodeList.add(domain.getNode("2bis"));
        nodeList.add(domain.getNode("3"));

        p1.createPathFromNode(nodeList);

        for (Link l : domain.getAllLinks()) {
            l.setBandwidth(150000);
        }

        //First establish an lsp in the ct1 with plevel 3

        Lsp lsp = new LspImpl(domain, "lsp1", 49000, p1, 1, 3, 3);
        domain.addLsp(lsp);

        //Second, establish an lsp in the ct0 with a plevel 1
        //it should preempt lsp1, even if the classtype is different
        lsp = new LspImpl(domain, "lsp2", 125000, p1, 0, 1, 1);
        domain.addLsp(lsp, true);

        domain.getLsp("lsp2");

        try {
            domain.getLsp("lsp1");
            Assert.fail("lsp2 should have been preempted");
        } catch (LspNotFoundException e) {
        }


    }
}
