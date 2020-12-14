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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gft.api.estudoapi.event.RecursoCriadoEvent;
import com.gft.api.estudoapi.model.Categoria;
import com.gft.api.estudoapi.repository.CategoriaRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "Categorias")
@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@ApiOperation("Listar todas as Categorias")
	@GetMapping
	public List<Categoria> listar(){
		return categoriaRepository.findAll();
	}
	
	@ApiOperation("Criar uma nova Categoria")
	@PostMapping
	public ResponseEntity<Categoria> criar(@ApiParam(value = "Informe o Nome da nova Categoria", example = "ex") @Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		Categoria categoriaSalva = categoriaRepository.save(categoria);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
	}
	
	@ApiOperation("Buscar uma Categoria especifica")
	@GetMapping("/{codigo}")
	public ResponseEntity<Categoria> buscarPeloCodigo(@ApiParam(value = "Informe o Código da Categoria", example = "1") @PathVariable Long codigo) {
		Categoria categoria = this.categoriaRepository.findById(codigo).orElse(null);
		return categoria != null ? ResponseEntity.ok(categoria) : ResponseEntity.notFound().build();
	}
	
	@ApiOperation("Remover uma Categoria")
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@ApiParam(value = "Informe o Código da Categoria", example = "1") @PathVariable Long codigo) {
		this.categoriaRepository.deleteById(codigo);
	}
}
