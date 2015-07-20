/**
 * Parse XML string and convert into Object
 * @author ngapham
 * @date 12/7/05
 */

package com.pulsardev.homebudgettracker.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.pulsardev.homebudgettracker.model.ExpenseDateReport;

public class XMLParser {
	
	// define XML tag of ExpenseDateReport object
	private static final String EXPENSE_DATE_ITEM = "item";
	private static final String EXPENSE_DATE_ID = "id";
	private static final String EXPENSE_DATE_DATE = "date";
	private static final String EXPENSE_DATE_AMOUNT = "amount";
	private static final String EXPENSE_DATE_CATEGORYID = "categoryId";
	private static final String EXPENSE_DATE_DESCRIPTION = "description";
	
	// static final strings: define patterns
	private static final String DATE_TIME_PATTERN = "EEE, LLL dd, yyyy";
	
	/**
	 * 
	 * @param xmlFilePath
	 * @return List of ExpenseDateReport in database
	 * @throws ParseException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @author ngapham
	 * @date 14/7/05
	 */
	public ArrayList<ExpenseDateReport> DOMParse(InputStream xmlFileIS) throws ParseException, ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		ArrayList<ExpenseDateReport> dateReportList = new ArrayList<ExpenseDateReport>();
		
		builder = factory.newDocumentBuilder();
		Document doc = builder.parse(xmlFileIS);
		
		// Start parsing
		Element root = doc.getDocumentElement();	// get the root, which is <expense_date>
		NodeList nodeList = root.getChildNodes();	// return all child nodes of root
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node instanceof Element) {			// Check if this node is Element type
				Element item = (Element) node;		// parse each <item>
				
				// get id of <item>
				String id = item.getAttribute(EXPENSE_DATE_ID);	
				UUID uuid = UUID.fromString(id);
				
				// get date of <item>
				NodeList listChild = item.getElementsByTagName(EXPENSE_DATE_DATE);	
				String dateString = listChild.item(0).getTextContent();
				SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_PATTERN);
				Date date = format.parse(dateString);
				
				// get amount of <item>
				listChild = item.getElementsByTagName(EXPENSE_DATE_AMOUNT);
				Float amount = Float.valueOf(listChild.item(0).getTextContent());
				
				// get category id of <item>
				listChild = item.getElementsByTagName(EXPENSE_DATE_CATEGORYID);
				int categoryId = Integer.valueOf(listChild.item(0).getTextContent());
				
				// get description of <item>
				listChild = item.getElementsByTagName(EXPENSE_DATE_DESCRIPTION);
				String descString = listChild.item(0).getTextContent();
				String description = new String(descString.getBytes("ISO-8859-1"), "UTF-8");
				
				// Create ExpenseDateReport Object with those attributes
				ExpenseDateReport mDateReport = new ExpenseDateReport(uuid, date, amount, categoryId, description);
				// And add to dateReportList
				dateReportList.add(mDateReport);
			}
		}
		return dateReportList;
	}
	
	/**
	 * Save Expense Date Report whenever adding new report (by date)
	 * @param xmlFilePath, currentDateReport
	 * @author ngapham
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws TransformerException 
	 * @date 20/7/2015
	 */
	public void saveExpDateReport(InputStream xmlFileIS, ExpenseDateReport currentDateReport) throws ParserConfigurationException, SAXException, IOException, TransformerException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		
		builder = factory.newDocumentBuilder();
		Document doc = builder.parse(xmlFileIS);
		
		// get the root element, which is <expense_date>
		Node expense_date = doc.getFirstChild();
		// add new item
		Element newItem = doc.createElement(EXPENSE_DATE_ITEM);
		// create each child node of new item from currentDateReport
		// and add all child node to newItem
		Element newItemId = doc.createElement(EXPENSE_DATE_ID);
		newItemId.appendChild(doc.createTextNode(String.valueOf(currentDateReport.getID())));
		newItem.appendChild(newItemId);
		
		Element newItemDate = doc.createElement(EXPENSE_DATE_DATE);
		newItemDate.appendChild(doc.createTextNode(String.valueOf(currentDateReport.getDate())));
		newItem.appendChild(newItemDate);
		
		Element newItemAmount = doc.createElement(EXPENSE_DATE_AMOUNT);
		newItemAmount.appendChild(doc.createTextNode(String.valueOf(currentDateReport.getAmount())));
		newItem.appendChild(newItemAmount);
		
		Element newItemCategoryId = doc.createElement(EXPENSE_DATE_CATEGORYID);
		newItemCategoryId.appendChild(doc.createTextNode(String.valueOf(currentDateReport)));
		newItem.appendChild(newItemCategoryId);
		
		Element newItemDescription = doc.createElement(EXPENSE_DATE_DESCRIPTION);
		newItemDescription.appendChild(doc.createTextNode(String.valueOf(currentDateReport.getDescription())));
		newItem.appendChild(newItemDescription);
		
		// add newItem to root
		expense_date.appendChild(newItem);
		
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		
		OutputStream o
		StreamResult result = new StreamResult(outputStream)
		transformer.transform(source, result);
	}
	
}
