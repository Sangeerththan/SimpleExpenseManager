package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

/**
 * Created by Sangerththan on 11/18/2017.
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;


public class EmbeddedAccountDAO implements AccountDAO {


    SQLiteDatabase db;
   /* public EmbeddedAccountDAO(Context context){

        this.dbhandler=dbhandler.getHelper(context);
    }*/
   public EmbeddedAccountDAO(SQLiteDatabase db){
       this.db = db;
   }
    //get account numbers
    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumbers = new ArrayList<>();

        String ACCOUNT_NUMBERS_SELECT_QUERY = "SELECT accountNo FROM Account";


        Cursor cursor = db.rawQuery(ACCOUNT_NUMBERS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {

                    String test=cursor.getString(cursor
                            .getColumnIndex("accountNo"));

                    accountNumbers.add(test);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return accountNumbers;
    }

    @Override

    //fetch all account details
    public List<Account> getAccountsList() {
        List<Account> accountdetail = new ArrayList<>();

        String ACCOUNT_DETAIL_SELECT_QUERY = "SELECT * FROM Account";


        Cursor cursor = db.rawQuery(ACCOUNT_DETAIL_SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Account acc=new Account(
                            cursor.getString(cursor.getColumnIndex("accountNo")),
                            cursor.getString(cursor.getColumnIndex("bankName")),
                            cursor.getString(cursor.getColumnIndex("accountHolderName")),
                                    cursor.getDouble(cursor.getColumnIndex("balance")));




                    accountdetail.add(acc);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return accountdetail;

    }

    //get account details for a particular account number
    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        String ACCOUNT_SELECT_QUERY = "SELECT * FROM Account where accountNo = "+accountNo;

        Cursor cursor = db.rawQuery(ACCOUNT_SELECT_QUERY, null);
        if (cursor != null)
            cursor.moveToFirst();

        Account td = new Account(
                cursor.getString(cursor.getColumnIndex("accountNo")),
                cursor.getString(cursor.getColumnIndex("bankName")),
                cursor.getString(cursor.getColumnIndex("accountHolderName")),
                cursor.getDouble(cursor.getColumnIndex("balance")));


        return td;

    }

    //add account
    @Override
    public void addAccount(Account account) {
        String sql = "INSERT INTO Account (accountNo,bankName,accountHolderName,balance) VALUES (?,?,?,?)";
        SQLiteStatement statement = db.compileStatement(sql);



        statement.bindString(1, account.getAccountNo());
        statement.bindString(2, account.getBankName());
        statement.bindString(3, account.getAccountHolderName());
        statement.bindDouble(4, account.getBalance());


        statement.executeInsert();
    }


    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        String sqlquery = "DELETE FROM Account WHERE accountNo = ?";
        SQLiteStatement statement = db.compileStatement(sqlquery);

        statement.bindString(1,accountNo);

        statement.executeUpdateDelete();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expense_Type, double _amount) throws InvalidAccountException {

        String sqlquery = "UPDATE Account SET balance = balance + ?";
        SQLiteStatement statement = db.compileStatement(sqlquery);
        if(expense_Type == ExpenseType.EXPENSE){
            statement.bindDouble(1,-_amount);
        }else{
            statement.bindDouble(1,_amount);
        }

        statement.executeUpdateDelete();
    }
}

