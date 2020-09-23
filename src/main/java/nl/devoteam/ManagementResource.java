package nl.devoteam;

import io.quarkus.oidc.UserInfo;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import nl.devoteam.reboot.RebootService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

@Path("/admin")
@Authenticated
public class ManagementResource {

    private final RebootService rebootService;
    private final EdgeResource edgeResource;
    private final SensorResource sensorResource;
    private final SecurityIdentity securityIdentity;
    private static final Logger LOG = Logger.getLogger(ManagementResource.class);

    public ManagementResource(RebootService rebootService, @RestClient EdgeResource edgeResource,
        @RestClient SensorResource sensorResource,
        SecurityIdentity securityIdentity) {
        this.rebootService = rebootService;
        this.edgeResource = edgeResource;
        this.sensorResource = sensorResource;
        this.securityIdentity = securityIdentity;
    }

    @GET
    @Path("/user-info")
    @Produces(MediaType.TEXT_PLAIN)
    public String printUsername() {
        if (isDevoteamEmployee()) {
            UserInfo userInfo = securityIdentity.getAttribute("userinfo");
            final String email = userInfo.getString("email");
            return "Authenticated with email: " + email;
        } else {
            return "Authenticated. Your ID is: " + securityIdentity.getPrincipal().getName();
        }
    }

    @GET
    @Path("/serial")
    @Produces(MediaType.TEXT_PLAIN)
    public String getSerial() {
        final String fileName = "/sys/firmware/devicetree/base/serial-number";
        File serialFile = new File(fileName);
        if (serialFile.exists() && serialFile.canRead()) {
            try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
                return stream.findFirst().get();
            } catch (IOException e) {
                throw new UnsupportedOperationException("Cannot obtain lock on file.", e);
            }
        } else {
            throw new UnsupportedOperationException("Cannot obtain lock on file.");
        }
    }

    @GET
    @Path("/health")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, HealthState> getHealth() {
        Map<String, HealthState> healthStateMap = new HashMap<>();

        HealthState unhealthyState = new HealthState();
        unhealthyState.setStatus("DOWN");

        HealthState unknownState = new HealthState();
        unknownState.setStatus("UNKNOWN");

        boolean devoteamEmployee = isDevoteamEmployee();

        if (devoteamEmployee) {
            try {
                HealthState edgeHealthState = edgeResource.getHealthState();
                healthStateMap.put("edge", edgeHealthState);
            } catch (Exception e) {
                healthStateMap.put("edge", unhealthyState);
            }
            try {
                HealthState sensorHealthState = sensorResource.getHealthState();
                healthStateMap.put("sensor", sensorHealthState);
            } catch (Exception e) {
                healthStateMap.put("sensor", unhealthyState);
            }
        } else {
            healthStateMap.put("edge", unknownState);
            healthStateMap.put("sensor", unknownState);
        }

        return healthStateMap;
    }

    @GET
    @Path("/reboot-services")
    @Produces(MediaType.APPLICATION_JSON)
    public String reboot() {
        if (!isDevoteamEmployee()) {
            return "{ \"nope\": \"lol\"}";
        } else {
            return rebootService.triggerReboot();
        }
    }

    private boolean isDevoteamEmployee() {
        try {
            UserInfo userInfo = securityIdentity.getAttribute("userinfo");
            boolean devoteamEmployee = false;
            if (userInfo != null) {
                final String hd = userInfo.getString("hd");
                devoteamEmployee = hd.equals("devoteam.com");
            }
            return devoteamEmployee;
        } catch (Exception e) {
            return false;
        }
    }
}
