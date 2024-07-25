package com.server.storefront.store.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "CREATOR_STORE")
@EqualsAndHashCode(callSuper = true)
public class CreatorStore extends Store {


}
