//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.03.28 at 12:33:07 CET 
//


package be.ac.ulg.montefiore.run.totem.scenario.model.jaxb;


/**
 * Java content class for LSPBackupCreation element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/home/hakan/svn-totem/totem/trunk/run-totem/src/resources/scenario/Scenario-v1_3.xsd line 381)
 * <p>
 * <pre>
 * &lt;element name="LSPBackupCreation">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;extension base="{http://jaxb.model.scenario.totem.run.montefiore.ulg.ac.be}BaseLSPCreationType">
 *         &lt;choice>
 *           &lt;element name="Detour">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;attribute name="methodType" type="{http://jaxb.model.scenario.totem.run.montefiore.ulg.ac.be}methodType" default="GLOBAL" />
 *                   &lt;attribute name="protectedLSP" use="required" type="{http://jaxb.model.scenario.totem.run.montefiore.ulg.ac.be}lspIdType" />
 *                   &lt;attribute name="protectionType" type="{http://jaxb.model.scenario.totem.run.montefiore.ulg.ac.be}protectionType" />
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="Bypass">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="protectedLink" maxOccurs="unbounded">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;attribute name="linkId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                   &lt;/sequence>
 *                   &lt;attribute name="dst" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/choice>
 *       &lt;/extension>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface LSPBackupCreation
    extends javax.xml.bind.Element, be.ac.ulg.montefiore.run.totem.scenario.model.jaxb.LSPBackupCreationType
{


}