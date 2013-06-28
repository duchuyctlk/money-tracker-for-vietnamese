package com.duchuyctlk.moneytracker.service;

import java.io.*;
import javax.microedition.rms.RecordStore;

import de.enough.polish.util.ArrayList;
import de.enough.polish.util.IntList;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

public class DatabaseService {

    private RecordStore rs = null;
    private IntList recordIDs = null; // integer list
    private boolean isOpen = false;
    private String REC_STORE = "";

    public DatabaseService(String name) {
        REC_STORE = name;
    }

    public void open() {
        try {
            // The second parameter indicates that the record store
            // should be created if it does not exist
            rs = RecordStore.openRecordStore(REC_STORE, true);
            isOpen = true;

            recordIDs = new IntList(); // int list

            // first time
            if (rs.getNumRecords() == 0) {
                String firstRecord = "1";
                byte[] rec = firstRecord.getBytes();
                rs.addRecord(rec, 0, rec.length);
            } else {
                // get meta record
                byte[] firstRecord = rs.getRecord(1);
                int len = rs.getRecordSize(1);

                // move to next record if this one has no data
                if (firstRecord == null) {
                    return;
                }

                // split current record into fields
                String str = new String(firstRecord, 0, len);
                String[] indexes = Utilities.Split(str, "|");

                for (int i = 1; i < indexes.length; i++) {
                    recordIDs.add(Integer.parseInt(indexes[i]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            StringBuffer dataBuffer = new StringBuffer("1");
            if (recordIDs.size() > 0) {
                dataBuffer.append("|");
            }

            for (int i = 0; i < recordIDs.size(); i++) {
                dataBuffer.append(recordIDs.get(i));
                if (i < recordIDs.size() - 1) {
                    dataBuffer.append("|");
                }
            }
            byte data[] = dataBuffer.toString().getBytes();

            // update meta record
            rs.setRecord(1, data, 0, data.length);

            rs.closeRecordStore();
            isOpen = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isOpen() {
        return isOpen;
    }

    /**
     *
     * @param str
     * @return: true if insert successfully
     */
    public boolean writeRecord(String str) {
        if (!isOpen) {
            return false;
        }
        byte[] rec = str.getBytes();
        try {
            int newID = rs.addRecord(rec, 0, rec.length);
            recordIDs.add(newID);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param params
     * @return null if close or list of string array
     */
    public ArrayList readRecords(String[] params) {
        if (!isOpen) {
            return null;
        }
        ArrayList result = new ArrayList();
        try {
            byte[] recData;
            int len;

            int size = recordIDs.size();
            for (int i = 0; i < size; i++) {
                int id = recordIDs.get(i);
                recData = rs.getRecord(id);
                len = rs.getRecordSize(id);

                // move to next record if this one has no data
                if (recData == null) {
                    continue;
                }

                // split current record into fields
                String str = new String(recData, 0, len);
                String[] fields = Utilities.Split(str, "|");

                // check if same data type
                if (fields == null || fields.length != params.length) {
                    continue;
                }

                boolean match = true;
                // check if match criteria
                for (int j = 0; j < params.length; j++) {
                    if (!params[j].equals("")) {
                        if (!fields[j].equals(params[j])) {
                            match = false;
                            break;
                        }
                    }
                }

                if (match) {
                    // add matched record to list
                    result.add(fields);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *
     * @param params
     * @return -1 if not open or number of affected records
     */
    public int updateRecord(String[] params, String value) {
        if (!isOpen) {
            return -1;
        }
        int result = 0;
        try {
            byte[] recData;
            int len;
            int size = recordIDs.size();
            for (int i = 1; i <= size; i++) {
                int id = recordIDs.get(i);
                recData = rs.getRecord(id);
                len = rs.getRecordSize(id);

                // move to next record if this one has no data
                if (recData == null) {
                    continue;
                }

                // split current record into fields
                String str = new String(recData, 0, len);
                String[] fields = Utilities.Split(str, "|");

                // check if same data type
                if (fields == null || fields.length != params.length) {
                    continue;
                }

                // check if match criteria
                boolean match = true;
                for (int j = 0; j < params.length; j++) {
                    if (!params[j].equals("")) {
                        if (!fields[j].equals(params[j])) {
                            match = false;
                            break;
                        }
                    }
                }

                if (match) {
                    // change matched records
                    byte[] newValue = value.getBytes();
                    rs.setRecord(id, newValue, 0, newValue.length);
                    result++;
                }
            }
        } catch (Exception e) {
            e.toString();
        }
        return result;
    }

    /**
     *
     * @param params
     * @return -1 if not open or number of deleted records
     */
    public int deleteRecord(String[] params) {
        if (!isOpen) {
            return -1;
        }
        int result = 0;
        try {
            byte[] recData;
            int len;
            int i = 0;
            while (i < recordIDs.size()) {
                int id = recordIDs.get(i);
                recData = rs.getRecord(id);
                len = rs.getRecordSize(id);

                // move to next record if this one has no data
                if (recData == null) {
                    i++;
                    continue;
                }

                // split current record into fields
                String str = new String(recData, 0, len);
                String[] fields = Utilities.Split(str, "|");

                // check if same data type
                if (fields == null || fields.length != params.length) {
                    i++;
                    continue;
                }

                // check if match criteria
                boolean match = true;
                for (int j = 0; j < params.length; j++) {
                    if (!params[j].equals("")) {
                        if (!fields[j].equals(params[j])) {
                            match = false;
                            break;
                        }
                    }
                }

                if (match) {
                    // delete matched records
                    rs.deleteRecord(id);
                    recordIDs.removeElementAt(i);
                    result++;
                } else {
                    i++;
                }
            }
        } catch (Exception e) {
            e.toString();
        }
        return result;
    }
}
