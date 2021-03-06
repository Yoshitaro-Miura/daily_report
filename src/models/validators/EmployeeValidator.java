package models.validators;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import models.Employee;
import utils.DButil;

//社員番号、氏名、パスワードの必須入力チェック
public class EmployeeValidator {

    //validatorメソッドの定義。戻り値はエラーリスト
    public static List<String> validate(Employee e, Boolean code_duplicate_check_flag, Boolean password_check_flag){

        //エラーリストの宣言
        List<String> errors = new ArrayList<String>();

        //社員番号入力エラーの格納

        //validatCodeメソッドの呼び出し ★e.getCode()でいいのか？⇒いいのか・・。５７行目でnullが入るかもだから
        //メソッドの戻り値をcode_errorに入れる
        String code_error = _validateCode(e.getCode(), code_duplicate_check_flag);

        if(!code_error.equals("")){
            //errorsにcode_errorを入れる
            errors.add(code_error);
        }

        //氏名エラー
        String name_error = _validateName(e.getName());
        if(!name_error.equals("")){
            errors.add(name_error);
        }

        //パスワードエラー
        String password_error = _validataPassword(e.getPassword(), password_check_flag);
        if(!password_error.equals("")){
            errors.add(password_error);
        }

        return errors;

    }


    private static String _validateCode(String code, Boolean code_duplicate_check_flag) {
        // 必須入力チェック
        if(code == null || code.equals("")){
            return "社員番号を入力してください。";
        }

        //重複チェック
        if(code_duplicate_check_flag == true){
            EntityManager em = DButil.createEntityManager();
            long employees_count = (long)em.createNamedQuery("checkRegisteredCode", Long.class).setParameter("code", code).getSingleResult();
            em.close();
            if(employees_count > 0){
                return "入力された社員番号の情報はすでに存在しています。";
            }
        }
        return "";
    }


    private static String _validateName(String name) {
        if(name == null || name.equals("")){
            return "氏名を入力してください。";
        }

        return "";
    }



    //パスワードの必須入力チェック
    private static String _validataPassword(String password, Boolean password_check_flag){

        //パスワードを変更する場合のみ実行
        //update時はnullのとき、password_check_flagはfalseになる
        if(password_check_flag == true && (password == null || password.equals(""))){
            return "パスワードを入力してください";
        }

        return "";
    }



}
