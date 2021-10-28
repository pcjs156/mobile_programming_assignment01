package com.example.assignment01;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.assignment01.parcelable.User;
import com.example.assignment01.util.MessageBox;

public class UserInfoDBManager extends SQLiteOpenHelper {
    public UserInfoDBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE IF NOT EXISTS user_info(" +
                "id TEXT UNIQUE PRIMARY KEY, pw TEXT," +
                "name TEXT, tel TEXT, address TEXT)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE user_info";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    public boolean create(SQLiteDatabase db, MessageBox msg, String id, String pw, String pwAgain, String name, String tel, String address) {
        String sql = String.format("INSERT INTO user_info values (\"%s\", \"%s\", \"%s\", \"%s\", \"%s\");",
                id, pw, name, tel, address);

        // 필수 입력값이 전달되지 않은 경우
        if (id.isEmpty() || pw.isEmpty() || pwAgain.isEmpty()) {
            msg.setBody("ID, 비밀번호, 비밀번호 확인란은 반드시 입력되어야 합니다.");
            return false;
        }

        // 비밀번호와 비밀번호 확인란의 값이 다른 경우
        if (!pw.equals(pwAgain)) {
            msg.setBody("비밀번호와 비밀번호 확인란의 값이 다릅니다.");
            return false;
        }

        // 비밀번호가 7자 미만이거나, 알파벳과 숫자가 모두 사용되지 않은 경우
        final String PW_NUM_CHECK_REGEX = "[0-9]+";
        final String PW_ALPHA_CHECK_REGEX = "[a-zA-Z]+";
        final String PW_FULL_CHECK_REGEX = "[0-9a-zA-Z]+";
        if (pw.length() < 7 || pw.matches(PW_NUM_CHECK_REGEX) ||
                pw.matches(PW_ALPHA_CHECK_REGEX) || !pw.matches(PW_FULL_CHECK_REGEX)) {
            msg.setBody("비밀번호는 7자 이상의 숫자와 알파벳의 조합으로 구성되어야 합니다.");
            return false;
        }

        try {
            db.execSQL(sql);
        }
        // 이미 사용중인 ID인 경우
        catch (SQLiteConstraintException e) {
            msg.setBody("사용중인 ID입니다.");
            return false;
        }

        return true;
    }

    public User login(SQLiteDatabase db, String id, String pw) {
        String sql = "SELECT id, pw, name, tel, address FROM user_info WHERE id LIKE '%" + id + "%' AND pw LIKE '%" + pw + "%';";

        Cursor c = db.rawQuery(sql, null);

        boolean exists = c.moveToNext();
        if (!exists) {
            return null;
        } else {
            User user = new User(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4));
            return user;
        }
    }
}
