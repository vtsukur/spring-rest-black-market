package org.vtsukur.spring.rest.market.infrastructure;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.*;

/**
 * @author volodymyr.tsukur
 */
@Converter
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Long> {

    @Override
    public Long convertToDatabaseColumn(final LocalDateTime attribute) {
        return (attribute != null) ? attribute.toInstant(ZoneOffset.UTC).toEpochMilli() : null;
    }

    @Override
    public LocalDateTime convertToEntityAttribute(final Long dbData) {
        return (dbData != null) ? LocalDateTime.ofInstant(Instant.ofEpochMilli(dbData), ZoneOffset.UTC) : null;
    }

}
