/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Aunsha Asaithambi
 */
public class ShowsHandler implements SOAPHandler<SOAPMessageContext> {
    
    @Override
    public boolean handleMessage(SOAPMessageContext messageContext) {
        try {
            SOAPMessage message = messageContext.getMessage();
            Boolean outbound = (Boolean) messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
            Node childElements = message.getSOAPBody().getFirstChild().getFirstChild();
            if(!outbound){
                if (message.getSOAPBody().getFirstChild().getLocalName().equals("addShow") || 
                    message.getSOAPBody().getFirstChild().getLocalName().equals("updateShow")) { 
                    NodeList listOfElements = childElements.getChildNodes();
                    for(int i=0;i<listOfElements.getLength();i++){
                        if(listOfElements.item(i).getTextContent().equalsIgnoreCase("disney")){
                            SOAPBody soapBody = message.getSOAPPart().getEnvelope().getBody();
                            SOAPFault soapFault = soapBody.addFault();
                            soapFault.setFaultString("Sorry! Cannot add a show with the name 'Disney' in any field.");
                            throw new SOAPFaultException(soapFault);
                        }
                    }
                    
                    message.writeTo(System.out);
                }
            }
            
        }
        catch (SOAPException ex) {
            Logger.getLogger(ShowsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ShowsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    @Override
    public Set<QName> getHeaders() {
        return new HashSet<QName>();
    }
    
    @Override
    public boolean handleFault(SOAPMessageContext messageContext) {
        return true;
    }
    
    public void close(MessageContext context) {
    }
    
}

