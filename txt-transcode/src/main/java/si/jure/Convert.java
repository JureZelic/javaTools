package si.jure;


import java.nio.file.Files;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import java.io.IOException;
import java.lang.SecurityException;
import java.lang.UnsupportedOperationException;
import java.lang.IllegalArgumentException;

public class Convert {


    public static void main(String [] args)
    {
        try {
        	checkAttributes(args);
                doConvert(args[0], args[1], args[2], args[3]);
        } catch (IllegalArgumentException e) {
        	printHelp();
        } catch (IOException e) {
        	System.out.println(e.getMessage());
        } catch (UnsupportedOperationException e) {
        	System.out.println(e.getMessage());
        } catch (SecurityException e) {
        	System.out.println(e.getMessage());
        }                       
    }

   	
    private static void doConvert(String inFile, String outFile, String inCharset, String outCharset ) throws IOException, UnsupportedOperationException, SecurityException  {
        byte[] bytes;
        Path path;
        path = Paths.get(inFile);
        bytes = Files.readAllBytes(path);
        String fileContent=new String(bytes, inCharset);
        bytes = fileContent.getBytes(Charset.forName(outCharset));
        path = Paths.get(outFile);
        Files.write(path,bytes,StandardOpenOption.CREATE);
   	
    }

    private static void printHelp() {
		System.out.println("java -jar txt-convert-<VERSION>.jar INPUT_FILE OUTPUT_FILE INPUT_CHARSET OUTPUT_CHARSET");		
	}

	private static void checkAttributes(String[] args) throws IllegalArgumentException {
		if (args.length!=4) {
			throw new IllegalArgumentException();
		}
		return;		
	}
}
