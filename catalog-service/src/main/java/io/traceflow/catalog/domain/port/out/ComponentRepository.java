package io.traceflow.catalog.domain.port.out;

import io.traceflow.catalog.domain.model.Component;
import io.traceflow.catalog.domain.model.PageResult;
import io.traceflow.catalog.domain.model.SerialNumber;
import io.traceflow.catalog.domain.model.TimelineEntry;
import java.util.List;
import java.util.Optional;

public interface ComponentRepository {
    void save(Component component);

    boolean existsBySerial(SerialNumber serialNumber);

    Optional<Component> findBySerial(SerialNumber serialNumber);

    PageResult<Component> findAll(int page, int size);

    void appendTimeline(SerialNumber serialNumber, TimelineEntry entry);

    List<TimelineEntry> findTimeline(SerialNumber serialNumber);
}
