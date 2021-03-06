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
package be.ac.ulg.montefiore.run.totem.domain.model.impl;

import be.ac.ulg.montefiore.run.totem.domain.model.Link;
import be.ac.ulg.montefiore.run.totem.domain.exception.DiffServConfigurationException;
import be.ac.ulg.montefiore.run.totem.domain.exception.LinkCapacityExceededException;

/*
* Changes:
* --------
*
*/

/**
* Snapshot of the bandwidth sharing. It uses temporary data and do not really update link reserved bandwidth information.
*
* <p>Creation date: 14/12/2007
*
* @author Gaël Monfort (monfort@run.montefiore.ulg.ac.be)
*/

public class BandwidthSharingBandwidthManagementSnapshot extends BandwidthSharingBandwidthManagement {

    public BandwidthSharingBandwidthManagementSnapshot(BandwidthSharingBandwidthManagement bwManagement) throws DiffServConfigurationException {
        super(bwManagement.domain, new SnapshotBandwidthSharingTopologyData(bwManagement.topoData));
    }

    final public void init() {
    }

    /**
     * Do nothing as real reservation should not occur
     * @param link
     * @param incBw
     * @param priority
     */
    final protected void changeReservation(Link link, float incBw, int priority) {
    }


}
