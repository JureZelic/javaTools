package si.jure.companiesroute;


import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.zipfile.ZipSplitter;

public class CompaniesFileDownload extends RouteBuilder {
    private String source="http://datoteke.durs.gov.si/DURS_zavezanci_PO.zip";

    public CompaniesFileDownload() {

        super();
    }

    /* Just for test */
    public CompaniesFileDownload(String source) {

        super();
        this.source=source;
    }

    @Override
    public void configure() throws Exception {
        // create routes for picking files published at http://www.durs.gov.si/si/storitve/seznami_davcnih_zavezancev/
        from(source)
        //.log(LoggingLevel.INFO, "Route started!")
        //.to("file:durs_data")
        .split(new ZipSplitter()).streaming()
        .convertBodyTo(String.class)
                .choice()
                .when(body().isNotNull())
                .to("file:durs_data")
                 .split(body().tokenize("\n")).streaming()
                .log(LoggingLevel.INFO, "Message body: ${body}")
                    .end()
                .end()
        .end();

    }

}
