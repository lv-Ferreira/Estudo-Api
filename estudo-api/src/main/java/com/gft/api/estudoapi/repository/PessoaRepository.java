package com.gft.api.estudoapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gft.api.estudoapi.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>{

}
