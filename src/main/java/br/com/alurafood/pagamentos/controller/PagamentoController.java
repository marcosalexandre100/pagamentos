package br.com.alurafood.pagamentos.controller;

import br.com.alurafood.pagamentos.dto.PagamentoDto;
import br.com.alurafood.pagamentos.service.PagamentoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {
    @Autowired
    private PagamentoService service;

    @GetMapping
    public Page<PagamentoDto> list(@PageableDefault(size=10) Pageable paginacao){
        return service.obterTodos(paginacao);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PagamentoDto> detalhar(@PathVariable @NotNull Long id){
       PagamentoDto dto = service.obterPorId(id);
       return ResponseEntity.ok(dto);
    }
    @PostMapping
    public ResponseEntity<PagamentoDto> cadastrar(@RequestBody @Valid  PagamentoDto dto, UriComponentsBuilder uriBuilder){
       PagamentoDto pagamento =  service.criarPagamento(dto);
        URI endereco = uriBuilder.path("/pagamentos/{id}").buildAndExpand(pagamento.getId()).toUri();

        return ResponseEntity.created(endereco).body(pagamento);

    }
    @PutMapping("/{id}")
    public  ResponseEntity<PagamentoDto> atualizar(@PathVariable @NotNull Long id, @RequestBody @Valid PagamentoDto dto){
        PagamentoDto atualizado = service.atualizarPagamento(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @PutMapping("cancelamentos/{id}")
    public ResponseEntity<PagamentoDto> cancelar(@PathVariable @NotNull Long id){
        PagamentoDto dto = service.excluirPagamento(id);
        return ResponseEntity.ok(dto);
    }


}
