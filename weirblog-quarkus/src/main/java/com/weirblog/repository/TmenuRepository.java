package com.weirblog.repository;

import javax.enterprise.context.ApplicationScoped;

import com.weirblog.entity.Tmenu;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class TmenuRepository implements PanacheRepository<Tmenu> {

}
