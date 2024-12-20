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
import com.tradingtols.br.scraper.model.entity.categorias.ProdutoCategoria;
import com.tradingtols.br.scraper.model.repository.ProdutoRepository;
import com.tradingtols.br.scraper.model.tools.StringToProdutoCategoriaFlagConverter;
import com.tradingtols.br.scraper.service.Companias;

@Service
public class ScraperHenryShein extends ScraperClazz
{
	private static final String RESULTS_GRID = ".dfd-results-grid";
//	private static final String URL = "https://dentalexpress.pt/#df08/fullscreen/m=and";
	private static final String URL = "https://henryschein.pt/dental/#6f59/fullscreen/m=and";

	public ScraperHenryShein(ProdutoRepository repo) {super(repo);}

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
			System.out.println("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			System.out.println("43-[ScraperHenryShein] - search = " + search);
		
			String searchURL = String.format("%s%s%s", URL,"&q=",search);
			page.navigate(searchURL);
			
			try {
				Locator searchBox = page.locator("#dfd-searchbox-id-zyF4E-input");
				if(searchBox.isVisible()) searchBox.fill(search);
				try {
					page.waitForSelector(RESULTS_GRID);
				} catch (Exception e) {
					// TODO: handle exception
				}
				page.waitForLoadState();
				
				boolean hasMoreContent = true;
				int previousItemCount = 0;
				
				do {
					Locator resultContainer = page.locator(RESULTS_GRID);
					if (!resultContainer.isVisible()) return;
	
					resultContainer.evaluate("el => el.scrollIntoView({ block: 'end', behavior: 'smooth' })");
					page.waitForTimeout(2000);
	
					Locator cards = resultContainer.locator(".dfd-card.dfd-card-preset-product.dfd-card-type-pro_dept");
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

			String imgSrc = "", desc = "", price = "", href = "", externalId = "";
			
			externalId = splitTextoByLastChar(cards.nth(i).getAttribute("dfd-value-dfid"), '@');
			href = cards.nth(i).getAttribute("dfd-value-link");
			
			Locator img = cards.nth(i).locator(".dfd-card-media > div > img");
			if(img.isVisible()) {
				imgSrc = img.getAttribute("src");
			}

			Locator divTitle = cards.nth(i).locator(".dfd-card-content.dfd-card-flex > .dfd-card-title ");
			if(divTitle.isVisible()) {
				desc = divTitle.innerText();
				Locator priceContainer = cards.nth(i).locator(".dfd-card-pricing");
				Locator oldPrice = priceContainer.locator(".dfd-card-price");
				Locator newPrice = priceContainer.locator(".dfd-card-price.dfd-card-price--sale");
				if(newPrice.isVisible()) {
					price = newPrice.innerText();
				} else {
					price = oldPrice.first().innerText();
				}
				
			}
			var produto = new ProdutoHenrySchein(0L, Companias.HENRY_SCHEIN.getNome(), desc, externalId, href, price, imgSrc, "", new Date() );
			List<ProdutoCategoria> categorias = extract (desc);
			produto.setCategorias(categorias);
			repo.save(produto);
		}
		
	}

	@Override
	public	void handleProfisionalWarning(Page page) {
		System.out.println("...handleProfissionalWarning...");
		ElementHandle checkbox = page.querySelector(".custom-control-input.check-confirm-professional");
		if(checkbox!=null && checkbox.isVisible()) {
			try {
				checkbox.click();
				Locator button = page.locator(".btn.btn-primary.action-confirm-professional");
				if (button.isVisible()) {
					button.click();
				}
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
