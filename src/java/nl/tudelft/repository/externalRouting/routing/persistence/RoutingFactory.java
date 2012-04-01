/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tudelft.repository.externalRouting.routing.persistence;

import be.ac.ulg.montefiore.run.totem.core.PreferenceManager;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import nl.tudelft.repository.externalRouting.routing.model.jaxb.ExternalRouting;
import org.apache.log4j.Logger;

/**
 *
 * @author hendrik
 */
public class RoutingFactory {

    private static Logger logger = Logger.getLogger(RoutingFactory.class);
    
    public static ExternalRouting loadExternalRouting(String fileName) {
        return loadExternalRouting(new File(fileName));
    }
    
    public static ExternalRouting loadExternalRouting(File file) {
        ExternalRouting r = null;
        JAXBContext jc;
        try {
            jc = JAXBContext.newInstance("nl.tudelft.repository.externalRouting.routing.model.jaxb");
            Unmarshaller u = jc.createUnmarshaller();
            r = (ExternalRouting) u.unmarshal(file);
        } catch (JAXBException ex) {
            logger.warn(ex);
        }
        
        return r;
    }

    public static void saveExternalRouting(String fileName, ExternalRouting r) {
        try {
            JAXBContext jc = JAXBContext.newInstance("nl.tudelft.repository.externalRouting.routing.model.jaxb");
            Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
            m.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, PreferenceManager.getInstance().getPrefs().get("ROUTING-SCHEMA-LOCATION", "") );
            m.marshal(r, new FileWriter(fileName));
        } catch (IOException ex) {
            logger.warn(ex);
        } catch (JAXBException ex) {
            logger.warn(ex);
        }
    }
    
    private RoutingFactory() {
    }
    
}
