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

import be.ac.ulg.montefiore.run.totem.repository.CSPF.CSPFElem;
import be.ac.ulg.montefiore.run.totem.repository.CSPF.CSPFPriorityQueue;
import be.ac.ulg.montefiore.run.totem.domain.exception.InvalidDomainException;
import be.ac.ulg.montefiore.run.totem.domain.model.Node;
import be.ac.ulg.montefiore.run.totem.domain.model.Domain;
import be.ac.ulg.montefiore.run.totem.domain.model.impl.NodeImpl;
import be.ac.ulg.montefiore.run.totem.domain.persistence.DomainFactory;

import junit.framework.Assert;

import java.io.FileNotFoundException;
import java.util.Random;

import org.junit.Test;

/*
 * Changes:
 * --------
 *  - 14-Oct-2005: Change update test, add testRandomUpdate(), some other modifications and reorganizations. (GMO)
 *  - 27-Feb-2006: update code to new CSPFElem structure (JLE).
 *  - 27-Feb-2006: comments correction (JLE).
 */

/**
 * Test a CSPF priority queue
 *
 * <p>Creation date: 05-Jan.-2004
 *
 * @author  Fabian Skivee (skivee@run.montefiore.ulg.ac.be)
 */
public class CSPFPriorityQueueTest {

    private CSPFPriorityQueue heap;

    private static long getUsedMemory() {
        Runtime r = Runtime.getRuntime();
        return r.totalMemory() - r.freeMemory();
    }

    @Test
    public void testSimpleAdd() throws InvalidDomainException, FileNotFoundException {
        String fileName = "src/resources/junit-test/test-domain1.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        heap = new CSPFPriorityQueue(10);
        Node node1 = new NodeImpl(domain,"abc");
        Node node2 = new NodeImpl(domain,"fdgddg");
        Node node3 = new NodeImpl(domain,"def");
        Node node4 = new NodeImpl(domain,"dzef");
        CSPFElem elem1 = new CSPFElem(node1,10,node2,6, null);
        CSPFElem elem2 = new CSPFElem(node3,34,node4,67, null);
        heap.add(elem1);
        heap.add(elem2);

        Assert.assertEquals(heap.removeNext().getId(),"abc");
        Assert.assertEquals(heap.removeNext().getId(),"def");
    }

    @Test
    public void testRandomAdd() throws InvalidDomainException, FileNotFoundException {
        String fileName = "src/resources/junit-test/test-domain1.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        heap = new CSPFPriorityQueue(10);
        int nbElem = 1000;
        Random generCost = new Random();
        long cost[] = new long[nbElem * 32];
        for(int i=0;i < (nbElem * 32);i++) {
            cost[i] = generCost.nextLong();
        }

        for(int j=0;j < 5;j++) {
            nbElem = nbElem * 2;

            heap = new CSPFPriorityQueue(nbElem);
            System.gc();
            long time = System.currentTimeMillis();
            long usedMem = getUsedMemory();
            for(int i=0;i < nbElem;i++) {
                Node node = new NodeImpl(domain,new String(""+i));
                heap.add(new CSPFElem(node,cost[i],node,0, null));
            }
            usedMem = getUsedMemory() - usedMem;
            time = System.currentTimeMillis() - time;
            //System.out.println("Add " + nbElem + " item takes " + time + " millisecond");
            //System.out.println("Heap of " + heap.size() + " items build with " + heap.getKeyComps() + " key comparaisons");
            Assert.assertEquals(nbElem, heap.size());

            /* Check heap condition */
            float old_value = Float.NEGATIVE_INFINITY;
            for (int i=0; i < nbElem; i++) {
                 float key = heap.removeNext().getKey();
                 Assert.assertTrue(key >= old_value);
                 old_value = key;
            }
            Assert.assertTrue(heap.size() == 0);
        }
    }

