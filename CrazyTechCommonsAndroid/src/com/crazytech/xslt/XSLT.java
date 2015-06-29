package com.crazytech.xslt;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;



public class XSLT{
	
	/**
	 * Create the frame.
	 */
	public XSLT() {
		super();
	}
	
	public static String transform(String strXsl, String strXml, Map<String,String> nvp, String charSet) throws TransformerException, IOException {
	    String response = "";

	    InputStream ds = null;
	    ds = new ByteArrayInputStream(strXml.getBytes(charSet));
	    
	    Source xmlSource = new StreamSource(ds);
	    
	    InputStream xs = new ByteArrayInputStream(strXsl.getBytes(charSet));
	    Source xsltSource = new StreamSource(xs);
	    
	    StringWriter writer = new StringWriter();
	    Result result = new StreamResult(writer);
	    TransformerFactory tFactory = TransformerFactory.newInstance();
	    Transformer transformer = tFactory.newTransformer(xsltSource);
	    if (nvp!=null&&nvp.size()>0) {
	    	for (String key : nvp.keySet()) {
	    		transformer.setParameter(key, nvp.get(key));
	    	}
	    }
	    
	    transformer.transform(xmlSource, result);
	    response = writer.toString();
	    
	    ds.close();
	    xs.close();
	    
	    xmlSource = null;
	    xsltSource = null;
	    return response;
	}

}
