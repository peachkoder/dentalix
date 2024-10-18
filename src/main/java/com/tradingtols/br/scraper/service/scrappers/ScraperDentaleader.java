package com.tradingtols.br.scraper.service.scrappers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.tradingtols.br.scraper.model.entity.Produto;
import com.tradingtols.br.scraper.model.entity.ProdutoDentaleader;
import com.tradingtols.br.scraper.model.repository.ProdutoRepository;
import com.tradingtols.br.scraper.service.Companias;

//@Service
public class ScraperDentaleader extends ScraperClazz {

	

	private static final String URL = "https://www.dentaleader.com/#/dfclassic/query=";
	private static final String TAG_SEARCH_BOX = "#search";
	private static final String TAG_CONTAINER_RESULTS = ".df-results";
	private static final String TAG_CARD_CONTAINER = ".df-card";
	private static final String TAG_CARD = ".df-card__main";
	private static final String TAG_CARD_IMAGE = ".df-card__image > img";
	private static final String TAG_CARD_TITLE = ".df-card__content > .df-card__title";
	private static final String TAG_CARD_DESCRIPTION = ".df-card__content > .df-card__description";

	private static final String TAG_CARD_PRICE_CONTAINER = ".df-card__pricing";
	private static final String TAG_CARD_PRICE_OLD = ".df-card__content > .df-card__pricing > .df-card__price.df-card__price--old";
	private static final String TAG_CARD_PRICE_NEW = ".df-card__content > .df-card__pricing > .df-card__price.df-card__price--new";
	private static final String TAG_CARD_PRICE = ".df-card__content > .df-card__pricing > span";

	private List<ProdutoDentaleader> listaProdutos = new ArrayList<>();

