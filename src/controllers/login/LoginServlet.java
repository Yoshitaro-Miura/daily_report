package controllers.login;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DButil;
import utils.EncryptUtil;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */

    //初回はこっちで呼ばれる
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //ここでsession_idを_tokenに入れちゃう
        request.setAttribute("_token", request.getSession().getId());
        //これなに？
        request.setAttribute("hasError", false);

        //nullじゃないなら
        if (request.getSession().getAttribute("flush") != null){
            //セッションのflushをリクエストスコープに入れる
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            //セッションのは消す
            request.getSession().removeAttribute("flush");
        }

        //_tokenだけセッションにいれて、login.jspに戻す
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/login/login.jsp");
        rd.forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */


    //login.jspからはこっちが呼ばれる
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        //ログイン処理を実行
        Boolean check_result = false;

        String code = request.getParameter("code");
        String plain_pass = request.getParameter("password");


        Employee e = null;

        if (code != null && !code.equals("") && plain_pass != null && !plain_pass.equals("")){
            EntityManager em = DButil.createEntityManager();
            //ここの記述よくわからんが、暗号化したいということ★
            //aplicationスコープ、セッションスコープ、リスナーの使い方確認
            String password = EncryptUtil.getPasswordEncrypt(plain_pass, (String)this.getServletContext().getAttribute("pepper"));




            System.out.println(password);

            try{
                //ここの文法要チェック。やってることは、checkLoginCodeAndPasswordにパラメータを渡しているっぽい
                e = em.createNamedQuery("checkLoginCodeAndPassword", Employee.class)
                        //createNamedQueryの戻り値に対して、setParameterメソッドを呼ぶ
                        .setParameter("code",code)
                        .setParameter("pass", password)
                        //一つの結果を取得する
                        .getSingleResult();

            } catch(NoResultException ex) {
                em.close();
            }

            if (e != null){
                check_result = true;
            }

            //こういう書き方できる？falseということ★
            if (!check_result){
                request.setAttribute("_token", request.getSession().getId());
                //エラーだよ、ということ。jsp側で使う？
                request.setAttribute("hasError", true);
                request.setAttribute("code", code);

                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/login/login.jsp");
                rd.forward(request, response);
            } else {
                //eの結果がjsp側で必要

                //eを「login_employee」とする。
                //eは当該コードのemployee情報
                request.getSession().setAttribute("login_employee", e);
                request.getSession().setAttribute("flush", "ログインしました。");
                response.sendRedirect(request.getContextPath() + "/");
            }

        } else {
            System.out.println("エラー");
        }





    }

}
