//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.04.01 at 04:43:24 CEST 
//


package be.ac.ulg.montefiore.run.totem.scenario.model.jaxb;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/data/software/totem/totem.git/src/resources/scenario/Scenario-v1_3.xsd line 636)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://jaxb.model.scenario.totem.run.montefiore.ulg.ac.be}eventType">
 *       &lt;attribute name="BGPbaseDirectory" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="BGPdirFileName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="NETFLOWbaseDirectory" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="NETFLOWdirFileName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="clusterFileName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="minutes" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="samplingRate" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="trafficMatrixFileName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface GenerateIntraTMType
    extends be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.EventType
{


    /**
     * Gets the value of the netfloWdirFileName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getNETFLOWdirFileName();

    /**
     * Sets the value of the netfloWdirFileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setNETFLOWdirFileName(java.lang.String value);

    boolean isSetNETFLOWdirFileName();

    void unsetNETFLOWdirFileName();

    /**
     * Gets the value of the clusterFileName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getClusterFileName();

    /**
     * Sets the value of the clusterFileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setClusterFileName(java.lang.String value);

    boolean isSetClusterFileName();

    void unsetClusterFileName();

    /**
     * Gets the value of the samplingRate property.
     * 
     */
    int getSamplingRate();

    /**
     * Sets the value of the samplingRate property.
     * 
     */
    void setSamplingRate(int value);

    boolean isSetSamplingRate();

    void unsetSamplingRate();

    /**
     * Gets the value of the bgPbaseDirectory property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getBGPbaseDirectory();

    /**
     * Sets the value of the bgPbaseDirectory property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setBGPbaseDirectory(java.lang.String value);

    boolean isSetBGPbaseDirectory();

    void unsetBGPbaseDirectory();

    /**
     * Gets the value of the netfloWbaseDirectory property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getNETFLOWbaseDirectory();

    /**
     * Sets the value of the netfloWbaseDirectory property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setNETFLOWbaseDirectory(java.lang.String value);

    boolean isSetNETFLOWbaseDirectory();

    void unsetNETFLOWbaseDirectory();

    /**
     * Gets the value of the minutes property.
     * 
     */
    int getMinutes();

    /**
     * Sets the value of the minutes property.
     * 
     */
    void setMinutes(int value);

    boolean isSetMinutes();

    void unsetMinutes();

    /**
     * Gets the value of the bgPdirFileName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getBGPdirFileName();

    /**
     * Sets the value of the bgPdirFileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setBGPdirFileName(java.lang.String value);

    boolean isSetBGPdirFileName();

    void unsetBGPdirFileName();

    /**
     * Gets the value of the trafficMatrixFileName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getTrafficMatrixFileName();

    /**
     * Sets the value of the trafficMatrixFileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setTrafficMatrixFileName(java.lang.String value);

    boolean isSetTrafficMatrixFileName();

    void unsetTrafficMatrixFileName();

}
