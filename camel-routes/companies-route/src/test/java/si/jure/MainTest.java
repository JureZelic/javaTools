package si.jure;

import org.apache.camel.CamelContext;
import org.apache.camel.component.http.HttpComponent;
import org.apache.camel.impl.DefaultCamelContext;
import si.jure.companiesroute.CompaniesFileDownload;

public class MainTest{
    public static void main(String args[])throws Exception{
        CamelContext context = new DefaultCamelContext();
        // add http Component
        HttpComponent httpComponent = new HttpComponent();
        context.addComponent("http", httpComponent);

        context.addRoutes(new CompaniesFileDownload("file:test-samples/DURS_zavezanci_PO.zip?noop=true"));

        context.start();
        Thread.sleep(10000);
        context.stop();
    }
}
