package nl.devoteam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("")
@RegisterRestClient(configKey = "sensor-resource")
public interface SensorResource {

    @GET
    @Path("/health")
    @Produces("application/json")
    HealthState getHealthState();
}
