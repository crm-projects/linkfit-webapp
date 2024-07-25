package com.server.storefront.admin;

import org.springframework.data.repository.CrudRepository;

public interface AdminRepository<T,ID> extends CrudRepository<T,ID> {
}
