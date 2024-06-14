package degreesapp.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/**
 * @author Mohammed Abdalgader
 */

@Entity
@ApiModel (
        description = "A department, such as the Department of Computer Science. " +
                "A department is primarily identified by its department code, such as \"COM S\"."
)
public class Department {

    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    @ApiModelProperty ( "Primary key in the database" )
    private Long departmentId;
    @ApiModelProperty ( "The long-form name" )
    private String departmentName;
    @ApiModelProperty ( "The department's postal address" )
    private String departmentAddress;
    @ApiModelProperty ( "The department's abbreviation, like \"COM S\"" )
    private String departmentCode;

    public Department() {
    }

    public Department(Long departmentId , String departmentName , String departmentAddress , String departmentCode) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.departmentAddress = departmentAddress;
        this.departmentCode = departmentCode;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setDepartmentAddress(String departmentAddress) {
        this.departmentAddress = departmentAddress;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getDepartmentAddress() {
        return departmentAddress;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }
}
