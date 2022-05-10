package com.codesoom.assignment.domain;

public interface TestProductRepositoryDouble extends ProductRepository {
    @Override
    void deleteAll();

    @Override
    void delete(Product product);
}
