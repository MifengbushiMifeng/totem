//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.04.01 at 05:02:38 CEST 
//


package nl.tudelft.repository.externalRouting.routing.model.jaxb;


/**
 * Java content class for delayUnits.
 *  <p>The following schema fragment specifies the expected content contained within this java content object.
 * <p>
 * <pre>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *   &lt;enumeration value="ns"/>
 *   &lt;enumeration value="ï¿½s"/>
 *   &lt;enumeration value="ms"/>
 *   &lt;enumeration value="s"/>
 * &lt;/restriction>
 * </pre>
 * 
 */
public class DelayUnits {

    private final static java.util.Map valueMap = new java.util.HashMap();
    public final static java.lang.String _NS = com.sun.xml.bind.DatatypeConverterImpl.installHook("ns");
    public final static nl.tudelft.repository.externalRouting.routing.model.jaxb.DelayUnits NS = new nl.tudelft.repository.externalRouting.routing.model.jaxb.DelayUnits(_NS);
    public final static java.lang.String _Ï_S = com.sun.xml.bind.DatatypeConverterImpl.installHook("\u00ef\u00bf\u00bds");
    public final static nl.tudelft.repository.externalRouting.routing.model.jaxb.DelayUnits Ï_S = new nl.tudelft.repository.externalRouting.routing.model.jaxb.DelayUnits(_Ï_S);
    public final static java.lang.String _MS = com.sun.xml.bind.DatatypeConverterImpl.installHook("ms");
    public final static nl.tudelft.repository.externalRouting.routing.model.jaxb.DelayUnits MS = new nl.tudelft.repository.externalRouting.routing.model.jaxb.DelayUnits(_MS);
    public final static java.lang.String _S = com.sun.xml.bind.DatatypeConverterImpl.installHook("s");
    public final static nl.tudelft.repository.externalRouting.routing.model.jaxb.DelayUnits S = new nl.tudelft.repository.externalRouting.routing.model.jaxb.DelayUnits(_S);
    private final java.lang.String lexicalValue;
    private final java.lang.String value;

    protected DelayUnits(java.lang.String v) {
        value = v;
        lexicalValue = v;
        valueMap.put(v, this);
    }

    public java.lang.String toString() {
        return lexicalValue;
    }

    public java.lang.String getValue() {
        return value;
    }

    public final int hashCode() {
        return super.hashCode();
    }

    public final boolean equals(java.lang.Object o) {
        return super.equals(o);
    }

    public static nl.tudelft.repository.externalRouting.routing.model.jaxb.DelayUnits fromValue(java.lang.String value) {
        nl.tudelft.repository.externalRouting.routing.model.jaxb.DelayUnits t = ((nl.tudelft.repository.externalRouting.routing.model.jaxb.DelayUnits) valueMap.get(value));
        if (t == null) {
            throw new java.lang.IllegalArgumentException();
        } else {
            return t;
        }
    }

    public static nl.tudelft.repository.externalRouting.routing.model.jaxb.DelayUnits fromString(java.lang.String str) {
        return fromValue(str);
    }

}
