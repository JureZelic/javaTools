package si.jure;

import java.io.FileOutputStream;
import java.nio.charset.Charset;
import org.jdom.output.Format;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;



public class Convert {
	private static final Map<String, char[]> translateTable=createMap();
	
	private static Map<String, char[]> createMap() {
		Map<String,char[]> map=new HashMap<String,char[]>(); 

        //                            ch     zh     sh     dz     CH     ZH     SH     DZ
		char[] list1 = new char[]{   232,   158,   138,   240,   200,   142,   138,   208};
		char[] list2 = new char[]{0x010D,0x017E,0x0161,0x0111,0x010C,0x017D,0x0160,0x0110};
		
		map.put("WINDOWS-1250",list1);
		map.put("UTF-8",list2);
		
		return map;
	}

    public static void main(String [] args)
    {
        try {
        	checkAttributes(args);
        } catch (Exception e) {
        	printHelp();
        	return;
        }        
        //System.out.println("Default Charset=" + Charset.defaultCharset().toString());
        doConvert(args[0], args[1]);
    }

   	
   	private static void doConvert(String inFile, String outFile) {
   		// System.setProperty("file.encoding", outTable);
   		
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc;
		try {
			SAXBuilder builder = new SAXBuilder();
			doc =  builder.build(inFile);
        
	        // Output the resulting document.
			XMLOutputter outputter = new XMLOutputter();
			Format f=Format.getPrettyFormat();
			f=f.setEncoding(Charset.defaultCharset().toString());
			outputter.setFormat(f);
			//outputter.output(doc, System.out);
			outputter.output(doc, new FileOutputStream(outFile));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} 
   	
   	}

    private static void printHelp() {
		System.out.println("java [-Dfile.encoding=ENCODING] -classpath jdom-1.1.jar -jar file-convert-<VERSION>.jar INPUT_FILE OUTPUT_FILE");		
	}

	private static void checkAttributes(String[] args) throws Exception{
		if (args.length!=2) {
			throw new Exception();
		}
		return;		
	}
}
