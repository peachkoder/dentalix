package com.tradingtols.br.scraper.service.scrappers;
import static com.tradingtols.br.scraper.model.tools.CategoriasExtractor.extract;

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
import com.tradingtols.br.scraper.model.entity.ProdutoDentalexpress;
import com.tradingtols.br.scraper.model.entity.ProdutoHenrySchein;
import com.tradingtols.br.scraper.model.entity.ProdutoMontellano;
import com.tradingtols.br.scraper.model.entity.categorias.ProdutoCategoria;
import com.tradingtols.br.scraper.model.repository.ProdutoRepository;
import com.tradingtols.br.scraper.model.tools.StringToProdutoCategoriaFlagConverter;
import com.tradingtols.br.scraper.service.Companias;

@Service
public class ScraperMontellano extends ScraperClazz
{
	private static final String CATALOG_LIST = ".products-catalog__list products-catalog__list--static";
//	private static final String URL = "https://dentalexpress.pt/#df08/fullscreen/m=and";
	private static final String URL = "https://www.montellano.pt/products/search?q=%s&subfamilies=true";

	public ScraperMontellano(ProdutoRepository repo) {super(repo);}

	@Override
	public List<Produto> scrap(List<String> searchList) {
		try (Playwright playwright = Playwright.create()){
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			Page page = browser.newPage();
			page.setDefaultNavigationTimeout(60000);
			page.navigate(URL);
			
			handleCookieConsent(page);			
			handleProfisionalWarning(page);
//			handleFirstSearchBarClick(page);

			processPage(page, searchList);
			browser.close();
		}
		return null;
	}


	private void processPage(Page page, List<String> searchList) {
		
		for (String search : searchList) {
//			search = search.replace(" ", "%20");
			search = search.trim().replace(" ", "+");
			System.out.println("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			System.out.println("57-[ScraperMontellano] - search = " + search);
		
			String searchURL = String.format(URL,search);
			page.navigate(searchURL);
			
			try {
//				Locator searchBox = page.locator("#dfd-searchbox-id-zyF4E-input");
//				if(searchBox.isVisible()) searchBox.fill(search);
				try {
					page.waitForSelector(CATALOG_LIST);
				} catch (Exception e) {
					// TODO: handle exception
				}
				page.waitForLoadState();
				
				boolean hasMoreContent = true;
				int previousItemCount = 0;
				
				do {
					Locator resultContainer = page.locator(CATALOG_LIST);
					if (!resultContainer.isVisible()) return;
	
					resultContainer.evaluate("el => el.scrollIntoView({ block: 'end', behavior: 'smooth' })");
					page.waitForTimeout(2000);
	
					Locator cards = resultContainer.locator(".product-card   ");
					int currentItemCount = cards.count();
					
					try {
						if (currentItemCount > previousItemCount) {
							processCards(page, cards, previousItemCount);
						} else {
							hasMoreContent = false;
						}
						previousItemCount = currentItemCount;
					} catch (Exception e) {
						System.err.println("84-[processPage] - Erro $$$$$$$$$$$$$$$$$$$$$$\n"+
								e.getLocalizedMessage());
					}
					page.waitForTimeout(5000);
				} while (hasMoreContent);
			} catch (Exception e) {
				e.printStackTrace();
				page.waitForTimeout(5000);
			}
		}
		
	}

	private void processCards(Page page, Locator cards, int pos) {
		
		for (int i = pos; i < cards.count(); i++) {

			String imgSrc = "", desc = "", price = "", href = "", externalId = "", brand = "";
						
			Locator produtoDetails = cards.nth(i).locator("product-card__details.product-card__details--desktop");
			if(produtoDetails.isVisible()) {
				Locator spans = produtoDetails.locator("span");
				brand = spans.first().innerText();
				externalId = brand + "_" + spans.locator("span").last().innerText();
//				externalId = externalId.replace("_Ref. Grupo", "");
			}
			
			Locator cardImage = cards.nth(i).locator(".product-card__image > a");
			if(cardImage.isVisible()) {
				href = cardImage.getAttribute("href");
				imgSrc = cardImage.locator("img").getAttribute("src");
			}
			
			Locator cardName = cards.nth(i).locator(".product-card__information-wrapper > .product-card__name > a");
			if(cardName.isVisible()) {
				desc = cardName.innerText();
			}
			
			Locator cardPriceContainer = cards.nth(i).locator(".product-card__information-wrapper > .product-card__price > .product-card__price--final > span");
			if(cardPriceContainer.isVisible()) {
				price = cardPriceContainer.first().innerText() +
						cardPriceContainer.nth(1).innerText() +
						cardPriceContainer.last().innerText();
			}
			
			Locator img = cards.nth(i).locator(".dfd-card-media > div > img");
			if(img.isVisible()) {
				imgSrc = img.getAttribute("src");
			}

			var produto = new ProdutoMontellano(0L, Companias.MONTELLANO.getNome(), desc, externalId, href, price, imgSrc, brand, new Date() );
			List<ProdutoCategoria> categorias = extract (desc);
			produto.setCategorias(categorias);
			repo.save(produto);
		}
		
	}

	@Override
	public	void handleProfisionalWarning(Page page) {
		System.out.println("...handleProfissionalWarning...");
		ElementHandle button = page.querySelector(".professional-advice__confirm.button.button--primary");
		if(button!=null && button.isVisible()) {
			try {
				button.click();
			} catch (Exception e) {
				System.err.println("150-[handleProfissionalWarning] - Erro:\n"+
				e.getLocalizedMessage());
			}
		}
	}
	
	@Override
	public void handleCookieConsent(Page page) {
		Locator buttonDecline = page.locator(".sc-dcJsrY.fCNVay");
		if (buttonDecline.isVisible())
			buttonDecline.click();
	}
	
	@Override
	public List<Produto> scrap(String search) {
		return null;
	}
}
