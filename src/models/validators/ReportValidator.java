package models.validators;

import java.util.ArrayList;
import java.util.List;

import models.Report;

public class ReportValidator {

    //validateメソッド。Reportオブジェクトがパラメータ、errorsを返す
    public static List<String> validate(Report r){

        //エラーリストを作る
        List<String> errors = new ArrayList<String>();


        //_validateTitleメソッドに、getTitleの結果をいれた結果をtitle_errorに入れる
        String title_error = _validateTitle(r.getTitle());

        //title_errorが""じゃないなら
        if(!title_error.equals("")){
            //リストerrorsに、title_errorの内容を追加
            errors.add(title_error);
        }



        String content_error = _valdateContent(r.getContent());

        if(!content_error.equals("")){
            errors.add(content_error);
        }

        return errors;

    }


    private static String _validateTitle(String title) {
        // TODO 自動生成されたメソッド・スタブ

        if(title == null || title.equals("")){
            return "タイトルを入力してください。";
        }

        return "";
    }

    private static String _valdateContent(String content) {
        // TODO 自動生成されたメソッド・スタブ

        if(content == null || content.equals("")){
            return "内容を入力してください。";
        }

        return "";
    }

}
