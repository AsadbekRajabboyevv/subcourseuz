package uz.asadbek.subcourse.authority.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import uz.asadbek.base.filter.BaseFilter;

@Data
@EqualsAndHashCode(callSuper = true)
public class AuthorityFilter extends BaseFilter {

    private String name;
}
