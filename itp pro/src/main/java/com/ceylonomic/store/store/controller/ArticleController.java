package com.ceylonomic.store.store.controller;

import java.io.IOException;
import java.security.Provider.Service;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ceylonomic.store.store.domain.Article;
import com.ceylonomic.store.store.domain.ArticleBuilder;
import com.ceylonomic.store.store.domain.Category;
import com.ceylonomic.store.store.domain.Size;
import com.ceylonomic.store.store.domain.pdfexporter;
import com.ceylonomic.store.store.service.ArticleService;
import com.lowagie.text.DocumentException;

@Controller
@RequestMapping("/article")
public class ArticleController {

	@Autowired
	private ArticleService articleService;
	
	@RequestMapping("/add")
	public String addArticle(Model model) {
		Article article = new Article();
		model.addAttribute("article", article);
		model.addAttribute("allSizes", articleService.getAllSizes());
		model.addAttribute("allCategories", articleService.getAllCategories());
		return "addArticle";
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public String addArticlePost(@ModelAttribute("article") Article article, HttpServletRequest request) {
		Article newArticle = new ArticleBuilder()
				.withTitle(article.getTitle())
				.stockAvailable(article.getStock())
				.withPrice(article.getPrice())
				.imageLink(article.getPicture())
				.sizesAvailable(Arrays.asList(request.getParameter("size").split("\\s*,\\s*")))
				.ofCategories(Arrays.asList(request.getParameter("category").split("\\s*,\\s*")))
				.build();		
		articleService.saveArticle(newArticle);	
		return "redirect:article-list";
	}
	
	@RequestMapping("/article-list")
	public String articleList(Model model) {
		List<Article> articles = articleService.findAllArticles();
		model.addAttribute("articles", articles);
		return "articleList";
	}
	
	@RequestMapping("/edit")
	public String editArticle(@RequestParam("id") Long id, Model model) {
		Article article = articleService.findArticleById(id);
		String preselectedSizes = "";
		for (Size size : article.getSizes()) {
			preselectedSizes += (size.getValue() + ",");
		}
		
		String preselectedCategories = "";
		for (Category category : article.getCategories()) {
			preselectedCategories += (category.getName() + ",");
		}		
		model.addAttribute("article", article);
		model.addAttribute("preselectedSizes", preselectedSizes);
		model.addAttribute("preselectedCategories", preselectedCategories);
		model.addAttribute("allSizes", articleService.getAllSizes());
		model.addAttribute("allCategories", articleService.getAllCategories());
		return "editArticle";
	}
	
	@RequestMapping(value="/edit", method=RequestMethod.POST)
	public String editArticlePost(@ModelAttribute("article") Article article, HttpServletRequest request) {		
		Article newArticle = new ArticleBuilder()
				.withTitle(article.getTitle())
				.stockAvailable(article.getStock())
				.withPrice(article.getPrice())
				.imageLink(article.getPicture())
				.sizesAvailable(Arrays.asList(request.getParameter("size").split("\\s*,\\s*")))
				.ofCategories(Arrays.asList(request.getParameter("category").split("\\s*,\\s*")))
				.build();
		newArticle.setId(article.getId());
		articleService.saveArticle(newArticle);	
		return "redirect:article-list";
	}
	
	@RequestMapping("/delete")
	public String deleteArticle(@RequestParam("id") Long id) {
		articleService.deleteArticleById(id);
		return "redirect:article-list";
	}
	
	
	@GetMapping("/export")
    public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=articles_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
         
        List<Article> articles = articleService.findAllArticles();
         
        pdfexporter exporter = new pdfexporter(articles);
        exporter.export(response);
         
    }
	
	
	@GetMapping("/barChart")
	public String findAllArticle(Model model) {	
		
	List<String> titleList= articleService.findAllArticles().stream().map(x->x.getTitle()).collect(Collectors.toList());
	List<Double> priceList = articleService.findAllArticles().stream().map(x-> x.getPrice()).collect(Collectors.toList());
	model.addAttribute("title", titleList);
	model.addAttribute("price", priceList);
	return "barChart";
	
	}
	
}
