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
import com.amazonaws.services.qldbsession.AmazonQLDBSessionClientBuilder;
import software.amazon.qldb.PooledQldbDriver;
import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    static String ledgerName = null;

    private static void exitWithMessage(String message, int exitCode) {
        System.out.println(message);
        System.exit(exitCode);
    }

    private static String waitCommand() {
        Scanner sc = new Scanner(System.in);
        System.out.print(("PartiQL (" + ledgerName + ") > "));
        return sc.nextLine();
    }

    private static QldbSession getQldbSession(String inputName) {
        AmazonQLDBSessionClientBuilder builder = AmazonQLDBSessionClientBuilder.standard();
        QldbSession temp = null;
        try {
            temp = PooledQldbDriver.builder()
                    .withLedger(inputName)
                    .withRetryLimit(3)
                    .withSessionClientBuilder(builder)
                    .build().getSession();
            ledgerName = inputName;
        } catch (Exception e) {
            // This is our first connection attempt so let's show the header before the error message
            if (ledgerName != null) System.out.println(Constants.QLDB_HEADER);
            exitWithMessage(Constants.ERROR + e.getMessage(), 2);
        }
        return temp;
    }


    public static void main(String[] args) throws ParseException, IOException {

        CLIParameters myParameters = new CLIParameters(args);

        QldbSession mySession = getQldbSession(myParameters.getLedgerName());

        if (myParameters.getRunQuery() != null) {
            Executor.runQuery(mySession, myParameters.getRunQuery(), myParameters.getIonMode());
            System.exit(0);
        }

        System.out.println(Constants.QLDB_HEADER);
        if (myParameters.getTargetTable() != null)
            exitWithMessage(String.format(Constants.RECORDS_INSERTED_TO_THE_TABLE, ImportData.importCSV(mySession, myParameters), myParameters.getTargetTable()), 0);


        String cmd;

        while (true) {

            cmd = waitCommand();

            if (cmd.equalsIgnoreCase("QUIT")) {
                exitWithMessage("Bye!", 0);
            }

            if (cmd.toUpperCase().contains("CONN ")) {
                String newLedgerName = cmd.split("(?i)CONN ")[1];
                mySession = getQldbSession(newLedgerName);
                continue;
            }

            if (!cmd.isEmpty()) Executor.runQuery(mySession, cmd, myParameters.getIonMode());
        }
    }


}
