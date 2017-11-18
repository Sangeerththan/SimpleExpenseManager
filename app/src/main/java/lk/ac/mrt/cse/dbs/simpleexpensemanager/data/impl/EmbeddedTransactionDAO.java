package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Sangeerththan on 11/18/2017.
 */


public class EmbeddedTransactionDAO implements TransactionDAO {

    //transaction table



    SQLiteDatabase db;

    public EmbeddedTransactionDAO(SQLiteDatabase db){

      this.db=db;
    }


    @Override
    //insert values into transaction table
    public void logTransaction(Date date_, String accountNo, ExpenseType expenseType_, double amount_){



            String insert_query = "INSERT INTO Account_Transaction (accountNo,expenseType,amount,date) VALUES (?,?,?,?)";
            SQLiteStatement statement = db.compileStatement(insert_query);

            statement.bindString(1,accountNo);
            statement.bindLong(2,(expenseType_ == ExpenseType.EXPENSE) ? 0 : 1);
            statement.bindDouble(3,amount_);
            statement.bindLong(4,date_.getTime());

            statement.executeInsert();



    }

    @Override
    //get all transactions
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactions = new ArrayList<>();

        String TRANSACTION_DETAIL_SELECT_QUERY = "SELECT * FROM Account_Transaction";
        Cursor cursor = db.rawQuery(TRANSACTION_DETAIL_SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Transaction trans=new Transaction(
                            new Date(cursor.getLong(cursor.getColumnIndex("date"))),
                            cursor.getString(cursor.getColumnIndex("accountNo")),
                            (cursor.getInt(cursor.getColumnIndex("expenseType")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                   cursor.getDouble(cursor.getColumnIndex("amount")));




                    transactions.add(trans);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }


        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        //int size =int numRows = DatabaseUtils.queryNumEntries( Table_1);;


        List<Transaction> transdetail = new ArrayList<>();

        String TRANS_DETAIL_SELECT_QUERY = "SELECT * FROM Account_Transaction LIMIT"+limit;


        Cursor cursor = db.rawQuery(TRANS_DETAIL_SELECT_QUERY, null);


            if (cursor.moveToFirst()) {
                do {
                    Transaction trans=new Transaction(
                            new Date(cursor.getLong(cursor.getColumnIndex("date"))),
                            cursor.getString(cursor.getColumnIndex("accountNo")),
                            (cursor.getInt(cursor.getColumnIndex("expenseType")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                            cursor.getDouble(cursor.getColumnIndex("amount")));


                    transdetail.add(trans);

                } while (cursor.moveToNext());
            }

        return  transdetail;
    }





}

