
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MTAReturnResponse {
    
    private String returnCode;
    private String responseCode;
    private String responseMessage;
    private Object data;
    
    public MTAReturnResponse() {
    }

    public MTAReturnResponse(String returnCode, String responseCode, String responseMessage) {
        this.returnCode = returnCode;
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }
    public MTAReturnResponse(String returnCode, String responseCode, String responseMessage, Object data) {
        this.returnCode = returnCode;
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.data = data;
    }
}