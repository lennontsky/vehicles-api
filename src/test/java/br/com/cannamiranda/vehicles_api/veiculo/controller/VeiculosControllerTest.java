// java
package br.com.cannamiranda.vehicles_api.veiculo.controller;

import br.com.cannamiranda.vehicles_api.veiculo.model.Veiculo;
import br.com.cannamiranda.vehicles_api.veiculo.model.DadosVeiculo;
import br.com.cannamiranda.vehicles_api.veiculo.processor.VeiculoProcessor;
import br.com.cannamiranda.vehicles_api.veiculo.repository.VeiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VeiculosControllerTest {

    @InjectMocks
    private VeiculosController controller;

    @Mock
    private VeiculoProcessor processor;

    @Mock
    private VeiculoRepository repository;

    private Veiculo sampleVeiculo;

    @BeforeEach
    void setUp() {
        sampleVeiculo = mock(Veiculo.class);
    }

    @Test
    void consultaTodosVeiculos_devePassarFiltrosParaProcessor() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<List<Veiculo>> page = new PageImpl<>(List.of(List.of(sampleVeiculo)));
        ResponseEntity<Page<List<Veiculo>>> expected = ResponseEntity.ok(page);

        when(processor.buscarVeiculos(eq(pageable), anyMap())).thenReturn(expected);

        ResponseEntity<Page<List<Veiculo>>> resp = controller.consultaTodosVeiculos(
                pageable, "1000", "20000", "vermelho", "Toyota", "2020"
        );

        assertSame(expected, resp);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String>> captor = ArgumentCaptor.forClass(Map.class);
        verify(processor, times(1)).buscarVeiculos(eq(pageable), captor.capture());
        Map<String, String> filtros = captor.getValue();
        assertEquals("1000", filtros.get("valorMinimo"));
        assertEquals("20000", filtros.get("valorMaximo"));
        assertEquals("vermelho", filtros.get("cor"));
        assertEquals("Toyota", filtros.get("marca"));
        assertEquals("2020", filtros.get("ano"));
    }

    @Test
    void consultaTodosVeiculos_semFiltros_devePassarMapaVazio() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<List<Veiculo>> page = new PageImpl<>(List.of(List.of(sampleVeiculo)));
        ResponseEntity<Page<List<Veiculo>>> expected = ResponseEntity.ok(page);

        when(processor.buscarVeiculos(eq(pageable), anyMap())).thenReturn(expected);

        ResponseEntity<Page<List<Veiculo>>> resp = controller.consultaTodosVeiculos(
                pageable, null, null, null, null, null
        );

        assertSame(expected, resp);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String>> captor = ArgumentCaptor.forClass(Map.class);
        verify(processor).buscarVeiculos(eq(pageable), captor.capture());
        Map<String, String> filtros = captor.getValue();
        assertTrue(filtros.isEmpty());
    }

    @Test
    void detalhesVeiculo_delegatesToProcessor() {
        Long id = 42L;
        ResponseEntity<Veiculo> expected = ResponseEntity.ok(sampleVeiculo);
        when(processor.buscarVeiculoPorId(id)).thenReturn(expected);

        ResponseEntity<Veiculo> resp = controller.detalhesVeiculo(id);

        assertSame(expected, resp);
        verify(processor).buscarVeiculoPorId(id);
    }

    @Test
    void relatorioVeiculosPorMarca_delegatesToProcessor() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Veiculo> lista = List.of(sampleVeiculo);
        ResponseEntity<List<Veiculo>> expected = ResponseEntity.ok(lista);
        when(processor.obterRelatorioVeiculosPorMarca()).thenReturn(expected);

        ResponseEntity<List<Veiculo>> resp = controller.relatorioVeiculosPorMarca(pageable);

        assertSame(expected, resp);
        verify(processor).obterRelatorioVeiculosPorMarca();
    }

    @Test
    void adicionarVeiculo_constroiUriEDelegatesToProcessor() {
        DadosVeiculo dados = mock(DadosVeiculo.class);
        when(dados.placa()).thenReturn("ABC1A23");

        URI expectedUri = URI.create("/veiculos/ABC1A23");
        Veiculo created = sampleVeiculo;
        ResponseEntity<Veiculo> expected = ResponseEntity.created(expectedUri).body(created);

        // aceitamos qualquer URI, mas capturamos para validar que placa foi usada no path
        ArgumentCaptor<URI> uriCaptor = ArgumentCaptor.forClass(URI.class);
        when(processor.adicionarVeiculo(eq(dados), any(URI.class))).thenReturn(expected);

        ResponseEntity<Veiculo> resp = controller.adicionarVeiculo(dados, UriComponentsBuilder.newInstance());

        assertSame(expected, resp);
        verify(processor).adicionarVeiculo(eq(dados), uriCaptor.capture());
        URI passed = uriCaptor.getValue();
        assertNotNull(passed);
        assertTrue(passed.toString().endsWith("/ABC1A23"));
    }

    @Test
    void atualizarVeiculoCompletamente_delegatesToProcessor() {
        Veiculo toUpdate = sampleVeiculo;
        Veiculo updated = sampleVeiculo;
        ResponseEntity<Veiculo> expected = ResponseEntity.ok(updated);

        when(processor.atualizarVeiculoCompleto(toUpdate)).thenReturn(expected);

        ResponseEntity<Veiculo> resp = controller.atualizarVeiculoCompletamente(toUpdate);

        assertSame(expected, resp);
        verify(processor).atualizarVeiculoCompleto(toUpdate);
    }

//    @Test
//    void deletarVeiculo_delegatesToProcessor() {
//        Long id = 7L;
//        ResponseEntity<?> expected = ResponseEntity.noContent().build();
//        when(processor.desativarVeiculo(id)).thenReturn(expected);
//
//        ResponseEntity<?> resp = controller.deletarVeiculo(id);
//
//        assertSame(expected, resp);
//        verify(processor).desativarVeiculo(id);
//    }
}