import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class trancategory {

    private String lastActivationDate;
    private String creator;
    private String createdDate;
    private String lastActivationUser;
    private String transactionGroupId;
    private String transactionGroupsUid;
    private String objectClass;
    private String description;
    private String uniqueMember;
    private String isActive;
    
}