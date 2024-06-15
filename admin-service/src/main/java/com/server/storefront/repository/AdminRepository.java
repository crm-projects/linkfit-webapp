package com.server.storefront.repository;

import org.springframework.data.repository.CrudRepository;

public interface AdminRepository<T,ID> extends CrudRepository<T,ID> {
}
