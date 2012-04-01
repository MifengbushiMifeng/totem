/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tudelft.repository.externalRouting.routing.facade;

/**
 *
 * @author hendrik
 */
public class ExternalRoutingException extends Exception {

    public ExternalRoutingException(Throwable thrwbl) {
        super(thrwbl);
    }

    public ExternalRoutingException() {
    }

    public ExternalRoutingException(String string) {
        super(string);
    }

    public ExternalRoutingException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }
    
}
