package uz.asadbek.subcourse.util.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class NameEmbedded {
    @Column(name = "name_uz", nullable = false, length = 500)
    private String nameUz;

    @Column(name = "name_ru", nullable = false, length = 500)
    private String nameRu;

    @Column(name = "name_en", nullable = false, length = 500)
    private String nameEn;

    @Column(name = "name_crl", nullable = false, length = 500)
    private String nameCrl;
}
