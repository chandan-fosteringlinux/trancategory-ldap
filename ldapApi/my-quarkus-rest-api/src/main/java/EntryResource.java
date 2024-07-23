import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/entry")
public class EntryResource {

    @Inject
    Connection connection;

    @GET
    @Path("/add")
    @Produces(MediaType.TEXT_PLAIN)
    public Response addEntry() {
        boolean added = connection.addTestEntry();
        if (added) {
            return Response.ok("Entry added successfully").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Entry not added").build();
        }
    }

    

    
    
}