	public ScraperDentaleader(ProdutoRepository repo) {
		super(repo);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public List<Produto> scrap(String search) {
		return null;
	}

	@Override
	public List<Produto> scrap(List<String> searchList) {
		
		try (Playwright playwright = Playwright.create();) {
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			Page page = browser.newPage();
			
			var newURL = String.format("%s%s", URL, searchList.getFirst());
			page.navigate(newURL);

			handleCookieConsent(page);
			handleProfisionalWarning(page);
			processPage(page, searchList);
			browser.close();
		}

		return null;
	}

	private void processPage(Page page, List<String> searchList) {
		System.out.println("53-[Dentaleader] - processPage...");

		for (String search : searchList) {
			// Encontre a caixa de pesquisa
			Locator caixaPesquisa = page.locator(TAG_SEARCH_BOX);
			if (caixaPesquisa.isVisible()) {
				caixaPesquisa.fill(search);
//				caixaPesquisa.press("enter");
			}
			
			page.waitForSelector(TAG_CONTAINER_RESULTS);
			page.waitForLoadState();
			
			// dados daa página para o scroll
			int windowHeight =(int) page.evaluate("document.body.scrollHeight");
			int xOffset = 0;
			

			boolean hasMoreContent = true;
			int previousItemCount = 0;
			//rollingPage(page, false);

			Locator resultContainer = page.locator(TAG_CONTAINER_RESULTS);

			do { 
				// Captura os itens carregados até agora
				Locator cards = resultContainer.locator(TAG_CARD_CONTAINER);
				
				int currentItemCount = cards.count();
				boolean isPageRolled = false;
				// Verifica se mais itens foram carregados
				try {
					if (currentItemCount > previousItemCount) {
						// Captura os novos itens carregados
						for (int i = previousItemCount; i < currentItemCount; i++) {
							if(!isPageRolled) {
								//rollingPage(page, false);
								isPageRolled = true;
							}
							//locator(".df-results").locator(".df-card").nth(30).locator("a")
							var card = cards.nth(i).locator("a");
							 if(!card.isVisible()) { //as vezes falha
								System.out.println("******************");
								System.out.println("Card que não tem a tag =" + i);
								System.out.println("Card que não tem a tag =" + search);
								System.out.println("******************");
//								page.waitForSelector(".df-results.df-card > a");
								
								continue;
							}
							var hRef = card.getAttribute("href");
							var externalId = getExternalId(card);
							//locator(".df-results").locator(".df-card").nth(398).locator("a").locator("figure > img")
							String imgSrc = "";
							var img = card.locator("figure > img");
							if(img.isVisible()) {
								imgSrc = img.getAttribute("src");
							}
							
							var cardTitle = card.locator(TAG_CARD_TITLE);
							var desc = "";
							if (cardTitle.isVisible()) {
								desc = cardTitle.innerText();
							}
//						var title = card.locator(TAG_CARD_TITLE).innerText();
							var pricing = card.locator(TAG_CARD_PRICE_CONTAINER);
							String price = "";
							if (pricing.isVisible()) {
								var newPrice = card.locator(TAG_CARD_PRICE_NEW);
								var oldPrice = card.locator(TAG_CARD_PRICE_OLD);
								if(oldPrice.isVisible()) {
//- waiting for locator(".df-results").locator(".df-card").nth(1).locator("a").locator(".df-card__content > .df-card__pricing > .df-card__price.df-card__price--new")									
									if (newPrice.isVisible()) price = newPrice.innerText();
								} else {
									price = card.locator(TAG_CARD_PRICE).innerText();
								}
							}

							var produto = new ProdutoDentaleader(0L,Companias.DENTALEADER.getNome(), desc, externalId, hRef, price, imgSrc, "", new Date());
							System.out.println("\n135-[ScraperDentaleader]["+search+"]  - produto n.=" + i);
							System.out.println("135-[ScraperDentaleader]["+search+"] - " + produto.toString());
							//listaProdutos.add(produto);
							repo.save(produto);
							//addToList(produto);
							//rollingPage(page, false);
						}
						previousItemCount = currentItemCount;
						// Scroll the footer into view, forcing an "infinite list" to load more content
						//page.locator(".wrapper-widget-categories.widget-full-width").scrollIntoViewIfNeeded();
						// Rola até o final da página para carregar mais conteúdo
						
						page.evaluate("window.scrollTo(" + xOffset + "," + windowHeight+")");	
						xOffset += windowHeight;						
						page.waitForTimeout(3000); // Aguarda um tempo para que novos itens sejam carregados
						// Rola do final da página para cima para carregar mais conteúdo
//						rollingPage(page, false);
					} else {
						// Se nenhum novo item foi carregado, então não há mais conteúdo
						hasMoreContent = false;
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					System.out.println("\n154-[ScraperDentaleader] - listaProdutos size = " + listaProdutos.size());
					repo.saveAll(listaProdutos);
					listaProdutos.clear();
				}
			} while (hasMoreContent);
			
			//saveToDb();
			System.out.println("\n162-[ScraperDentaleader]["+search+"] - listaProdutos size = " + listaProdutos.size());
			repo.saveAll(listaProdutos);
			listaProdutos.clear();
		}
	}
	
//	private void rolarPgina(Page page, int x) {
//		
//		page.evaluate("window.scrollTo(" + x + ", document.body.scrollHeight)");	
//		
//		
//	}

	private String getExternalId(Locator card) {
		String attribute = card.getAttribute("data-dfid");
		return splitTextoByLastChar(attribute, '@'); 
	}

	private void rollingPage(Page page, boolean bottomUp) {
//					page.evaluate("window.scrollTo(0, document.body.scrollHeight)");
		// Rolar a página para forçar o carregar os cards
		  // Obter a altura total da página
		Locator resultContainer = page.locator(".df-results");
//		Locator resultContainer = page.locator(TAG_CONTAINER_RESULTS);
//		resultContainer.scrollIntoViewIfNeeded();
//		page.waitForTimeout(1000); // Aguarda um tempo para que novos itens sejam carregados
//        int totalHeight = (int) resultContainer.evaluate("el => el.offsetHeight");
        int totalHeight = (int) page.evaluate("document.body.scrollHeight");
        int viewportHeight = (int) page.evaluate("window.innerHeight");
        int scrollPosition = bottomUp ? viewportHeight : 0;
        int scrollIncrement = bottomUp ? -100 : 100; // Incremento de rolagem (em pixels)

        // Rolagem humana
        while (scrollPosition < totalHeight) {
            // Rolar para baixo
//            resultContainer.evaluate("window.scrollBy(0, " + scrollIncrement + ");");
            resultContainer.evaluate("el => el.scrollTop += " + scrollIncrement);
//            page.evaluate("window.scrollBy(0, " + scrollIncrement + ");");

            // Atualizar a posição de rolagem
            scrollPosition += scrollIncrement;

            // Aguardar um tempo aleatório para mimetizar um comportamento humano
            int waitTime = (int) (Math.random() * 200) + 200; // Tempo entre 200ms e 400ms
            page.waitForTimeout(waitTime);
        }
	}

	@Override
	public void handleCookieConsent(Page page) {
		System.out.println("handleCookieConsent...");
		Locator divCookie = page.locator(".cookie-notice  ");
		if (divCookie.isVisible()) {
			var button = divCookie.locator(".cm-btn.cm-btn-success");
			button.click();
		}
	}
}
