package uz.asadbek.subcourse.user.filter;

import lombok.Data;
import uz.asadbek.base.filter.BaseFilter;

@Data
public class UserFilter extends BaseFilter {
    private Long id;
    private String email;
    private String role; //select option
    private String firstName;
    private String lastName;
    private String position; //select option
    private String phone;
}
