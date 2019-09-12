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

import org.apache.commons.cli.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class CLIParameters {

    private String ledgerName;
    private String importFile;
    private Integer bufferSize = 0;
    private String targetTable;
    private String runQuery;

    CLIParameters(String[] args) throws ParseException {

        Options options = new Options();

        Option ledgerParameter = new Option("l", "ledger", true, Constants.THE_LEDGER_NAME_TO_CONNECT);
        ledgerParameter.setRequired(true);
        options.addOption(ledgerParameter);

        Option verboseMode = new Option("v", "verbose", false, Constants.ENABLE_VERBOSE_MODE);
        verboseMode.setRequired(false);
        options.addOption(verboseMode);

        Option queryRun = new Option("q", "query", true, Constants.RUN_A_QUERY_SILENTLY_AND_EXIT);
        queryRun.setRequired(false);
        options.addOption(queryRun);

        Option tableName = new Option("t", "table", true, Constants.THE_TABLE_NAME_TO_IMPORT_DATA);
        tableName.setRequired(false);
        options.addOption(tableName);

        Option fileName = new Option("f", "file", true, Constants.THE_SOURCE_FILE_CSV_TO_IMPORT_DATA);
        fileName.setRequired(false);
        options.addOption(fileName);

        Option bufferParam = new Option("b", "buffer", true, Constants.BUFFER_SIZE_WHEN_IMPORTING_DATA);
        bufferParam.setRequired(false);
        options.addOption(bufferParam);

        CommandLineParser cmdParser = new DefaultParser();
        HelpFormatter helpFormatter = new HelpFormatter();
        CommandLine cmdLine = null;

        try {
            cmdLine = cmdParser.parse(options, args);

            if (!cmdLine.getArgList().isEmpty())
                throw new ParseException(Constants.UNRECOGNIZED_PARAMETERS + cmdLine.getArgList());

            if(cmdLine.hasOption("query") && cmdLine.hasOption("table"))
                throw new ParseException(Constants.QUERY_AND_TABLE_OPTIONS_CAN_NOT_BE_USED_TOGETHER);

            if(cmdLine.hasOption("query") && cmdLine.hasOption("file"))
                throw new ParseException(Constants.QUERY_AND_FILE_OPTIONS_CAN_NOT_BE_USED_TOGETHER);

            if(cmdLine.hasOption("table") && !cmdLine.hasOption("file"))
                throw new ParseException(Constants.YOU_SHOULD_SPECIFY_THE_FILENAME_TO_BE_ABLE_TO_IMPORT_DATA);

        } catch (ParseException e) {

            System.out.println(Constants.QLDB_HEADER);
            System.out.println("ERROR: " + e.getMessage());
            helpFormatter.printHelp(Constants.HELP_MESSAGE, options);
            System.exit(1);
        }

        setLedgerName(cmdLine.getParsedOptionValue("ledger").toString());

        if (cmdLine.hasOption("query")) setRunQuery( cmdLine.getParsedOptionValue("query").toString());
        if (cmdLine.hasOption("table")) setTargetTable( cmdLine.getParsedOptionValue("table").toString());
        if (cmdLine.hasOption("file")) setImportFile( cmdLine.getParsedOptionValue("file").toString());
        if (cmdLine.hasOption("buffer")) setBufferSize( Integer.valueOf( cmdLine.getParsedOptionValue("buffer").toString()));

        BasicConfigurator.configure();
        if (cmdLine.hasOption("verbose")) Logger.getRootLogger().setLevel(Level.DEBUG);
        else Logger.getRootLogger().setLevel(Level.OFF);


    }


    public String getImportFile() {
        return importFile;
    }

    public void setImportFile(String importFile) {
        this.importFile = importFile;
    }

    public Integer getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(Integer bufferSize) {
        this.bufferSize = bufferSize;
    }

    public String getTargetTable() {
        return targetTable;
    }

    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    public String getRunQuery() {
        return runQuery;
    }

    public void setRunQuery(String runQuery) {
        this.runQuery = runQuery;
    }

    public String getLedgerName() {
        return ledgerName;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName;
    }
}
