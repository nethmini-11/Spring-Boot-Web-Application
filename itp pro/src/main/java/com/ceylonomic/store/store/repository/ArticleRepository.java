package com.ceylonomic.store.store.repository;


import java.util.List;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ceylonomic.store.store.domain.Article;

public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {
	
	@EntityGraph(attributePaths = { "sizes", "categories"  })
	List<Article> findAllEagerBy();	
		
	@EntityGraph(attributePaths = { "sizes", "categories" })
	Optional<Article> findById(Long id);
	
	@Query("SELECT DISTINCT s.value FROM Size s")
	List<String> findAllSizes();
	
	@Query("SELECT DISTINCT c.name FROM Category c")
	List<String> findAllCategories();
	
	
}
