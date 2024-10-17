package com.tradingtols.br.scraper.service.scrappers;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.tradingtols.br.scraper.model.entity.Produto;
import com.tradingtols.br.scraper.model.entity.ProdutoDentalix;
import com.tradingtols.br.scraper.model.repository.ProdutoRepository;
import com.tradingtols.br.scraper.service.Companias;

@Service
public class ScraperDentalExpress extends ScraperClazz
{
	private static final String RESULTS_GRID = ".dfd-results-grid";
	private static final String PRODUCTS_SEARCH_RESULT = ".products-search-result";
	private static final String CARD = ".product-item.responsive.product-model-item";
	private static final String URL = "https://www.dentaltix.com/pt/search-results?keyword=%s&_page=1";

	public ScraperDentalExpress(ProdutoRepository repo) {super(repo);}

	@Override
	public List<Produto> scrap(List<String> searchList) {
		try (Playwright playwright = Playwright.create()){
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			Page page = browser.newPage();
			page.setDefaultNavigationTimeout(60000);
			
			handleProfisionalWarning(page);
			handleCookieConsent(page);			
			handleFirstSearchBarClick(page);

			processPage(page, searchList);
			browser.close();
		}
		return null;
	}


	private void processPage(Page page, List<String> searchList) {
		
		for (String search : searchList) {
			System.out.println("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			System.out.println("43-[ScraperDentalExpress] - search = " + search);
		
			String searchURL = String.format(URL,search);
			page.navigate(searchURL);
			
			try {
				Locator searchBox = page.locator("#dfd-searchbox-id-zyF4E-input");
				if(searchBox.isVisible()) searchBox.fill(search);
				page.waitForSelector(RESULTS_GRID);
			} catch (Exception e) {
				e.printStackTrace();
			}
			page.waitForLoadState();
			
			boolean hasMorePages = true;
			boolean bottomUp = false;
			boolean hasMoreContent = true;
			int previousItemCount = 0;
			
			do {//dfd-card dfd-card-preset-product dfd-card-type-pro_dept
				Locator resultContainer = page.locator(RESULTS_GRID);
				if (!resultContainer.isVisible()) return;
				
				if (bottomUp) {
					resultContainer.evaluate("el => el.scrollIntoView({ block: 'start', behavior: 'smooth' })");
				} else {
					resultContainer.evaluate("el => el.scrollIntoView({ block: 'end', behavior: 'smooth' })");
				}
				bottomUp = !bottomUp;
				
				page.waitForTimeout(500);
//				page.waitForTimeout(500);
//				resultContainer.evaluate("el => el.scrollIntoView({ block: 'end', behavior: 'smooth' })");
				Locator cards = resultContainer.locator(".dfd-card.dfd-card-preset-product.dfd-card-type-pro_dept");
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

	private void handleFirstSearchBarClick(Page page) {
		Locator searchTopBar = page.locator("#search_mini_form");
		if(searchTopBar.isVisible()) {
			searchTopBar.locator("#search").click();
//				searchTopBar.locator("#search").fill(searchList.getFirst());;
		}
	}
	


	private void processCards(Page page, Locator cards) {
		for (int i = 0; i < cards.count(); i++) {
			String imgSrc = "";
			String desc = "";
			String brand = "";
			String price = "";
			String href = "";
			String externalId = "";
			
			/*iting for locator(".products-search-result").locator(".product-item.responsive.product-model-item")
			 * .nth(4).locator(".flag-brand-container > span")
			 * for locator(".products-search-result").locator(".product-item.responsive.product-model-item")
			 * .nth(1).locator("a").first()			
			 */
			Locator link = cards.nth(i).locator("a.image-container");
			if (link.isVisible()) {
				//link = link.locator("a").first();
				href = link.getAttribute("href");
				Locator img = link.locator("img");
				if(img.isVisible()) {
					
					imgSrc = img.getAttribute("src");
					externalId = splitTextoByLastChar(imgSrc, '=');
				}
			}
			
			Locator linkTitle = cards.nth(i).locator("a.product-item-title > strong");
			if(linkTitle.isVisible()) {
				desc = linkTitle.innerText();
			}
			
			/*
			 *  waiting for locator(".products-search-result")
			 *  .locator(".product-item.responsive.product-model-item")
			 *  .nth(2).locator(".flag-brand-container > span")
			 */
			Locator spanBrand = cards.nth(i).locator(".flag-brand-container > span");
			if(spanBrand.isVisible()) {
				brand = splitTextoByLastChar(spanBrand.innerText(), ' ');
			}
			/*
			 * 
- waiting for locator(".products-search-result")
.locator(".product-item.responsive.product-model-item").first().locator(".mini-price-rating > p")
			 */
			
			/*
			 * - waiting for locator(".products-search-result")
			 * .locator(".product-item.responsive.product-model-item")
			 * .nth(1).locator(".mini-price-rating > p > del")
			 */
			Locator priceContainer = cards.nth(i).locator(".mini-price-rating > p");
			Locator oldPrice = priceContainer.locator("del");
			if(oldPrice.isVisible()) {
				price = priceContainer.innerText();
				price = price.substring(oldPrice.innerText().length());
			}
			var produto = new ProdutoDentalix(0L, Companias.DENTALIX.getNome(), desc, externalId, href, price, imgSrc, brand, new Date() );
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
	public	void handleProfisionalWarning(Page page) {
		System.out.println("...handleProfissionalWarning...");
		ElementHandle checkbox = page.querySelector("#professional-input-custom");
		if(checkbox.isVisible()) {
			try {
				checkbox.click();
				Locator button = page.locator("btn.btn-primary.action-confirm-professional");
				if (button.isVisible()) {
					button.click();
				}
			} catch (Exception e) {
				System.err.println("180-[handleProfissionalWarning] - Erro:\n");
				e.printStackTrace();
			}
		}
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
