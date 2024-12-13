package br.com.alurafood.pagamentos.service;

import br.com.alurafood.pagamentos.dto.PagamentoDto;
import br.com.alurafood.pagamentos.model.Pagamentos;
import br.com.alurafood.pagamentos.model.Status;
import br.com.alurafood.pagamentos.repository.PagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PagamentoService {
   @Autowired
   private PagamentoRepository pagamentoRepository;
   @Autowired
    private ModelMapper modelMapper;


   public Page<PagamentoDto> obterTodos(Pageable paginacao){
       return pagamentoRepository
               .findAll(paginacao)
               .map(p -> modelMapper.map(p, PagamentoDto.class));

    }

   public PagamentoDto obterPorId(Long id){
      Pagamentos pagamentos = pagamentoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
      return modelMapper.map(pagamentos, PagamentoDto.class);
   }

   public PagamentoDto criarPagamento(PagamentoDto dto){
       Pagamentos pagamento = modelMapper.map(dto, Pagamentos.class);
       pagamento.setStatus(Status.CRIADO);
       pagamentoRepository.save(pagamento);

       return modelMapper.map(pagamento, PagamentoDto.class);
   }

   public PagamentoDto excluirPagamento(Long id){
      Optional<Pagamentos> pagamentos =  pagamentoRepository.findById(id);

      if(!pagamentos.isPresent()){
          throw new EntityNotFoundException();
      }
      pagamentos.get().setStatus(Status.CANCELADO);
      pagamentoRepository.save(pagamentos.get());

      return modelMapper.map(pagamentos, PagamentoDto.class);

      
   }

    public PagamentoDto atualizarPagamento(Long id, PagamentoDto dto) {
       Pagamentos pagamento =modelMapper.map(dto, Pagamentos.class);
       pagamento.setId(id);
       pagamento = pagamentoRepository.save(pagamento);
       return modelMapper.map(pagamento, PagamentoDto.class);
    }
}
