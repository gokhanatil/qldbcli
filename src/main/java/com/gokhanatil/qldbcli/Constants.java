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

public abstract class Constants {

    public static final String DELIMITER = ",";

    public static final String HELP_MESSAGE = "qldbcli -l ledgername [-q query] [-v] [-no] [-t tablename -f filename -b size ]";
    public static final String THE_LEDGER_NAME_TO_CONNECT = "the ledger name to connect";
    public static final String ENABLE_VERBOSE_MODE = "enable verbose mode";
    public static final String ENABLE_ION_MODE = "enable Ion output mode";
    public static final String RUN_A_QUERY_SILENTLY_AND_EXIT = "run a query (silently) and exit";
    public static final String THE_TABLE_NAME_TO_IMPORT_DATA = "the table name to import data";
    public static final String THE_SOURCE_FILE_CSV_TO_IMPORT_DATA = "the source file (csv) to import data";
    public static final String BUFFER_SIZE_WHEN_IMPORTING_DATA = "buffer size when importing data";
    public static final String REMOVE_THE_1000_ROWS_LIMIT = "remove the 1000 rows limit";
    public static final String UNRECOGNIZED_PARAMETERS = "Unrecognized parameters: ";
    public static final String QUERY_AND_TABLE_OPTIONS_CAN_NOT_BE_USED_TOGETHER = "Query and table options can not be used together.";
    public static final String QUERY_AND_FILE_OPTIONS_CAN_NOT_BE_USED_TOGETHER = "Query and file options can not be used together.";
    public static final String YOU_SHOULD_SPECIFY_THE_FILENAME_TO_BE_ABLE_TO_IMPORT_DATA = "You should specify the filename to be able to import data.";
    public static final String ERROR = "ERROR: ";
    public static final String RECORDS_INSERTED_TO_THE_TABLE = "%d records inserted to the table '%s'.";
    public static final String INSERT_INTO = "INSERT INTO ";
    public static final String QLDB_HEADER =
            "----------------------------------------------------------\n" +
            "QLDB \"the missing\" Command Line Client v0.2 by Gokhan Atil\n" +
            "----------------------------------------------------------";
}
