package com.gft.api.estudoapi.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gft.api.estudoapi.event.RecursoCriadoEvent;
import com.gft.api.estudoapi.model.Pessoa;
import com.gft.api.estudoapi.repository.PessoaRepository;
import com.gft.api.estudoapi.service.PessoaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "Pessoas")
@RestController
@RequestMapping("/pessoas")
public class PessoaResource {

	@Autowired
	PessoaRepository pessoaRepository;
	
	@Autowired
	private PessoaService pessoaService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@ApiOperation("Listar todas as Pessoas")
	@GetMapping
	public List<Pessoa> listar(){
		return pessoaRepository.findAll();
	}
	
	@ApiOperation("Criar uma nova Pessoa")
	@PostMapping
	public ResponseEntity<Pessoa> criar(@ApiParam(value = "Informe os Dados da nova Pessoa", example = "ex") @Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {
		Pessoa pessoaSalva = pessoaRepository.save(pessoa);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
	}
	
	@ApiOperation("Buscar uma Pessoa especifica")
	@GetMapping("/{codigo}")
	public ResponseEntity<Pessoa> buscarPeloCodigo(@ApiParam(value = "Informe o Código da Pessoa", example = "1") @PathVariable Long codigo) {
		Pessoa pessoa = this.pessoaRepository.findById(codigo).orElse(null);
		return pessoa != null ? ResponseEntity.ok(pessoa) : ResponseEntity.notFound().build();
	}
	
	@ApiOperation("Remover uma Pessoa")
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@ApiParam(value = "Informe o Código da Pessoa", example = "1") @PathVariable Long codigo) {
		this.pessoaRepository.deleteById(codigo);
	}
	
	@ApiOperation("Atualizar as Informações de uma Pessoa")
	@PutMapping("/{codigo}")
	public ResponseEntity<Pessoa> atualizar(@ApiParam(value = "Informe o Código da Pessoa", example = "1") @PathVariable Long codigo, @Valid @RequestBody Pessoa pessoa){
		Pessoa pessoaSalva = pessoaService.atualizar(codigo, pessoa);	
		return ResponseEntity.ok(pessoaSalva);
	}
	
	@ApiOperation("Atualizar o Status de uma Pessoa")
	@PutMapping("/{codigo}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarPropriedadeAtivo(@ApiParam(value = "Informe o Código da Pessoa", example = "1") @PathVariable Long codigo, @RequestBody Boolean ativo) {
		pessoaService.atualizarPropriedadeAtivo(codigo, ativo);
	}
}
