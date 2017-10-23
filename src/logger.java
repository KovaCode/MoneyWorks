import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class logger {

	// private static String filepath = "library/logg/logg.xml";
	private static String filepath;
	static Node nodeRoot;
	static Node nodeOperater;
	static Node nodeDatum;
	static Node nodeLog;

	static Boolean chkOper = false;
	static Boolean chkDatum = false;

	// ******************privremeno******************//
	static int ID = 0;
	static String sOperater = "SYSTEM";
	static String sPoruka;

	private static String filepathErr;
	static String sPorukaErr;

	// **********************************************//

	public logger(String sOpis) {

	}

	public static void logg(String Opis) {
		sPoruka = "(" + DEF.getTime() + ") - " + Opis;

		if (login.getFlgProfile() == false) {
			sOperater = "0";
			filepath = "logg/";
		} else {

			if (DEF.getgUserID() == 0) {
				sOperater = "SYSTEM";
			} else {
				sOperater = RSet.getOsoba(DEF.getgUserID()) + "_"
						+ DEF.getgUserID();
			}

			filepath = DEF.getgLoggPath();

		}

		try {
			File file = new File(filepath);
			boolean success = file.createNewFile();
			if (success) {
				System.out.println();
				Instantiate(filepath);
			}
		} catch (IOException e) {
			// logger.loggErr("logger " + e.getMessage());
		}

		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			System.out.println("Logger filepath: " + filepath);
			Document doc = docBuilder.parse(filepath);

			// root node

			nodeRoot = doc.getFirstChild();

			// ************************************************//
			// ** napraviti provjeru root node-a **//
			// ************************************************//

			// // provjera node-a operatera...
			// chkOper = false;
			// for (int i = 1; i < nodeRoot.getChildNodes().getLength(); i=i+2)
			// {
			// if
			// (nodeRoot.getChildNodes().item(i).getNodeName().equalsIgnoreCase(sOperater))
			// {
			// ID = i/2;
			// nodeOperater = doc.getElementsByTagName(sOperater).item(0); //
			// gOsoba vidjeti koji item?
			// chkOper = true;
			// break;
			//
			// }else{
			// chkOper = false;
			// }
			// }
			//
			// if (chkOper == false) {
			// // System.out.println(sOperater);
			// nodeOperater = doc.createElement(sOperater);
			// nodeRoot.appendChild(nodeOperater);
			// }

			// provjera node-a datuma...
			chkDatum = false;
			for (int i = 0; i < nodeRoot.getChildNodes().getLength(); i++) {
				if (nodeRoot.getChildNodes().item(i).getNodeName()
						.equalsIgnoreCase("_" + DEF.getDate())) {

					nodeDatum = doc.getElementsByTagName("_" + DEF.getDate())
							.item(ID);

					chkDatum = true;
					break;
				} else {
					chkDatum = false;
				}
			}

			if (chkDatum == false) {
				nodeDatum = doc.createElement("_" + DEF.getDate());
				nodeRoot.appendChild(nodeDatum);
			}

			// kreiranje logg-ova
			nodeLog = doc.createElement("_" + DEF.getTimeLogg());
			nodeLog.appendChild(doc.createTextNode(sPoruka));
			nodeDatum.appendChild(nodeLog);

			/** dio koji napravi fancy xml formu **/
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
					"yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
			// logger.loggErr("logger " + pce.getMessage());
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
			// logger.loggErr("logger " + tfe.getMessage());
		} catch (IOException ioe) {
			ioe.printStackTrace();
			// logger.loggErr("logger " + ioe.getMessage());
		} catch (SAXException sae) {
			sae.printStackTrace();
			// logger.loggErr("logger " + sae.getMessage());
		}
	}

	public static void loggErr(String Opis) {
		// sPoruka="(" + DEF.getTime()+ ") - " + Opis;
		// filepath = DEF.getgLoggErrPath();
		// System.out.println("err-1");
		// System.out.println("file: " + filepath);
		//
		// try {
		// File file = new File(filepath);
		// boolean success = file.createNewFile();
		// if (success) {
		// Instantiate(filepath);
		// }
		// } catch (IOException e) {
		// logger.loggErr("logger " + e.getMessage());
		// }
		//
		//
		//
		// try {
		// DocumentBuilderFactory docFactory =
		// DocumentBuilderFactory.newInstance();
		// DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		// Document doc = docBuilder.parse(filepath);
		//
		// //root node
		//
		// nodeRoot = doc.getFirstChild();
		//
		//
		//
		//
		// //provjera node-a datuma...
		// chkDatum = false;
		// for (int i = 0; i < nodeRoot.getChildNodes().getLength(); i++) {
		// if
		// (nodeRoot.getChildNodes().item(i).getNodeName().equalsIgnoreCase("_"
		// + DEF.getDate())) {
		//
		// nodeDatum = doc.getElementsByTagName("_" + DEF.getDate()).item(ID);
		//
		// chkDatum = true;
		// break;
		// }else{
		// chkDatum = false;
		// }
		// }
		//
		// if (chkDatum == false) {
		// nodeDatum = doc.createElement("_" + DEF.getDate());
		// nodeRoot.appendChild(nodeDatum);
		// }
		//
		//
		// //kreiranje logg-ova
		// nodeLog = doc.createElement("_" + DEF.getTimeLogg());
		// nodeLog.appendChild(doc.createTextNode(sPoruka));
		// nodeDatum.appendChild(nodeLog);
		//
		//
		// /** dio koji napravi fancy xml formu**/
		// TransformerFactory transformerFactory =
		// TransformerFactory.newInstance();
		// Transformer transformer = transformerFactory.newTransformer();
		// DOMSource source = new DOMSource(doc);
		// StreamResult result = new StreamResult(new File(filepath));
		// transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"yes");
		// transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		// transformer.transform(source, result);
		//
		// } catch (ParserConfigurationException pce) {
		// pce.printStackTrace();
		// logger.loggErr("logger " + pce.getMessage());
		// } catch (TransformerException tfe) {
		// tfe.printStackTrace();
		// logger.loggErr("logger " + tfe.getMessage());
		// } catch (IOException ioe) {
		// ioe.printStackTrace();
		// // logger.loggErr("logger " + ioe.getMessage());
		// } catch (SAXException sae) {
		// sae.printStackTrace();
		// logger.loggErr("logger " + sae.getMessage());
		// }
	}

	/**
	 * Instantiate - metoda koja kreira XML file i dodaje root node (razmisliti)
	 **/
	private static void Instantiate(String path) {
		try {

			// DocumentBuilderFactory factory =
			// DocumentBuilderFactory.newInstance();
			// DocumentBuilder loader = factory.newDocumentBuilder();
			// Document document = loader.newDocument();
			//
			// Element root = document.createElement("LOGG");
			// document.appendChild(root);

			Document doc = createDomDocument();

			// Insert the root element node
			Element element = doc.createElement("LOGG");
			doc.appendChild(element);

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(path));

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			System.out.println(source);
			System.out.println(result);

			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
					"yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);

		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}

	}

	public static Document createDomDocument() {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.newDocument();
			return doc;
		} catch (ParserConfigurationException e) {
		}
		return null;
	}

}
