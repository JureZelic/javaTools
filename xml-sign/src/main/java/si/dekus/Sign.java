package si.dekus;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.crypto.MarshalException;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sign {

    public static void main(String [] args)
    {
        try {
        	checkAttributes(args);
        } catch (Exception e) {
        	printHelp();
        }
    	
    	try {
			signReq(args[0],args[1],args[2]);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} 
    }

    private static void printHelp() {
		System.out.println("java -Djavax.net.ssl.keyStore=KEYSTORE -Djavax.net.ssl.keyStorePassword=KEYSTORE_PASSWORD -jar xmlsign-<VERSION>.jar INPUT_FILE OUTPUT_FILE KEYSTORE_ALIAS");
		
	}

	private static void checkAttributes(String[] args) throws Exception{
		if (args.length!=3) {
			throw new Exception();
		}
		
	}

	private static void signReq(String inDocument,String outDocument,String keystoreAlias) throws Exception {
        XMLSignatureFactory fac=null;
        X509Certificate cert=null;
        KeyStore.PrivateKeyEntry keyEntry=null;
        KeyInfo ki=null;
        SignedInfo si=null;
        Document doc=null;
        
        String keyStore=System.getProperty("javax.net.ssl.keyStore");
        String keyStorePassword=System.getProperty("javax.net.ssl.keyStorePassword");
        

        try {
            // Create a DOM XMLSignatureFactory that will be used to
            // generate the enveloped signature.
            fac = XMLSignatureFactory.getInstance("DOM");

            // Create a Reference to the enveloped document (in this case,
            // you are signing the whole document, so a URI of "" signifies
            // that, and also specify the SHA1 digest algorithm and
            // the ENVELOPED Transform.
            Reference ref = fac.newReference
                    ("", fac.newDigestMethod(DigestMethod.SHA1, null),
                            Collections.singletonList
                                    (fac.newTransform
                                            (Transform.ENVELOPED, (TransformParameterSpec) null)),
                            null, null);

            // Create the SignedInfo.
            si = fac.newSignedInfo
                    (fac.newCanonicalizationMethod
                            (CanonicalizationMethod.INCLUSIVE,
                                    (C14NMethodParameterSpec) null),
                            fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
                            Collections.singletonList(ref));

            // Load the KeyStore and get the signing key and certificate.
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(keyStore), keyStorePassword.toCharArray());
            keyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry (keystoreAlias, new KeyStore.PasswordProtection(keyStorePassword.toCharArray()));
            if (keyEntry==null) {
            	throw new Exception ("Kea pair "+keystoreAlias+" not found in "+keyStore); 
            }
            cert = (X509Certificate) keyEntry.getCertificate();


            // Create the KeyInfo containing the X509Data.
            KeyInfoFactory kif = fac.getKeyInfoFactory();
            List x509Content = new ArrayList();
            x509Content.add(cert.getSubjectX500Principal().getName());
            x509Content.add(cert);
            X509Data xd = kif.newX509Data(x509Content);
            ki = kif.newKeyInfo(Collections.singletonList(xd));

            // Instantiate the document to be signed.
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            doc = dbf.newDocumentBuilder().parse
                    (new FileInputStream(inDocument));

            // Create a DOMSignContext and specify the RSA PrivateKey and
            // location of the resulting XMLSignature's parent element.
            DOMSignContext dsc = new DOMSignContext
                    (keyEntry.getPrivateKey(), doc.getDocumentElement());

            // Create the XMLSignature, but don't sign it yet.
            XMLSignature signature = fac.newXMLSignature(si, ki);

            // Marshal, generate, and sign the enveloped signature.
            signature.sign(dsc);	

            // Output the resulting document.
            OutputStream os = new FileOutputStream(outDocument);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            trans.transform(new DOMSource(doc), new StreamResult(os));
        } catch (TransformerException e) {
        	throw new RuntimeException(e);
        } catch (XMLSignatureException e) {
        	throw new RuntimeException(e);
        } catch (MarshalException e) {
        	throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
        	throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
        	throw new RuntimeException(e);
        }         
    }

}
