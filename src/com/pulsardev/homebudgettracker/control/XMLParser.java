/**
 * Parse XML string and convert into Object
 * @author ngapham
 * @date 12/7/05
 */

package com.pulsardev.homebudgettracker.control;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.pulsardev.homebudgettracker.model.ExpenseDateReport;

public class XMLParser {
	
	// static final strings: define XML tag
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
	 * @return
	 * @throws ParseException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @author ngapham
	 * @date 14/7/05
	 */
	public ArrayList<ExpenseDateReport> DOMParse(String xmlFilePath) throws ParseException, ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		ArrayList<ExpenseDateReport> dateReportList = new ArrayList<ExpenseDateReport>();
		
		builder = factory.newDocumentBuilder();
		InputStream in = new FileInputStream(xmlFilePath);
		Document doc = builder.parse(in);
		
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
}
