import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Generator {
	private String xmlFile;
	private String outputPath;
	
	public Generator(String xml, String output)
	{
		xmlFile = xml;
		outputPath = output;
		
	}
	
	public void StartGenerationFromXML()
	{
		// Initialisation de la structure qui contiendra le résultat final
		List<String> res = new ArrayList<String>();
		ResetList(res);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			// On récupère le XML dans le Document (utilisation de Dom)
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(xmlFile));
			
			Element racine = document.getDocumentElement();
			NodeList racineNoeuds = racine.getChildNodes();
			int nbNoeuds = racineNoeuds.getLength();
			// Traitement class par class (les tags du XML)
			for(int i = 0;i<nbNoeuds;i++)
			{
				if(racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE)
				{
					Element node = (Element) racineNoeuds.item(i);
					// début de la récupération du code, chaque
					//composant va écrire dans "res" (c'est pourquoi on le passe en paramètre,
					//il serait possible d'en faire une variable statique à la place)
					new ClassComponent(node).getCode(res);
				}
				// Affichage dans la console, possibilité d'écrire dans un fichier à la place
				for(int j = 0; j<res.size();j++)
				{
					System.out.println(res.get(j));
				}
				ResetList(res);
			}
		}
		catch (final ParserConfigurationException e) 
		{
		  e.printStackTrace();
		}
		catch (final SAXException e) 
		{
		  e.printStackTrace();
		}
		catch (final IOException e) 
		{
		  e.printStackTrace();
		}
	}
	// Reset (ou initialise) la liste après le traitement de chaque classe
	private void ResetList(List<String> r)
	{
		r.clear();
		r.add("");
		r.add("");
		r.add("");
		r.add("");
		r.add("");
	}
	

}
