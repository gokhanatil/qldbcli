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

import com.amazon.ion.IonStruct;
import com.amazon.ion.IonValue;
import software.amazon.qldb.QldbSession;
import software.amazon.qldb.Result;


import java.util.ArrayList;
import java.util.List;

public abstract class Executor {

    public static void runQuery(QldbSession mySession, String cmd, Boolean ionMode) {
        List<IonStruct> documentList = new ArrayList<>();

        try {

            Result result = mySession.execute(cmd);
            List<String> tableColumns = new ArrayList<>();

            for (IonValue row : result) {
                if (row.getClass().toString().contains("IonStruct")) {
                    for (IonValue column : (IonStruct) row)
                        if (!tableColumns.contains(column.getFieldName()))
                            tableColumns.add(column.getFieldName());
                    documentList.add((IonStruct) row);
                } else
                    System.out.println(row);
            }

            if (! ionMode) {
                writeCSV(tableColumns);

                for (IonStruct doc : documentList) {
                    StringBuilder outputLine = new StringBuilder();
                    for (String columnName : tableColumns) {
                        if (doc.get(columnName) == null) {
                            outputLine.append(Constants.DELIMITER);
                        } else
                            outputLine.append(doc.get(columnName) + Constants.DELIMITER);
                    }
                    System.out.println(outputLine.toString().replaceAll(",$", ""));
                }
            }
            else {
                for (IonStruct doc : documentList) {
                    writeIon(doc);
                }
            }

        } catch (Exception e) {
            System.out.println(Constants.ERROR + e.getMessage());
        }
    }

    private static void writeIon(IonStruct doc) {
        System.out.println(doc.toPrettyString());
    }

    private static void writeCSV(List<String> tempList) {
        StringBuilder tempCSV = new StringBuilder();
        for (String temp : tempList)
            tempCSV.append(temp + Constants.DELIMITER);
        System.out.println(tempCSV.toString().replaceAll(",$", ""));
    }
}
