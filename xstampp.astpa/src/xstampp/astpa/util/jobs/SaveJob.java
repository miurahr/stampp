/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.util.jobs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Observable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang3.StringEscapeUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import xstampp.astpa.Messages;
import xstampp.astpa.haz.HAZController;
import xstampp.astpa.haz.IHAZModel;
import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;
import xstampp.util.XstamppJob;

import com.sun.xml.bind.marshaller.CharacterEscapeHandler;
import com.sun.xml.bind.marshaller.DataWriter;

/**
 * a runtime job which is stores a DataModel in a given File
 * 
 * @author Lukas Balzer
 * @since 2.0
 * 
 */
public class SaveJob extends XstamppJob {

	final File file;
	final IDataModel controller;
	private boolean compatibilityMode;
	private boolean ready = false;

	/**
	 * 
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param file
	 *            the file in which the job should store the results
	 * @param controller
	 *            the Data model which should be stored, this must be a
	 *            JAXBContext
	 */
	public SaveJob(File file, IDataModel controller) {
		super(Messages.saveHaz);
		this.compatibilityMode = false;
		this.file = file;
		this.controller = controller;
		
	}
	

	@Override
	protected Observable getModelObserver() {
		return (Observable) controller;
	}
	
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask(Messages.savingHaz, IProgressMonitor.UNKNOWN);
		JAXBContext context;
		File backupDir = new File(file.getParentFile()+File.separator+".metadata"+File.separator+".backup");
		if(!backupDir.isDirectory()){
			backupDir.mkdirs();
		}
		File tmpFile = new File(backupDir,"."+file.getName());
		try {

				tmpFile.createNewFile();
				Object haz;
				//if the user wants to use the old haz format, than a new controller is created 
				//containing the old data model
				if(tmpFile.getName().endsWith("haz")){
					haz = new HAZController((IHAZModel) this.controller);
				}else{
					haz = this.controller;
				}
				context = JAXBContext.newInstance(haz.getClass());
				
				Marshaller m = context.createMarshaller();
				
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				// set the Jaxb encoding format to set a more powerful encoding mechanism than the
				// Standard UTF-8
				m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
				
				FileOutputStream  writer = new FileOutputStream(file);
				
				m.marshal(haz,writer);
//				if(this.compatibilityMode){
//					m.marshal(haz,writer);
//				}
//				else{
//					PrintWriter printWriter = new PrintWriter(writer);
//					DataWriter dataWriter = new DataWriter(printWriter, "Unicode", new MyEscapeHandler());
//					m.marshal(haz,dataWriter);
//					printWriter.close();
//					
//				}
				writer.close();
//				copy(tmpFile, file);
		} catch (Exception e) {
			e.printStackTrace();
			setError(e);
			ProjectManager.getLOGGER().error(e.getMessage(), e);
			return Status.CANCEL_STATUS;
		}
		
		return Status.OK_STATUS;
	}

	public void setCompabillityMode(boolean compatibilityMode) {
		this.compatibilityMode = compatibilityMode;
		this.ready=true;
	}
	public boolean isReady() {
		return this.ready;
	}
	private void copy(File fromFile, File toFile) throws IOException{
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(toFile));
			BufferedReader reader = new BufferedReader(new FileReader(fromFile));
			String line = reader.readLine();
			while(line != null){
				writer.write(line);
				writer.newLine();
				line = reader.readLine();
			}
			writer.close();
			reader.close();
		}catch(IOException e){
			throw e;
		}
	}
	
}

class MyEscapeHandler implements CharacterEscapeHandler{

	@Override
	public void escape(char[] ch, int start, int length, boolean isAttVal, Writer out) throws IOException {

	    String string = new String(ch);
	    String sub = string.substring(start, start+length);
	    int limit = start + length;
	    
	    sub = StringEscapeUtils.ESCAPE_HTML4.translate(sub);
	    if (start != limit) {
	      out.write(sub);
	    }
	  
	}
	
}
