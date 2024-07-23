import java.util.ArrayList;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/entry2")
public interface service {
    
    @Path("/add")
    @POST
    public Response addStates(ArrayList<trancategory> tranCategory);
    
}