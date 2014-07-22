package org.apache.camel.examples.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.core.Response;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.Test;

public class CxfRsTest extends CamelBlueprintTestSupport {

    private String startEndpoint = "direct-vm:in";
    private String mockTenant = "mock:common.tenant.in";
    private String mockAuth = "mock:common.authentication.in";
    private String putIn = "direct-vm:application.put";
    private String postIn = "direct-vm:application.post";

    @Override
    protected java.util.Properties useOverridePropertiesWithPropertiesComponent() {
        Properties extra = new Properties();
        extra.put("cxf.application.in", startEndpoint);
        extra.put("common.tenant.in", mockTenant);
        extra.put("common.authentication.in", mockAuth);
        extra.put("application.put.in", putIn);
        extra.put("application.post.in", postIn);
        return extra;
    }

    // @Override
    // protected String getBundleFilter() {
    // return "(!(Bundle-SymbolicName=org.apache.cxf.cxf*))";
    // }

    @Override
    protected String getBlueprintDescriptor() {
        return "/OSGI-INF/blueprint/blueprint.xml,"
                + "/OSGI-INF/blueprint/beans.xml,"
                + "/OSGI-INF/blueprint/camel-post.xml,"
                + "/OSGI-INF/blueprint/camel-put.xml,"
                + "/OSGI-INF/blueprint/camel-tracer.xml";
    }

    @Test
    public void testRoutePut() throws Exception {
        getMockEndpoint(mockTenant).expectedMessageCount(1);
        getMockEndpoint(mockAuth).expectedMessageCount(1);

        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("Authorization", "Bearer TGT-LKJSHFMDHFMHDFHS");
        headers.put("operationName", "put");
        headers.put("id", "12345");

        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setHeaders(headers);
        exchange.getIn().setBody("{}");

        Object result = template.requestBodyAndHeaders(startEndpoint, "{}",
                headers);

        assertIsInstanceOf(Response.class, result);

        // assert expectations
        assertMockEndpointsSatisfied();
    }

    @Test
    public void testRoutePost() throws Exception {
        getMockEndpoint(mockTenant).expectedMessageCount(1);
        getMockEndpoint(mockAuth).expectedMessageCount(1);

        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("Authorization", "Bearer TGT-LKJSHFMDHFMHDFHS");
        headers.put("operationName", "post");

        Object result = template.requestBodyAndHeaders(startEndpoint, "{}",
                headers);

        Response response = (Response) result;
        assertNotNull(response.getLocation());

        // assert expectations
        assertMockEndpointsSatisfied();
    }

}
