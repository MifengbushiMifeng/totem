//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.03.28 at 12:33:07 CET 
//


package be.ac.ulg.montefiore.run.totem.scenario.model.jaxb;


/**
 * Java content class for chartFormatType.
 *  <p>The following schema fragment specifies the expected content contained within this java content object.
 * <p>
 * <pre>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *   &lt;enumeration value="JPG"/>
 *   &lt;enumeration value="PNG"/>
 *   &lt;enumeration value="EPS"/>
 * &lt;/restriction>
 * </pre>
 * 
 */
public class ChartFormatType {

    private final static java.util.Map valueMap = new java.util.HashMap();
    public final static java.lang.String _JPG = com.sun.xml.bind.DatatypeConverterImpl.installHook("JPG");
    public final static be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.ChartFormatType JPG = new be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.ChartFormatType(_JPG);
    public final static java.lang.String _PNG = com.sun.xml.bind.DatatypeConverterImpl.installHook("PNG");
    public final static be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.ChartFormatType PNG = new be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.ChartFormatType(_PNG);
    public final static java.lang.String _EPS = com.sun.xml.bind.DatatypeConverterImpl.installHook("EPS");
    public final static be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.ChartFormatType EPS = new be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.ChartFormatType(_EPS);
    private final java.lang.String lexicalValue;
    private final java.lang.String value;

    protected ChartFormatType(java.lang.String v) {
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

    public static be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.ChartFormatType fromValue(java.lang.String value) {
        be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.ChartFormatType t = ((be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.ChartFormatType) valueMap.get(value));
        if (t == null) {
            throw new java.lang.IllegalArgumentException();
        } else {
            return t;
        }
    }

    public static be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.ChartFormatType fromString(java.lang.String str) {
        return fromValue(str);
    }

}