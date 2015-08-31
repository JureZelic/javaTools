java -Dfile.encoding=ISO8859-2 -classpath jdom-1.1.jar -jar xml-transcode-1.0.jar InputWIN1250.xml OutputISO8859-2.xml

java -Dfile.encoding=WINDOWS-1250 -classpath jdom-1.1.jar -jar xml-transcode-1.0.jar InputWIN1250.xml OutputWIN1250.xml

java -Dfile.encoding=UTF-8 -classpath jdom-1.1.jar -jar xml-transcode-1.0.jar InputWIN1250.xml OutputUTF8.xml

java -Dfile.encoding=UTF-16 -classpath jdom-1.1.jar -jar xml-transcode-1.0.jar InputWIN1250.xml OutputUTF16.xml