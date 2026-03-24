package uz.asadbek.subcourse.util.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ContentEmbedded {

    @Column(name = "content_uz", columnDefinition = "TEXT", nullable = false)
    private String contentUz;

    @Column(name = "content_en", columnDefinition = "TEXT", nullable = false)
    private String contentEn;

    @Column(name = "content_ru", columnDefinition = "TEXT", nullable = false)
    private String contentRu;

    @Column(name = "content_crl", columnDefinition = "TEXT", nullable = false)
    private String contentCrl;
}
