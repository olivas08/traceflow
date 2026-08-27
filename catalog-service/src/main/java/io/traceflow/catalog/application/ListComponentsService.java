package io.traceflow.catalog.application;

import io.traceflow.catalog.domain.model.Component;
import io.traceflow.catalog.domain.model.PageResult;
import io.traceflow.catalog.domain.port.in.ListComponentsUseCase;
import io.traceflow.catalog.domain.port.out.ComponentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ListComponentsService implements ListComponentsUseCase {
    private final ComponentRepository components;

    public ListComponentsService(ComponentRepository components) {
        this.components = components;
    }

    @Override
    @Transactional
    public PageResult<Component> execute(int page, int size) {
        return components.findAll(page, size);
    }
}
