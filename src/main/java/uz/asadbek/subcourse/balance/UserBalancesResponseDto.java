package uz.asadbek.subcourse.balance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserBalancesResponseDto {

    private Long countUsers;
    private Long uzsBalances;
    private Long rubBalances;
    private Long usdBalances;
}
