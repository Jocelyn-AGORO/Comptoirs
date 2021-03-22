package comptoirs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import comptoirs.dao.ProduitRepository;
import comptoirs.dto.UnitesParProduit;

@DataJpaTest
class ProduitRepositoryTest {
	Logger logger = LoggerFactory.getLogger(ProduitRepositoryTest.class);

	// Pour afficher du JSON dans les logs
	private final ObjectMapper mapper = new ObjectMapper();		

	@Autowired 
	private ProduitRepository daoProduit;
	
	@Test
	@Sql("small_data.sql")		
	void calculCorrectDesStatistiques() throws IOException {
		logger.debug("Calcul des statistiques");
		int categorieAvecProduit = 1;
		int categorieSansProduit = 2;
		List<UnitesParProduit> results  = daoProduit.produitsVendusPour(categorieSansProduit);
		assertEquals(0, results.size(),   
			"La catégorie 2 n'a pas de produit dans le jeu de test");
		results = daoProduit.produitsVendusPour(categorieAvecProduit);
		logger.debug("Résultats de la requête: "+ mapper.writeValueAsString(results));
		assertEquals(2, results.size(),   
			"La catégorie 1 a deux produits différents vendus dans le jeu de test");
		assertEquals(35, results.get(1).getUnites(), "On a vendu 35 unités du produit 'Chang' dans le jeu de test");
	}
}
