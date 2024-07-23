import java.util.ArrayList;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.directory.api.ldap.extras.controls.vlv.VirtualListViewRequestImpl;
import org.apache.directory.api.ldap.extras.controls.vlv_impl.VirtualListViewResponseContainer;
import org.apache.directory.api.ldap.model.cursor.SearchCursor;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapNoSuchObjectException;
import org.apache.directory.api.ldap.model.message.AddResponse;
import org.apache.directory.api.ldap.model.message.Control;
import org.apache.directory.api.ldap.model.message.ModifyRequest;
import org.apache.directory.api.ldap.model.message.ModifyResponse;
import org.apache.directory.api.ldap.model.message.SearchRequest;
import org.apache.directory.api.ldap.model.message.SearchRequestImpl;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.message.controls.SortRequestImpl;
import org.apache.directory.api.ldap.model.name.Dn;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;


import jakarta.ws.rs.core.Response;

@Slf4j //log.debug("Received Request")
public class serviceimpl implements service {

    @Override
    public Response addStates(ArrayList<trancategory> tranCategory) {
        return null;
       
    }
}

// private MTAReturnResponse addState(trancategory tc) {
    //     MTAReturnResponse mtaReturnResponse = null;
    //     // if (!validAddRequest(tc, mtaReturnResponse)) {
    //     //     return mtaReturnResponse;
    //     // }
    //     try{
    //         Entry entry = stateSerializer.getAddRequest(tc);
    //         AddResponse addResponse = dataConnection.ldapEntry(databaseBorrow.getLdapConnection(), entry);
    //         return responseFromLdapResults.getLdapResponse(addResponse.getLdapResult());
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return new MTAReturnResponse("1", "4098", e.getLocalizedMessage());
    //     }
    // }

 // // TODO Auto-generated method stub
        // log.debug("Received Request for State Creation. Request was {}", tranCategory);
        // List<MTAReturnResponse> mtaReturnResponses = new ArrayList<>();
        // for (trancategory tc : tranCategory) {
        //     log.info("Adding statesMaster entry with : {}", tc);
        //     MTAReturnResponse mtaReturnResponse = addState(tc);
        //     // log.info(("Response for adding statesMaster is : {}"), mtaReturnResponse);
        //     // mtaReturnResponses.add(mtaReturnResponse);
        // }
        // // log.info("Returning Response : {} ", mtaReturnResponses);
        // // return Response.ok(mtaReturnResponses).build();
        // throw new UnsupportedOperationException("Unimplemented method 'addStates'");