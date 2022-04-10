package com.ceylonomic.store.store.domain;

import java.awt.Color;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
 
 
public class pdfexporter {
    private List<Article> articles;
     
    public pdfexporter(List<Article> articles) {
        this.articles = articles;
    }
 
    private void writeTableHeader(PdfPTable table) {
    	PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.GREEN);
        cell.setPadding(3);
         
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);
         
        cell.setPhrase(new Phrase("Product ID", font));
         
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Product Name", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Product Price", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("stock Level", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Product Image", font));
        table.addCell(cell);
    }
     
    private void writeTableData(PdfPTable table) {
    	for (Article articles :articles ){
    		
    		table.addCell(String.valueOf(articles.getId()));
    		table.addCell(articles.getTitle());
    		table.addCell(String.valueOf(articles.getPrice()));
    		table.addCell(String.valueOf(articles.getStock()));
    		table.addCell(articles.getPicture());
    		
    	}
        
    }
     
    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
         
        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(10);
        font.setColor(Color.BLACK);
         
        Paragraph p = new Paragraph("List of all products", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
         
        document.add(p);
         
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 3.5f, 3.0f, 3.0f, 1.5f});
        table.setSpacingBefore(10);
         
        writeTableHeader(table);
        writeTableData(table);
         
        document.add(table);
         
        document.close();
         
    }
}