    @Test
    public void testRandomUpdate() throws InvalidDomainException, FileNotFoundException {
        String fileName = "src/resources/junit-test/test-domain1.xml";
        Domain domain = DomainFactory.loadDomain(fileName);
        int nbElem = 2000;
        Random generCost = new Random();
        heap = new CSPFPriorityQueue(nbElem);

        System.gc();
        long time = System.currentTimeMillis();
        long usedMem = getUsedMemory();
        for(int i=0;i < nbElem;i++) {
            Node node = new NodeImpl(domain,new String(""+i));
            heap.add(new CSPFElem(node,generCost.nextFloat(),node,0, null));
        }
        usedMem = getUsedMemory() - usedMem;
        time = System.currentTimeMillis() - time;
        //System.out.println("Add " + nbElem + " item takes " + time + " millisecond");
        //System.out.println("Heap of " + heap.size() + " items build with " + heap.getKeyComps() + " key comparaisons");
        Assert.assertEquals(nbElem, heap.size());

        Random generid = new Random();
        for(int i=0;i < nbElem;i++) {
            Node node = new NodeImpl(domain,new String(""+(generid.nextInt(nbElem))));
            heap.update(new CSPFElem(node,generCost.nextFloat(),node,0, null));
        }

        /* Check heap condition */
        float old_value = Float.NEGATIVE_INFINITY;
        for (int i=0; i < nbElem; i++) {
            float key = heap.removeNext().getKey();
            Assert.assertTrue(key >= old_value);
            old_value = key;
        }
        Assert.assertTrue(heap.size() == 0);
    }

    @Test
    public void testSimpleUpdate() {
        heap = new CSPFPriorityQueue(10);
        heap.add(new CSPFElem(new NodeImpl(null,"de2.de.re0.00"),100,null,0, null));
        Assert.assertEquals(1,heap.size());
        heap.add(new CSPFElem(new NodeImpl(null,"nl1.nl.re0.00"),99,null,0, null));
        Assert.assertEquals(2,heap.size());
        heap.add(new CSPFElem(new NodeImpl(null,"at1.at.re0.00"),2,null,0, null));
        Assert.assertEquals(3,heap.size());
        heap.update(new CSPFElem(new NodeImpl(null,"de2.de.re0.00"),97,null,0, null));
        Assert.assertEquals(3,heap.size());
        heap.update(new CSPFElem(new NodeImpl(null,"at1.at.re0.00"),98,null,0, null));
        Assert.assertEquals(3,heap.size());

        CSPFElem pathNode = (CSPFElem) heap.removeNext();

        Assert.assertEquals(pathNode.getId(),"de2.de.re0.00");
        Assert.assertEquals(2,heap.size());
        pathNode = (CSPFElem) heap.removeNext();
        Assert.assertEquals(pathNode.getId(),"at1.at.re0.00");
        Assert.assertEquals(1,heap.size());
        pathNode = (CSPFElem) heap.removeNext();
        Assert.assertEquals(pathNode.getId(),"nl1.nl.re0.00");
        Assert.assertEquals(0,heap.size());

    }


    @Test
    public void testSimpleRemove() {
        heap = new CSPFPriorityQueue(10);
        heap.add(new CSPFElem(new NodeImpl(null,"de2.de.re0.00"),100,null,0, null));
        heap.add(new CSPFElem(new NodeImpl(null,"nl1.nl.re0.00"),5,null,0, null));
        heap.add(new CSPFElem(new NodeImpl(null,"it1.it.re0.00"),10,null,0, null));
        heap.update(new CSPFElem(new NodeImpl(null,"de2.de.re0.00"),2,null,0, null));
        heap.add(new CSPFElem(new NodeImpl(null,"at1.at.re0.00"),15,null,0, null));
        heap.add(new CSPFElem(new NodeImpl(null,"cz1.cz.re0.00"),12,null,0, null));



        CSPFElem pathNode = (CSPFElem) heap.removeNext();
        Assert.assertEquals(pathNode.getId(),"de2.de.re0.00");
        Assert.assertEquals(4,heap.size());
        pathNode = (CSPFElem) heap.removeNext();
        Assert.assertEquals(pathNode.getId(),"nl1.nl.re0.00");
        Assert.assertEquals(3,heap.size());
        pathNode = (CSPFElem) heap.removeNext();
        Assert.assertEquals(pathNode.getId(),"it1.it.re0.00");
        Assert.assertEquals(2,heap.size());
        pathNode = (CSPFElem) heap.removeNext();
        Assert.assertEquals(pathNode.getId(),"cz1.cz.re0.00");
        Assert.assertEquals(1,heap.size());
        pathNode = (CSPFElem) heap.removeNext();
        Assert.assertEquals(pathNode.getId(),"at1.at.re0.00");
        Assert.assertEquals(0,heap.size());
    }

}
