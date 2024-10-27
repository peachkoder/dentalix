package com.tradingtols.br.scraper.service.scrappers;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.tradingtols.br.scraper.model.entity.Produto;
import com.tradingtols.br.scraper.model.entity.ProdutoDentalix;
import com.tradingtols.br.scraper.model.entity.ProdutoMinhoMedica;
import com.tradingtols.br.scraper.model.repository.ProdutoRepository;
import com.tradingtols.br.scraper.service.Companias;

@Service
public class ScraperMinhoMedica extends ScraperClazz
{
	private static final String CARDS_CONTAINER = ".product_list grid .profile-default > row";
	private static final String CARD = ".product-item.responsive.product-model-item";
	private static final String URL = "https://loja.minhomedica.pt/pt/pesquisa?controller=search&s=%s";

	public ScraperMinhoMedica(ProdutoRepository repo) {super(repo);}

	@Override
	public List<Produto> scrap(List<String> searchList) {
		try (Playwright playwright = Playwright.create()){
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			Page page = browser.newPage();
			page.setDefaultNavigationTimeout(60000);
			processPage(page, searchList);
			browser.close();
		}
		return null;
	}


	private void processPage(Page page, List<String> searchList) {
		
		for (String search : searchList) {
			System.out.println("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			System.out.println("44-[ScraperMinhoMedica] - search = " + search);
		
			search = search.trim().replace(" ", "+");
			
			String searchURL = String.format(URL,search);
			page.navigate(searchURL);
			
			//sair do aviso da newsletter
			page.mouse().click(1, 1);
//			handleCookieConsent(page);
//			handleProfisionalWarning(page);
			
			try {
				Locator qtdOfProdutos = page.locator(".products-founds-copy");
				if(qtdOfProdutos.isVisible()) 
					page.waitForSelector(CARDS_CONTAINER);
			} catch (Exception e) {
				e.printStackTrace();
			}
			boolean hasMorePages = true;
			boolean bottomUp = false;
			
			do {
				Locator cardsContainer = page.locator(CARDS_CONTAINER);
				if (!cardsContainer.isVisible()) return;
				
				cardsContainer.evaluate("el => el.scrollIntoView({ block: 'start', behavior: 'smooth' })");
				
				page.waitForTimeout(500);
//				resultContainer.evaluate("el => el.scrollIntoView({ block: 'end', behavior: 'smooth' })");
				Locator cards = cardsContainer.locator("div");
				try {
					if (cards.count() > 0) processCards(page, cards);
				} catch (Exception e) {
					e.printStackTrace();
				}
				page.waitForTimeout(5000);
				hasMorePages = getNextPage(page);
			} while (hasMorePages);
		}
		
	}
	


	private void processCards(Page page, Locator cards) {
		for (int i = 0; i < cards.count(); i++) {
			 String imgSrc = "",desc = "",brand = "",price = "",href = "", externalId = "";
			
			Locator link = cards.nth(i).locator("product-image > a");
			if (link.isVisible()) {
				//link = link.locator("a").first();
				href = link.getAttribute("href");
				Locator img = link.locator("img");
				if(img.isVisible()) {
					imgSrc = img.getAttribute("src");
					externalId = img.getAttribute("data-idproduct");
				}
			}
			
			Locator linkTitle = cards.nth(i).locator(".product-meta > .h3.product-title > a");
			if(linkTitle.isVisible()) {
				desc = linkTitle.innerText();
			}
			
//			Locator spanBrand = cards.nth(i).locator(".flag-brand-container > span");
//			if(spanBrand.isVisible()) {
//				brand = splitTextoByLastChar(spanBrand.innerText(), ' ');
//			}

			Locator priceContainer = cards.nth(i).locator(".product-price-and-shipping  > span > span");
			if(priceContainer.isVisible()) {
				price = priceContainer.nth(1).innerText();
			}
			
			var produto = new ProdutoMinhoMedica(0L, Companias.MINHO_MEDICA.getNome(), desc, externalId, href, price, imgSrc, brand, new Date() );
			repo.save(produto);
			System.out.println("\n*******************");
			System.out.println(produto.toString());
			
		}
		
	}


	private boolean getNextPage(Page page) {
		Locator buttonNextPage = page.locator(".p-paginator-next.p-paginator-element.p-link");
		if(!buttonNextPage.isVisible()) return false;
		
		buttonNextPage.click();
		
		return true;
		
	}

	@Override
	public void handleCookieConsent(Page page) {
		Locator buttonDecline = page.locator("#CybotCookiebotDialogBodyButtonDecline");
		if (buttonDecline.isVisible())
			buttonDecline.click();
	}
	
	@Override
	public List<Produto> scrap(String search) {
		
		
		// TODO Auto-generated method stub
		return null;
	}
}
