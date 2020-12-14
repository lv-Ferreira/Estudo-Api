package com.gft.api.estudoapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gft.api.estudoapi.model.Lancamento;
import com.gft.api.estudoapi.repository.lancamento.LancamentoRepositoryQuery;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery{

}
