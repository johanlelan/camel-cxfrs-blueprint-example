package cxf.blueprint.test;

import java.util.Properties;

import javax.ws.rs.core.Response;

import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.Test;

public class CxfRsTest extends CamelBlueprintTestSupport {

    private String startEndpoint = "direct-vm:in";
    private String tenantIn = "direct-vm:common.tenant.in";
    private String authenticationIn = "direct-vm:common.authentication.in";
    private String putIn = "direct-vm:tiers.put";
    private String postIn = "direct-vm:tiers.post";
    private String mockUserAuthenticationIn = "mock:user.authentication.in";
    private String mockUserIdenticationIn = "mock:user.identication.in";

    @Override
    protected java.util.Properties useOverridePropertiesWithPropertiesComponent() {
        Properties extra = new Properties();
        extra.put("cxf.tiers.in", startEndpoint);
        extra.put("common.tenant.in", tenantIn);
        extra.put("common.authentication.in", authenticationIn);
        extra.put("tiers.put.in", putIn);
        extra.put("tiers.post.in", postIn);
        extra.put("user.authentication.in", mockUserAuthenticationIn);
        extra.put("user.identication.in", mockUserIdenticationIn);
        return extra;
    }

    @Override
    protected String getBundleFilter() {
        return "(!(Bundle-SymbolicName=org.apache.cxf.cxf*))";
    }

    @Override
    protected String getBlueprintDescriptor() {
        return "/OSGI-INF/blueprint/blueprint.xml,"
                + "/OSGI-INF/blueprint/activemq.xml,"
                + "/OSGI-INF/blueprint/beans.xml,"
                + "/OSGI-INF/blueprint/camel-common-authentication.xml,"
                + "/OSGI-INF/blueprint/camel-common-tenant.xml,"
                + "/OSGI-INF/blueprint/camel-post.xml,"
                + "/OSGI-INF/blueprint/camel-put.xml,"
                + "/OSGI-INF/blueprint/camel-tracer.xml";
    }

    @Test
    public void testRoute() throws Exception {
        getMockEndpoint(mockUserAuthenticationIn)
                .expectedMinimumMessageCount(1);
        getMockEndpoint(mockUserIdenticationIn).expectedMinimumMessageCount(1);

        assertTrue(template.requestBodyAndHeader(startEndpoint, "{}",
                "Authorization", "Bearer TGT-LKJSHFMDHFMHDFHS") instanceof Response);

        // assert expectations
        assertMockEndpointsSatisfied();
    }

}
