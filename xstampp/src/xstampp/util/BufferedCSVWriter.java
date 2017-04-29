/*******************************************************************************
 * Copyright (c) 2013, 2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * a Writer Class which extends the functionality of the BufferedWriter to
 * enhance the function to write an csv file
 * 
 * @author Lukas Balzer
 * 
 */
public class BufferedCSVWriter extends BufferedWriter {
  private final char seperator;

  /**
   * 
   * @author Lukas Balzer
   * @param fileWriter
   *          the writer Object which shall be buffered
   * @param seperator
   *          the separator for the csv file
   */
  public BufferedCSVWriter(Writer fileWriter, char seperator) {
    super(fileWriter);
    this.seperator = seperator;
  }

  /**
   * writes a cell to the CSV file which ends with the separator character
   * 
   * @author Lukas Balzer
   * 
   * @param text
   *          the contents of the cell
   * @throws IOException
   *           if the text can not be written
   */
  public void writeCell(String text) throws IOException {
    if(text != null){
      write(text.replaceAll("\\n|[\\r]\\n|" + seperator, ""));
    }
    write(this.seperator);
  }

  /**
   * writes a cell to the CSV file which ends with the separator character
   * 
   * @author Lukas Balzer
   * 
   * @param nr
   *          the contents of the cell
   * @throws IOException
   *           if the text can not be written
   */
  public void writeCell(int nr) throws IOException {
    super.write(nr);
    super.write(this.seperator);
  }

  /**
   * writes a cell to the CSV file which ends with the separator character
   * 
   * @author Lukas Balzer
   * 
   * @throws IOException
   *           if the text can not be written
   */
  public void writeCell() throws IOException {
    super.write(this.seperator);
  }
}