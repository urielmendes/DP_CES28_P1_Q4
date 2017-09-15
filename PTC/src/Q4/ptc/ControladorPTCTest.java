package Q4.ptc;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ControladorPTCTest {

	@InjectMocks private ControladorPTC ptc;
	
	@Mock private Sensor sensor;
	@Mock private Datacenter datacenter;
	@Mock private PainelCondutor painelcond;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	///////////item a ///////////////////////////////////////////////
	@Test
	public void testaInicializacaoPTC() {
		assertNotNull(ptc);
	}
	/////////////////////////////////////////////////////////////////////
	
	
	/////////////item b////////////////////////////////////////////////
	@Test
	public void testaCruzamentoFalse(){
			Mockito.when(sensor.isCruzamento()).thenReturn(false);
			
			//velocidade baixa
			Mockito.when(sensor.getVelocidade()).thenReturn(10.0);
			ptc.run();
			Mockito.verify(painelcond, Mockito.times(0)).aceleraVelocidadeTrem(Mockito.anyDouble());
			
			//velocidade alta
			Mockito.when(sensor.getVelocidade()).thenReturn(120.0);
			ptc.run();
			Mockito.verify(painelcond, Mockito.times(0)).diminuiVelocidadeTrem(Mockito.anyDouble());
	}
	/////////////////////////////////////////////////////////////////////
	
	
	//////////////item c///////////////////////////////////////////////////
	@Test
	public void testaCruzamentoVelocidadeAlta(){
		Mockito.when(sensor.isCruzamento()).thenReturn(true);
		Mockito.when(sensor.getVelocidade()).thenReturn(120.0);
		Mockito.when(painelcond.imprimirAviso(Mockito.anyString(), Mockito.anyInt())).thenReturn(true);
		
		//painelcond.imprimirAviso() deve ser chamada exatamente uma vez
		//painelcond.diminuiVelocidadeTrem() nao deve ser chamada
		ptc.run();
		Mockito.verify(painelcond, Mockito.times(1)).imprimirAviso("Velocidade alta", 1);
		Mockito.verify(painelcond, Mockito.times(0)).diminuiVelocidadeTrem(Mockito.anyDouble());
	}
	///////////////////////////////////////////////////////////////
	
	
	////////////item d//////////////////////////////////////////////
	@Test
	public void testaCruzamentoVelocidadeBaixaSemConfirmacao(){
		Mockito.when(sensor.isCruzamento()).thenReturn(true);
		Mockito.when(sensor.getVelocidade()).thenReturn(10.0);
		Mockito.when(painelcond.imprimirAviso(Mockito.anyString(), Mockito.anyInt())).thenReturn(false);
		
		
		//O controlador tenta enviar msg duas vezes para o painel, logo
		//painelcond.imprimirAviso() deve ser chamada duas vezes
		//O metodo aceleraVelocidadeTrem deve ser chamado
		ptc.run();
		Mockito.verify(painelcond, Mockito.times(2)).imprimirAviso("Velocidade Baixa", 1);
		Mockito.verify(painelcond, Mockito.times(1)).aceleraVelocidadeTrem(Mockito.anyDouble());
	}
	////////////////////////////////////////////////////////////
}