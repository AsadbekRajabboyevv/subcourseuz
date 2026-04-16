package uz.asadbek.subcourse.util;

import uz.asadbek.subcourse.util.embedded.DescriptionEmbedded;
import uz.asadbek.subcourse.util.embedded.NameEmbedded;

public interface MultiLangMapperSupport {

    default String toCrl(String uz) {
        return uz == null ? null : LangUtils.toCyrillic(uz);
    }

    default void fillName(NameEmbedded name) {
        if (name != null && name.getNameUz() != null) {
            name.setNameCrl(toCrl(name.getNameUz()));
        }
    }

    default void fillDescription(DescriptionEmbedded desc) {
        if (desc != null && desc.getDescriptionUz() != null) {
            desc.setDescriptionCrl(toCrl(desc.getDescriptionUz()));
        }
    }
}
