/*
 * MIT License
 *
 * Copyright (c) 2019 Gokhan Atil (https://gokhanatil.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.gokhanatil.qldbcli;

import software.amazon.qldb.QldbSession;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;

public abstract class ImportData {

    public static int importCSV(QldbSession mySession, CLIParameters params) throws IOException {

        int numberOfRecords = 0;

        FileReader myFile = new FileReader( params.getImportFile() );
        CSVReader csvReader = new CSVReader(myFile);

        String[] columnNames = csvReader.readNext();

        StringBuilder bufferJSON = new StringBuilder();

        String[] nextRecord;
        int numberOfDocs = 0;
        while ( (nextRecord = csvReader.readNext()) != null ) {

            StringBuilder newJSON = new StringBuilder(" { ");
            int i = 0;
            for (String col : nextRecord) {

                newJSON.append("'" + columnNames[i++] + "': ").append( "'" + col.replace("'", "''" ).trim() + "',");

            }

            if ( bufferJSON.length() > 0 ) bufferJSON.append(",");
            bufferJSON.append(newJSON.toString().replaceAll(",$", "}"));

            if (bufferJSON.length() > params.getBufferSize() || ( ++numberOfDocs == 40 ) ) {
                Executor.runQuery( mySession, Constants.INSERT_INTO + params.getTargetTable() + " <<" + bufferJSON + " >>", false);
                numberOfDocs = 0;
                bufferJSON.delete(0, bufferJSON.length() );
            }

            numberOfRecords++;

        }

        // flush the buffer
        if (bufferJSON.length() > 0 )
            Executor.runQuery(mySession, Constants.INSERT_INTO + params.getTargetTable() + " <<" + bufferJSON + " >>", false);

        return numberOfRecords;

    }
}
